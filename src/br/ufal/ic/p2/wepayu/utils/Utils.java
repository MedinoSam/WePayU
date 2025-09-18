package br.ufal.ic.p2.wepayu.utils;

import br.ufal.ic.p2.wepayu.Exception.*;
import br.ufal.ic.p2.wepayu.enums.TipoDeEmpregado;
import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.models.empregado.EmpregadoAssalariado;
import br.ufal.ic.p2.wepayu.models.empregado.EmpregadoComissionado;
import br.ufal.ic.p2.wepayu.models.empregado.EmpregadoHorista;
import br.ufal.ic.p2.wepayu.models.pagamento.MetodoPagamento;
import br.ufal.ic.p2.wepayu.models.sistemaponto.EmpregadosNoSistemaPonto;
import br.ufal.ic.p2.wepayu.models.sistemasindicato.EmpregadosNoSistemaSindical;
import br.ufal.ic.p2.wepayu.models.sistemasindicato.MembroSindicato;
import br.ufal.ic.p2.wepayu.models.sistemavendas.EmpregadosNoSistemaVendas;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Utils {

    public static String retornaAtributosEmpregadoAssalariado(EmpregadoAssalariado empregado, String atributo)  throws AtributoNaoExisteException, MetodoDePagamentoInvalidoException, EmpregadoNaoComissionadoException, EmpregadoNaoRecebeViaBancoException, EmpregadoNaoSindicalizadoException{
        return switch (atributo) {
            case "nome" -> empregado.getNome();
            case "endereco" -> empregado.getEndereco();
            case "tipo" -> empregado.getTipo();
            case "salario" -> converterSalarioParaString(empregado.getSalarioMensal());
            case "metodoPagamento" -> retornaMetodoPagamento(empregado.getMetodoPagamento());
            case "comissao" -> throw new EmpregadoNaoComissionadoException("Empregado eh assalariado nao comissionado");
            case "banco" -> retornaDadosBancarios(empregado.getMetodoPagamento(), "banco");
            case "agencia" -> retornaDadosBancarios(empregado.getMetodoPagamento(), "agencia");
            case "contaCorrente" -> retornaDadosBancarios(empregado.getMetodoPagamento(), "contaCorrente");
            case "taxaSindical" -> retornaDadosSindicais(empregado.getMembroSindicato(), "taxaSindical");
            case "idSindicato" -> retornaIdSindicato(empregado.getMembroSindicato());
            case "sindicalizado" -> empregado.getMembroSindicato().getSindicalizado() ? "true" : "false";
            default -> throw new AtributoNaoExisteException("Atributo nao existe.");
        };
    }

    public static String retornaAtributosEmpregadoComissionado(EmpregadoComissionado empregado, String atributo)  throws AtributoNaoExisteException, MetodoDePagamentoInvalidoException, EmpregadoNaoComissionadoException, EmpregadoNaoRecebeViaBancoException, EmpregadoNaoSindicalizadoException{
        return switch (atributo) {
            case "nome" -> empregado.getNome();
            case "endereco" -> empregado.getEndereco();
            case "tipo" -> empregado.getTipo();
            case "salario" -> converterSalarioParaString(empregado.getSalarioMensal());
            case "metodoPagamento" -> retornaMetodoPagamento(empregado.getMetodoPagamento());
            case "comissao" ->  formataCasasDecimais(empregado.getTaxaDeComissao());
            case "banco" -> retornaDadosBancarios(empregado.getMetodoPagamento(), "banco");
            case "agencia" -> retornaDadosBancarios(empregado.getMetodoPagamento(), "agencia");
            case "contaCorrente" -> retornaDadosBancarios(empregado.getMetodoPagamento(), "contaCorrente");
            case "taxaSindical" -> retornaDadosSindicais(empregado.getMembroSindicato(), "taxaSindical");
            case "idSindicato" -> retornaIdSindicato(empregado.getMembroSindicato());
            case "sindicalizado" -> empregado.getMembroSindicato().getSindicalizado() ? "true" : "false";
            default -> throw new AtributoNaoExisteException("Atributo nao existe.");
        };
    }

    public static String retornaAtributosEmpregadoHorista(EmpregadoHorista empregado, String atributo)  throws AtributoNaoExisteException, MetodoDePagamentoInvalidoException, EmpregadoNaoComissionadoException, EmpregadoNaoRecebeViaBancoException, EmpregadoNaoSindicalizadoException{
        return switch (atributo) {
            case "nome" -> empregado.getNome();
            case "endereco" -> empregado.getEndereco();
            case "tipo" -> empregado.getTipo();
            case "salario" -> converterSalarioParaString(empregado.getSalarioPorHora());
            case "metodoPagamento" -> retornaMetodoPagamento(empregado.getMetodoPagamento());
            case "comissao" -> throw new EmpregadoNaoComissionadoException("Empregado eh assalariado nao comissionado");
            case "banco" -> retornaDadosBancarios(empregado.getMetodoPagamento(), "banco");
            case "agencia" -> retornaDadosBancarios(empregado.getMetodoPagamento(), "agencia");
            case "contaCorrente" -> retornaDadosBancarios(empregado.getMetodoPagamento(), "contaCorrente");
            case "taxaSindical" -> retornaDadosSindicais(empregado.getMembroSindicato(), "taxaSindical");
            case "idSindicato" -> retornaIdSindicato(empregado.getMembroSindicato());
            case "sindicalizado" -> empregado.getMembroSindicato().getSindicalizado() ? "true" : "false";
            default -> throw new AtributoNaoExisteException("Atributo nao existe.");
        };
    }

    public static EmpregadoHorista converteComissionadoParaHorista(EmpregadoComissionado empregado, double salarioPorHora) {
        EmpregadoHorista empregadoHorista = new EmpregadoHorista();
        empregadoHorista.setId(empregado.getId());
        empregadoHorista.setNome(empregado.getNome());
        empregadoHorista.setEndereco(empregado.getEndereco());
        empregadoHorista.setTipo(TipoDeEmpregado.HORISTA.getValue());
        empregadoHorista.setMetodoPagamento(empregado.getMetodoPagamento());
        empregadoHorista.setSalarioPorHora(salarioPorHora);
        empregadoHorista.setMembroSindicato(empregado.getMembroSindicato());
        return empregadoHorista;
    }

    public static EmpregadoComissionado converteAssalariadoParaComissionado(EmpregadoAssalariado empregado, double comissao) {
        EmpregadoComissionado empregadoComissionado = new EmpregadoComissionado();
        empregadoComissionado.setId(empregado.getId());
        empregadoComissionado.setNome(empregado.getNome());
        empregadoComissionado.setEndereco(empregado.getEndereco());
        empregadoComissionado.setTipo(TipoDeEmpregado.COMISSIONADO.getValue());
        empregadoComissionado.setMetodoPagamento(empregado.getMetodoPagamento());
        empregadoComissionado.setSalarioMensal(empregado.getSalarioMensal());
        empregadoComissionado.setTaxaDeComissao(comissao);
        empregadoComissionado.setMembroSindicato(empregado.getMembroSindicato());
        return empregadoComissionado;
    }

    public static EmpregadoComissionado converterHoristaParaComissionado(EmpregadoHorista empregado, double comissao) {
        EmpregadoComissionado empregadoComissionado = new EmpregadoComissionado();
        empregadoComissionado.setId(empregado.getId());
        empregadoComissionado.setNome(empregado.getNome());
        empregadoComissionado.setEndereco(empregado.getEndereco());
        empregadoComissionado.setTipo(TipoDeEmpregado.COMISSIONADO.getValue());
        empregadoComissionado.setMetodoPagamento(empregado.getMetodoPagamento());
        empregadoComissionado.setSalarioMensal(empregado.getSalarioPorHora());
        empregadoComissionado.setTaxaDeComissao(comissao);
        empregadoComissionado.setMembroSindicato(empregado.getMembroSindicato());
        return empregadoComissionado;
    }

    public static void salvarEmpregadoXML(List<Empregado> empregados, String arquivo) {
        try {
            XMLEncoder enconder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(arquivo)));
            for (Empregado empregado : empregados) {
                enconder.writeObject(empregado);
            }
            enconder.close();
        } catch (FileNotFoundException fileNotFound) {
            System.out.println("Erro: nao foi possivel salvar os dados");
        }
    }

    public static List<Empregado> trazerDadosDosEmpregadosXML (String arquivo) {
        List<Empregado> empregados = new ArrayList<>();
        try {
            XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(arquivo)));
            Object object;
            while (true) {
                try {
                    object = decoder.readObject();
                    empregados.add((Empregado) object);
                } catch (Exception e) {
                    break;
                }
            }
        } catch (FileNotFoundException fileNotFound) {
            System.out.println("Erro: Nao foi possivel ler os dados");
        }
        return empregados;
    }

    public static void salvarPontoDosEmpregadosXML(EmpregadosNoSistemaPonto dadosEmpregados, String arquivo) {
        try {
            XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(arquivo)));
            encoder.writeObject(dadosEmpregados);
            encoder.close();
        } catch (FileNotFoundException e) {
            System.out.println("Erro: nao foi possivel salvar os dados no sistema de ponto");
        }
    }

    public static EmpregadosNoSistemaPonto  trazerDadosDoPontoXMl(String arquivo) {
        EmpregadosNoSistemaPonto dadosEmpregados = new EmpregadosNoSistemaPonto();

        try {
            XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(arquivo)));
            Object object;
            while (true) {
                try {
                    object = decoder.readObject();
                    dadosEmpregados = (EmpregadosNoSistemaPonto) object;
                } catch (Exception e) {
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Nao foi possivel carregas os dados do ponto");
        }
        return dadosEmpregados;
    }

    public static void salvarDadosSistemaSindical(EmpregadosNoSistemaSindical dadosEmpregados, String arquvivo) {
        try {
            XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(arquvivo)));
            encoder.writeObject(dadosEmpregados);
            encoder.close();
        } catch (FileNotFoundException e) {
            System.out.println("ERRO: nao foi possivel salvar dados no sistema sindical");
        }
    }

    public static EmpregadosNoSistemaSindical trazerDadosSistemaSindicalXML(String arquivo) {
        EmpregadosNoSistemaSindical dadosEmpregados = new EmpregadosNoSistemaSindical();

        try {
            XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(arquivo)));
            Object object;
            while (true) {
                try {
                    object = decoder.readObject();
                    dadosEmpregados = (EmpregadosNoSistemaSindical) object;
                } catch (Exception e) {
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Erro: nao foi possivel acessar os dados no sistema sindical");
        }

        return dadosEmpregados;
    }

    public static void salvarDadosSistemVendasXML(EmpregadosNoSistemaVendas dadosEmpregados, String arquivo) {
        try {
            XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(arquivo)));
            encoder.writeObject(dadosEmpregados);
            encoder.close();
        } catch (FileNotFoundException e) {
            System.out.println("Erro: nao foi possivel salvar os dados das vendas");
        }
    }

    public static EmpregadosNoSistemaVendas trazerDadosSistemaVendasXML(String arquivo) {
        EmpregadosNoSistemaVendas dadosEmpregados = new EmpregadosNoSistemaVendas();

        try {
            XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(arquivo)));
            Object object;
            while (true) {
                try {
                    object = decoder.readObject();
                    dadosEmpregados = (EmpregadosNoSistemaVendas) object;
                } catch (Exception e) {
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Erro: nao foi possivel acessar os daods de vendas");
        }
        return dadosEmpregados;
    }

    public static String retornaDadosBancarios(MetodoPagamento metodoPagamento, String tipoDoDado) throws EmpregadoNaoRecebeViaBancoException, DadosInvalidoException {

        if (!metodoPagamento.getReceberViaBanco()) {
            throw new EmpregadoNaoRecebeViaBancoException("Empregado nao recebe em banco.");
        }
        return switch (tipoDoDado) {
            case "banco" -> metodoPagamento.getBanco().getBanco();
            case "agencia" -> metodoPagamento.getBanco().getAgencia();
            case "contaCorrente" -> metodoPagamento.getBanco().getContaCorrente();
            default -> throw new DadosInvalidoException("Dado bancario invalido");
        };
    }

    public static String retornaDadosSindicais(MembroSindicato membroSindicato, String tipoDoDado) throws EmpregadoNaoSindicalizadoException{
        if (!membroSindicato.getSindicalizado()) {
            System.out.println("empregado nao sindicalizado");
            throw new EmpregadoNaoSindicalizadoException("Empregado nao eh sindicalizado.");
        }
        return switch (tipoDoDado) {
            case "idSindicato" -> membroSindicato.getIdMembro();
            case "taxaSindical" -> converterSalarioParaString(membroSindicato.getTaxaSindical());
            default -> throw new DadosInvalidoException("Dado sindical invalido");
        };
    }

    public static String converterSalarioParaString (double salario) {

        DecimalFormat df = new DecimalFormat("0.00");
        String salarioConvertido = df.format(salario);
        salarioConvertido = salarioConvertido.replace(".", ",");
        return salarioConvertido;
    }

    public static double converteAtributoStringParaDouble(String atributo, String valorAtributo) {
        if (valorAtributo.isBlank() || valorAtributo.isEmpty()) {
            return 0.0;
        }
        if (contemLetras(valorAtributo)) throw new DadosInvalidoException(atributo+ " deve ser numeric" + (atributo.equals("Comissao") ? "a." : "o."));
        String valorAtributoFormatado = valorAtributo.replace(',', '.');
        return Double.parseDouble(valorAtributoFormatado);
    }

    public static double converteQualquerAtributoStringParaDouble(String valorAtributo) {
        if (valorAtributo.isBlank() || valorAtributo.isEmpty()) {
            return 0.0;
        }
        String valorAtributoFormatado = valorAtributo.replace(',', '.');
        return Double.parseDouble(valorAtributoFormatado);
    }

    public static String formataCasasDecimais(Double valor) {
        DecimalFormatSymbols sy = new DecimalFormatSymbols(Locale.getDefault());
        sy.setDecimalSeparator(',');
        sy.setGroupingSeparator('.');

        DecimalFormat df = new DecimalFormat("0.00", sy);
        return df.format(valor);
    }

    public static void excluirArquivo(String caminho) {
        File arquivo = new File(caminho);
        try {
            arquivo.delete();
        } catch (Exception e) {
            throw new RuntimeException("erro na exlusao dos arquivos");
        }
    }

    public static MembroSindicato criaMembroSindicato(boolean sindicalizado) {
        MembroSindicato membroSindicato = new MembroSindicato();
        membroSindicato.setSindicalizado(sindicalizado);
        membroSindicato.setTaxaSindical(1.0);
        return membroSindicato;
    }

    public static double validarSalario(String salario) throws DadosInvalidoException{
        if (salario.isEmpty() || salario.isBlank()) {
            throw  new DadosInvalidoException("Salario nao pode ser nulo.");
        }
        double salarioEmDouble = converteAtributoStringParaDouble("Salario", salario);
        if (salarioEmDouble < 0) {
            throw new DadosInvalidoException("Salario deve ser nao-negativo.");
        }
        if (contemLetras(salario)) throw new DadosInvalidoException("Salario deve ser numerico.");
        return salarioEmDouble;
    }

    public static double validarComissao(String comissao)throws DadosInvalidoException {
        if (comissao.isEmpty() || comissao.isBlank()) {
            throw new DadosInvalidoException("Comissao nao pode ser nula.");
        }
        double comissaoEmDouble = converteAtributoStringParaDouble("Comissao", comissao);
        if (comissaoEmDouble < 0) {
            throw new DadosInvalidoException("Comissao deve ser nao-negativa.");
        }
        if (contemLetras(comissao)) throw new DadosInvalidoException("Comissao deve ser numerica.");

        return comissaoEmDouble;
    }


    public static void validarIdentificaoSindical(String valor) throws DadosInvalidoException{
        if (valor.isEmpty() || valor.isBlank()) throw new DadosInvalidoException("Identificacao do sindicato nao pode ser nula.");
    }

    public static void validarTaxaSindical(String valor) throws DadosInvalidoException {
        if (valor.isEmpty() || valor.isBlank()) throw new DadosInvalidoException("Taxa sindical nao pode ser nula.");
        if (contemLetras(valor)) throw new DadosInvalidoException("Taxa sindical deve ser numerica.");

    }
    public static void validarInformacoesBancarias(String atributo, String valor)throws DadosInvalidoException {
        if (valor.isEmpty() || valor.isBlank()) {
            throw new DadosInvalidoException(atributo +  " nao pode ser nulo.");
        }
    }

    public static boolean validarData(String data) {

        if (data.isBlank() || data.isEmpty()) return false;

        if (!data.matches("^\\d{1,2}(\\/|-)\\d{1,2}(\\/|-)\\d{4}$")) return false;

        String[] dataParticionada = data.split("\\/|-");
        int dia = Integer.parseInt(dataParticionada[0]);
        int mes = Integer.parseInt(dataParticionada[1]);
        int ano = Integer.parseInt(dataParticionada[2]);

        if (dia < 1 || dia > 31 || mes < 1 || mes > 12) return false;

        boolean ehBissexto = (ano % 4 == 0) && ((ano % 100 != 0) || (ano % 400 == 0));
        int diaFevereiro = ehBissexto ? 29 : 28;

        if (mes == 2 && dia > diaFevereiro) return false;

        return true;
    }

    public static Boolean contemLetras(String texto) {
        return texto != null && texto.matches(".*[a-zA-Z].*");
    }

    public static String retornaMetodoPagamento(MetodoPagamento metodoPagamento) {
        if(metodoPagamento.getReceberViaBanco())
            return "banco";
        if(metodoPagamento.getReceberEmMaos())
            return "emMaos";
        if(metodoPagamento.getReceberViaCorreios())
            return "correios";
        return "";
    }

    public static String retornaIdSindicato(MembroSindicato membroSindicato) throws EmpregadoNaoSindicalizadoException {
        if (!membroSindicato.getSindicalizado()) throw new EmpregadoNaoSindicalizadoException("Empregado nao eh sindicalizado.");
        return membroSindicato.getIdMembro();
    }

}
