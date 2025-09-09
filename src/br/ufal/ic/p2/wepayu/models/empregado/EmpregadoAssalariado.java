package br.ufal.ic.p2.wepayu.models.empregado;

import br.ufal.ic.p2.wepayu.models.pagamento.MetodoPagamento;

public class EmpregadoAssalariado extends  Empregado{

    private double salarioMensal;

    public EmpregadoAssalariado(String nome, String endereco, String id, MetodoPagamento metodoPagamento, double salarioMensal, String tipo) throws IllegalArgumentException {
        super(nome, endereco, id, metodoPagamento, tipo);
        setSalarioMensal(validaSalario(salarioMensal));
    }

    public double getSalarioMensal() {
        return salarioMensal;
    }

    public void setSalarioMensal(double salarioMensal) throws IllegalArgumentException {
        this.salarioMensal = salarioMensal;
    }

    public double validaSalario(double salario) throws IllegalArgumentException {
        if (salario == 0) {
            throw new IllegalArgumentException("Salario nao pode ser nulo");
        }
        if (salario < 0) {
            throw  new IllegalArgumentException("Salario nao pode ser negativo");
        }
        return  salario;
    }

   /* @Override
    public double calcularSalario() {
        return this.getSalarioMensal();
    }*/
}
