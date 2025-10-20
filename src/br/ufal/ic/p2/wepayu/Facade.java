package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.models.pagamento.AgendaDePagamentos;
import br.ufal.ic.p2.wepayu.models.sindicato.MembroSindicato;
import br.ufal.ic.p2.wepayu.services.*;
import br.ufal.ic.p2.wepayu.services.implementation.*;
import br.ufal.ic.p2.wepayu.commands.*;
import br.ufal.ic.p2.wepayu.Exception.*;
import java.util.Map;
import java.util.HashMap;


public class Facade {

    private final EmpregadoService empregadoService;
    private final SindicatoService sindicatoService;
    private final LancamentoService lancamentoService;
    private final FolhaPagamentoService folhaPagamentoService;
    private final PersistenciaService persistenciaService;

    private final Map<String, Empregado> empregados;
    private final Map<String, MembroSindicato> membrosSindicato;
    private int id;
    private final CommandManager commandManager;
    private boolean sistemaEncerrado = false;

    public Facade() {
        this.empregados = new HashMap<>();
        this.membrosSindicato = new HashMap<>();
        this.id = 0;
        this.commandManager = new CommandManager();

        this.empregadoService = new EmpregadoServiceImpl(empregados, membrosSindicato, id, commandManager);
        this.sindicatoService = new SindicatoServiceImpl(membrosSindicato, empregados, commandManager);
        this.lancamentoService = new LancamentoServiceImpl(empregados, commandManager);
        this.folhaPagamentoService = new FolhaPagamentoServiceImpl(empregados, membrosSindicato);
        this.persistenciaService = new PersistenciaServiceImpl(empregados, membrosSindicato, id);

        this.persistenciaService.carregarSistema();
    }


    public String criarEmpregado(String nome, String endereco, String tipo, String salario)
            throws NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoNaoPodeSerNuloException, TipoInvalidoException, SalarioNaoPodeSerNuloException, SalarioDeveSerNumericoException, SalarioDeveSerNaoNegativoException {
        String id = empregadoService.criarEmpregado(nome, endereco, tipo, salario);
        salvarSistema();
        return id;
    }


    public String criarEmpregado(String nome, String endereco, String tipo, String salario, String comissao)
            throws Exception {
        String id = empregadoService.criarEmpregado(nome, endereco, tipo, salario, comissao);
        salvarSistema();
        return id;
    }


    public void alteraEmpregado(String emp, String atributo, String valor)
            throws Exception {
        empregadoService.alteraEmpregado(emp, atributo, valor);
        salvarSistema();
    }


    public void alteraEmpregado(String emp, String atributo, String valor, String comissao_salario)
            throws Exception {
        empregadoService.alteraEmpregado(emp, atributo, valor, comissao_salario);
        salvarSistema();
    }


    public void alteraEmpregado(String emp, String atributo, String valor1, String banco, String agencia, String contaCorrente)
            throws Exception {
        empregadoService.alteraEmpregado(emp, atributo, valor1, banco, agencia, contaCorrente);
        salvarSistema();
    }


    public void alteraEmpregado(String emp, String atributo, String valor, String idSindicato, String taxaSindical)
            throws Exception {
        empregadoService.alteraEmpregado(emp, atributo, valor, idSindicato, taxaSindical);
        salvarSistema();
    }


    public void alteraEmpregado(String emp, String atributo, String valor1, String banco, String agencia, String contaCorrente, String comissao)
            throws Exception {
        empregadoService.alteraEmpregado(emp, atributo, valor1, banco, agencia, contaCorrente, comissao);
        salvarSistema();
    }


    public void removerEmpregado(String emp) throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException{
        empregadoService.removerEmpregado(emp);
        salvarSistema();
    }


    public String getEmpregadoPorNome(String emp, String indice) throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, IndiceNaoPodeSerNuloException {
        return empregadoService.getEmpregadoPorNome(emp, indice);
    }

    public String getAtributoEmpregado(String emp, String atributo)
            throws EmpregadoNaoExisteException, NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoNaoPodeSerNuloException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, IndiceNaoPodeSerNuloException, IdentificacaoSindicatoNaoPodeSerNulaException, TaxaSindicalNaoPodeSerNulaException, DataNaoPodeSerNulaException, HorasNaoPodemSerNulasException, AtributoNaoExisteException,
            EmpregadoNaoEhComissionadoException, EmpregadoNaoEhSindicalizadoException,
            EmpregadoNaoRecebeEmBancoException {
        return empregadoService.getAtributoEmpregado(emp, atributo);
    }


    public int getNumeroDeEmpregados() {
        return empregados.size();
    }

    public MembroSindicato criarMembro(String id, String taxa)
            throws Exception {
        MembroSindicato membro = sindicatoService.criarMembro(id, taxa);
        salvarSistema();
        return membro;
    }

    public void lancaTaxaServico(String membro, String data, String valor)
            throws Exception {
        sindicatoService.lancaTaxaServico(membro, data, valor);
        salvarSistema();
    }


    public String getTaxasServico(String empregado, String dataInicial, String dataFinal)
            throws EmpregadoNaoExisteException, NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoNaoPodeSerNuloException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, IndiceNaoPodeSerNuloException, IdentificacaoSindicatoNaoPodeSerNulaException, TaxaSindicalNaoPodeSerNulaException, DataNaoPodeSerNulaException, HorasNaoPodemSerNulasException, DataInvalidaException {
        return sindicatoService.getTaxasServico(empregado, dataInicial, dataFinal);
    }


    public void lancaCartao(String emp, String data, String horas)
            throws Exception {
        lancamentoService.lancaCartao(emp, data, horas);
        salvarSistema();
    }


    public void lancaVenda(String emp, String data, String valor)
            throws Exception {
        lancamentoService.lancaVenda(emp, data, valor);
        salvarSistema();
    }


    public String getHorasNormaisTrabalhadas(String emp, String dataInicial, String dataFinal)
            throws EmpregadoNaoExisteException, NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoNaoPodeSerNuloException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, IndiceNaoPodeSerNuloException, IdentificacaoSindicatoNaoPodeSerNulaException, TaxaSindicalNaoPodeSerNulaException, DataNaoPodeSerNulaException, HorasNaoPodemSerNulasException, DataInvalidaException {
        return lancamentoService.getHorasNormaisTrabalhadas(emp, dataInicial, dataFinal);
    }


    public String getHorasExtrasTrabalhadas(String emp, String dataInicial, String dataFinal)
            throws EmpregadoNaoExisteException, NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoNaoPodeSerNuloException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, IndiceNaoPodeSerNuloException, IdentificacaoSindicatoNaoPodeSerNulaException, TaxaSindicalNaoPodeSerNulaException, DataNaoPodeSerNulaException, HorasNaoPodemSerNulasException, DataInvalidaException {
        return lancamentoService.getHorasExtrasTrabalhadas(emp, dataInicial, dataFinal);
    }


    public String getVendasRealizadas(String emp, String dataInicial, String dataFinal)
            throws EmpregadoNaoExisteException, NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoNaoPodeSerNuloException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, IndiceNaoPodeSerNuloException, IdentificacaoSindicatoNaoPodeSerNulaException, TaxaSindicalNaoPodeSerNulaException, DataNaoPodeSerNulaException, HorasNaoPodemSerNulasException, DataInvalidaException {
        return lancamentoService.getVendasRealizadas(emp, dataInicial, dataFinal);
    }


    public String totalFolha(String data) throws DataInvalidaException {
        return folhaPagamentoService.totalFolha(data);
    }


    public void rodaFolha(String data, String arquivo) throws DataInvalidaException {
        RodaFolhaCommand command = new RodaFolhaCommand(data, arquivo, folhaPagamentoService);
        commandManager.executar(command);
    }


    public void salvarSistema() {
        persistenciaService.salvarSistema();
    }


    public void carregarSistema() {
        persistenciaService.carregarSistema();
    }


    public void zerarSistema() {
        ZerarSistemaCommand command = new ZerarSistemaCommand(empregados, membrosSindicato);
        commandManager.executar(command);
    }


    public void encerrarSistema() {
        persistenciaService.encerrarSistema();
        sistemaEncerrado = true;
    }


    public void undo() throws Exception {
        if (sistemaEncerrado) {
            throw new NaoPodeComandosAposEncerrarSistemaException("Nao pode dar comandos depois de encerrarSistema.");
        }
        commandManager.undo();
    }


    public void redo() throws Exception {
        commandManager.redo();
    }

    public void criarAgendaDePagamentos(String descricao) throws IllegalArgumentException {
        AgendaDePagamentos.criarAgenda(descricao);
        salvarSistema();
    }
}