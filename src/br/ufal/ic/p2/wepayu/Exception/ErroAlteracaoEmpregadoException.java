package br.ufal.ic.p2.wepayu.Exception;

public class ErroAlteracaoEmpregadoException extends RuntimeException {
    public ErroAlteracaoEmpregadoException(String message) {
        super(message);
    }

    public ErroAlteracaoEmpregadoException(String msg, Throwable cause) {super(msg, cause);}
}
