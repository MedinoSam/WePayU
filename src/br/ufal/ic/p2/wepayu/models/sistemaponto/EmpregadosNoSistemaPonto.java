package br.ufal.ic.p2.wepayu.models.sistemaponto;

import java.util.ArrayList;
import java.util.List;

public class EmpregadosNoSistemaPonto {

    private List<CartaoDePonto> listaDeCartoes = new ArrayList<>();

    public List<CartaoDePonto> getCartoes() {
        return listaDeCartoes;
    }

    public void setCartoes(List<CartaoDePonto> listaDeCartoes) {
        this.listaDeCartoes = listaDeCartoes;
    }

    public void adicionarCartao(CartaoDePonto cartao) {
        listaDeCartoes.add(cartao);
    }
}
