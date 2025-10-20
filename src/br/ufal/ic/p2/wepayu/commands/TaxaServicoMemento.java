package br.ufal.ic.p2.wepayu.commands;

import br.ufal.ic.p2.wepayu.models.sindicato.MembroSindicato;
import br.ufal.ic.p2.wepayu.models.sindicato.TaxaServico;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class TaxaServicoMemento implements Memento {
    private final String membroId;
    private final Map<String, MembroSindicato> membrosSindicato;
    private final List<TaxaServico> taxasAnteriores;

    public TaxaServicoMemento(String membroId, Map<String, MembroSindicato> membrosSindicato) {
        this.membroId = membroId;
        this.membrosSindicato = membrosSindicato;

        MembroSindicato membro = membrosSindicato.get(membroId);
        if (membro != null) {
            this.taxasAnteriores = new ArrayList<>(membro.getTaxasDeServicos());
        } else {
            this.taxasAnteriores = new ArrayList<>();
        }
    }

    @Override
    public void restaurar() {
        MembroSindicato membro = membrosSindicato.get(membroId);
        if (membro != null) {
            membro.getTaxasDeServicos().clear();
            membro.getTaxasDeServicos().addAll(taxasAnteriores);
        }
    }
}