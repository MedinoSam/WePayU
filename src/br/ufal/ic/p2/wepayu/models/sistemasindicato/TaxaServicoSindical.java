package br.ufal.ic.p2.wepayu.models.sistemasindicato;

import java.time.LocalDate;

public class TaxaServicoSindical {

    private String idMembro;
    private LocalDate data;
    private double valor;

    public TaxaServicoSindical(String idMembro, LocalDate data, double valor) {
        this.idMembro = idMembro;
        this.data = data;
        this.valor = valor;
    }

    public String getIdMembro() {
        return idMembro;
    }

    public void setIdMembro(String idMembro) {
        this.idMembro = idMembro;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
