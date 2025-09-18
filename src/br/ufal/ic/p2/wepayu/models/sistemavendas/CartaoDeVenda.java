package br.ufal.ic.p2.wepayu.models.sistemavendas;

import java.io.Serializable;
import java.time.LocalDate;

public class CartaoDeVenda implements Serializable {
    private String idEmpregado;
    private String data;
    private double valor;

    public CartaoDeVenda(String idEmpregado, String data, double valor) {
        this.idEmpregado = idEmpregado;
        this.data = data;
        this.valor = valor;
    }

    public CartaoDeVenda() {
    }

    public String getIdEmpregado() {
        return idEmpregado;
    }

    public void setIdEmpregado(String idEmpregado) {
        this.idEmpregado = idEmpregado;
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
