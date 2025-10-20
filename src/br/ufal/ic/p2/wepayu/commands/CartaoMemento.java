package br.ufal.ic.p2.wepayu.commands;

import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.models.empregado.EmpregadoHorista;
import br.ufal.ic.p2.wepayu.models.CartaoDePonto;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class CartaoMemento implements Memento {
    private final String empId;
    private final Map<String, Empregado> empregados;
    private final List<CartaoDePonto> cartoesAnteriores;


    public CartaoMemento(String empId, Map<String, Empregado> empregados) {
        this.empId = empId;
        this.empregados = empregados;

        Empregado empregado = empregados.get(empId);
        if (empregado instanceof EmpregadoHorista) {
            EmpregadoHorista horista = (EmpregadoHorista) empregado;
            this.cartoesAnteriores = new ArrayList<>(horista.getCartoes());
        } else {
            this.cartoesAnteriores = new ArrayList<>();
        }
    }


    @Override
    public void restaurar() {
        Empregado empregado = empregados.get(empId);
        if (empregado instanceof EmpregadoHorista) {
            EmpregadoHorista horista = (EmpregadoHorista) empregado;
            horista.getCartoes().clear();
            horista.getCartoes().addAll(cartoesAnteriores);
        }
    }
}