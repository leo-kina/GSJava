package br.com.fiap.womanshield.exception;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Campo obrigatorio vazio ou invalido. Vira 400 no GlobalExceptionHandler.
 */
public class CampoInvalidoException extends RuntimeException {

    private final Map<String, String> campos = new LinkedHashMap<>();

    public CampoInvalidoException(String mensagemGeral) {
        super(mensagemGeral);
    }

    public CampoInvalidoException(String mensagemGeral, String campo, String motivo) {
        super(mensagemGeral);
        this.campos.put(campo, motivo);
    }

    public CampoInvalidoException campo(String campo, String motivo) {
        this.campos.put(campo, motivo);
        return this;
    }

    public Map<String, String> getCampos() {
        return campos;
    }
}
