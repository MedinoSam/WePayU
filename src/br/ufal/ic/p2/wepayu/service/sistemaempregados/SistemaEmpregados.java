package br.ufal.ic.p2.wepayu.service.sistemaempregados;

import br.ufal.ic.p2.wepayu.Exception.*;
import br.ufal.ic.p2.wepayu.enums.TipoDeEmpregado;
import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.models.empregado.EmpregadoAssalariado;
import br.ufal.ic.p2.wepayu.models.empregado.EmpregadoComissionado;
import br.ufal.ic.p2.wepayu.models.empregado.EmpregadoHorista;
import br.ufal.ic.p2.wepayu.models.pagamento.Banco;
import br.ufal.ic.p2.wepayu.models.pagamento.MetodoPagamento;
import br.ufal.ic.p2.wepayu.models.sistemasindicato.MembroSindicato;
import br.ufal.ic.p2.wepayu.repository.EmpregadoRepository;
import br.ufal.ic.p2.wepayu.utils.Utils;

import java.util.List;

public class SistemaEmpregados {

    private final EmpregadoRepository empregadoRepository = new EmpregadoRepository();

    public String retornaAtributoEmpregado (Empregado empregado, String atributo) {
        return switch (empregado.getClass().getSimpleName()) {
            case "EmpregadoAssalariado" -> Utils.retornaAtributosEmpregadoAssalariado((EmpregadoAssalariado) empregado, atributo);
            case "EmpregadoHorista" -> Utils.retornaAtributosEmpregadoHorista((EmpregadoHorista) empregado, atributo);
            case "EmpregadoComissionado" -> Utils.retornaAtributosEmpregadoComissionado((EmpregadoComissionado) empregado, atributo);
            default -> throw new AtributoNaoExisteException("Atributo inexistente!");
        };
    }

    //cria um empregado assalariado que rebene em maos
    public Empregado criarEmpregado (String nome, String endereco, String tipo, String salario) {
        if (tipo.equals(TipoDeEmpregado.COMISSIONADO.getValue())) {
            throw new TipoInvalidoException("Tipo nao aplicavel.");
        }

        MetodoPagamento metodoPagamento = new MetodoPagamento();
        metodoPagamento.setReceberEmMaos(true);
        double salarioEmDouble = Utils.converteAtributoStringParaDouble("Salario", salario);
        return new EmpregadoAssalariado(nome, endereco, metodoPagamento, salarioEmDouble, tipo, Utils.criaMembroSindicato(false));
    }

    //cria um empregado comissionado que rebene em maos
    public Empregado criarEmpregado (String nome, String endereco, String tipo, String salario, String comissao) {
        if (tipo.equals((TipoDeEmpregado.ASSALARIADO.getValue())) || tipo.equals(TipoDeEmpregado.HORISTA.getValue())) {
            throw new TipoInvalidoException("Tipo nao aplicavel.");
        }
        MetodoPagamento metodoPagamento = new MetodoPagamento();
        metodoPagamento.setReceberEmMaos(true);
        double salarioEmDouble = Utils.converteAtributoStringParaDouble("Salario", salario);
        double comissaoEmDOuble = Utils.converteAtributoStringParaDouble("Comissao", comissao);
        return new EmpregadoComissionado(nome, endereco, metodoPagamento, salarioEmDouble, comissaoEmDOuble, tipo, Utils.criaMembroSindicato(false));
    }

    public String getEmpregadoPorNome(String nome, int indice, List<Empregado> emprgados) throws EmpregadoNaoExisteException{
        List<Empregado> empregadosFiltradosPorNome = emprgados.stream().filter(empregado -> empregado.getNome().equals(nome)).toList();
        if (empregadosFiltradosPorNome.isEmpty()) {
            throw new EmpregadoNaoExisteException("Nao ha empregado com esse nome.");
        }
        int indiceReal = indice -1;
        return empregadosFiltradosPorNome.get(indiceReal).getId();
    }

    public Empregado alteraSalario(Empregado empregado, String valor) {
        double salario = Utils.validarSalario(valor);
        empregado.ajustaSalario(salario);
        return empregado;
    }

    public EmpregadoComissionado alteraComissao(Empregado empregado, String valor) {
        double comissao = Utils.validarComissao(valor);
        empregado.alteraComissao(comissao);
        return (EmpregadoComissionado) empregado;
    }

    public Empregado alteraSindicalizado(Empregado empregado, String valor) {
        if (!(valor.equals("true") || valor.equals("false"))) {
            throw new DadosInvalidoException("Valor deve ser true ou false.");
        }
        empregado.setMembroSindicato(Utils.criaMembroSindicato(Boolean.parseBoolean(valor)));
        return empregado;
    }

    public Empregado alteraMetodoPagamento  (Empregado empregado, String valor) {
        if (!EmpregadoRepository.METODOS_PAGAMENTO.contains(valor)) {
            throw new DadosInvalidoException("Metodo de pagamento invalido.");
        }
        if (valor.equals("correios")) {
            empregado.getMetodoPagamento().setReceberViaCorreios(true);
        }
        if (valor.equals("emMaos")) {
            empregado.getMetodoPagamento().setReceberEmMaos(true);
        }
        return empregado;
    }

    public Empregado alteraTipo(Empregado empregado, String valor) {
        if (!EmpregadoRepository.TIPOS_EMPREGADOS.contains(valor)) {
            throw new DadosInvalidoException("Tipo invalido.");
        }
        if (valor.equals(TipoDeEmpregado.ASSALARIADO.getValue())) {
            empregado.setTipo(valor);
        }
        return empregado;
    }

    public Empregado alteraEmpregado(Empregado empregado, String idEmpregado, String atributo, Boolean valor, String idSindicato, String taxaSindical, List<String> membros)  throws DadosInvalidoException{

        Utils.validarInformacoesSindicais(idSindicato);
        Utils.validarInformacoesSindicais(taxaSindical);

        double taxaSindicalDouble = Utils.converteQualquerAtributoStringParaDouble(taxaSindical);
        if (taxaSindicalDouble < 0) {
            throw new DadosInvalidoException("Taxa sindical deve ser nao-negativa.");
        }
        if (atributo.equals("sindicalizado")) {
            MembroSindicato membroSindicato = new MembroSindicato(idSindicato, taxaSindicalDouble, idEmpregado, valor);
            if (!membros.contains(idSindicato)) {
                membros.add(idSindicato);
            }
            else {
                throw new EmpregadoDuplicadoException("Ha outro empregado com esta identificacao de sindicato");
            }
            empregado.setMembroSindicato(membroSindicato);
        }
        return empregado;
    }

    public Empregado alteraEmpregado(Empregado empregado, String atributo, String valor, String banco, String agencia, String contaCorrente) {
        if (atributo.equals("metodoPagamento")) {
            alteraMetodoPagamento(empregado, valor, banco, agencia, contaCorrente);
        }
        return empregado;
    }

    public Empregado alteraEmpregado(Empregado empregado, String atributo, String valor, String grana) throws Exception {
        if (atributo.equals("tipo")) {
            if (TipoDeEmpregado.COMISSIONADO.getValue().equals(valor)) {
                empregado = converteParaComissionado(empregado, grana);
            }
            else if (TipoDeEmpregado.HORISTA.getValue().equals(valor)) {
                empregado = converteParaHorista(empregado, grana);
            }
        }
        return empregado;
    }

    public Empregado alteraEmpregado(Empregado empregado, String atributo, String valor) {
        switch (atributo) {
            case "nome" -> {
                empregado.setNome(valor);
                return empregado;
            }
            case "endereco" -> {
                empregado.setEndereco(valor);
                return empregado;
            }
            case "tipo" -> {
                return alteraTipo(empregado, valor);
            }
            case "salario" -> {
                return alteraSalario(empregado, valor);
            }
            case "metodoPagamento" -> {
                return alteraMetodoPagamento(empregado, valor);
            }
            case "comissao" -> {
                return alteraComissao(empregado, valor);
            }
            case "sindicalizado" ->{
                return alteraSindicalizado(empregado, valor);
            }
            default -> throw new AtributoNaoExisteException("atributo inexistente");
        }
    }

    public void alteraMetodoPagamento(Empregado empregado, String valor, String banco, String agencia, String contaCorrente) {
        if (!EmpregadoRepository.METODOS_PAGAMENTO.contains(valor)) {
            throw new AtributoNaoExisteException("Metodo de pagamento inexistente");
        }

        if (valor.equals("banco")) {
            Utils.validarInformacoesBancarias(banco);
            Utils.validarInformacoesBancarias(agencia);
            Utils.validarInformacoesBancarias(contaCorrente);
            Banco bancoEntity = new Banco(banco, agencia, contaCorrente);
            MetodoPagamento metodoPagamento = new MetodoPagamento();
            metodoPagamento.setReceberViaBanco(true);
            metodoPagamento.setBanco(bancoEntity);
            empregado.setMetodoPagamento(metodoPagamento);
        }
    }

    private EmpregadoHorista converteParaHorista(Empregado empregado, String salarioHora) {
        EmpregadoComissionado empregadoComissionado =  (EmpregadoComissionado) empregado;
        double salarioHoraDouble = Utils.converteAtributoStringParaDouble("Salario", salarioHora);
        return Utils.converteComissionadoParaHorista(empregadoComissionado, salarioHoraDouble);
    }

    //assalariado para comissionado
    private Empregado converteParaComissionado(Empregado empregado, String comissao) throws Exception {
        double comissaoDouble = Utils.converteAtributoStringParaDouble("Comissao", comissao);
        if (!empregado.getId().isEmpty()) {
            return empregado.converteEmpregado(empregado, comissaoDouble);
        }
        return null;
    }

    public void  validarAtributoEmpregado(String atributo) throws AtributoNaoExisteException{
        if (!EmpregadoRepository.ATRIBUTOS_EMPREGADOS.contains(atributo)) throw new AtributoNaoExisteException("Atributo nao existe.");
    }

}
