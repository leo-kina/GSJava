package br.com.fiap.womanshield.dto;

import java.util.List;

public class DisponibilidadeRequest {

    private Boolean sempre;
    private List<Integer> dias;
    private Integer horaInicio;
    private Integer horaFim;

    public Boolean getSempre() {
        return sempre;
    }

    public void setSempre(Boolean sempre) {
        this.sempre = sempre;
    }

    public List<Integer> getDias() {
        return dias;
    }

    public void setDias(List<Integer> dias) {
        this.dias = dias;
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
}
