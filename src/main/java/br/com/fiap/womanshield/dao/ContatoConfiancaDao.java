package br.com.fiap.womanshield.dao;

import br.com.fiap.womanshield.model.ContatoConfianca;

import java.util.List;
import java.util.Optional;

public interface ContatoConfiancaDao {

    List<ContatoConfianca> listarPorUsuaria(Long idUsuaria);

    Optional<ContatoConfianca> buscarPorId(Long idContato);

    int contarPorUsuaria(Long idUsuaria);

    boolean existeTelefoneNaUsuaria(Long idUsuaria, String telefone, Long idContatoParaIgnorar);

    Optional<ContatoConfianca> buscarPrincipal(Long idUsuaria);

    Optional<ContatoConfianca> buscarOutroContato(Long idUsuaria, Long idContatoParaIgnorar);

    ContatoConfianca inserir(ContatoConfianca contato);

    void atualizar(ContatoConfianca contato);

    void marcarPrincipal(Long idContato, boolean principal);

    void desmarcarPrincipalDaUsuaria(Long idUsuaria);

    void remover(Long idContato);

    boolean possuiHistoricoDeAlertas(Long idContato);
}
