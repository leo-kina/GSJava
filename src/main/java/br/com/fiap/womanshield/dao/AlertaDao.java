package br.com.fiap.womanshield.dao;

import br.com.fiap.womanshield.model.Alerta;

import java.util.List;
import java.util.Optional;

public interface AlertaDao {

    Alerta inserir(Alerta alerta);

    Optional<Alerta> buscarPorId(Long idAlerta);

    List<Alerta> listarPorUsuaria(Long idUsuaria);

    void remover(Long idAlerta);
}
