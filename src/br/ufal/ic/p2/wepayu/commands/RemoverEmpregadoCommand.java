package br.ufal.ic.p2.wepayu.commands;

import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoEncontradoException;
import br.ufal.ic.p2.wepayu.Exception.ErroRemocaoEmpregadoException;
import java.util.Map;

public class RemoverEmpregadoCommand implements Command {
    private String id;
    private Empregado empregadoRemovido;
    private Map<String, Empregado> empregados;

    public RemoverEmpregadoCommand(String id, Map<String, Empregado> empregados) {
        this.id = id;
        this.empregados = empregados;
    }

    @Override
    public void executar() {
        try {
            this.empregadoRemovido = empregados.get(id);
            if (empregadoRemovido == null) {
                throw new EmpregadoNaoEncontradoException("Empregado não encontrado: " + id);
            }
            empregados.remove(id);
        } catch (EmpregadoNaoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            throw new ErroRemocaoEmpregadoException("Erro ao remover empregado: " + e.getMessage(), e);
        }
    }

    @Override
    public void desfazer() {
        if (empregadoRemovido != null) {
            empregados.put(id, empregadoRemovido);
        }
    }
}