package br.com.fiap.womanshield.model;

import java.math.BigDecimal;

public class Bairro {

    private Integer idBairro;
    private Integer idMunicipio;
    private String nomeBairro;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Integer indiceRisco;
    private BigDecimal ocorrenciaPrevista;

    public Integer getIdBairro() {
        return idBairro;
    }

    public void setIdBairro(Integer idBairro) {
        this.idBairro = idBairro;
    }

    public Integer getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(Integer idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public String getNomeBairro() {
        return nomeBairro;
    }

    public void setNomeBairro(String nomeBairro) {
        this.nomeBairro = nomeBairro;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public Integer getIndiceRisco() {
        return indiceRisco;
    }

    public void setIndiceRisco(Integer indiceRisco) {
        this.indiceRisco = indiceRisco;
    }

    public BigDecimal getOcorrenciaPrevista() {
        return ocorrenciaPrevista;
    }

    public void setOcorrenciaPrevista(BigDecimal ocorrenciaPrevista) {
        this.ocorrenciaPrevista = ocorrenciaPrevista;
    }
}
