package br.ufal.ic.p2.wepayu.services.implementation;

import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.models.sindicato.MembroSindicato;
import br.ufal.ic.p2.wepayu.models.sindicato.TaxaServico;
import br.ufal.ic.p2.wepayu.services.SindicatoService;
import br.ufal.ic.p2.wepayu.commands.*;
import br.ufal.ic.p2.wepayu.Exception.*;
import br.ufal.ic.p2.wepayu.factories.MembroSindicatoFactory;
import br.ufal.ic.p2.wepayu.utils.ValorMonetarioUtils;

import java.time.format.ResolverStyle;
import java.util.Map;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class SindicatoServiceImpl implements SindicatoService {

    private Map<String, MembroSindicato> membrosSindicato;
    private Map<String, Empregado> empregados;
    private CommandManagerInterface commandManager;

    public SindicatoServiceImpl(Map<String, MembroSindicato> membrosSindicato,
                                Map<String, Empregado> empregados,
                                CommandManagerInterface commandManager) {
        this.membrosSindicato = membrosSindicato;
        this.empregados = empregados;
        this.commandManager = commandManager;
    }

    @Override
    public MembroSindicato criarMembro(String id, String taxa)
            throws IdentificacaoSindicatoNaoPodeSerNulaException, TaxaSindicalNaoPodeSerNulaException, ValorMonetarioInvalidoException,
            ValorDeveSerNumericoException, ValorDeveSerNaoNegativoException {

        MembroSindicato membro = MembroSindicatoFactory.criarMembro(id, taxa);
        CriarMembroSindicatoCommand command = new CriarMembroSindicatoCommand(membro, membrosSindicato);
        commandManager.executar(command);

        return membro;
    }

    @Override
    public void lancaTaxaServico(String membro, String data, String valor)
            throws IdentificacaoSindicatoNaoPodeSerNulaException, DataNaoPodeSerNulaException, ValorNaoPodeSerNuloException, DataInvalidaException, ValorMonetarioInvalidoException,
            MembroSindicatoNaoEncontradoException, ValorDeveSerPositivoException {

        if(membro == null || membro.isBlank()) throw new IdentificacaoSindicatoNaoPodeSerNulaException("Identificacao do membro nao pode ser nula.");
        if(data == null || data.isBlank()) throw new DataNaoPodeSerNulaException("Data nao pode ser nula.");
        if(valor == null || valor.isBlank()) throw new ValorNaoPodeSerNuloException("Valor nao pode ser nulo.");

        try {
            validarDataStrict(data, "Data");
        } catch (DataInvalidaException e) {
            throw e;
        }

        double nmrValor;
        try {
            nmrValor = Double.parseDouble(valor.replace(",", "."));
            if(nmrValor <= 0) throw new ValorMonetarioInvalidoException("Valor deve ser positivo.");
        } catch (NumberFormatException e) {
            throw new ValorMonetarioInvalidoException("Valor deve ser numerico.");
        }

        LancarTaxaServicoCommand command = new LancarTaxaServicoCommand(membro, data, valor, membrosSindicato);
        commandManager.executar(command);
    }

    @Override
    public void removerMembro(String id)
            throws IdentificacaoSindicatoNaoPodeSerNulaException, MembroSindicatoNaoEncontradoException {

        if(id == null || id.isBlank()) {
            throw new IdentificacaoSindicatoNaoPodeSerNulaException("Identificacao do membro nao pode ser nula.");
        }

        if(!membrosSindicato.containsKey(id)) {
            throw new MembroSindicatoNaoEncontradoException("Membro nao encontrado.");
        }

        membrosSindicato.remove(id);
    }

    @Override
    public MembroSindicato getMembro(String id)
            throws IdentificacaoSindicatoNaoPodeSerNulaException, MembroSindicatoNaoEncontradoException {

        if(id == null || id.isBlank()) {
            throw new IdentificacaoSindicatoNaoPodeSerNulaException("Identificacao do membro nao pode ser nula.");
        }

        MembroSindicato membro = membrosSindicato.get(id);
        if(membro == null) {
            throw new MembroSindicatoNaoEncontradoException("Membro nao encontrado.");
        }

        return membro;
    }

    @Override
    public String getTaxasServico(String emp, String dataInicial, String dataFinal)
            throws EmpregadoNaoExisteException, IdentificacaoEmpregadoNaoPodeSerNulaException, DataNaoPodeSerNulaException, DataInvalidaException,
            EmpregadoNaoEhSindicalizadoException, DataInicialPosteriorDataFinalException {
        if(emp == null || emp.isBlank()) throw new IdentificacaoEmpregadoNaoPodeSerNulaException("Identificacao do empregado nao pode ser nula.");
        if(dataInicial == null || dataInicial.isBlank()) throw new DataNaoPodeSerNulaException("Data inicial nao pode ser nula.");
        if(dataFinal == null || dataFinal.isBlank()) throw new DataNaoPodeSerNulaException("Data final nao pode ser nula.");
        if(!empregados.containsKey(emp))  throw new EmpregadoNaoExisteException("Empregado nao existe.");

        Empregado empregado = empregados.get(emp);

        if (empregado.getSindicato() == null) throw new TipoInvalidoException("Empregado nao eh sindicalizado.");

        LocalDate dtInicial, dtFinal;
        try {
            dtInicial = validarDataStrict(dataInicial, "Data inicial");
        } catch (DataInvalidaException e) {
            throw e;
        }

        try {
            dtFinal = validarDataStrict(dataFinal, "Data final");
        } catch (DataInvalidaException e) {
            throw e;
        }

        if(dtInicial.isAfter(dtFinal)) throw new DataInvalidaException("Data inicial nao pode ser posterior aa data final.");

        ArrayList<TaxaServico> taxas = empregado.getSindicato().getTaxasDeServicos();
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("d/M/yyyy");

        double valorTotalTaxas = taxas.stream()
                .filter(taxa -> {
                    LocalDate dataDaTaxa = LocalDate.parse(taxa.getData(), formatador);
                    boolean aposOuIgualInicio = !dataDaTaxa.isBefore(dtInicial);
                    boolean antesOuIgualFim = dataDaTaxa.isBefore(dtFinal);
                    return aposOuIgualInicio && antesOuIgualFim;
                })
                .mapToDouble(TaxaServico::getValor)
                .sum();

        return ValorMonetarioUtils.formatarValorMonetario(valorTotalTaxas);
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