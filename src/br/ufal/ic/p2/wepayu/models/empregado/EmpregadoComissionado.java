package br.ufal.ic.p2.wepayu.models.empregado;

import br.ufal.ic.p2.wepayu.interfaces.EmpregadoInterface;
import br.ufal.ic.p2.wepayu.models.pagamento.MetodoPagamento;
import br.ufal.ic.p2.wepayu.models.sistemasindicato.MembroSindicato;
import br.ufal.ic.p2.wepayu.utils.Utils;

import java.io.Serializable;

public class EmpregadoComissionado extends Empregado implements EmpregadoInterface {


    // taxa de comissao tem que vir de algum lugar
    //Uma venda tem uma data, um valor, e uma taxa de comissao
    private double salarioMensal;
    private double taxaDeComissao = 0;

    public EmpregadoComissionado(String nome, String endereco, MetodoPagamento metodoPagamento, double salarioMensal, double taxaDeComissao, String tipo, MembroSindicato membroSindicato) throws IllegalArgumentException {
        super(nome, endereco, metodoPagamento, tipo, membroSindicato);
        setSalarioMensal(validaSalario(salarioMensal));
        setTaxaDeComissao(validarComissao(taxaDeComissao));

    }

    public EmpregadoComissionado() {

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
            throw new IllegalArgumentException("Salario nao pode ser nulo.");
        }
        if (salario < 0) {
            throw  new IllegalArgumentException("Salario deve ser nao-negativo.");
        }
        return  salario;
    }

    public double validarComissao(double comissao) throws IllegalArgumentException {
        if (comissao == 0) {
            throw new IllegalArgumentException("Comissao nao pode ser nula.");
        }
        if (comissao < 0) {
            throw  new IllegalArgumentException("Comissao deve ser nao-negativa.");
        }
        return  comissao;
    }

    @Override
    public EmpregadoComissionado alteraComissao(double comissao) {
        this.taxaDeComissao = comissao;
        return this;
    }

    @Override
    public EmpregadoComissionado converteEmpregado(Empregado empregado, double comissao) throws Exception {
        return Utils.converterHoristaParaComissionado((EmpregadoHorista) empregado, comissao);
    }

    @Override
    public void ajustaSalario(double salario) {
        this.salarioMensal = salario;
    }
}
