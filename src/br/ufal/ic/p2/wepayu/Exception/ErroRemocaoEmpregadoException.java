package br.ufal.ic.p2.wepayu.Exception;

public class ErroRemocaoEmpregadoException extends RuntimeException {
    public ErroRemocaoEmpregadoException(String message) {
        super(message);
    }

    public ErroRemocaoEmpregadoException(String msg, Throwable cause) {super(msg, cause);}
}
