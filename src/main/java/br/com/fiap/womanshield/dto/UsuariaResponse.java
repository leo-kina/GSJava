package br.com.fiap.womanshield.dto;

import br.com.fiap.womanshield.model.Usuaria;

public class UsuariaResponse {

    private Long id;
    private String apelido;
    private String telefone;
    private boolean ativa;

    public static UsuariaResponse de(Usuaria usuaria) {
        UsuariaResponse dto = new UsuariaResponse();
        dto.id = usuaria.getIdUsuaria();
        dto.apelido = usuaria.getApelido();
        dto.telefone = usuaria.getTelefone();
        dto.ativa = usuaria.isAtiva();
        return dto;
    }

    public Long getId() {
        return id;
    }

    public String getApelido() {
        return apelido;
    }

    public String getTelefone() {
        return telefone;
    }

    public boolean isAtiva() {
        return ativa;
    }
}
