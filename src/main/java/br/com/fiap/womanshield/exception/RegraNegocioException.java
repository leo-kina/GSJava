package br.com.fiap.womanshield.exception;

/**
 * Alerta sem nenhum contato, ou usuaria desativada. Vira 422 no GlobalExceptionHandler.
 */
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String message) {
        super(message);
    }
}
