package br.com.fiap.womanshield.controller;

import br.com.fiap.womanshield.bo.BairroBO;
import br.com.fiap.womanshield.bo.ContatoConfiancaBO;
import br.com.fiap.womanshield.dto.ContatoRequest;
import br.com.fiap.womanshield.dto.ContatoResponse;
import br.com.fiap.womanshield.model.ContatoConfianca;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contatos")
public class ContatoController {

    private final ContatoConfiancaBO contatoBO = new ContatoConfiancaBO();
    private final BairroBO bairroBO = new BairroBO();

    @GetMapping("/{id}")
    public ContatoResponse buscar(@PathVariable Long id) {
        return paraResponse(contatoBO.buscarOuFalhar(id));
    }

    @PutMapping("/{id}")
    public ContatoResponse atualizar(@PathVariable Long id, @RequestBody ContatoRequest req) {
        return paraResponse(contatoBO.atualizar(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        contatoBO.remover(id);
        return ResponseEntity.noContent().build();
    }

    private ContatoResponse paraResponse(ContatoConfianca contato) {
        return ContatoResponse.de(contato, bairroBO.buscarOuFalhar(contato.getIdBairro()));
    }
}
