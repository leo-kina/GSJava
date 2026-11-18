package br.com.fiap.womanshield.bo;

import br.com.fiap.womanshield.bo.validacao.TelefoneValidador;
import br.com.fiap.womanshield.dao.UsuariaDao;
import br.com.fiap.womanshield.dto.UsuariaRequest;
import br.com.fiap.womanshield.exception.CampoInvalidoException;
import br.com.fiap.womanshield.exception.ConflitoException;
import br.com.fiap.womanshield.exception.RecursoNaoEncontradoException;
import br.com.fiap.womanshield.factory.DaoFactory;
import br.com.fiap.womanshield.model.Usuaria;

import java.util.List;

public class UsuariaBO {

    private final UsuariaDao usuariaDao = DaoFactory.getUsuariaDao();

    public List<Usuaria> listarTodas() {
        return usuariaDao.listarTodas();
    }

    public Usuaria buscarOuFalhar(Long idUsuaria) {
        return usuariaDao.buscarPorId(idUsuaria)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuaria " + idUsuaria + " nao encontrada."));
    }

    public Usuaria cadastrar(UsuariaRequest req) {
        String apelido = validarApelido(req.getApelido());
        String telefone = TelefoneValidador.validar(req.getTelefone());

        if (usuariaDao.existeTelefone(telefone)) {
            throw new ConflitoException("Telefone ja cadastrado para outra usuaria.");
        }

        Usuaria usuaria = new Usuaria();
        usuaria.setApelido(apelido);
        usuaria.setTelefone(telefone);
        return usuariaDao.inserir(usuaria);
    }

    public Usuaria atualizar(Long idUsuaria, UsuariaRequest req) {
        Usuaria atual = buscarOuFalhar(idUsuaria);

        String apelido = validarApelido(req.getApelido());
        String telefone = TelefoneValidador.validar(req.getTelefone());

        if (!telefone.equals(atual.getTelefone()) && usuariaDao.existeTelefone(telefone)) {
            throw new ConflitoException("Telefone ja cadastrado para outra usuaria.");
        }

        atual.setApelido(apelido);
        atual.setTelefone(telefone);
        usuariaDao.atualizar(atual);
        return atual;
    }

    public void desativar(Long idUsuaria) {
        buscarOuFalhar(idUsuaria);
        usuariaDao.desativar(idUsuaria);
    }

    private String validarApelido(String apelido) {
        if (apelido == null || apelido.isBlank()) {
            throw new CampoInvalidoException("Confira os campos destacados.", "apelido", "Apelido e obrigatorio.");
        }
        if (apelido.length() > 40) {
            throw new CampoInvalidoException("Confira os campos destacados.", "apelido",
                    "Apelido deve ter ate 40 caracteres.");
        }
        return apelido.trim();
    }
}
