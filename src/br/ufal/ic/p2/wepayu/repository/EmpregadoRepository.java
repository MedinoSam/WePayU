package br.ufal.ic.p2.wepayu.repository;

import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoExisteException;
import br.ufal.ic.p2.wepayu.Exception.IdNuloException;
import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.models.sistemaponto.CartaoDePonto;
import br.ufal.ic.p2.wepayu.models.sistemaponto.EmpregadosNoSistemaPonto;
import br.ufal.ic.p2.wepayu.models.sistemasindicato.EmpregadosNoSistemaSindical;
import br.ufal.ic.p2.wepayu.models.sistemasindicato.TaxaServicoSindical;
import br.ufal.ic.p2.wepayu.models.sistemavendas.CartaoDeVenda;
import br.ufal.ic.p2.wepayu.models.sistemavendas.EmpregadosNoSistemaVendas;
import br.ufal.ic.p2.wepayu.utils.Utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmpregadoRepository {

    List<Empregado> empregados = Utils.trazerDadosDosEmpregadosXML("./listaEmpregados.xml");
    EmpregadosNoSistemaSindical empregadosNoSistemaSindical = Utils.trazerDadosSistemaSindicalXML(  "./listaDadosSistemaSindical.xml");
    EmpregadosNoSistemaPonto empregadosNoSistemaPonto = Utils.trazerDadosDoPontoXMl("./listaDadosSistemaPonto.xml");
    EmpregadosNoSistemaVendas empregadosNoSistemaVendas = Utils.trazerDadosSistemaVendasXML("./listaDadosSistemaVendas.xml");

    public static final List<String> ATRIBUTOS_EMPREGADOS = List.of(
            "nome", "endereco", "tipo", "metodoPagamento", "sindicalizado",
            "idSindicato", "taxaSindical", "banco", "agencia",
            "contaCorrente", "comissao", "salario"
    );

    public static final List<String> TIPOS_EMPREGADOS = List.of(
            "assalariado", "comissionado", "horista"
    );

    public static final List<String> METODOS_PAGAMENTO = List.of(
            "banco", "emMaos", "correios"
    );

    public List<Empregado> adicionarEmpregado(Empregado empregado) {
        empregados.add(empregado);
        Utils.salvarEmpregadoXML(empregados, "./listaEmpregados.xml");
        return empregados;
    }

    public void removeEmpregado(Empregado empregado) {
        empregados.remove(empregado);
    }

    public List<Empregado> retornaTodosEmpregados() {
        return empregados;
    }

    public Empregado retornaEmpregadoPorId(String idEmpregado) throws EmpregadoNaoExisteException, IdNuloException {
        if (idEmpregado.isEmpty() || idEmpregado.isBlank()) {
            throw new IdNuloException("Identificacao do empregado nao pode ser nula.");
        }

        Optional<Empregado> empregadoOptional =
                empregados
                        .stream()
                        .filter(empregado -> empregado.getId().equals(idEmpregado))
                        .findFirst();
        if (empregadoOptional.isPresent()) {
            return empregadoOptional.get();
        }
        throw new EmpregadoNaoExisteException("Empregado nao existe.");
    }

    public void adicionarDadosEmpregadoSistemaPonto(String idEmpregado, String data, String horas) {
        CartaoDePonto ponto = new CartaoDePonto(data, horas, idEmpregado);
        empregadosNoSistemaPonto.adicionarCartao(ponto);
        Utils.salvarPontoDosEmpregadosXML(empregadosNoSistemaPonto, "./listaDadosSistemaPonto.xml");
    }

    public void adicionarDadosEmpregadoSistemaVendas(String idEmpregado, String data, double valor) {
        CartaoDeVenda venda = new CartaoDeVenda(idEmpregado, data, valor);
        empregadosNoSistemaVendas.adicionarVenda(venda);
        Utils.salvarDadosSistemVendasXML(empregadosNoSistemaVendas,"./listaDadosSistemaVendas.xml" );
    }

    public void adicionarDadosEmpregadoSistemaSindical(String idEmpregado, String data, double valor) {
        TaxaServicoSindical taxa = new TaxaServicoSindical(idEmpregado, data, valor);
        empregadosNoSistemaSindical.adicionarTaxa(taxa);
        Utils.salvarDadosSistemaSindical(empregadosNoSistemaSindical, "./listaDadosSistemaSindical.xml");
    }

    public List<CartaoDePonto> retornaPontosDoEmpregadoPorId(String idEmpregado) {

        return empregadosNoSistemaPonto
                .getCartoes()
                .stream()
                .filter(cartaoDePonto -> cartaoDePonto.getIdEmpregado().equals(idEmpregado))
                .toList();
    }

    public List<CartaoDeVenda> retornaVendasDoEmpregadoPorId(String idEmpregado) {
        return empregadosNoSistemaVendas
                .getListaDeVendas()
                .stream()
                .filter(cartaoDeVenda -> cartaoDeVenda.getIdEmpregado().equals(idEmpregado))
                .toList();
    }

    public List<TaxaServicoSindical> retornaTaxasSindicaisDoEmpregadoPorId(String idEmpregado) {
        return empregadosNoSistemaSindical
                .getListaDeTaxas()
                .stream()
                .filter(taxaServicoSindical -> taxaServicoSindical.getIdMembro().equals(idEmpregado))
                .toList();
    }

    public List<Empregado> zerarRepository() {
        Utils.excluirArquivo("./listaEmpregados.xml");
        Utils.excluirArquivo("./listaDadosSistemaPonto.xml");
        Utils.excluirArquivo("./listaDadosSistemaVendas.xml");
        Utils.excluirArquivo("./listaDadosSistemaSindical.xml");
        empregados = new ArrayList<>();
        return empregados;
    }

    public EmpregadosNoSistemaPonto retornaTodosDadosSistemaPonto() {
        return empregadosNoSistemaPonto;
    }
}
