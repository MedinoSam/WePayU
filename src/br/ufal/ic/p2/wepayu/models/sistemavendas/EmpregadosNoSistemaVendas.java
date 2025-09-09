package br.ufal.ic.p2.wepayu.models.sistemavendas;

import java.util.ArrayList;
import java.util.List;

public class EmpregadosNoSistemaVendas {
    private List<CartaoDeVenda> listaDeVendas = new ArrayList<>();

    public List<CartaoDeVenda> getListaDeVendas() {
        return listaDeVendas;
    }

    public void setListaDeVendas(List<CartaoDeVenda> listaDeVendas) {
        this.listaDeVendas = listaDeVendas;
    }

    public void adicionarVenda(CartaoDeVenda venda) {
        listaDeVendas.add(venda);
    }
}
