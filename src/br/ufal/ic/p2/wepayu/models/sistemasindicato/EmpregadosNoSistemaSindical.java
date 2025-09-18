package br.ufal.ic.p2.wepayu.models.sistemasindicato;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EmpregadosNoSistemaSindical implements Serializable  {

    private List<TaxaServicoSindical> listaDeTaxas = new ArrayList<>();

    public List<TaxaServicoSindical> getListaDeTaxas() {
        return listaDeTaxas;
    }

    public void setListaDeTaxas(List<TaxaServicoSindical> listaDeTaxas) {
        this.listaDeTaxas = listaDeTaxas;
    }

    public void adicionarTaxa(TaxaServicoSindical taxa) {
        listaDeTaxas.add(taxa);
    }

    public EmpregadosNoSistemaSindical() {
    }
}
