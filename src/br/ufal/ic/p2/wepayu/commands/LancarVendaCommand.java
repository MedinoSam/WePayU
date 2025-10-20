package br.ufal.ic.p2.wepayu.commands;

import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.models.empregado.EmpregadoComissionado;
import br.ufal.ic.p2.wepayu.models.ResultadoDeVenda;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoEncontradoException;
import br.ufal.ic.p2.wepayu.Exception.TipoEmpregadoInvalidoException;
import br.ufal.ic.p2.wepayu.Exception.ErroLancamentoVendaException;
import java.util.Map;

public class LancarVendaCommand implements Command {
    private String empId;
    private String data;
    private String valor;
    private Map<String, Empregado> empregados;
    private VendaMemento memento;

    public LancarVendaCommand(String empId, String data, String valor, Map<String, Empregado> empregados) {
        this.empId = empId;
        this.data = data;
        this.valor = valor;
        this.empregados = empregados;
    }

    @Override
    public void executar() {
        try {
            Empregado empregado = empregados.get(empId);
            if (empregado == null) {
                throw new EmpregadoNaoEncontradoException("Empregado não encontrado.");
            }

            if (!(empregado instanceof EmpregadoComissionado)) {
                throw new TipoEmpregadoInvalidoException("Empregado nao eh comissionado.");
            }

            EmpregadoComissionado comissionado = (EmpregadoComissionado) empregado;

            memento = new VendaMemento(empId, empregados);

            ResultadoDeVenda resultadoDeVenda = new ResultadoDeVenda(data, valor);
            comissionado.getResultadoDeVenda().add(resultadoDeVenda);
        } catch (EmpregadoNaoEncontradoException | TipoEmpregadoInvalidoException e) {
            throw e;
        } catch (Exception e) {
            throw new ErroLancamentoVendaException("Erro ao lançar venda: " + e.getMessage(), e);
        }
    }

    @Override
    public void desfazer() {
        if (memento != null) {
            memento.restaurar();
        }
    }
}