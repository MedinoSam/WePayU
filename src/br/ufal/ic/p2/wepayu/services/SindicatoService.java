package br.ufal.ic.p2.wepayu.services;

import br.ufal.ic.p2.wepayu.models.sindicato.MembroSindicato;
import br.ufal.ic.p2.wepayu.Exception.*;


public interface SindicatoService {
    MembroSindicato criarMembro(String id, String taxa)
            throws IdentificacaoSindicatoNaoPodeSerNulaException, TaxaSindicalNaoPodeSerNulaException, ValorMonetarioInvalidoException,
            ValorDeveSerNumericoException, ValorDeveSerNaoNegativoException;

    void removerMembro(String id)
            throws IdentificacaoSindicatoNaoPodeSerNulaException, MembroSindicatoNaoEncontradoException;

    MembroSindicato getMembro(String id)
            throws IdentificacaoSindicatoNaoPodeSerNulaException, MembroSindicatoNaoEncontradoException;

    void lancaTaxaServico(String membro, String data, String valor)
            throws IdentificacaoSindicatoNaoPodeSerNulaException, DataNaoPodeSerNulaException, ValorNaoPodeSerNuloException, DataInvalidaException, ValorMonetarioInvalidoException,
            MembroSindicatoNaoEncontradoException, ValorDeveSerPositivoException;

    String getTaxasServico(String empregado, String dataInicial, String dataFinal)
            throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, DataNaoPodeSerNulaException, DataInvalidaException,
            EmpregadoNaoEhSindicalizadoException, DataInicialPosteriorDataFinalException;
}