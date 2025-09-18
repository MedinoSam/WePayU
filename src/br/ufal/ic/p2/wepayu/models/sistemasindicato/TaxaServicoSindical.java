package br.ufal.ic.p2.wepayu.models.sistemasindicato;

import java.io.Serializable;
import java.time.LocalDate;

public class TaxaServicoSindical implements Serializable  {

    private String idMembro;
    private String data;
    private double valor;

    public TaxaServicoSindical(String idMembro, String data, double valor) {
        this.idMembro = idMembro;
        this.data = data;
        this.valor = valor;
    }

    public TaxaServicoSindical() {
    }

    public String getIdMembro() {
        return idMembro;
    }

    public void setIdMembro(String idMembro) {
        this.idMembro = idMembro;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
