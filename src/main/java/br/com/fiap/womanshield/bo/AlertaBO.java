package br.com.fiap.womanshield.bo;

import br.com.fiap.womanshield.dao.AlertaDao;
import br.com.fiap.womanshield.dao.ContatoConfiancaDao;
import br.com.fiap.womanshield.dao.DisponibilidadeDao;
import br.com.fiap.womanshield.dao.PeriodoDiaDao;
import br.com.fiap.womanshield.dto.AlertaRequest;
import br.com.fiap.womanshield.dto.ContatoEstimadoRequest;
import br.com.fiap.womanshield.exception.CampoInvalidoException;
import br.com.fiap.womanshield.exception.RecursoNaoEncontradoException;
import br.com.fiap.womanshield.exception.RegraNegocioException;
import br.com.fiap.womanshield.factory.DaoFactory;
import br.com.fiap.womanshield.model.Alerta;
import br.com.fiap.womanshield.model.AlertaContato;
import br.com.fiap.womanshield.model.Bairro;
import br.com.fiap.womanshield.model.ContatoConfianca;
import br.com.fiap.womanshield.model.Disponibilidade;
import br.com.fiap.womanshield.model.PeriodoDia;
import br.com.fiap.womanshield.model.Usuaria;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Regras do LEIA-ME secao 4 "Alerta". O calculo de minutos de cada contato vem pronto
 * (motor de rotas em Python); esta camada valida, ordena, descobre o periodo/disponibilidade e grava.
 */
public class AlertaBO {

    private static final Set<String> ORIGENS_VALIDAS = Set.of("GPS", "MANUAL");

    private final AlertaDao alertaDao = DaoFactory.getAlertaDao();
    private final ContatoConfiancaDao contatoDao = DaoFactory.getContatoConfiancaDao();
    private final DisponibilidadeDao disponibilidadeDao = DaoFactory.getDisponibilidadeDao();
    private final PeriodoDiaDao periodoDiaDao = DaoFactory.getPeriodoDiaDao();
    private final UsuariaBO usuariaBO = new UsuariaBO();
    private final BairroBO bairroBO = new BairroBO();

    public AlertaDetalhado registrar(AlertaRequest req) {
        if (req.getIdUsuaria() == null) {
            throw new CampoInvalidoException("Confira os campos destacados.", "idUsuaria", "Usuaria e obrigatoria.");
        }
        Usuaria usuaria = usuariaBO.buscarOuFalhar(req.getIdUsuaria());
        if (!usuaria.isAtiva()) {
            throw new RegraNegocioException("Usuaria desativada. Nao e possivel registrar alertas.");
        }

        if (req.getIdBairro() == null) {
            throw new CampoInvalidoException("Confira os campos destacados.", "idBairro", "Bairro e obrigatorio.");
        }
        Bairro bairro = bairroBO.buscarOuFalhar(req.getIdBairro());

        String origemLocal = validarOrigemLocal(req.getOrigemLocal());
        LocalDateTime dataHora = parseDataHora(req.getDataHora());

        if (req.getContatos() == null || req.getContatos().isEmpty()) {
            throw new RegraNegocioException(
                    "Nenhum contato de confianca cadastrado. Cadastre pelo menos um contato antes de pedir ajuda. "
                            + "Em perigo imediato, ligue 190.");
        }

        int diaSemana = dataHora.getDayOfWeek().getValue() - 1;
        int hora = dataHora.getHour();

        PeriodoDia periodo = periodoDiaDao.buscarPorHora(hora)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Nao ha periodo do dia cadastrado para a hora " + hora + "."));

        List<AlertaContato> contatosAvisados =
                montarContatosAvisados(req.getIdUsuaria(), req.getContatos(), diaSemana, hora);

        Alerta alerta = new Alerta();
        alerta.setIdUsuaria(req.getIdUsuaria());
        alerta.setIdBairro(bairro.getIdBairro());
        alerta.setIdPeriodo(periodo.getIdPeriodo());
        alerta.setDataHora(dataHora);
        alerta.setOrigemLocal(origemLocal);
        alerta.setContatosAvisados(contatosAvisados);

        Alerta salvo = alertaDao.inserir(alerta);
        return new AlertaDetalhado(salvo, bairro, periodo);
    }

    public AlertaDetalhado buscarOuFalhar(Long idAlerta) {
        Alerta alerta = alertaDao.buscarPorId(idAlerta)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Alerta " + idAlerta + " nao encontrado."));
        return detalhar(alerta);
    }

    public List<AlertaDetalhado> listarPorUsuaria(Long idUsuaria) {
        usuariaBO.buscarOuFalhar(idUsuaria);
        return alertaDao.listarPorUsuaria(idUsuaria).stream()
                .map(this::detalhar)
                .collect(Collectors.toList());
    }

    public void remover(Long idAlerta) {
        buscarOuFalhar(idAlerta);
        alertaDao.remover(idAlerta);
    }

    private AlertaDetalhado detalhar(Alerta alerta) {
        Bairro bairro = bairroBO.buscarOuFalhar(alerta.getIdBairro());
        PeriodoDia periodo = periodoDiaDao.buscarPorId(alerta.getIdPeriodo())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Periodo " + alerta.getIdPeriodo() + " nao encontrado."));
        return new AlertaDetalhado(alerta, bairro, periodo);
    }

    private List<AlertaContato> montarContatosAvisados(Long idUsuaria, List<ContatoEstimadoRequest> contatosRequest,
                                                         int diaSemana, int hora) {
        List<AlertaContato> contatosAvisados = contatosRequest.stream()
                .map(item -> montarContatoAvisado(idUsuaria, item, diaSemana, hora))
                .sorted(Comparator
                        .comparing(AlertaContato::getMinutosEstimados)
                        .thenComparing(ac -> !ac.isPrincipalTemp())
                        .thenComparing(AlertaContato::getNomeContato))
                .collect(Collectors.toList());

        for (int i = 0; i < contatosAvisados.size(); i++) {
            contatosAvisados.get(i).setOrdem(i + 1);
        }
        return contatosAvisados;
    }

    private AlertaContato montarContatoAvisado(Long idUsuaria, ContatoEstimadoRequest item, int diaSemana, int hora) {
        if (item.getIdContato() == null) {
            throw new CampoInvalidoException("Confira os campos destacados.", "contatos",
                    "idContato e obrigatorio para cada contato.");
        }
        if (item.getMinutosEstimados() == null || item.getMinutosEstimados() < 0) {
            throw new CampoInvalidoException("Confira os campos destacados.", "contatos",
                    "minutosEstimados deve ser maior ou igual a 0.");
        }

        ContatoConfianca contato = contatoDao.buscarPorId(item.getIdContato())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Contato " + item.getIdContato() + " nao encontrado."));

        if (!contato.getIdUsuaria().equals(idUsuaria)) {
            throw new CampoInvalidoException("Confira os campos destacados.", "contatos",
                    "O contato " + contato.getIdContato() + " nao pertence a esta usuaria.");
        }

        List<Disponibilidade> disponibilidades = disponibilidadeDao.listarPorContato(contato.getIdContato());
        boolean disponivel = disponibilidades.stream().anyMatch(d -> d.cobre(diaSemana, hora));

        AlertaContato ac = new AlertaContato();
        ac.setIdContato(contato.getIdContato());
        ac.setMinutosEstimados(item.getMinutosEstimados());
        ac.setDisponivel(disponivel);
        ac.setAvisado(true);
        ac.setNomeContato(contato.getNome());
        ac.setTelefoneContato(contato.getTelefone());
        ac.setPrincipalTemp(contato.isPrincipal());
        return ac;
    }

    private String validarOrigemLocal(String origemLocal) {
        if (origemLocal == null || !ORIGENS_VALIDAS.contains(origemLocal.toUpperCase())) {
            throw new CampoInvalidoException("Confira os campos destacados.", "origemLocal",
                    "Origem deve ser GPS ou MANUAL.");
        }
        return origemLocal.toUpperCase();
    }

    private LocalDateTime parseDataHora(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new CampoInvalidoException("Confira os campos destacados.", "dataHora", "Data/hora e obrigatoria.");
        }
        try {
            return LocalDateTime.parse(texto);
        } catch (DateTimeParseException e) {
            throw new CampoInvalidoException("Confira os campos destacados.", "dataHora",
                    "Data/hora deve estar no formato ISO, ex: 2026-11-02T22:40:00.");
        }
    }
}
