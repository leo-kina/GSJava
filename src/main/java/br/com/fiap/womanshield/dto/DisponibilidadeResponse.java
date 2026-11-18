package br.com.fiap.womanshield.dto;

import br.com.fiap.womanshield.model.Disponibilidade;

public class DisponibilidadeResponse {

    private Integer dia;
    private Integer horaInicio;
    private Integer horaFim;

    public static DisponibilidadeResponse de(Disponibilidade disponibilidade) {
        DisponibilidadeResponse dto = new DisponibilidadeResponse();
        dto.dia = disponibilidade.getDiaSemana();
        dto.horaInicio = disponibilidade.getHoraInicio();
        dto.horaFim = disponibilidade.getHoraFim();
        return dto;
    }

    public Integer getDia() {
        return dia;
    }

    public Integer getHoraInicio() {
        return horaInicio;
    }

    public Integer getHoraFim() {
        return horaFim;
    }
}
