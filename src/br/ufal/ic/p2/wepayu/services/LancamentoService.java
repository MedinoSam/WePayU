package br.ufal.ic.p2.wepayu.services;

import br.ufal.ic.p2.wepayu.Exception.*;


public interface LancamentoService {
    void lancaCartao(String emp, String data, String horas)
            throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, DataNaoPodeSerNulaException, HorasNaoPodemSerNulasException, DataInvalidaException,
            EmpregadoNaoEhTipoEsperadoException, ValorDeveSerPositivoException;

    void lancaVenda(String emp, String data, String valor)
            throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, DataNaoPodeSerNulaException, ValorNaoPodeSerNuloException, DataInvalidaException,
            ValorMonetarioInvalidoException, EmpregadoNaoEhTipoEsperadoException,
            ValorDeveSerPositivoException;

    String getHorasNormaisTrabalhadas(String emp, String dataInicial, String dataFinal)
            throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, DataNaoPodeSerNulaException, DataInvalidaException,
            EmpregadoNaoEhTipoEsperadoException, DataInicialPosteriorDataFinalException;

    String getHorasExtrasTrabalhadas(String emp, String dataInicial, String dataFinal)
            throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, DataNaoPodeSerNulaException, DataInvalidaException,
            EmpregadoNaoEhTipoEsperadoException, DataInicialPosteriorDataFinalException;

    String getVendasRealizadas(String emp, String dataInicial, String dataFinal)
            throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, DataNaoPodeSerNulaException, DataInvalidaException,
            EmpregadoNaoEhTipoEsperadoException, DataInicialPosteriorDataFinalException;
}