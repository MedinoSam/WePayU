package br.ufal.ic.p2.wepayu.models.pagamento;


public class EmMaos extends MetodoPagamento {

    public EmMaos() {}

    @Override
    public String getMetodoPagamento() {
        return "emMaos";
    }

    @Override
    public void Pagamento() {
        System.out.println("Em mãos");
    }

}