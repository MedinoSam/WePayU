package br.ufal.ic.p2.wepayu.models.empregado;


import br.ufal.ic.p2.wepayu.models.pagamento.AgendaPagamento;

public class EmpregadoAssalariado extends Empregado {
    private double salarioMensal;

    public EmpregadoAssalariado() { }

    public EmpregadoAssalariado(String nome, String endereco, double salario){
        super(nome, endereco);
        setSalarioMensal(salario);
        this.setAgendaPagamento(AgendaPagamento.getAgendaPadrao("assalariado"));
    }

    public double getSalarioMensal() {
        return salarioMensal;
    }

    public void setSalarioMensal(double salarioMensal) { this.salarioMensal = salarioMensal; }

    @Override
    public String getTipo() {
        return "assalariado";
    }

    @Override
    public String getSalario() {
        return forcarValorMonetario(this.salarioMensal);
    }

    private String forcarValorMonetario(double valor) {
        java.math.BigDecimal bd = java.math.BigDecimal.valueOf(valor);
        bd = bd.setScale(2, java.math.RoundingMode.DOWN);
        return bd.toString().replace('.', ',');
    }
}