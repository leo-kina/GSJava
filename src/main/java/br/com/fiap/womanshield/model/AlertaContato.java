package br.com.fiap.womanshield.model;

public class AlertaContato {

    private Long idAlerta;
    private Long idContato;
    private Integer ordem;
    private Integer minutosEstimados;
    private boolean disponivel;
    private boolean avisado;

    // Dados do contato, carregados por JOIN para montar a resposta sem ida extra ao banco.
    private String nomeContato;
    private String telefoneContato;

    // Nao persistido: usado so na hora de ordenar os contatos antes de gravar o alerta (ver AlertaBO).
    private boolean principalTemp;

    public Long getIdAlerta() {
        return idAlerta;
    }

    public void setIdAlerta(Long idAlerta) {
        this.idAlerta = idAlerta;
    }

    public Long getIdContato() {
        return idContato;
    }

    public void setIdContato(Long idContato) {
        this.idContato = idContato;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    public Integer getMinutosEstimados() {
        return minutosEstimados;
    }

    public void setMinutosEstimados(Integer minutosEstimados) {
        this.minutosEstimados = minutosEstimados;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public boolean isAvisado() {
        return avisado;
    }

    public void setAvisado(boolean avisado) {
        this.avisado = avisado;
    }

    public String getNomeContato() {
        return nomeContato;
    }

    public void setNomeContato(String nomeContato) {
        this.nomeContato = nomeContato;
    }

    public String getTelefoneContato() {
        return telefoneContato;
    }

    public void setTelefoneContato(String telefoneContato) {
        this.telefoneContato = telefoneContato;
    }

    public boolean isPrincipalTemp() {
        return principalTemp;
    }

    public void setPrincipalTemp(boolean principalTemp) {
        this.principalTemp = principalTemp;
    }
}
