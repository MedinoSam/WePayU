package br.ufal.ic.p2.wepayu.services;

import br.ufal.ic.p2.wepayu.Exception.*;


public interface FolhaPagamentoService {
    String totalFolha(String data) throws DataInvalidaException;

    void rodaFolha(String data, String arquivo) throws DataInvalidaException;
}