package br.ufal.ic.p2.wepayu.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class ValorMonetarioUtils {


    public static double forcarValorMonetario(double valor) {
        BigDecimal bd = BigDecimal.valueOf(valor);
        bd = bd.setScale(2, RoundingMode.DOWN);
        return bd.doubleValue();
    }

    public static double forcarValorMonetario(String valorStr) {
        if (valorStr == null || valorStr.isBlank()) {
            return 0.0;
        }

        String valorNormalizado = valorStr.replace(",", ".");

        try {
            double valor = Double.parseDouble(valorNormalizado);
            return forcarValorMonetario(valor);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }


    public static String formatarValorMonetario(double valor) {
        double valorTruncado = forcarValorMonetario(valor);

        return String.format("%.2f", valorTruncado).replace(".", ",");
    }

    public static BigDecimal forcarValorMonetario(BigDecimal valor) {
        if (valor == null) {
            return BigDecimal.ZERO;
        }
        return valor.setScale(2, RoundingMode.DOWN);
    }


    public static String formatarValorMonetario(BigDecimal valor) {
        if (valor == null) {
            return "0,00";
        }

        BigDecimal valorTruncado = forcarValorMonetario(valor);

        return String.format("%.2f", valorTruncado).replace(".", ",");
    }

}