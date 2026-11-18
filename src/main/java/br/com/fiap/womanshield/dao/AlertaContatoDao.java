package br.com.fiap.womanshield.dao;

import br.com.fiap.womanshield.model.AlertaContato;

import java.util.List;

public interface AlertaContatoDao {

    List<AlertaContato> listarPorAlerta(Long idAlerta);
}
