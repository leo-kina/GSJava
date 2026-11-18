package br.com.fiap.womanshield.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErroResposta {

    private final int status;
    private final String erro;
    private final Map<String, String> campos;

    public ErroResposta(int status, String erro, Map<String, String> campos) {
        this.status = status;
        this.erro = erro;
        this.campos = (campos == null || campos.isEmpty()) ? null : campos;
    }

    public int getStatus() {
        return status;
    }

    public String getErro() {
        return erro;
    }

    public Map<String, String> getCampos() {
        return campos;
    }
}
