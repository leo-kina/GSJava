package br.com.fiap.womanshield.dto;

import br.com.fiap.womanshield.bo.validacao.MascaraTelefone;
import br.com.fiap.womanshield.model.Bairro;
import br.com.fiap.womanshield.model.ContatoConfianca;

import java.util.List;
import java.util.stream.Collectors;

public class ContatoResponse {

    private Long id;
    private Long idUsuaria;
    private String nome;
    private String telefone;
    private BairroResumo bairro;
    private boolean principal;
    private List<DisponibilidadeResponse> disponibilidade;

    public static ContatoResponse de(ContatoConfianca contato, Bairro bairro) {
        ContatoResponse dto = new ContatoResponse();
        dto.id = contato.getIdContato();
        dto.idUsuaria = contato.getIdUsuaria();
        dto.nome = contato.getNome();
        dto.telefone = MascaraTelefone.mascarar(contato.getTelefone());
        dto.bairro = BairroResumo.de(bairro);
        dto.principal = contato.isPrincipal();
        dto.disponibilidade = contato.getDisponibilidades().stream()
                .map(DisponibilidadeResponse::de)
                .collect(Collectors.toList());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public Long getIdUsuaria() {
        return idUsuaria;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public BairroResumo getBairro() {
        return bairro;
    }

    public boolean isPrincipal() {
        return principal;
    }

    public List<DisponibilidadeResponse> getDisponibilidade() {
        return disponibilidade;
    }
}
