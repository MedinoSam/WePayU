package br.ufal.ic.p2.wepayu.Exception;

public class ErroLancamentoVendaException extends RuntimeException {
    public ErroLancamentoVendaException(String message) {
        super(message);
    }

    public ErroLancamentoVendaException(String msg, Throwable cause) {super(msg, cause);}
}
