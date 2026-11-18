package br.com.fiap.womanshield.dto;

public class ContatoRequest {

    private String nome;
    private String telefone;
    private Integer idBairro;
    private Boolean principal;
    private DisponibilidadeRequest disponibilidade;

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

    public Integer getIdBairro() {
        return idBairro;
    }

    public void setIdBairro(Integer idBairro) {
        this.idBairro = idBairro;
    }

    public Boolean getPrincipal() {
        return principal;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }

    public DisponibilidadeRequest getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(DisponibilidadeRequest disponibilidade) {
        this.disponibilidade = disponibilidade;
    }
}
