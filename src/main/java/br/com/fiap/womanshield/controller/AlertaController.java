package br.com.fiap.womanshield.controller;

import br.com.fiap.womanshield.bo.AlertaBO;
import br.com.fiap.womanshield.bo.AlertaDetalhado;
import br.com.fiap.womanshield.dto.AlertaRequest;
import br.com.fiap.womanshield.dto.AlertaResponse;
import br.com.fiap.womanshield.exception.CampoInvalidoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/alertas")
public class AlertaController {

    private final AlertaBO alertaBO = new AlertaBO();

    @PostMapping
    public ResponseEntity<AlertaResponse> registrar(@RequestBody AlertaRequest req) {
        AlertaDetalhado detalhado = alertaBO.registrar(req);
        AlertaResponse corpo = AlertaResponse.de(detalhado.alerta(), detalhado.bairro(), detalhado.periodo());
        return ResponseEntity.status(HttpStatus.CREATED).body(corpo);
    }

    @GetMapping
    public List<AlertaResponse> listarPorUsuaria(@RequestParam(required = false) Long idUsuaria) {
        if (idUsuaria == null) {
            throw new CampoInvalidoException("Confira os campos destacados.", "idUsuaria",
                    "Informe idUsuaria para consultar o historico de alertas.");
        }
        return alertaBO.listarPorUsuaria(idUsuaria).stream()
                .map(d -> AlertaResponse.de(d.alerta(), d.bairro(), d.periodo()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AlertaResponse buscar(@PathVariable Long id) {
        AlertaDetalhado detalhado = alertaBO.buscarOuFalhar(id);
        return AlertaResponse.de(detalhado.alerta(), detalhado.bairro(), detalhado.periodo());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        alertaBO.remover(id);
        return ResponseEntity.noContent().build();
    }
}
