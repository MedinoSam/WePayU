package br.ufal.ic.p2.wepayu.models.empregado;


import br.ufal.ic.p2.wepayu.models.pagamento.MetodoPagamento;

public class EmpregadoHorista extends Empregado{

    private double salarioPorHora = 0.0;

//    As horas trabalhadas serao retiradas da folha de ponto, devo validar elas la

    public EmpregadoHorista(String nome, String endereco, String id, MetodoPagamento metodoPagamento, double salarioPorHora, String tipo) throws  IllegalArgumentException {
        super(nome, endereco, id, metodoPagamento, tipo);
        setSalarioPorHora(validaTaxaSalarial(salarioPorHora));
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

    //tenho que remover esse metodo calcularSalario, pois é responsabilidade do sistema de pagamento
   /* @Override
    public double calcularSalario() {
        if (this.getHorasTrabalhadas() > 8) {
            int horasExcedentes = this.getHorasTrabalhadas() - 8;
            double novaTaxaSalarial = this.getSalarioPorHora()*1.5;
            return novaTaxaSalarial*horasExcedentes + 8*this.getSalarioPorHora();
        }
        return this.getSalarioPorHora()*this.getHorasTrabalhadas();
    }*/
}
