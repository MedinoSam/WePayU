package br.ufal.ic.p2.wepayu.Exception;

public class ErroExecucaoComandoException extends RuntimeException {
    public ErroExecucaoComandoException(String message) {
        super(message);
    }

    public ErroExecucaoComandoException(String msg, Throwable cause) {super(msg, cause);}
}
