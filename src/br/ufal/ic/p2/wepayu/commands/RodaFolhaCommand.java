package br.ufal.ic.p2.wepayu.commands;

import br.ufal.ic.p2.wepayu.Exception.DataInvalidaException;
import br.ufal.ic.p2.wepayu.services.FolhaPagamentoService;


public class RodaFolhaCommand implements Command {
    private String data;
    private String arquivo;
    private FolhaPagamentoService folhaPagamentoService;

    public RodaFolhaCommand(String data, String arquivo, FolhaPagamentoService folhaPagamentoService) {
        this.data = data;
        this.arquivo = arquivo;
        this.folhaPagamentoService = folhaPagamentoService;
    }

    @Override
    public void executar() {
        try {
            folhaPagamentoService.rodaFolha(data, arquivo);
        } catch (DataInvalidaException e) {
            // Re-lança como RuntimeException para manter compatibilidade com Command
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao rodar folha: " + e.getMessage(), e);
        }
    }

    @Override
    public void desfazer() {

    }
}