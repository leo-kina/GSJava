package br.com.fiap.womanshield.dto;

import br.com.fiap.womanshield.model.Bairro;

public class BairroResumo {

    private Integer id;
    private String nome;

    public static BairroResumo de(Bairro bairro) {
        BairroResumo dto = new BairroResumo();
        dto.id = bairro.getIdBairro();
        dto.nome = bairro.getNomeBairro();
        return dto;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}
