package br.ufal.ic.p2.wepayu.commands;

import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.Exception.ErroCriacaoEmpregadoException;
import java.util.Map;

public class CriarEmpregadoCommand implements Command {
    private String id;
    private Empregado empregado;
    private Map<String, Empregado> empregados;

    public CriarEmpregadoCommand(Empregado empregado, Map<String, Empregado> empregados) {
        this.empregado = empregado;
        this.empregados = empregados;
    }

    @Override
    public void executar() {
        try {
            this.id = empregado.getId();
            empregados.put(id, empregado);
        } catch (Exception e) {
            throw new ErroCriacaoEmpregadoException("Erro ao criar empregado: " + e.getMessage(), e);
        }
    }

    @Override
    public void desfazer() {
        if (id != null) {
            empregados.remove(id);
        }
    }

    public String getId() {
        return id;
    }
}