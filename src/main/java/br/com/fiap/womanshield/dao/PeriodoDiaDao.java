package br.com.fiap.womanshield.dao;

import br.com.fiap.womanshield.model.PeriodoDia;

import java.util.List;
import java.util.Optional;

public interface PeriodoDiaDao {

    List<PeriodoDia> listarTodos();

    Optional<PeriodoDia> buscarPorHora(int hora);

    Optional<PeriodoDia> buscarPorId(Integer idPeriodo);
}
