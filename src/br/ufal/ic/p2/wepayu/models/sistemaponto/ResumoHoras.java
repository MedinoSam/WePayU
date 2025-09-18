package br.ufal.ic.p2.wepayu.models.sistemaponto;

import br.ufal.ic.p2.wepayu.Exception.DataInvalidaException;
import br.ufal.ic.p2.wepayu.utils.Utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ResumoHoras {

    private double horasNormais;
    private double horasExtras;
    private double total;

    public ResumoHoras(double horasNormais, double horasExtras) throws DataInvalidaException {
        this.horasNormais = horasNormais;
        this.horasExtras = horasExtras;
        this.total = total;
    }

    public ResumoHoras() {
    }

    public double getHorasNormais() {
        return horasNormais;
    }

    public double getHorasExtras() {
        return horasExtras;
    }

    public double getTotal() {
        return total;
    }

    public static ResumoHoras calcularResumoHoras(List<CartaoDePonto> listaPonto, String dataInicial, String dataFinal) throws DataInvalidaException {


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        LocalDate inicio = LocalDate.parse(dataInicial, formatter);
        LocalDate fim = LocalDate.parse(dataFinal, formatter);

        if (inicio.isAfter(fim)) throw new DataInvalidaException("Data inicial nao pode ser posterior aa data final.");


        double horasNormais = 0.0;
        double horasExtras = 0.0;

        for (CartaoDePonto cartao : listaPonto) {
            LocalDate dataDoCartao = LocalDate.parse(cartao.getData(), formatter);
            double horaCartao = Utils.converteQualquerAtributoStringParaDouble(cartao.getHoras());

            if (!dataDoCartao.isBefore(inicio) && dataDoCartao.isBefore(fim)) {
                if (horaCartao > 8) {
                    horasNormais += 8;
                    horasExtras += (horaCartao - 8);
                } else {
                    horasNormais += horaCartao;
                }
            }
        }
        return new ResumoHoras(horasNormais, horasExtras);
    }
}
