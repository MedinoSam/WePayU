package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.Exception.*;
import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.repository.EmpregadoRepository;
import br.ufal.ic.p2.wepayu.service.sistemaempregados.SistemaEmpregados;
import br.ufal.ic.p2.wepayu.service.sistemaponto.SistemaPonto;
import br.ufal.ic.p2.wepayu.service.sistemasindicato.SistemaSindicato;
import br.ufal.ic.p2.wepayu.service.sistemavendas.SistemaVendas;
import br.ufal.ic.p2.wepayu.utils.Utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Facade {


    private final EmpregadoRepository empregadoRepository = new EmpregadoRepository();
    private final SistemaEmpregados sistemaEmpregados = new SistemaEmpregados();
    private final SistemaPonto sistemaPonto = new SistemaPonto();
    private final SistemaVendas sistemaVendas = new SistemaVendas();
    private final SistemaSindicato sistemaSindicato = new SistemaSindicato();
    public List<String> idMembros = new ArrayList<>();

    public void zerarSistema() {
        empregadoRepository.zerarRepository();
    }

    public void encerrarSistema() {
        Utils.salvarEmpregadoXML(empregadoRepository.retornaTodosEmpregados(), "./listaEmpregados.xml");
        idMembros = new ArrayList<>();
    }

    public void removerEmpregado(String idEmpregado) throws EmpregadoNaoExisteException, IdNuloException {
        var empregado = empregadoRepository.retornaEmpregadoPorId(idEmpregado);
        empregadoRepository.removeEmpregado(empregado);
    }

    public String getAtributoEmpregado(String idEmpregado, String atributo) throws EmpregadoNaoExisteException, IdNuloException{
        Empregado empregado = empregadoRepository.retornaEmpregadoPorId(idEmpregado);
        return sistemaEmpregados.retornaAtributoEmpregado(empregado, atributo);
    }

    public String criarEmpregado(String nome, String endereco, String tipo, String salario) {
        var empregado = sistemaEmpregados.criarEmpregado(nome, endereco, tipo, salario);
        empregadoRepository.adicionarEmpregado(empregado);
        return empregado.getId();
    }

    public String criarEmpregado(String nome, String endereco, String tipo, String salario,  String comissao) {

        var empregado = sistemaEmpregados.criarEmpregado(nome, endereco, tipo, salario, comissao);
        empregadoRepository.adicionarEmpregado(empregado);
        return empregado.getId();
    }

    public String getEmpregadoPorNome(String nome, int index) throws EmpregadoNaoExisteException {
        return sistemaEmpregados.getEmpregadoPorNome(nome, index, empregadoRepository.retornaTodosEmpregados());
    }

    public String getHorasNormaisTrabalhadas(String idEmpregado, String dataInicial, String dataFinal) throws EmpregadoNaoExisteException, DataInvalidaException{
        var empregado = empregadoRepository.retornaEmpregadoPorId(idEmpregado);
        sistemaPonto.validarEmpregadoHorista(empregado);
        return sistemaPonto.retornaHorasNormaisTrabalhadas(idEmpregado, dataInicial, dataFinal);
    }

    public String getHorasExtrasTrabalhadas(String idEmpregado, String dataInicial, String dataFinal) throws ParseException, EmpregadoNaoExisteException {
        var empregado = empregadoRepository.retornaEmpregadoPorId(idEmpregado);
        sistemaPonto.validarEmpregadoHorista(empregado);
        return sistemaPonto.retornaHorasExtrasTrabalhadas(idEmpregado, dataInicial, dataFinal);
    }

    public String getVendasRealizadas(String idEmpregado, String dataInicial, String dataFinal) throws EmpregadoNaoExisteException , EmpregadoNaoComissionadoException {
        var empregado = empregadoRepository.retornaEmpregadoPorId(idEmpregado);
        sistemaVendas.validarEmpregadoComissionado(empregado);
        return sistemaVendas.retornaVendas(idEmpregado, dataInicial, dataFinal);
    }

    public void lancaVenda(String idEmpregado, String data, String valor) throws EmpregadoNaoExisteException {
        var empregado = empregadoRepository.retornaEmpregadoPorId(idEmpregado);
        sistemaVendas.validarEmpregadoComissionado(empregado);
        sistemaVendas.lancaVenda(idEmpregado, data,  valor);
    }

    public void lancaCartao(String idEmpregado, String data, String horas) throws EmpregadoNaoExisteException {
        var empregado = empregadoRepository.retornaEmpregadoPorId(idEmpregado);
        sistemaPonto.lancaCartao(empregado, idEmpregado, data, horas);
    }

    public void alteraEmpregado(String idEmpregado, String atributo, String valor) throws AtributoNaoExisteException, EmpregadoNaoExisteException {
        sistemaEmpregados.validarAtributoEmpregado(atributo);
        var empregado = empregadoRepository.retornaEmpregadoPorId(idEmpregado);
        atualizaEmpregado(sistemaEmpregados.alteraEmpregado(empregado, atributo, valor));
    }

    public void alteraEmpregado(String idEmpregado, String atributo, String valor, String grana) throws EmpregadoNaoExisteException, Exception {
        var empregado = empregadoRepository.retornaEmpregadoPorId(idEmpregado);
        atualizaEmpregado(sistemaEmpregados.alteraEmpregado(empregado, atributo, valor, grana));
    }

    public void alteraEmpregado(String idEmpregado, String atributo, String valor, String banco, String agencia, String contaCorrente) throws EmpregadoNaoExisteException {
        var empregado = empregadoRepository.retornaEmpregadoPorId(idEmpregado);
        atualizaEmpregado(sistemaEmpregados.alteraEmpregado(empregado, atributo, valor, banco, agencia, contaCorrente));
    }

    public void alteraEmpregado(String idEmpregado, String atributo, boolean valor, String idSindicato, String taxaSindical) throws EmpregadoNaoExisteException {
        var empregado = empregadoRepository.retornaEmpregadoPorId(idEmpregado);
        atualizaEmpregado(sistemaEmpregados.alteraEmpregado(empregado, idEmpregado, atributo, valor, idSindicato, taxaSindical, idMembros));
    }

    public String getTaxasServico(String idEmpregado, String dataInicial, String dataFinal) throws EmpregadoNaoExisteException, EmpregadoNaoSindicalizadoException, MembroSindicatoNaoExiste {
        var empregado = empregadoRepository.retornaEmpregadoPorId(idEmpregado);
        sistemaSindicato.verificaEmpregadoSindicalizado(empregado);
        String idMembro = empregado.getMembroSindicato().getIdMembro();
        return sistemaSindicato.retornaTaxasServico(idMembro, dataInicial, dataFinal);
    }

    public void lancaTaxaServico(String idMembro, String data, String valor) {
        sistemaSindicato.verificaIdMembro(idMembro, idMembros);
        sistemaSindicato.lancaTaxaServico(idMembro, data, valor);
    }

    public String totalFolha(String data) {
        return "0,00";
    }


    private void atualizaEmpregado(Empregado empregado) {
        empregadoRepository.removeEmpregado(empregado);
        empregadoRepository.adicionarEmpregado(empregado);
    }

}
