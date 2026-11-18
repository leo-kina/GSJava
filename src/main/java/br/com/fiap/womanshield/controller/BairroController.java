package br.com.fiap.womanshield.controller;

import br.com.fiap.womanshield.bo.BairroBO;
import br.com.fiap.womanshield.dto.BairroResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bairros")
public class BairroController {

    private final BairroBO bairroBO = new BairroBO();

    @GetMapping
    public List<BairroResponse> listar() {
        return bairroBO.listarTodos().stream()
                .map(BairroResponse::de)
                .collect(Collectors.toList());
    }
}
