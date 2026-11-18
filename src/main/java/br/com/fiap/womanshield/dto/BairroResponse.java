package br.com.fiap.womanshield.dto;

import br.com.fiap.womanshield.model.Bairro;

public class BairroResponse {

    private Integer id;
    private String nome;
    private Integer indiceRisco;

    public static BairroResponse de(Bairro bairro) {
        BairroResponse dto = new BairroResponse();
        dto.id = bairro.getIdBairro();
        dto.nome = bairro.getNomeBairro();
        dto.indiceRisco = bairro.getIndiceRisco();
        return dto;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Integer getIndiceRisco() {
        return indiceRisco;
    }
}
