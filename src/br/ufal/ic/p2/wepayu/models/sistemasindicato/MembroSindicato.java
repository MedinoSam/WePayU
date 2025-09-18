package br.ufal.ic.p2.wepayu.models.sistemasindicato;

import java.io.Serializable;

public class MembroSindicato implements Serializable  {

    private String idMembro;
    private double taxaSindical;
    private String idEmpregado;
    private Boolean sindicalizado;

    public MembroSindicato(String idMembro, double taxaSindical, String idEmpregado, Boolean sindicalizado) {
        this.idMembro = idMembro;
        this.taxaSindical = taxaSindical;
        this.idEmpregado = idEmpregado;
        this.sindicalizado = sindicalizado;
    }

    public MembroSindicato() {
    }

    public String getIdMembro() {
        return idMembro;
    }

    public void setIdMembro(String idMembro) {
        this.idMembro = idMembro;
    }

    public double getTaxaSindical() {
        return taxaSindical;
    }

    public void setTaxaSindical(double taxaSindical) {
        this.taxaSindical = taxaSindical;
    }

    public String getIdEmpregado() {
        return idEmpregado;
    }

    public void setIdEmpregado(String idEmpregado) {
        this.idEmpregado = idEmpregado;
    }

    public Boolean getSindicalizado() {
        return sindicalizado;
    }

    public void setSindicalizado(Boolean sindicalizado) {
        this.sindicalizado = sindicalizado;
    }

}
