package br.com.fiap.womanshield.model;

public class Disponibilidade {

    private Long idDisponibilidade;
    private Long idContato;
    private Integer diaSemana;
    private Integer horaInicio;
    private Integer horaFim;

    public Disponibilidade() {
    }

    public Disponibilidade(Integer diaSemana, Integer horaInicio, Integer horaFim) {
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
    }

    public Long getIdDisponibilidade() {
        return idDisponibilidade;
    }

    public void setIdDisponibilidade(Long idDisponibilidade) {
        this.idDisponibilidade = idDisponibilidade;
    }

    public Long getIdContato() {
        return idContato;
    }

    public void setIdContato(Long idContato) {
        this.idContato = idContato;
    }

    public Integer getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(Integer diaSemana) {
        this.diaSemana = diaSemana;
    }

    public Integer getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Integer horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Integer getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(Integer horaFim) {
        this.horaFim = horaFim;
    }

    /**
     * Atravessa a meia-noite quando o fim e menor ou igual ao inicio (ex.: 22h as 6h).
     */
    public boolean atravessaMeiaNoite() {
        return horaFim <= horaInicio;
    }

    /**
     * Regra do LEIA-ME: considera o dia d e a hora h do alerta.
     * Sem atravessar meia-noite: disponivel quando dia == d e inicio <= h < fim.
     * Atravessando meia-noite: disponivel quando dia == d e h >= inicio, OU dia == (d+6)%7 e h < fim.
     */
    public boolean cobre(int diaAlerta, int horaAlerta) {
        if (!atravessaMeiaNoite()) {
            return diaSemana == diaAlerta && horaInicio <= horaAlerta && horaAlerta < horaFim;
        }
        boolean mesmoDiaAposInicio = diaSemana == diaAlerta && horaAlerta >= horaInicio;
        boolean diaAnteriorAntesDoFim = diaSemana == ((diaAlerta + 6) % 7) && horaAlerta < horaFim;
        return mesmoDiaAposInicio || diaAnteriorAntesDoFim;
    }
}
