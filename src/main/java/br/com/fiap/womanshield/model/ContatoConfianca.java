package br.com.fiap.womanshield.model;

import java.util.ArrayList;
import java.util.List;

public class ContatoConfianca {

    private Long idContato;
    private Long idUsuaria;
    private Integer idBairro;
    private String nome;
    private String telefone;
    private boolean principal;
    private List<Disponibilidade> disponibilidades = new ArrayList<>();

    public Long getIdContato() {
        return idContato;
    }

    public void setIdContato(Long idContato) {
        this.idContato = idContato;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }

    public List<Disponibilidade> getDisponibilidades() {
        return disponibilidades;
    }

    public void setDisponibilidades(List<Disponibilidade> disponibilidades) {
        this.disponibilidades = disponibilidades;
    }
}
