package br.com.fiap.womanshield.model;

import java.time.LocalDate;

public class Usuaria {

    private Long idUsuaria;
    private String apelido;
    private String telefone;
    private LocalDate dataCadastro;
    private boolean ativa;

    public Long getIdUsuaria() {
        return idUsuaria;
    }

    public void setIdUsuaria(Long idUsuaria) {
        this.idUsuaria = idUsuaria;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }
}
