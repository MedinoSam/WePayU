package br.ufal.ic.p2.wepayu.Exception;

public class EmpregadoNaoExisteComNomeException extends RuntimeException {
    public EmpregadoNaoExisteComNomeException(String message) {
        super(message);
    }
}
