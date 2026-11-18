package br.com.fiap.womanshield.dao;

import br.com.fiap.womanshield.model.Bairro;

import java.util.List;
import java.util.Optional;

public interface BairroDao {

    List<Bairro> listarTodos();

    Optional<Bairro> buscarPorId(Integer idBairro);
}
