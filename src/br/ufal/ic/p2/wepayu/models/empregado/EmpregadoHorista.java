package br.ufal.ic.p2.wepayu.models.empregado;

import br.ufal.ic.p2.wepayu.models.pagamento.AgendaPagamento;
import br.ufal.ic.p2.wepayu.models.CartaoDePonto;

import java.util.ArrayList;


public class EmpregadoHorista extends Empregado {
    private double salarioPorHora;
    private ArrayList<CartaoDePonto> cartoes = new  ArrayList<>();

    public EmpregadoHorista() { }

    public EmpregadoHorista(String nome, String endereco, double salario) {
        super(nome, endereco);
        this.salarioPorHora = salario;
        this.setAgendaPagamento(AgendaPagamento.getAgendaPadrao("horista"));
    }

    public double getSalarioPorHora() {
        return salarioPorHora;
    }

    public void setSalarioPorHora(double salarioPorHora) {
        this.salarioPorHora = salarioPorHora;
    }

    @Override
    public ArrayList<CartaoDePonto> getCartoes() { return this.cartoes; }

    public void setCartoes(ArrayList<CartaoDePonto> cartoes) {
        this.cartoes = cartoes;
    }

    @Override
    public void lancarCartao(CartaoDePonto cartao) {
        this.cartoes.add(cartao);
    }

    @Override
    public String getTipo() {
        return "horista";
    }

    @Override
    public String getSalario() {
        return truncarValorMonetario(this.salarioPorHora);
    }

    private String truncarValorMonetario(double valor) {
        java.math.BigDecimal bd = java.math.BigDecimal.valueOf(valor);
        bd = bd.setScale(2, java.math.RoundingMode.DOWN);
        return bd.toString().replace('.', ',');
    }
}