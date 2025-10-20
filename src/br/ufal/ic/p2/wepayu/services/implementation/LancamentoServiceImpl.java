package br.ufal.ic.p2.wepayu.services.implementation;

import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.services.LancamentoService;
import br.ufal.ic.p2.wepayu.models.*;
import br.ufal.ic.p2.wepayu.commands.*;
import br.ufal.ic.p2.wepayu.Exception.*;
import br.ufal.ic.p2.wepayu.utils.ValorMonetarioUtils;

import java.util.ArrayList;
import java.util.Map;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;


public class LancamentoServiceImpl implements LancamentoService {

    private Map<String, Empregado> empregados;
    private CommandManagerInterface commandManager;

    public LancamentoServiceImpl(Map<String, Empregado> empregados, CommandManagerInterface commandManager) {
        this.empregados = empregados;
        this.commandManager = commandManager;
    }

    @Override
    public void lancaCartao(String emp, String data, String horas)
            throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, DataNaoPodeSerNulaException, HorasNaoPodemSerNulasException, DataInvalidaException,
            EmpregadoNaoEhTipoEsperadoException, ValorDeveSerPositivoException {

        if(emp == null || emp.isBlank()) throw new IdentificacaoEmpregadoNaoPodeSerNulaException("Identificacao do empregado nao pode ser nula.");
        if(data == null || data.isBlank()) throw new DataNaoPodeSerNulaException("Data nao pode ser nula.");
        if(horas == null || horas.isBlank()) throw new HorasNaoPodemSerNulasException("Horas nao podem ser nulas.");
        if(!empregados.containsKey(emp)) throw new EmpregadoNaoExisteException("Empregado nao existe.");

        try {
            LocalDate.parse(data, DateTimeFormatter.ofPattern("d/M/yyyy"));
        } catch (Exception e) {
            throw new DataInvalidaException("Data invalida.");
        }

        double nmrHoras;
        try {
            nmrHoras = Double.parseDouble(horas.replace(",", "."));
            if(nmrHoras <= 0) throw new DataInvalidaException("Horas devem ser positivas.");
        } catch (NumberFormatException e) {
            throw new DataInvalidaException("Horas devem ser numericas.");
        }

        LancarCartaoCommand command = new LancarCartaoCommand(emp, data, horas, empregados);
        commandManager.executar(command);
    }

    @Override
    public void lancaVenda(String emp, String data, String valor)
            throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, DataNaoPodeSerNulaException, ValorNaoPodeSerNuloException, DataInvalidaException,
            ValorMonetarioInvalidoException, EmpregadoNaoEhTipoEsperadoException,
            ValorDeveSerPositivoException {

        if(emp == null || emp.isBlank()) throw new IdentificacaoEmpregadoNaoPodeSerNulaException("Identificacao do empregado nao pode ser nula.");
        if(data == null || data.isBlank()) throw new DataNaoPodeSerNulaException("Data nao pode ser nula.");
        if(valor == null || valor.isBlank()) throw new ValorNaoPodeSerNuloException("Valor nao pode ser nulo.");
        if(!empregados.containsKey(emp)) throw new EmpregadoNaoExisteException("Empregado nao existe.");

        try {
            LocalDate.parse(data, DateTimeFormatter.ofPattern("d/M/yyyy"));
        } catch (Exception e) {
            throw new DataInvalidaException("Data invalida.");
        }

        double nmrValor;
        try {
            nmrValor = Double.parseDouble(valor.replace(",", "."));
            if(nmrValor <= 0) throw new ValorMonetarioInvalidoException("Valor deve ser positivo.");
        } catch (NumberFormatException e) {
            throw new ValorMonetarioInvalidoException("Valor deve ser numerico.");
        }

        LancarVendaCommand command = new LancarVendaCommand(emp, data, valor, empregados);
        commandManager.executar(command);
    }

    @Override
    public String getHorasNormaisTrabalhadas(String emp, String dataInicial, String dataFinal)
            throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, DataNaoPodeSerNulaException, DataInvalidaException,
            EmpregadoNaoEhTipoEsperadoException, DataInicialPosteriorDataFinalException {
        if(emp == null || emp.isBlank()) throw new IdentificacaoEmpregadoNaoPodeSerNulaException("Identificacao do empregado nao pode ser nula.");
        if(!empregados.containsKey(emp))  throw new EmpregadoNaoExisteException("Empregado nao existe.");

        Empregado empregado = empregados.get(emp);

        if (!empregado.getTipo().equals("horista")) throw new EmpregadoNaoEhHoristaException("Empregado nao eh horista.");

        LocalDate dtInicial = validarDataStrict(dataInicial, "Data inicial");
        LocalDate dtFinal = validarDataStrict(dataFinal, "Data final");

        if (dtInicial.isAfter(dtFinal)) {
            throw new DataInvalidaException("Data inicial nao pode ser posterior aa data final.");
        }

        ArrayList<CartaoDePonto> cartoes = empregado.getCartoes();
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("d/M/yyyy");

        double horas = cartoes.stream()
                .filter(cartao -> {
                    LocalDate dataDoCartao = LocalDate.parse(cartao.getData(), formatador);
                    boolean aposOuIgualInicio = !dataDoCartao.isBefore(dtInicial);
                    boolean antesOuIgualFim = dataDoCartao.isBefore(dtFinal);
                    return aposOuIgualInicio && antesOuIgualFim;
                })
                .mapToDouble(cartao -> Math.min(cartao.getHoras(), 8.0))
                .sum();

        if (horas % 1 == 0) {
            return String.format("%.0f", horas);
        } else {
            return String.format("%.1f", horas).replace(".", ",");
        }
    }

    @Override
    public String getHorasExtrasTrabalhadas(String emp, String dataInicial, String dataFinal)
            throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, DataNaoPodeSerNulaException, DataInvalidaException,
            EmpregadoNaoEhTipoEsperadoException, DataInicialPosteriorDataFinalException {
        if(emp == null || emp.isBlank()) throw new IdentificacaoEmpregadoNaoPodeSerNulaException("Identificacao do empregado nao pode ser nula.");
        if(!empregados.containsKey(emp))  throw new EmpregadoNaoExisteException("Empregado nao existe.");

        Empregado empregado = empregados.get(emp);

        if (!empregado.getTipo().equals("horista")) throw new EmpregadoNaoEhHoristaException("Empregado nao eh horista.");

        LocalDate dtInicial = validarDataStrict(dataInicial, "Data inicial");
        LocalDate dtFinal = validarDataStrict(dataFinal, "Data final");

        if (dtInicial.isAfter(dtFinal)) {
            throw new DataInvalidaException("Data inicial nao pode ser posterior aa data final.");
        }

        ArrayList<CartaoDePonto> cartoes = empregado.getCartoes();
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("d/M/yyyy");

        double horas = cartoes.stream()
                .filter(cartao -> {
                    LocalDate dataDoCartao = LocalDate.parse(cartao.getData(), formatador);
                    boolean aposOuIgualInicio = !dataDoCartao.isBefore(dtInicial);
                    boolean antesOuIgualFim = dataDoCartao.isBefore(dtFinal);
                    return aposOuIgualInicio && antesOuIgualFim;
                })
                .mapToDouble(cartao -> Math.max(0.0, cartao.getHoras() - 8.0))
                .sum();

        if (horas % 1 == 0) {
            return String.format("%.0f", horas);
        } else {
            return String.format("%.1f", horas).replace(".", ",");
        }
    }

    @Override
    public String getVendasRealizadas(String emp, String dataInicial, String dataFinal)
            throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, DataNaoPodeSerNulaException, DataInvalidaException,
            EmpregadoNaoEhTipoEsperadoException, DataInicialPosteriorDataFinalException {
        if(emp == null || emp.isBlank()) throw new IdentificacaoEmpregadoNaoPodeSerNulaException("Identificacao do empregado nao pode ser nula.");
        if(!empregados.containsKey(emp))  throw new EmpregadoNaoExisteException("Empregado nao existe.");

        Empregado empregado = empregados.get(emp);

        if (!empregado.getTipo().equals("comissionado")) throw new TipoInvalidoException("Empregado nao eh comissionado.");

        LocalDate dtInicial = validarDataStrict(dataInicial, "Data inicial");
        LocalDate dtFinal = validarDataStrict(dataFinal, "Data final");

        if (dtInicial.isAfter(dtFinal)) {
            throw new DataInvalidaException("Data inicial nao pode ser posterior aa data final.");
        }

        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("d/M/yyyy");
        ArrayList<ResultadoDeVenda> vendas = empregado.getResultadoDeVenda();

        double valorTotalVendas = vendas.stream()
                .filter(venda -> {
                    LocalDate dataDaVenda = LocalDate.parse(venda.getData(), formatador);
                    boolean aposOuIgualInicio = !dataDaVenda.isBefore(dtInicial);
                    boolean antesOuIgualFim = dataDaVenda.isBefore(dtFinal);
                    return aposOuIgualInicio && antesOuIgualFim;
                })
                .mapToDouble(ResultadoDeVenda::getValor)
                .sum();

        return ValorMonetarioUtils.formatarValorMonetario(valorTotalVendas);
    }

    private LocalDate validarDataStrict(String data, String tipoData) throws DataInvalidaException {
        try {
            DateTimeFormatter formatador = DateTimeFormatter.ofPattern("d/M/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);
            return LocalDate.parse(data, formatador);
        } catch (Exception e) {
            throw new DataInvalidaException(tipoData + " invalida.");
        }
    }
}