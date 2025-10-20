package br.ufal.ic.p2.wepayu.commands;

import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.models.sindicato.MembroSindicato;
import br.ufal.ic.p2.wepayu.models.pagamento.AgendaDePagamentos;

import java.util.Map;
import java.util.HashMap;


public class ZerarSistemaCommand implements Command {
    private Map<String, Empregado> empregados;
    private Map<String, MembroSindicato> membrosSindicato;
    private Map<String, Empregado> empregadosBackup;
    private Map<String, MembroSindicato> membrosSindicatoBackup;

    public ZerarSistemaCommand(Map<String, Empregado> empregados, Map<String, MembroSindicato> membrosSindicato) {
        this.empregados = empregados;
        this.membrosSindicato = membrosSindicato;
    }

    @Override
    public void executar() {
        empregadosBackup = new HashMap<>(empregados);
        membrosSindicatoBackup = new HashMap<>(membrosSindicato);

        empregados.clear();
        membrosSindicato.clear();
        AgendaDePagamentos.limparAgendasCustomizadas();
    }

    @Override
    public void desfazer() {
        empregados.clear();
        empregados.putAll(empregadosBackup);

        membrosSindicato.clear();
        membrosSindicato.putAll(membrosSindicatoBackup);
    }
}