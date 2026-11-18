package br.com.fiap.womanshield.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Formato de erro padronizado da API (ver secao "Formato de erro" do LEIA-ME):
 * { "status": 400, "erro": "...", "campos": { "telefone": "..." } }
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CampoInvalidoException.class)
    public ResponseEntity<ErroResposta> tratarCampoInvalido(CampoInvalidoException e) {
        ErroResposta corpo = new ErroResposta(400, mensagemOuPadrao(e.getMessage()), e.getCampos());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(corpo);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResposta> tratarValidacaoBean(MethodArgumentNotValidException e) {
        Map<String, String> campos = new LinkedHashMap<>();
        for (FieldError erro : e.getBindingResult().getFieldErrors()) {
            campos.put(erro.getField(), erro.getDefaultMessage());
        }
        ErroResposta corpo = new ErroResposta(400, "Confira os campos destacados.", campos);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(corpo);
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResposta> tratarNaoEncontrado(RecursoNaoEncontradoException e) {
        ErroResposta corpo = new ErroResposta(404, e.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(corpo);
    }

    @ExceptionHandler(ConflitoException.class)
    public ResponseEntity<ErroResposta> tratarConflito(ConflitoException e) {
        ErroResposta corpo = new ErroResposta(409, e.getMessage(), null);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(corpo);
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroResposta> tratarRegraNegocio(RegraNegocioException e) {
        ErroResposta corpo = new ErroResposta(422, e.getMessage(), null);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(corpo);
    }

    @ExceptionHandler(PersistenciaException.class)
    public ResponseEntity<ErroResposta> tratarPersistencia(PersistenciaException e) {
        ErroResposta corpo = new ErroResposta(500, "Erro inesperado ao acessar o banco de dados.", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(corpo);
    }

    private String mensagemOuPadrao(String mensagem) {
        return (mensagem == null || mensagem.isBlank()) ? "Confira os campos destacados." : mensagem;
    }
}
