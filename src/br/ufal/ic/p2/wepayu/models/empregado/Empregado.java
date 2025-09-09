package br.ufal.ic.p2.wepayu.models.empregado;

import br.ufal.ic.p2.wepayu.models.pagamento.MetodoPagamento;

public  class Empregado {


    private String id;
    private String nome;
    private String endereco;
    private String tipo;
    private MetodoPagamento metodoPagamento;


    public Empregado(String nome, String endereco, String id, MetodoPagamento metodoPagamento, String tipo)  {
        this.nome = nome;
        this.endereco = endereco;
        this.id = id;
        this.metodoPagamento = metodoPagamento;
        this.tipo = tipo;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }
}
