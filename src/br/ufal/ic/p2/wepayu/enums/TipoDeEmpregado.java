package br.ufal.ic.p2.wepayu.enums;

import br.ufal.ic.p2.wepayu.Exception.AtributoNaoExisteException;

public enum TipoDeEmpregado {

    ASSALARIADO("assalariado"),
    HORISTA("horista"),
    COMISSIONADO("comissionado");

    private String value;

    TipoDeEmpregado(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static void validarTipo(String tipo) throws AtributoNaoExisteException {
        for (TipoDeEmpregado tipoDeEmpregado : TipoDeEmpregado.values()) {
            if (tipoDeEmpregado.getValue().equalsIgnoreCase(tipo)) {
                return;
            }
        }
        throw new AtributoNaoExisteException("Tipo invalido.");
    }
}
