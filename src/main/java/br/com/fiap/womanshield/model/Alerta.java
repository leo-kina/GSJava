package br.com.fiap.womanshield.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Alerta {

    private Long idAlerta;
    private Long idUsuaria;
    private Integer idBairro;
    private Integer idPeriodo;
    private LocalDateTime dataHora;
    private String origemLocal;
    private List<AlertaContato> contatosAvisados = new ArrayList<>();

    public Long getIdAlerta() {
        return idAlerta;
    }

    public void setIdAlerta(Long idAlerta) {
        this.idAlerta = idAlerta;
    }

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

    public Integer getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(Integer idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getOrigemLocal() {
        return origemLocal;
    }

    public void setOrigemLocal(String origemLocal) {
        this.origemLocal = origemLocal;
    }

    public List<AlertaContato> getContatosAvisados() {
        return contatosAvisados;
    }

    public void setContatosAvisados(List<AlertaContato> contatosAvisados) {
        this.contatosAvisados = contatosAvisados;
    }
}
