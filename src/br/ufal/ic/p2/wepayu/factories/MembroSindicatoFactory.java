package br.ufal.ic.p2.wepayu.factories;

import br.ufal.ic.p2.wepayu.Exception.*;
import br.ufal.ic.p2.wepayu.models.sindicato.MembroSindicato;
import br.ufal.ic.p2.wepayu.utils.ValorMonetarioUtils;


public class MembroSindicatoFactory {


    public static MembroSindicato criarMembro(String idMembro, String taxaSindical)
            throws IdentificacaoSindicatoNaoPodeSerNulaException, TaxaSindicalNaoPodeSerNulaException, ValorDeveSerNumericoException,
            ValorDeveSerNaoNegativoException {

        if (idMembro == null || idMembro.isBlank()) {
            throw new IdentificacaoSindicatoNaoPodeSerNulaException("Identificacao do membro nao pode ser nula.");
        }

        if (taxaSindical == null || taxaSindical.isBlank()) {
            throw new TaxaSindicalNaoPodeSerNulaException("Taxa sindical nao pode ser nula.");
        }

        double taxaNumerica;
        try {
            taxaNumerica = Double.parseDouble(taxaSindical.replace(",", "."));
        } catch (NumberFormatException e) {
            throw new ValorDeveSerNumericoException("Taxa sindical deve ser numerica.");
        }

        if (taxaNumerica < 0) {
            throw new ValorDeveSerNaoNegativoException("Taxa sindical deve ser nao-negativa.");
        }

        taxaNumerica = ValorMonetarioUtils.forcarValorMonetario(taxaNumerica);

        return new MembroSindicato(idMembro, String.valueOf(taxaNumerica));
    }


    public static MembroSindicato criarMembro(String idMembro, double taxaSindical)
            throws IdentificacaoSindicatoNaoPodeSerNulaException, ValorDeveSerNaoNegativoException {

        if (idMembro == null || idMembro.isBlank()) {
            throw new IdentificacaoSindicatoNaoPodeSerNulaException("Identificacao do membro nao pode ser nula.");
        }

        if (taxaSindical < 0) {
            throw new ValorDeveSerNaoNegativoException("Taxa sindical deve ser nao-negativa.");
        }

        taxaSindical = ValorMonetarioUtils.forcarValorMonetario(taxaSindical);

        return new MembroSindicato(idMembro, String.valueOf(taxaSindical));
    }
}