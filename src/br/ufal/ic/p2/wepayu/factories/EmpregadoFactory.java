package br.ufal.ic.p2.wepayu.factories;

import br.ufal.ic.p2.wepayu.Exception.*;
import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.models.empregado.EmpregadoAssalariado;
import br.ufal.ic.p2.wepayu.models.empregado.EmpregadoComissionado;
import br.ufal.ic.p2.wepayu.models.empregado.EmpregadoHorista;
import br.ufal.ic.p2.wepayu.utils.ValorMonetarioUtils;

public class EmpregadoFactory {

    public static Empregado criarEmpregado(String tipo, String nome, String endereco, String salario)
            throws TipoInvalidoException, SalarioNaoPodeSerNuloException, SalarioDeveSerNumericoException,
            SalarioDeveSerNaoNegativoException {

        if (salario == null || salario.isBlank()) {
            throw new SalarioNaoPodeSerNuloException("Salario nao pode ser nulo.");
        }

        double salarioNumerico;
        try {
            salarioNumerico = Double.parseDouble(salario.replace(",", "."));
        } catch (NumberFormatException e) {
            throw new SalarioDeveSerNumericoException("Salario deve ser numerico.");
        }

        if (salarioNumerico < 0) {
            throw new SalarioDeveSerNaoNegativoException("Salario deve ser nao-negativo.");
        }

        salarioNumerico = ValorMonetarioUtils.forcarValorMonetario(salarioNumerico);

        return switch (tipo) {
            case "assalariado" -> new EmpregadoAssalariado(nome, endereco, salarioNumerico);
            case "horista" -> new EmpregadoHorista(nome, endereco, salarioNumerico);
            case "comissionado" -> throw new TipoInvalidoException("Tipo nao aplicavel.");
            default -> throw new TipoInvalidoException("Tipo invalido.");
        };
    }

    public static Empregado criarEmpregado(String tipo, String nome, String endereco, String salario, String comissao)
            throws TipoInvalidoException, SalarioNaoPodeSerNuloException, SalarioDeveSerNumericoException,
            SalarioDeveSerNaoNegativoException, ComissaoNaoPodeSerNulaException,
            ComissaoDeveSerNumericaException, ComissaoDeveSerNaoNegativaException {

        if (salario == null || salario.isBlank()) {
            throw new SalarioNaoPodeSerNuloException("Salario nao pode ser nulo.");
        }

        double salarioNumerico;
        try {
            salarioNumerico = Double.parseDouble(salario.replace(",", "."));
        } catch (NumberFormatException e) {
            throw new SalarioDeveSerNumericoException("Salario deve ser numerico.");
        }

        if (salarioNumerico < 0) {
            throw new SalarioDeveSerNaoNegativoException("Salario deve ser nao-negativo.");
        }

        if (comissao == null || comissao.isBlank()) {
            throw new ComissaoNaoPodeSerNulaException("Comissao nao pode ser nula.");
        }

        double comissaoNumerica;
        try {
            comissaoNumerica = Double.parseDouble(comissao.replace(",", "."));
        } catch (NumberFormatException e) {
            throw new ComissaoDeveSerNumericaException("Comissao deve ser numerica.");
        }

        if (comissaoNumerica < 0) {
            throw new ComissaoDeveSerNaoNegativaException("Comissao deve ser nao-negativa.");
        }

        salarioNumerico = ValorMonetarioUtils.forcarValorMonetario(salarioNumerico);
        comissaoNumerica = ValorMonetarioUtils.forcarValorMonetario(comissaoNumerica);

        return switch (tipo) {
            case "assalariado", "horista" -> throw new TipoInvalidoException("Tipo nao aplicavel.");
            case "comissionado" -> new EmpregadoComissionado(nome, endereco, salarioNumerico, comissaoNumerica);
            default -> throw new TipoInvalidoException("Tipo invalido.");
        };
    }
}