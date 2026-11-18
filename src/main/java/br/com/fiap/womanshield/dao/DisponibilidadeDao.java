package br.com.fiap.womanshield.dao;

import br.com.fiap.womanshield.model.Disponibilidade;

import java.util.List;

public interface DisponibilidadeDao {

    List<Disponibilidade> listarPorContato(Long idContato);
}
