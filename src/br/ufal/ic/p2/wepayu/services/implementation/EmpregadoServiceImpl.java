package br.ufal.ic.p2.wepayu.services.implementation;

import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.models.empregado.EmpregadoComissionado;
import br.ufal.ic.p2.wepayu.models.pagamento.Banco;
import br.ufal.ic.p2.wepayu.models.sindicato.MembroSindicato;
import br.ufal.ic.p2.wepayu.services.EmpregadoService;
import br.ufal.ic.p2.wepayu.factories.EmpregadoFactory;
import br.ufal.ic.p2.wepayu.commands.*;
import br.ufal.ic.p2.wepayu.commands.CommandManagerInterface;
import br.ufal.ic.p2.wepayu.Exception.*;
import br.ufal.ic.p2.wepayu.utils.ValorMonetarioUtils;

import java.util.List;
import java.util.Map;

public class EmpregadoServiceImpl implements EmpregadoService {

    private Map<String, Empregado> empregados;
    private Map<String, MembroSindicato> membrosSindicato;
    private int id;
    private CommandManagerInterface commandManager;

    public EmpregadoServiceImpl(Map<String, Empregado> empregados,
                                Map<String, MembroSindicato> membrosSindicato,
                                int id,
                                CommandManagerInterface commandManager) {
        this.empregados = empregados;
        this.membrosSindicato = membrosSindicato;
        this.id = id;
        this.commandManager = commandManager;
    }

    @Override
    public String criarEmpregado(String nome, String endereco, String tipo, String salario)
            throws NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoNaoPodeSerNuloException, TipoInvalidoException, SalarioNaoPodeSerNuloException,
            SalarioDeveSerNumericoException, SalarioDeveSerNaoNegativoException {

        if(nome == null || nome.isBlank()) throw new NomeNaoPodeSerNuloException("Nome nao pode ser nulo.");
        if(endereco == null || endereco.isBlank()) throw new EnderecoNaoPodeSerNuloException("Endereco nao pode ser nulo.");
        if(tipo == null || tipo.isBlank()) throw new TipoNaoPodeSerNuloException("Tipo nao pode ser nulo.");

        Empregado empregado = EmpregadoFactory.criarEmpregado(tipo, nome, endereco, salario);
        String idEmpregado = String.valueOf(id++);
        empregado.setId(idEmpregado);

        CriarEmpregadoCommand command = new CriarEmpregadoCommand(empregado, empregados);
        commandManager.executar(command);

        return idEmpregado;
    }

    @Override
    public String criarEmpregado(String nome, String endereco, String tipo, String salario, String comissao)
            throws NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoNaoPodeSerNuloException, TipoInvalidoException, SalarioNaoPodeSerNuloException,
            SalarioDeveSerNumericoException, SalarioDeveSerNaoNegativoException,
            ComissaoNaoPodeSerNulaException, ComissaoDeveSerNumericaException,
            ComissaoDeveSerNaoNegativaException {

        if(nome == null || nome.isBlank()) throw new NomeNaoPodeSerNuloException("Nome nao pode ser nulo.");
        if(endereco == null || endereco.isBlank()) throw new EnderecoNaoPodeSerNuloException("Endereco nao pode ser nulo.");
        if(tipo == null || tipo.isBlank()) throw new TipoNaoPodeSerNuloException("Tipo nao pode ser nulo.");

        Empregado empregado = EmpregadoFactory.criarEmpregado(tipo, nome, endereco, salario, comissao);
        String idEmpregado = String.valueOf(id++);
        empregado.setId(idEmpregado);

        CriarEmpregadoCommand command = new CriarEmpregadoCommand(empregado, empregados);
        commandManager.executar(command);

        return idEmpregado;
    }

    @Override
    public void alteraEmpregado(String emp, String atributo, String valor)
            throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, TipoInvalidoException,
            AtributoNaoExisteException, ValorDeveSerNumericoException, ValorDeveSerNaoNegativoException,
            MetodoPagamentoInvalidoException, ValorDeveSerTrueOuFalseException,
            IdentificacaoSindicatoJaExisteException {

        if(emp == null || emp.isBlank()) throw new IdentificacaoEmpregadoNaoPodeSerNulaException("Identificacao do empregado nao pode ser nula.");
        if(!empregados.containsKey(emp)) throw new EmpregadoNaoExisteException("Empregado nao existe.");

        Map<String, String> valores = Map.of("valor", valor);
        AlterarEmpregadoCommand command = new AlterarEmpregadoCommand(emp, atributo, valores, empregados, membrosSindicato);
        commandManager.executar(command);
    }

    @Override
    public void alteraEmpregado(String emp, String atributo, String valor, String comissao_salario)
            throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, TipoInvalidoException,
            AtributoNaoExisteException, ValorDeveSerNumericoException, ValorDeveSerNaoNegativoException,
            MetodoPagamentoInvalidoException, ValorDeveSerTrueOuFalseException,
            IdentificacaoSindicatoJaExisteException {

        if(emp == null || emp.isBlank()) throw new IdentificacaoEmpregadoNaoPodeSerNulaException("Identificacao do empregado nao pode ser nula.");
        if(!empregados.containsKey(emp)) throw new EmpregadoNaoExisteException("Empregado nao existe.");

        Map<String, String> valores;
        if ("tipo".equals(atributo)) {
            if ("comissionado".equals(valor)) {
                valores = Map.of("valor", valor, "comissao", comissao_salario);
            } else {
                valores = Map.of("valor", valor, "salario", comissao_salario);
            }
        } else if ("comissao".equals(atributo)) {
            valores = Map.of("valor", valor, "comissao", comissao_salario);
        } else {
            valores = Map.of("valor", valor);
        }

        AlterarEmpregadoCommand command = new AlterarEmpregadoCommand(emp, atributo, valores, empregados, membrosSindicato);
        commandManager.executar(command);
    }

    @Override
    public void alteraEmpregado(String emp, String atributo, String valor1, String banco, String agencia, String contaCorrente)
            throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, TipoInvalidoException,
            AtributoNaoExisteException, ValorDeveSerNumericoException, ValorDeveSerNaoNegativoException,
            MetodoPagamentoInvalidoException, ValorDeveSerTrueOuFalseException,
            IdentificacaoSindicatoJaExisteException {

        if(emp == null || emp.isBlank()) throw new IdentificacaoEmpregadoNaoPodeSerNulaException("Identificacao do empregado nao pode ser nula.");
        if(!empregados.containsKey(emp)) throw new EmpregadoNaoExisteException("Empregado nao existe.");

        Map<String, String> valores = Map.of("valor1", valor1, "banco", banco, "agencia", agencia, "contaCorrente", contaCorrente);
        AlterarEmpregadoCommand command = new AlterarEmpregadoCommand(emp, atributo, valores, empregados, membrosSindicato);
        commandManager.executar(command);
    }

    @Override
    public void alteraEmpregado(String emp, String atributo, String valor, String idSindicato, String taxaSindical)
            throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, TipoInvalidoException,
            AtributoNaoExisteException, ValorDeveSerNumericoException, ValorDeveSerNaoNegativoException,
            MetodoPagamentoInvalidoException, ValorDeveSerTrueOuFalseException,
            IdentificacaoSindicatoJaExisteException {

        if(emp == null || emp.isBlank()) throw new IdentificacaoEmpregadoNaoPodeSerNulaException("Identificacao do empregado nao pode ser nula.");
        if(!empregados.containsKey(emp)) throw new EmpregadoNaoExisteException("Empregado nao existe.");

        Map<String, String> valores = Map.of("valor", valor, "idSindicato", idSindicato, "taxaSindical", taxaSindical);
        AlterarEmpregadoCommand command = new AlterarEmpregadoCommand(emp, atributo, valores, empregados, membrosSindicato);
        commandManager.executar(command);
    }

    @Override
    public void alteraEmpregado(String emp, String atributo, String valor1, String banco, String agencia, String contaCorrente, String comissao)
            throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, ValorNaoPodeSerNuloException, TipoInvalidoException,
            AtributoNaoExisteException, ValorDeveSerNumericoException, ValorDeveSerNaoNegativoException,
            MetodoPagamentoInvalidoException, ValorDeveSerTrueOuFalseException,
            IdentificacaoSindicatoJaExisteException {

        if(emp == null || emp.isBlank()) throw new IdentificacaoEmpregadoNaoPodeSerNulaException("Identificacao do empregado nao pode ser nula.");
        if(!empregados.containsKey(emp)) throw new EmpregadoNaoExisteException("Empregado nao existe.");

        Map<String, String> valores = Map.of("valor1", valor1, "banco", banco, "agencia", agencia, "contaCorrente", contaCorrente, "comissao", comissao);
        AlterarEmpregadoCommand command = new AlterarEmpregadoCommand(emp, atributo, valores, empregados, membrosSindicato);
        commandManager.executar(command);
    }

    @Override
    public void removerEmpregado(String emp) throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException {
        if(emp == null || emp.isBlank()) throw new IdentificacaoEmpregadoNaoPodeSerNulaException("Identificacao do empregado nao pode ser nula.");
        if(!empregados.containsKey(emp)) throw new EmpregadoNaoExisteException("Empregado nao existe.");

        RemoverEmpregadoCommand command = new RemoverEmpregadoCommand(emp, empregados);
        commandManager.executar(command);
    }

    @Override
    public String getEmpregadoPorNome(String emp, String indice) throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, IndiceNaoPodeSerNuloException {
        if(emp == null || emp.isBlank()) throw new IdentificacaoEmpregadoNaoPodeSerNulaException("Identificacao do empregado nao pode ser nula.");

        int indiceInt;
        try {
            indiceInt = Integer.parseInt(indice);
        } catch (NumberFormatException e) {
            throw new IndiceNaoPodeSerNuloException("Indice deve ser numerico.");
        }

        List<Empregado> encontrados = empregados.values().stream()
                .filter(empregado -> empregado.getNome().equals(emp))
                .toList();

        if (encontrados.isEmpty())
            throw new EmpregadoNaoExisteException("Nao ha empregado com esse nome.");

        if (indiceInt < 1 || indiceInt > encontrados.size())
            throw new EmpregadoNaoExisteException("Nao ha empregado com esse nome.");

        Empregado escolhido = encontrados.get(indiceInt - 1);

        return escolhido.getId();
    }

    @Override
    public String getAtributoEmpregado(String emp, String atributo)
            throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, AtributoNaoPodeSerNuloException, AtributoNaoExisteException,
            EmpregadoNaoEhComissionadoException, EmpregadoNaoEhSindicalizadoException,
            EmpregadoNaoRecebeEmBancoException {

        if(emp == null || emp.isBlank()) throw new IdentificacaoEmpregadoNaoPodeSerNulaException("Identificacao do empregado nao pode ser nula.");
        if(atributo == null || atributo.isBlank()) throw new AtributoNaoPodeSerNuloException("Atributo nao pode ser nulo.");
        if(!empregados.containsKey(emp)) throw new EmpregadoNaoExisteException("Empregado nao existe.");

        Empregado empregado = empregados.get(emp);

        switch (atributo) {
            case "nome":
                return empregado.getNome();
            case "endereco":
                return empregado.getEndereco();
            case "tipo":
                return empregado.getTipo();
            case "salario":
                return empregado.getSalario();
            case "sindicalizado":
                return empregado.getSindicato() != null ? "true" : "false";
            case "idSindicato":
                if (empregado.getSindicato() == null) {
                    throw new EmpregadoNaoEhSindicalizadoException("Empregado nao eh sindicalizado.");
                }
                return empregado.getSindicato().getIdMembro();
            case "taxaSindical":
                if (empregado.getSindicato() == null) {
                    throw new EmpregadoNaoEhSindicalizadoException("Empregado nao eh sindicalizado.");
                }
                return ValorMonetarioUtils.formatarValorMonetario(empregado.getSindicato().getTaxaSindical());
            case "metodoPagamento":
                return empregado.getMetodoPagamento().getMetodoPagamento();
            case "banco":
                if (empregado.getMetodoPagamento() instanceof Banco) {
                    return ((Banco) empregado.getMetodoPagamento()).getBanco();
                }
                throw new EmpregadoNaoRecebeEmBancoException("Empregado nao recebe em banco.");
            case "agencia":
                if (empregado.getMetodoPagamento() instanceof Banco) {
                    return ((Banco) empregado.getMetodoPagamento()).getAgencia();
                }
                throw new EmpregadoNaoRecebeEmBancoException("Empregado nao recebe em banco.");
            case "contaCorrente":
                if (empregado.getMetodoPagamento() instanceof Banco) {
                    return ((Banco) empregado.getMetodoPagamento()).getContaCorrente();
                }
                throw new EmpregadoNaoRecebeEmBancoException("Empregado nao recebe em banco.");
            case "comissao":
                if (empregado instanceof EmpregadoComissionado) {
                    return String.valueOf(((EmpregadoComissionado) empregado).getTaxaDeComissao()).replace('.', ',');
                }
                throw new EmpregadoNaoEhComissionadoException("Empregado nao eh comissionado.");
            case "agendaPagamento":
                return empregado.getAgendaPagamento().getAgenda();
            default:
                throw new AtributoNaoExisteException("Atributo nao existe.");
        }
    }
}