package br.com.fiap.womanshield.model;

import java.math.BigDecimal;

public class PeriodoDia {

    private Integer idPeriodo;
    private String nomePeriodo;
    private Integer horaInicio;
    private Integer horaFim;
    private BigDecimal fatorTempo;
    private BigDecimal fatorRisco;

    public Integer getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(Integer idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public String getNomePeriodo() {
        return nomePeriodo;
    }

    public void setNomePeriodo(String nomePeriodo) {
        this.nomePeriodo = nomePeriodo;
    }

    public Integer getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Integer horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Integer getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(Integer horaFim) {
        this.horaFim = horaFim;
    }

    public BigDecimal getFatorTempo() {
        return fatorTempo;
    }

    public void setFatorTempo(BigDecimal fatorTempo) {
        this.fatorTempo = fatorTempo;
    }

    public BigDecimal getFatorRisco() {
        return fatorRisco;
    }

    public void setFatorRisco(BigDecimal fatorRisco) {
        this.fatorRisco = fatorRisco;
    }

    /**
     * Contempla o periodo cuja faixa [horaInicio, horaFim) contem a hora informada.
     */
    public boolean contemHora(int hora) {
        return horaInicio <= hora && hora < horaFim;
    }
}
