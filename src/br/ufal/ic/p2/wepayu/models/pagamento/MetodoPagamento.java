package br.ufal.ic.p2.wepayu.models.pagamento;

import br.ufal.ic.p2.wepayu.Exception.MetodoDePagamentoInvalidoException;

import java.io.Serializable;

public class MetodoPagamento implements Serializable  {

    private String metodo;
    private Banco banco;
    private Boolean receberEmMaos;
    private Boolean receberViaCorreios;
    private Boolean receberViaBanco;

    public MetodoPagamento() {
    }

    /*public String getMetodo() throws MetodoDePagamentoInvalidoException{
        return switch (this.metodo) {
            case "banco" -> "banco";
            case "emMaos" -> "emMaos";
            case "correios" -> "correios";
            default -> throw new MetodoDePagamentoInvalidoException("Metodo de pagamento invalido");
        };
    }*/

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public Boolean getReceberEmMaos() {
        return receberEmMaos;
    }

    public void setReceberEmMaos(Boolean receberEmMaos) {
        this.receberEmMaos = receberEmMaos;
        this.receberViaBanco = false;
        this.receberViaCorreios = false;
    }

    public Boolean getReceberViaCorreios() {
        return receberViaCorreios;
    }

    public void setReceberViaCorreios(Boolean receberViaCorreios) {
        this.receberViaCorreios = receberViaCorreios;
        this.receberViaBanco = false;
        this.receberEmMaos = false;
    }

    public Boolean getReceberViaBanco() {
        return receberViaBanco;
    }

    public void setReceberViaBanco(Boolean receberViaBanco) {
        this.receberViaBanco = receberViaBanco;
        this.receberEmMaos = false;
        this.receberViaCorreios = false;
    }
}
