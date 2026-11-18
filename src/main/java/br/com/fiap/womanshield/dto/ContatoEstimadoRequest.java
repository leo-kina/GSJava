package br.com.fiap.womanshield.dto;

public class ContatoEstimadoRequest {

    private Long idContato;
    private Integer minutosEstimados;

    public Long getIdContato() {
        return idContato;
    }

    public void setIdContato(Long idContato) {
        this.idContato = idContato;
    }

    public Integer getMinutosEstimados() {
        return minutosEstimados;
    }

    public void setMinutosEstimados(Integer minutosEstimados) {
        this.minutosEstimados = minutosEstimados;
    }
}
