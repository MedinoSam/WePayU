package br.ufal.ic.p2.wepayu.service.sistemavendas;

import br.ufal.ic.p2.wepayu.Exception.DadosInvalidoException;
import br.ufal.ic.p2.wepayu.Exception.DataInvalidaException;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoComissionadoException;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoHoristaException;
import br.ufal.ic.p2.wepayu.enums.TipoDeEmpregado;
import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.models.sistemavendas.CartaoDeVenda;
import br.ufal.ic.p2.wepayu.repository.EmpregadoRepository;
import br.ufal.ic.p2.wepayu.utils.Utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class SistemaVendas {

    private final EmpregadoRepository empregadoRepository = new EmpregadoRepository();

    public String retornaVendas(String idEmpregado, String dataInicial, String dataFinal) throws DataInvalidaException {

        if (!Utils.validarData(dataInicial)) throw new DataInvalidaException("Data inicial invalida.");
        if (!Utils.validarData(dataFinal)) throw new DataInvalidaException("Data final invalida.");

        var vendas = empregadoRepository.retornaVendasDoEmpregadoPorId(idEmpregado);

        return formatarVendas(calcularVendas(vendas, dataInicial, dataFinal));
    }

    public void lancaVenda(String idEmpregado, String data, String valor) {

        if (!Utils.validarData(data)) throw new DataInvalidaException("Data invalida.");

        if (Utils.converteQualquerAtributoStringParaDouble(valor) <= 0) throw new DadosInvalidoException("Valor deve ser positivo.");

        if (idEmpregado.isEmpty() || idEmpregado.isBlank()) throw new DadosInvalidoException("Id nao pode ser negativo");


        empregadoRepository.adicionarDadosEmpregadoSistemaVendas(idEmpregado, data, Utils.converteQualquerAtributoStringParaDouble(valor));
    }

    public static double calcularVendas(List<CartaoDeVenda> vendas, String dataInicial, String dataFinal) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        LocalDate inicio = LocalDate.parse(dataInicial, formatter);
        LocalDate fim = LocalDate.parse(dataFinal, formatter);

        double vendasTotais = 0.0;

        if (vendas.isEmpty()) return 0.0;
        if (inicio.isAfter(fim)) throw new DataInvalidaException("Data inicial nao pode ser posterior aa data final.");

        for (CartaoDeVenda cartaoDeVenda : vendas) {
            LocalDate dataVenda = LocalDate.parse(cartaoDeVenda.getData(), formatter);
            double valorVenda = cartaoDeVenda.getValor();

            if (!dataVenda.isBefore(inicio) && dataVenda.isBefore(fim)) {
                vendasTotais += valorVenda;
            }
        }
        return vendasTotais;
    }

    public static String formatarVendas(double vendas) {
        DecimalFormatSymbols sy = new DecimalFormatSymbols(Locale.getDefault());
        sy.setDecimalSeparator(',');
        DecimalFormat df = new DecimalFormat("0.00", sy);
        return df.format(vendas);
    }

    public void validarEmpregadoComissionado(Empregado empregado) throws EmpregadoNaoComissionadoException {
        if (!empregado.getTipo().equals(TipoDeEmpregado.COMISSIONADO.getValue())) throw new EmpregadoNaoComissionadoException("Erro: empregado deve ser comissionado");
    }

}
