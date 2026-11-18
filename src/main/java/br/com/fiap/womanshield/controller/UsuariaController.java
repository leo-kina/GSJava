package br.com.fiap.womanshield.controller;

import br.com.fiap.womanshield.bo.BairroBO;
import br.com.fiap.womanshield.bo.ContatoConfiancaBO;
import br.com.fiap.womanshield.bo.UsuariaBO;
import br.com.fiap.womanshield.dto.ContatoRequest;
import br.com.fiap.womanshield.dto.ContatoResponse;
import br.com.fiap.womanshield.dto.UsuariaRequest;
import br.com.fiap.womanshield.dto.UsuariaResponse;
import br.com.fiap.womanshield.model.ContatoConfianca;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarias")
public class UsuariaController {

    private final UsuariaBO usuariaBO = new UsuariaBO();
    private final ContatoConfiancaBO contatoBO = new ContatoConfiancaBO();
    private final BairroBO bairroBO = new BairroBO();

    @GetMapping
    public List<UsuariaResponse> listar() {
        return usuariaBO.listarTodas().stream()
                .map(UsuariaResponse::de)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UsuariaResponse buscar(@PathVariable Long id) {
        return UsuariaResponse.de(usuariaBO.buscarOuFalhar(id));
    }

    @PostMapping
    public ResponseEntity<UsuariaResponse> cadastrar(@RequestBody UsuariaRequest req) {
        UsuariaResponse corpo = UsuariaResponse.de(usuariaBO.cadastrar(req));
        return ResponseEntity.status(HttpStatus.CREATED).body(corpo);
    }

    @PutMapping("/{id}")
    public UsuariaResponse atualizar(@PathVariable Long id, @RequestBody UsuariaRequest req) {
        return UsuariaResponse.de(usuariaBO.atualizar(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        usuariaBO.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/contatos")
    public List<ContatoResponse> listarContatos(@PathVariable Long id) {
        return contatoBO.listarPorUsuaria(id).stream()
                .map(this::paraResponse)
                .collect(Collectors.toList());
    }

    @PostMapping("/{id}/contatos")
    public ResponseEntity<ContatoResponse> cadastrarContato(@PathVariable Long id, @RequestBody ContatoRequest req) {
        ContatoConfianca contato = contatoBO.cadastrar(id, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(paraResponse(contato));
    }

    private ContatoResponse paraResponse(ContatoConfianca contato) {
        return ContatoResponse.de(contato, bairroBO.buscarOuFalhar(contato.getIdBairro()));
    }
}
