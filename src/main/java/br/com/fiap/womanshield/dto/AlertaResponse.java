package br.com.fiap.womanshield.dto;

import br.com.fiap.womanshield.model.Alerta;
import br.com.fiap.womanshield.model.Bairro;
import br.com.fiap.womanshield.model.PeriodoDia;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class AlertaResponse {

    private Long id;
    private Long idUsuaria;
    private BairroResumo bairro;
    private String periodo;
    private String dataHora;
    private String origemLocal;
    private List<ContatoAvisadoResponse> contatosAvisados;

    public static AlertaResponse de(Alerta alerta, Bairro bairro, PeriodoDia periodo) {
        AlertaResponse dto = new AlertaResponse();
        dto.id = alerta.getIdAlerta();
        dto.idUsuaria = alerta.getIdUsuaria();
        dto.bairro = BairroResumo.de(bairro);
        dto.periodo = periodo.getNomePeriodo();
        dto.dataHora = alerta.getDataHora().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        dto.origemLocal = alerta.getOrigemLocal();
        dto.contatosAvisados = alerta.getContatosAvisados().stream()
                .map(ContatoAvisadoResponse::de)
                .collect(Collectors.toList());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public Long getIdUsuaria() {
        return idUsuaria;
    }

    public BairroResumo getBairro() {
        return bairro;
    }

    public String getPeriodo() {
        return periodo;
    }

    public String getDataHora() {
        return dataHora;
    }

    public String getOrigemLocal() {
        return origemLocal;
    }

    public List<ContatoAvisadoResponse> getContatosAvisados() {
        return contatosAvisados;
    }
}
