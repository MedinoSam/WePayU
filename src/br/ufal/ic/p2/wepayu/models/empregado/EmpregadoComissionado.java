package br.ufal.ic.p2.wepayu.models.empregado;

import br.ufal.ic.p2.wepayu.models.pagamento.MetodoPagamento;

public class EmpregadoComissionado extends Empregado{


    // taxa de comissao tem que vir de algum lugar
    //Uma venda tem uma data, um valor, e uma taxa de comissao
    private double salarioMensal;
    private double taxaDeComissao = 0;

    public EmpregadoComissionado(String nome, String endereco, String id, MetodoPagamento metodoPagamento, double salarioMensal, double taxaDeComissao, String tipo) throws IllegalArgumentException {
        super(nome, endereco, id, metodoPagamento, tipo);
        setSalarioMensal(validaSalario(salarioMensal));
        setTaxaDeComissao(validarComissao(taxaDeComissao));

    }

    public void setTaxaDeComissao(double taxaDeComissao) throws IllegalArgumentException{
        this.taxaDeComissao = taxaDeComissao;
    }

    public void setSalarioMensal(double salarioMensal) throws IllegalArgumentException{
        this.salarioMensal = salarioMensal;
    }

    public double getSalarioMensal() {
        return salarioMensal;
    }

    public double getTaxaDeComissao() {
        return taxaDeComissao;
    }


    public double validaSalario(double salario) throws IllegalArgumentException{
        if (salario == 0) {
            throw new IllegalArgumentException("Salario nao pode ser nulo");
        }
        if (salario < 0) {
            throw  new IllegalArgumentException("Salario nao pode ser negativo");
        }
        return  salario;
    }

    public double validarComissao(double comissao) throws IllegalArgumentException {
        if (comissao == 0) {
            throw new IllegalArgumentException("Comissao nao pode ser nula");
        }
        if (comissao < 0) {
            throw  new IllegalArgumentException("Comissao nao pode ser negativa");
        }
        return  comissao;
    }

    /*@Override
    public double calcularSalario() {
        return this.getSalarioMensal() + (this.getValorEmVendas()*this.getTaxaDeComissao());
    }*/
}
