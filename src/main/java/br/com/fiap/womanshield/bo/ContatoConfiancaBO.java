package br.com.fiap.womanshield.bo;

import br.com.fiap.womanshield.bo.validacao.TelefoneValidador;
import br.com.fiap.womanshield.dao.ContatoConfiancaDao;
import br.com.fiap.womanshield.dao.DisponibilidadeDao;
import br.com.fiap.womanshield.dto.ContatoRequest;
import br.com.fiap.womanshield.dto.DisponibilidadeRequest;
import br.com.fiap.womanshield.exception.CampoInvalidoException;
import br.com.fiap.womanshield.exception.ConflitoException;
import br.com.fiap.womanshield.exception.RecursoNaoEncontradoException;
import br.com.fiap.womanshield.factory.DaoFactory;
import br.com.fiap.womanshield.model.ContatoConfianca;
import br.com.fiap.womanshield.model.Disponibilidade;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ContatoConfiancaBO {

    private static final int LIMITE_CONTATOS = 6;

    private final ContatoConfiancaDao contatoDao = DaoFactory.getContatoConfiancaDao();
    private final DisponibilidadeDao disponibilidadeDao = DaoFactory.getDisponibilidadeDao();
    private final BairroBO bairroBO = new BairroBO();
    private final UsuariaBO usuariaBO = new UsuariaBO();

    public List<ContatoConfianca> listarPorUsuaria(Long idUsuaria) {
        usuariaBO.buscarOuFalhar(idUsuaria);
        List<ContatoConfianca> contatos = contatoDao.listarPorUsuaria(idUsuaria);
        contatos.forEach(this::carregarDisponibilidades);
        return contatos;
    }

    public ContatoConfianca buscarOuFalhar(Long idContato) {
        ContatoConfianca contato = contatoDao.buscarPorId(idContato)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Contato " + idContato + " nao encontrado."));
        carregarDisponibilidades(contato);
        return contato;
    }

    public ContatoConfianca cadastrar(Long idUsuaria, ContatoRequest req) {
        usuariaBO.buscarOuFalhar(idUsuaria);

        String nome = validarNome(req.getNome());
        String telefone = TelefoneValidador.validar(req.getTelefone());
        Integer idBairro = validarIdBairro(req.getIdBairro());
        bairroBO.buscarOuFalhar(idBairro);
        List<Disponibilidade> disponibilidades = validarDisponibilidade(req.getDisponibilidade());

        int totalAtual = contatoDao.contarPorUsuaria(idUsuaria);
        if (totalAtual >= LIMITE_CONTATOS) {
            throw new ConflitoException("Limite de " + LIMITE_CONTATOS + " contatos de confianca atingido.");
        }
        if (contatoDao.existeTelefoneNaUsuaria(idUsuaria, telefone, null)) {
            throw new ConflitoException("Esse telefone ja esta cadastrado como contato desta usuaria.");
        }

        boolean principal = totalAtual == 0 || Boolean.TRUE.equals(req.getPrincipal());
        if (principal && totalAtual > 0) {
            contatoDao.desmarcarPrincipalDaUsuaria(idUsuaria);
        }

        ContatoConfianca contato = new ContatoConfianca();
        contato.setIdUsuaria(idUsuaria);
        contato.setIdBairro(idBairro);
        contato.setNome(nome);
        contato.setTelefone(telefone);
        contato.setPrincipal(principal);
        contato.setDisponibilidades(disponibilidades);

        return contatoDao.inserir(contato);
    }

    public ContatoConfianca atualizar(Long idContato, ContatoRequest req) {
        ContatoConfianca atual = buscarOuFalhar(idContato);

        String nome = validarNome(req.getNome());
        Integer idBairro = validarIdBairro(req.getIdBairro());
        bairroBO.buscarOuFalhar(idBairro);

        String telefone = atual.getTelefone();
        if (req.getTelefone() != null && !req.getTelefone().isBlank()) {
            telefone = TelefoneValidador.validar(req.getTelefone());
            if (!telefone.equals(atual.getTelefone())
                    && contatoDao.existeTelefoneNaUsuaria(atual.getIdUsuaria(), telefone, idContato)) {
                throw new ConflitoException("Esse telefone ja esta cadastrado como contato desta usuaria.");
            }
        }

        List<Disponibilidade> disponibilidades = req.getDisponibilidade() != null
                ? validarDisponibilidade(req.getDisponibilidade())
                : List.of();

        atual.setNome(nome);
        atual.setIdBairro(idBairro);
        atual.setTelefone(telefone);
        atual.setDisponibilidades(disponibilidades);
        contatoDao.atualizar(atual);

        if (req.getPrincipal() != null) {
            aplicarTrocaDePrincipal(atual, req.getPrincipal());
        }

        return buscarOuFalhar(idContato);
    }

    public void remover(Long idContato) {
        ContatoConfianca contato = buscarOuFalhar(idContato);

        if (contatoDao.possuiHistoricoDeAlertas(idContato)) {
            throw new ConflitoException(
                    "Esse contato ja foi avisado em algum alerta; o historico e mantido e por isso ele nao pode ser removido.");
        }

        boolean eraPrincipal = contato.isPrincipal();
        contatoDao.remover(idContato);

        if (eraPrincipal) {
            contatoDao.buscarOutroContato(contato.getIdUsuaria(), idContato)
                    .ifPresent(outro -> contatoDao.marcarPrincipal(outro.getIdContato(), true));
        }
    }

    private void aplicarTrocaDePrincipal(ContatoConfianca contato, boolean deveSerPrincipal) {
        if (deveSerPrincipal && !contato.isPrincipal()) {
            contatoDao.desmarcarPrincipalDaUsuaria(contato.getIdUsuaria());
            contatoDao.marcarPrincipal(contato.getIdContato(), true);
        } else if (!deveSerPrincipal && contato.isPrincipal()) {
            contatoDao.buscarOutroContato(contato.getIdUsuaria(), contato.getIdContato())
                    .ifPresent(outro -> {
                        contatoDao.marcarPrincipal(contato.getIdContato(), false);
                        contatoDao.marcarPrincipal(outro.getIdContato(), true);
                    });
        }
    }

    private void carregarDisponibilidades(ContatoConfianca contato) {
        contato.setDisponibilidades(disponibilidadeDao.listarPorContato(contato.getIdContato()));
    }

    private String validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new CampoInvalidoException("Confira os campos destacados.", "nome", "Nome ou apelido e obrigatorio.");
        }
        if (nome.length() > 40) {
            throw new CampoInvalidoException("Confira os campos destacados.", "nome",
                    "Nome ou apelido deve ter ate 40 caracteres.");
        }
        return nome.trim();
    }

    private Integer validarIdBairro(Integer idBairro) {
        if (idBairro == null) {
            throw new CampoInvalidoException("Confira os campos destacados.", "idBairro", "Bairro e obrigatorio.");
        }
        return idBairro;
    }

    private List<Disponibilidade> validarDisponibilidade(DisponibilidadeRequest req) {
        if (req == null) {
            throw new CampoInvalidoException("Confira os campos destacados.", "disponibilidade",
                    "Disponibilidade e obrigatoria.");
        }

        if (Boolean.TRUE.equals(req.getSempre())) {
            List<Disponibilidade> todasAsDisponibilidades = new ArrayList<>();
            for (int dia = 0; dia <= 6; dia++) {
                todasAsDisponibilidades.add(new Disponibilidade(dia, 0, 24));
            }
            return todasAsDisponibilidades;
        }

        List<Integer> dias = req.getDias();
        if (dias == null || dias.isEmpty()) {
            throw new CampoInvalidoException("Confira os campos destacados.", "disponibilidade",
                    "Informe pelo menos 1 dia.");
        }
        for (Integer dia : dias) {
            if (dia == null || dia < 0 || dia > 6) {
                throw new CampoInvalidoException("Confira os campos destacados.", "disponibilidade",
                        "Dia da semana deve ser de 0 (segunda) a 6 (domingo).");
            }
        }

        Integer horaInicio = req.getHoraInicio();
        Integer horaFim = req.getHoraFim();
        if (horaInicio == null || horaInicio < 0 || horaInicio > 23) {
            throw new CampoInvalidoException("Confira os campos destacados.", "disponibilidade",
                    "Hora de inicio deve ser de 0 a 23.");
        }
        if (horaFim == null || horaFim < 1 || horaFim > 24) {
            throw new CampoInvalidoException("Confira os campos destacados.", "disponibilidade",
                    "Hora de fim deve ser de 1 a 24.");
        }
        if (horaInicio.equals(horaFim)) {
            throw new CampoInvalidoException("Confira os campos destacados.", "disponibilidade",
                    "Hora de inicio e fim nao podem ser iguais.");
        }

        return dias.stream()
                .map(dia -> new Disponibilidade(dia, horaInicio, horaFim))
                .collect(Collectors.toList());
    }
}
