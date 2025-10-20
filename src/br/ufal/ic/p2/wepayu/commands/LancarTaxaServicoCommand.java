package br.ufal.ic.p2.wepayu.commands;

import br.ufal.ic.p2.wepayu.models.sindicato.MembroSindicato;
import br.ufal.ic.p2.wepayu.models.sindicato.TaxaServico;
import br.ufal.ic.p2.wepayu.Exception.MembroSindicatoNaoEncontradoException;
import br.ufal.ic.p2.wepayu.Exception.ErroLancamentoTaxaServicoException;
import java.util.Map;


public class LancarTaxaServicoCommand implements Command {
    private String membroId;
    private String data;
    private String valor;
    private Map<String, MembroSindicato> membrosSindicato;
    private TaxaServicoMemento memento;

    public LancarTaxaServicoCommand(String membroId, String data, String valor, Map<String, MembroSindicato> membrosSindicato) {
        this.membroId = membroId;
        this.data = data;
        this.valor = valor;
        this.membrosSindicato = membrosSindicato;
    }

    @Override
    public void executar() {
        try {
            MembroSindicato membro = membrosSindicato.get(membroId);
            if (membro == null) {
                throw new MembroSindicatoNaoEncontradoException("Membro nao existe.");
            }

            memento = new TaxaServicoMemento(membroId, membrosSindicato);

            TaxaServico taxaServico = new TaxaServico(data, valor);
            membro.getTaxasDeServicos().add(taxaServico);
        } catch (MembroSindicatoNaoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            throw new ErroLancamentoTaxaServicoException("Erro ao lançar taxa de serviço: " + e.getMessage(), e);
        }
    }

    @Override
    public void desfazer() {
        if (memento != null) {
            memento.restaurar();
        }
    }
}