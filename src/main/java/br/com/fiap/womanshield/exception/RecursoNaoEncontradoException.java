package br.com.fiap.womanshield.exception;

/**
 * Usuaria, contato, bairro ou alerta nao encontrado. Vira 404 no GlobalExceptionHandler.
 */
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String message) {
        super(message);
    }
}
