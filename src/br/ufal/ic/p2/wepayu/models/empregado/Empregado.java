package br.ufal.ic.p2.wepayu.models.empregado;

import br.ufal.ic.p2.wepayu.models.*;
import br.ufal.ic.p2.wepayu.models.pagamento.AgendaPagamento;
import br.ufal.ic.p2.wepayu.models.pagamento.EmMaos;
import br.ufal.ic.p2.wepayu.models.pagamento.MetodoPagamento;
import br.ufal.ic.p2.wepayu.models.sindicato.MembroSindicato;

import java.util.ArrayList;


public abstract class Empregado {
    private String Id;
    private String nome;
    private String endereco;
    private MembroSindicato sindicato;
    private MetodoPagamento metodoPagamento = new EmMaos();
    private AgendaPagamento agendaPagamento;

    public Empregado() { }


    public Empregado(String nome, String endereco) {
        setNome(nome);
        setEndereco(endereco);
        this.agendaPagamento = new AgendaPagamento();
    }


    public final String getNome() {
        return nome;
    }


    public final void setNome(String nome) { this.nome = nome; }


    public final String getEndereco() {
        return endereco;
    }

    public final void setEndereco(String endereco) { this.endereco = endereco; }

    public abstract String getTipo();

    public abstract String getSalario();

    public final String ehSindicalizado(){
        if(sindicato == null){
            return "false";
        }
        return "true";
    }

    public void setTaxaDeComissao(double taxaDeComissao) { }

    public double getTaxaDeComissao() { return 0; }

    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(MetodoPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public ArrayList<CartaoDePonto> getCartoes() { return null; }

    public ArrayList<ResultadoDeVenda> getResultadoDeVenda() { return null; }

    public final String getId() {
        return Id;
    }

    public final void setId(String Id) {
        this.Id = Id;
    }

    public void lancarCartao(CartaoDePonto cartao) { }

    public void lancarResultadoDeVenda(ResultadoDeVenda resultadoDeVenda) { }

    public final MembroSindicato getSindicato() {
        return sindicato;
    }

    public final void setSindicato(MembroSindicato sindicato) {
        this.sindicato = sindicato;
    }


    public final AgendaPagamento getAgendaPagamento() {
        return agendaPagamento;
    }

    public final void setAgendaPagamento(AgendaPagamento agendaPagamento) {
        this.agendaPagamento = agendaPagamento;
    }

    public final void setAgendaPagamento(String agenda) {
        this.agendaPagamento = new AgendaPagamento(agenda);
    }
}