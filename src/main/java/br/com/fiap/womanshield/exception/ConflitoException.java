package br.com.fiap.womanshield.exception;

/**
 * Telefone ja cadastrado, limite de 6 contatos, contato com historico de alertas.
 * Vira 409 no GlobalExceptionHandler.
 */
public class ConflitoException extends RuntimeException {

    public ConflitoException(String message) {
        super(message);
    }
}
