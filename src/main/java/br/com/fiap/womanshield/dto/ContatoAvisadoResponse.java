package br.com.fiap.womanshield.dto;

import br.com.fiap.womanshield.bo.validacao.MascaraTelefone;
import br.com.fiap.womanshield.model.AlertaContato;

public class ContatoAvisadoResponse {

    private Integer ordem;
    private Long idContato;
    private String nome;
    private String telefone;
    private Integer minutosEstimados;
    private boolean disponivel;
    private boolean avisado;

    public static ContatoAvisadoResponse de(AlertaContato ac) {
        ContatoAvisadoResponse dto = new ContatoAvisadoResponse();
        dto.ordem = ac.getOrdem();
        dto.idContato = ac.getIdContato();
        dto.nome = ac.getNomeContato();
        dto.telefone = MascaraTelefone.mascarar(ac.getTelefoneContato());
        dto.minutosEstimados = ac.getMinutosEstimados();
        dto.disponivel = ac.isDisponivel();
        dto.avisado = ac.isAvisado();
        return dto;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public Long getIdContato() {
        return idContato;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public Integer getMinutosEstimados() {
        return minutosEstimados;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public boolean isAvisado() {
        return avisado;
    }
}
