package br.ufal.ic.p2.wepayu.models.empregado;


import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoComissionadoException;
import br.ufal.ic.p2.wepayu.interfaces.EmpregadoInterface;
import br.ufal.ic.p2.wepayu.models.pagamento.MetodoPagamento;
import br.ufal.ic.p2.wepayu.models.sistemasindicato.MembroSindicato;
import br.ufal.ic.p2.wepayu.utils.Utils;

import java.io.Serializable;

public class EmpregadoHorista extends Empregado implements EmpregadoInterface {

    private double salarioPorHora = 0.0;

//    As horas trabalhadas serao retiradas da folha de ponto, devo validar elas la

    public EmpregadoHorista(String nome, String endereco, MetodoPagamento metodoPagamento, double salarioPorHora, String tipo, MembroSindicato membroSindicato) throws  IllegalArgumentException {
        super(nome, endereco, metodoPagamento, tipo, membroSindicato);
        setSalarioPorHora(validaTaxaSalarial(salarioPorHora));
    }

    public EmpregadoHorista() {

    }

    public double getSalarioPorHora() {
        return salarioPorHora;
    }


    public void setSalarioPorHora(double salarioPorHora) throws IllegalArgumentException {
        this.salarioPorHora = salarioPorHora;
    }


    public double validaTaxaSalarial(double taxaSalarial) throws IllegalArgumentException {
        if (taxaSalarial == 0) {
            throw new IllegalArgumentException("Taxa salarial nao pode ser nula");
        }
        if (taxaSalarial < 0) {
            throw  new IllegalArgumentException("Taxa salarial nao pode ser negativa");
        }
        return  taxaSalarial;
    }

    @Override
    public EmpregadoComissionado alteraComissao(double comissao) throws EmpregadoNaoComissionadoException{
        throw new EmpregadoNaoComissionadoException("Empregado nao eh comissionado");
    }

    @Override
    public EmpregadoComissionado converteEmpregado(Empregado empregado, double comissao) throws Exception {
        return Utils.converterHoristaParaComissionado((EmpregadoHorista) empregado, comissao);
    }

    @Override
    public void ajustaSalario(double salario) {
        this.salarioPorHora = salario;
   }
}
