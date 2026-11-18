package br.com.fiap.womanshield.dto;

import java.util.List;

public class AlertaRequest {

    private Long idUsuaria;
    private Integer idBairro;
    private String origemLocal;
    private String dataHora;
    private List<ContatoEstimadoRequest> contatos;

    public Long getIdUsuaria() {
        return idUsuaria;
    }

    public void setIdUsuaria(Long idUsuaria) {
        this.idUsuaria = idUsuaria;
    }

    public Integer getIdBairro() {
        return idBairro;
    }

    public void setIdBairro(Integer idBairro) {
        this.idBairro = idBairro;
    }

    public String getOrigemLocal() {
        return origemLocal;
    }

    public void setOrigemLocal(String origemLocal) {
        this.origemLocal = origemLocal;
    }

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }

    public List<ContatoEstimadoRequest> getContatos() {
        return contatos;
    }

    public void setContatos(List<ContatoEstimadoRequest> contatos) {
        this.contatos = contatos;
    }
}
