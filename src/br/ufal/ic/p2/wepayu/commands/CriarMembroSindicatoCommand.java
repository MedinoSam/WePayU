package br.ufal.ic.p2.wepayu.commands;

import br.ufal.ic.p2.wepayu.models.sindicato.MembroSindicato;
import br.ufal.ic.p2.wepayu.Exception.ErroCriacaoMembroSindicatoException;
import java.util.Map;

public class CriarMembroSindicatoCommand implements Command {
    private MembroSindicato membro;
    private Map<String, MembroSindicato> membrosSindicato;

    public CriarMembroSindicatoCommand(MembroSindicato membro, Map<String, MembroSindicato> membrosSindicato) {
        this.membro = membro;
        this.membrosSindicato = membrosSindicato;
    }

    @Override
    public void executar() {
        try {
            membrosSindicato.put(membro.getIdMembro(), membro);
        } catch (Exception e) {
            throw new ErroCriacaoMembroSindicatoException("Erro ao criar membro do sindicato: " + e.getMessage(), e);
        }
    }

    @Override
    public void desfazer() {
        membrosSindicato.remove(membro.getIdMembro());
    }
}