package br.ufal.ic.p2.wepayu.models.sistemaponto;

import br.ufal.ic.p2.wepayu.Exception.DataInvalidaException;
import br.ufal.ic.p2.wepayu.utils.Utils;

import java.io.Serializable;


public class CartaoDePonto implements Serializable {

    private String idEmpregado;
    private String data;
    private String horas;

    public CartaoDePonto(String data, String horas, String idEmpregado) {
        this.data = data;
        this.idEmpregado = idEmpregado;
        setHoras(validaHora(horas));
    }

    public CartaoDePonto() {
    }

    public void setHoras(String horas) {
        this.horas = horas;
    }

    public String getIdEmpregado() {
        return idEmpregado;
    }

    public String getData() {
        return data;
    }

    public String getHoras() {
        return horas;
    }

    public void setIdEmpregado(String idEmpregado) {
        this.idEmpregado = idEmpregado;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String validaHora (String hora) throws DataInvalidaException {
            if (hora.equalsIgnoreCase("0")) {
                throw  new DataInvalidaException("O numero de horas nao pode ser nulo");
            }
            double horasEmDouble = Utils.converteQualquerAtributoStringParaDouble(hora);
            if (horasEmDouble < 0) {
                throw  new DataInvalidaException("O numero de horas nao pode ser negativo");
            }
            return hora;
    }
}
