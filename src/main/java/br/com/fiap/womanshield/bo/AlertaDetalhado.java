package br.com.fiap.womanshield.bo;

import br.com.fiap.womanshield.model.Alerta;
import br.com.fiap.womanshield.model.Bairro;
import br.com.fiap.womanshield.model.PeriodoDia;

/**
 * Alerta + os dados de bairro/periodo que o Controller precisa para montar a resposta,
 * sem obrigar o Controller a falar diretamente com os DAOs.
 */
public record AlertaDetalhado(Alerta alerta, Bairro bairro, PeriodoDia periodo) {
}
