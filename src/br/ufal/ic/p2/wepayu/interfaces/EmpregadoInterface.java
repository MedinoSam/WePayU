package br.ufal.ic.p2.wepayu.interfaces;

import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.models.empregado.EmpregadoComissionado;

public interface EmpregadoInterface {
    public void ajustaSalario(double salairo);
    public EmpregadoComissionado alteraComissao (double comissao);
    public EmpregadoComissionado converteEmpregado(Empregado empregado, double comissao) throws Exception;
}
