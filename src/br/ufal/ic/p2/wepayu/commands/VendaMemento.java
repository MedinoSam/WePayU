package br.ufal.ic.p2.wepayu.commands;

import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.models.empregado.EmpregadoComissionado;
import br.ufal.ic.p2.wepayu.models.ResultadoDeVenda;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class VendaMemento implements Memento {
    private final String empId;
    private final Map<String, Empregado> empregados;
    private final List<ResultadoDeVenda> vendasAnteriores;

    public VendaMemento(String empId, Map<String, Empregado> empregados) {
        this.empId = empId;
        this.empregados = empregados;

        Empregado empregado = empregados.get(empId);
        if (empregado instanceof EmpregadoComissionado) {
            EmpregadoComissionado comissionado = (EmpregadoComissionado) empregado;
            this.vendasAnteriores = new ArrayList<>(comissionado.getResultadoDeVenda());
        } else {
            this.vendasAnteriores = new ArrayList<>();
        }
    }

    @Override
    public void restaurar() {
        Empregado empregado = empregados.get(empId);
        if (empregado instanceof EmpregadoComissionado) {
            EmpregadoComissionado comissionado = (EmpregadoComissionado) empregado;
            comissionado.getResultadoDeVenda().clear();
            comissionado.getResultadoDeVenda().addAll(vendasAnteriores);
        }
    }
}