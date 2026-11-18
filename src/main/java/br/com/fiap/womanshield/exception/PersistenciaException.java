package br.com.fiap.womanshield.exception;

/**
 * Envolve falhas de SQL/JDBC inesperadas (conexao caiu, erro de sintaxe etc.).
 * Vira 500 no GlobalExceptionHandler.
 */
public class PersistenciaException extends RuntimeException {

    public PersistenciaException(String message, Throwable cause) {
        super(message, cause);
    }
}
