package br.com.fiap.womanshield.bo;

import br.com.fiap.womanshield.dao.BairroDao;
import br.com.fiap.womanshield.exception.RecursoNaoEncontradoException;
import br.com.fiap.womanshield.factory.DaoFactory;
import br.com.fiap.womanshield.model.Bairro;

import java.util.List;

public class BairroBO {

    private final BairroDao bairroDao = DaoFactory.getBairroDao();

    public List<Bairro> listarTodos() {
        return bairroDao.listarTodos();
    }

    public Bairro buscarOuFalhar(Integer idBairro) {
        if (idBairro == null) {
            throw new RecursoNaoEncontradoException("Bairro nao encontrado.");
        }
        return bairroDao.buscarPorId(idBairro)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Bairro " + idBairro + " nao encontrado."));
    }
}
