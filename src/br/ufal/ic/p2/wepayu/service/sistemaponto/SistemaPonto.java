package br.ufal.ic.p2.wepayu.service.sistemaponto;

import br.ufal.ic.p2.wepayu.Exception.DadosInvalidoException;
import br.ufal.ic.p2.wepayu.Exception.DataInvalidaException;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoHoristaException;
import br.ufal.ic.p2.wepayu.enums.TipoDeEmpregado;
import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.models.sistemaponto.CartaoDePonto;
import br.ufal.ic.p2.wepayu.models.sistemaponto.EmpregadosNoSistemaPonto;
import br.ufal.ic.p2.wepayu.models.sistemaponto.ResumoHoras;
import br.ufal.ic.p2.wepayu.repository.EmpregadoRepository;
import br.ufal.ic.p2.wepayu.utils.Utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class SistemaPonto {

    private final EmpregadoRepository empregadoRepository = new EmpregadoRepository();
    EmpregadosNoSistemaPonto empregadosNoSistemaPonto = empregadoRepository.retornaTodosDadosSistemaPonto();

    public SistemaPonto() {

    }

    public String retornaHorasExtrasTrabalhadas(String idEmpregado, String dataInicial, String dataFinal) throws ParseException , DataInvalidaException{

        if (!Utils.validarData(dataInicial)) throw  new DataInvalidaException("Data inicial invalida.");
        if (!Utils.validarData(dataFinal)) throw new DataInvalidaException("Data final invalida.");

        var cartoesPontoEmpregado = empregadoRepository.retornaPontosDoEmpregadoPorId(idEmpregado);
        if (cartoesPontoEmpregado.isEmpty()) return "0";

        ResumoHoras resumoHoras = ResumoHoras.calcularResumoHoras(cartoesPontoEmpregado, dataInicial, dataFinal);
        return formataHorasDoubleParaString(resumoHoras.getHorasExtras());
    }

    public String retornaHorasNormaisTrabalhadas(String idEmpregado, String dataInicial, String dataFinal) throws  DataInvalidaException{

        if (!Utils.validarData(dataInicial)) throw  new DataInvalidaException("Data inicial invalida.");
        if (!Utils.validarData(dataFinal)) throw new DataInvalidaException("Data final invalida.");


        var cartoesPontoEmpregado = empregadoRepository.retornaPontosDoEmpregadoPorId(idEmpregado);

        if (cartoesPontoEmpregado.isEmpty()) return "0";
        /*o prroblema ta aqui*/
        ResumoHoras resumoHoras = ResumoHoras.calcularResumoHoras(cartoesPontoEmpregado, dataInicial, dataFinal);
        return formataHorasDoubleParaString(resumoHoras.getHorasNormais());
    }

    public void lancaCartao(Empregado empregado, String idEmpregado, String data, String horas)  throws EmpregadoNaoHoristaException, DadosInvalidoException{

        validarEmpregadoHorista(empregado);

        if (!Utils.validarData(data)) throw new DataInvalidaException("Data invalida.");

        if (Utils.converteQualquerAtributoStringParaDouble(horas) <= 0) throw new DadosInvalidoException("Horas devem ser positivas.");

        if (idEmpregado.isEmpty() || idEmpregado.isBlank()) throw new DadosInvalidoException("id empregado nao pode ser nulo");

        empregadoRepository.adicionarDadosEmpregadoSistemaPonto(idEmpregado, data, horas);
    }

    private String formataHorasDoubleParaString(double somatorioHoras) {
            DecimalFormatSymbols sy = new DecimalFormatSymbols(Locale.getDefault());
            sy.setDecimalSeparator(',');
            DecimalFormat df = new DecimalFormat("#.##", sy);
            return df.format(somatorioHoras);
    }

    public void validarEmpregadoHorista(Empregado empregado) throws EmpregadoNaoHoristaException {
        if (!empregado.getTipo().equals(TipoDeEmpregado.HORISTA.getValue())) throw new EmpregadoNaoHoristaException("Empregado nao eh horista.");
    }
}
