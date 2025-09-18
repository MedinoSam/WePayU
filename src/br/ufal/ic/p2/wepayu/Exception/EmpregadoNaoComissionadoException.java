package br.ufal.ic.p2.wepayu.Exception;

public class EmpregadoNaoComissionadoException extends RuntimeException{
    public EmpregadoNaoComissionadoException(String msg){
        super("Empregado nao eh comissionado.");
    }

}
