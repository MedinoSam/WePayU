package br.ufal.ic.p2.wepayu.service.sistemasindicato;

import br.ufal.ic.p2.wepayu.Exception.*;
import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.models.sistemasindicato.TaxaServicoSindical;
import br.ufal.ic.p2.wepayu.repository.EmpregadoRepository;
import br.ufal.ic.p2.wepayu.utils.Utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class SistemaSindicato {

    private final EmpregadoRepository empregadoRepository = new EmpregadoRepository();

    public String retornaTaxasServico(String idEmpregado, String dataInicial, String dataFinal) {

        if (!Utils.validarData(dataInicial)) throw new DataInvalidaException("Data inicial invalida.");
        if (!Utils.validarData(dataFinal)) throw new DataInvalidaException("Data final invalida.");

        var taxas = empregadoRepository.retornaTaxasSindicaisDoEmpregadoPorId(idEmpregado);
        if (taxas.isEmpty()) return "0,00";

        return formataTaxasDoubleParaString(calcularTaxas(taxas, dataInicial, dataFinal));
    }

    public void lancaTaxaServico(String idMembro, String data, String valor) {

        if (!Utils.validarData(data)) throw new DataInvalidaException("Data invalida.");
        if (Utils.converteQualquerAtributoStringParaDouble(valor) <= 0) throw new DadosInvalidoException("Valor deve ser positivo.");
        if (idMembro.isBlank() || idMembro.isEmpty()) throw new DadosInvalidoException("Identificacao do membro nao pode ser nula.");

        empregadoRepository.adicionarDadosEmpregadoSistemaSindical(idMembro, data, Utils.converteQualquerAtributoStringParaDouble(valor));
    }

    public static double calcularTaxas(List<TaxaServicoSindical> taxas, String dataInicial, String dataFinal) throws DataInvalidaException{

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        LocalDate inicio = LocalDate.parse(dataInicial, formatter);
        LocalDate fim = LocalDate.parse(dataFinal, formatter);
        double taxaTotal = 0.0;

        if (taxas.isEmpty()) return 0.0;
        if (inicio.isAfter(fim)) throw new DataInvalidaException("Data inicial nao pode ser posterior aa data final.");

        for (TaxaServicoSindical taxa : taxas) {
            LocalDate dataCartao = LocalDate.parse(taxa.getData(), formatter);
            double taxaCarto = taxa.getValor();

            if (!dataCartao.isBefore(inicio) && dataCartao.isBefore(fim)) {
                taxaTotal += taxaCarto;
            }
        }
        return taxaTotal;
    }

    private String formataTaxasDoubleParaString(double vendas) {
        DecimalFormatSymbols sy = new DecimalFormatSymbols(Locale.getDefault());
        sy.setDecimalSeparator(',');
        DecimalFormat df = new DecimalFormat("0.00", sy);
        return df.format(vendas);
    }

    public void verificaEmpregadoSindicalizado(Empregado empregado) throws MembroSindicatoNaoExiste, EmpregadoNaoSindicalizadoException{
        if (empregado == null) throw new MembroSindicatoNaoExiste("Empregado nao participa de sindicato");

        if (!empregado.getMembroSindicato().getSindicalizado()) throw new EmpregadoNaoSindicalizadoException("Empregado nao eh sindicalizado.");
    }

    public void verificaIdMembro(String idMembro, List<String> listaDeIdMembros) {
        if (idMembro.isEmpty() || idMembro.isBlank()) throw new IdNuloException("Identificacao do membro nao pode ser nula.");
        if (!listaDeIdMembros.contains(idMembro)) throw new MembroSindicatoNaoExiste("Membro nao existe.");
    }
}
