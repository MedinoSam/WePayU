package br.ufal.ic.p2.wepayu.Exception;

public class ErroCriacaoMembroSindicatoException extends RuntimeException {
    public ErroCriacaoMembroSindicatoException(String message) {
        super(message);
    }

    public ErroCriacaoMembroSindicatoException(String msg, Throwable cause) {super(msg, cause);}
}
