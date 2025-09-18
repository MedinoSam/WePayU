package br.ufal.ic.p2.wepayu.models.empregado;

import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoComissionadoException;
import br.ufal.ic.p2.wepayu.interfaces.EmpregadoInterface;
import br.ufal.ic.p2.wepayu.models.pagamento.MetodoPagamento;
import br.ufal.ic.p2.wepayu.models.sistemasindicato.MembroSindicato;
import br.ufal.ic.p2.wepayu.utils.Utils;

import java.io.Serializable;

public class EmpregadoAssalariado extends  Empregado implements  EmpregadoInterface  {

    private double salarioMensal;

    public EmpregadoAssalariado(String nome, String endereco, MetodoPagamento metodoPagamento, double salarioMensal, String tipo, MembroSindicato membroSindicato) throws IllegalArgumentException {
        super(nome, endereco, metodoPagamento, tipo, membroSindicato);
        setSalarioMensal(validaSalario(salarioMensal));
    }

    public EmpregadoAssalariado() {
    }



    public double getSalarioMensal() {
        return salarioMensal;
    }

    public void setSalarioMensal(double salarioMensal) throws IllegalArgumentException {
        this.salarioMensal = salarioMensal;
    }

    public double validaSalario(double salario) throws IllegalArgumentException {
        if (salario == 0) {
            throw new IllegalArgumentException("Salario nao pode ser nulo.");
        }
        if (salario < 0) {
            throw  new IllegalArgumentException("Salario deve ser nao-negativo.");
        }
        return  salario;
    }
    @Override
    public EmpregadoComissionado alteraComissao(double comissao) throws EmpregadoNaoComissionadoException{
        throw new EmpregadoNaoComissionadoException("Empregado nao eh comissionado");

    }

    @Override
    public EmpregadoComissionado converteEmpregado(Empregado empregado, double comissao) throws Exception {
        return Utils.converteAssalariadoParaComissionado((EmpregadoAssalariado) empregado, comissao);
    }

    @Override
    public void ajustaSalario(double salario) {
        this.salarioMensal = salario;
    }
}
