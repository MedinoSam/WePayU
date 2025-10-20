package br.ufal.ic.p2.wepayu.services;

import br.ufal.ic.p2.wepayu.Exception.*;

public interface EmpregadoService {
    String criarEmpregado(String nome, String endereco, String tipo, String salario)
            throws NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoNaoPodeSerNuloException, TipoInvalidoException, SalarioNaoPodeSerNuloException,
            SalarioDeveSerNumericoException, SalarioDeveSerNaoNegativoException;

    String criarEmpregado(String nome, String endereco, String tipo, String salario, String comissao)
            throws NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoNaoPodeSerNuloException, TipoInvalidoException, SalarioNaoPodeSerNuloException,
            SalarioDeveSerNumericoException, SalarioDeveSerNaoNegativoException,
            ComissaoNaoPodeSerNulaException, ComissaoDeveSerNumericaException,
            ComissaoDeveSerNaoNegativaException;

    void alteraEmpregado(String emp, String atributo, String valor)
            throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, TipoInvalidoException,
            AtributoNaoExisteException, ValorDeveSerNumericoException, ValorDeveSerNaoNegativoException,
            MetodoPagamentoInvalidoException, ValorDeveSerTrueOuFalseException,
            IdentificacaoSindicatoJaExisteException;

    void alteraEmpregado(String emp, String atributo, String valor, String comissao_salario)
            throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, TipoInvalidoException,
            AtributoNaoExisteException, ValorDeveSerNumericoException, ValorDeveSerNaoNegativoException,
            MetodoPagamentoInvalidoException, ValorDeveSerTrueOuFalseException,
            IdentificacaoSindicatoJaExisteException;

    void alteraEmpregado(String emp, String atributo, String valor1, String banco, String agencia, String contaCorrente)
            throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, TipoInvalidoException,
            AtributoNaoExisteException, ValorDeveSerNumericoException, ValorDeveSerNaoNegativoException,
            MetodoPagamentoInvalidoException, ValorDeveSerTrueOuFalseException,
            IdentificacaoSindicatoJaExisteException;

    void alteraEmpregado(String emp, String atributo, String valor, String idSindicato, String taxaSindical)
            throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, TipoInvalidoException,
            AtributoNaoExisteException, ValorDeveSerNumericoException, ValorDeveSerNaoNegativoException,
            MetodoPagamentoInvalidoException, ValorDeveSerTrueOuFalseException,
            IdentificacaoSindicatoJaExisteException;

    void alteraEmpregado(String emp, String atributo, String valor1, String banco, String agencia, String contaCorrente, String comissao)
            throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, TipoInvalidoException,
            AtributoNaoExisteException, ValorDeveSerNumericoException, ValorDeveSerNaoNegativoException,
            MetodoPagamentoInvalidoException, ValorDeveSerTrueOuFalseException,
            IdentificacaoSindicatoJaExisteException;

    void removerEmpregado(String emp) throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException;

    String getEmpregadoPorNome(String emp, String indice) throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, IndiceNaoPodeSerNuloException;

    String getAtributoEmpregado(String emp, String atributo)
            throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, AtributoNaoExisteException,
            EmpregadoNaoEhComissionadoException, EmpregadoNaoEhSindicalizadoException,
            EmpregadoNaoRecebeEmBancoException;
}