package br.ufal.ic.p2.wepayu.models.empregado;

import br.ufal.ic.p2.wepayu.Exception.AtributoNaoExisteException;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoComissionadoException;
import br.ufal.ic.p2.wepayu.enums.TipoDeEmpregado;
import br.ufal.ic.p2.wepayu.interfaces.EmpregadoInterface;
import br.ufal.ic.p2.wepayu.models.pagamento.MetodoPagamento;
import br.ufal.ic.p2.wepayu.models.sistemasindicato.MembroSindicato;
import br.ufal.ic.p2.wepayu.utils.Utils;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public  class Empregado implements Serializable, EmpregadoInterface {


    private String id;
    private String nome;
    private String endereco;
    private String tipo;
    private MetodoPagamento metodoPagamento;
    private MembroSindicato membroSindicato;


    public  Empregado(String nome, String endereco, MetodoPagamento metodoPagamento, String tipo, MembroSindicato membroSindicato) throws AtributoNaoExisteException {
        setNome(nome);
        setEndereco(endereco);
        setTipo(validarTipo(tipo));
        this.id = UUID.randomUUID().toString();
        this.metodoPagamento = metodoPagamento;
        this.membroSindicato = membroSindicato;
    }

    public Empregado() {

    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getTipo() {
        return tipo;
    }

    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }

    public MembroSindicato getMembroSindicato() {
        return membroSindicato;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = validaAtributo("Nome", nome);
    }

    public void setEndereco(String endereco) {
        this.endereco = validaAtributo("Endereco", endereco);
    }

    public void setTipo(String tipo) {
        this.tipo = validarTipo(tipo);
    }

    public void setMetodoPagamento(MetodoPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public void setMembroSindicato(MembroSindicato membroSindicato) {
        this.membroSindicato = membroSindicato;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Empregado empregado)) return false;
        return Objects.equals(id, empregado.id) && Objects.equals(nome, empregado.nome) && Objects.equals(endereco, empregado.endereco) && Objects.equals(tipo, empregado.tipo) && Objects.equals(metodoPagamento, empregado.metodoPagamento) && Objects.equals(membroSindicato, empregado.membroSindicato);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, endereco, tipo, metodoPagamento, membroSindicato);
    }

    private String validarTipo(String tipo) throws AtributoNaoExisteException {
        if (tipo.isBlank() || tipo.isEmpty()) {
            throw new AtributoNaoExisteException("Erro: tipo nao pode ser nulo");
        }
        TipoDeEmpregado.validarTipo(tipo);
        return tipo;
    }


    @Override
    public EmpregadoComissionado alteraComissao(double comissao) throws EmpregadoNaoComissionadoException{
        throw new EmpregadoNaoComissionadoException("Empregado nao eh comissionado");
    }

    @Override
    public EmpregadoComissionado converteEmpregado(Empregado empregado, double comissao) throws Exception {
        return Utils.converterHoristaParaComissionado((EmpregadoHorista) empregado, comissao);
    }

    @Override
    public void ajustaSalario(double salairo) {
    }

    private String validaAtributo(String atributo, String valor) throws AtributoNaoExisteException {
        if (valor.isEmpty() || valor.isBlank())
            throw new AtributoNaoExisteException(atributo + " nao pode ser nulo.");
        return valor;
    }
}
