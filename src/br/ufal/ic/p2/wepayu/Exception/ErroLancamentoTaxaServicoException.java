package br.ufal.ic.p2.wepayu.Exception;

public class ErroLancamentoTaxaServicoException extends RuntimeException {
    public ErroLancamentoTaxaServicoException(String message) {
        super(message);
    }

    public ErroLancamentoTaxaServicoException(String msg, Throwable cause) {super(msg, cause);}
}
