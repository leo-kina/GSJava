package br.com.fiap.womanshield.dao;

import br.com.fiap.womanshield.model.Usuaria;

import java.util.List;
import java.util.Optional;

public interface UsuariaDao {

    List<Usuaria> listarTodas();

    Optional<Usuaria> buscarPorId(Long idUsuaria);

    boolean existeTelefone(String telefone);

    Usuaria inserir(Usuaria usuaria);

    void atualizar(Usuaria usuaria);

    void desativar(Long idUsuaria);
}
