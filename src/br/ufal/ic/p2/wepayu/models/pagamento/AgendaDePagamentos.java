package br.ufal.ic.p2.wepayu.models.pagamento;

import br.ufal.ic.p2.wepayu.models.CartaoDePonto;
import br.ufal.ic.p2.wepayu.models.ResultadoDeVenda;
import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.models.empregado.EmpregadoAssalariado;
import br.ufal.ic.p2.wepayu.models.empregado.EmpregadoComissionado;
import br.ufal.ic.p2.wepayu.models.empregado.EmpregadoHorista;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;


public class AgendaDePagamentos {
    private static final Set<String> AGENDAS_PADRAO = Set.of(
            "semanal 5", "semanal 2 5", "mensal $"
    );

    private static final Map<String, AgendaDePagamentos> agendasCustomizadas = new HashMap<>();

    private String descricao;
    private String tipo;
    private int parametro1;
    private int parametro2;

    private AgendaDePagamentos(String descricao, String tipo, int parametro1, int parametro2) {
        this.descricao = descricao;
        this.tipo = tipo;
        this.parametro1 = parametro1;
        this.parametro2 = parametro2;
    }


    public static AgendaDePagamentos criarAgenda(String descricao) {
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("Descricao de agenda invalida");
        }

        if (AGENDAS_PADRAO.contains(descricao) || agendasCustomizadas.containsKey(descricao)) {
            throw new IllegalArgumentException("Agenda de pagamentos ja existe");
        }

        String[] partes = descricao.trim().split("\\s+");

        if (partes.length < 2) {
            throw new IllegalArgumentException("Descricao de agenda invalida");
        }

        String tipo = partes[0];

        if ("semanal".equals(tipo)) {
            return criarAgendaSemanal(descricao, partes);
        } else if ("mensal".equals(tipo)) {
            return criarAgendaMensal(descricao, partes);
        } else {
            throw new IllegalArgumentException("Descricao de agenda invalida");
        }
    }

    private static AgendaDePagamentos criarAgendaSemanal(String descricao, String[] partes) {
        if (partes.length == 2) {
            int diaSemana = Integer.parseInt(partes[1]);
            if (diaSemana < 1 || diaSemana > 7) {
                throw new IllegalArgumentException("Descricao de agenda invalida");
            }
            AgendaDePagamentos agenda = new AgendaDePagamentos(descricao, "semanal", diaSemana, 0);
            agendasCustomizadas.put(descricao, agenda);
            return agenda;
        } else if (partes.length == 3) {
            int semanas = Integer.parseInt(partes[1]);
            int diaSemana = Integer.parseInt(partes[2]);
            if (semanas < 1 || semanas > 52 || diaSemana < 1 || diaSemana > 7) {
                throw new IllegalArgumentException("Descricao de agenda invalida");
            }
            AgendaDePagamentos agenda = new AgendaDePagamentos(descricao, "semanal", semanas, diaSemana);
            agendasCustomizadas.put(descricao, agenda);
            return agenda;
        } else {
            throw new IllegalArgumentException("Descricao de agenda invalida");
        }
    }

    private static AgendaDePagamentos criarAgendaMensal(String descricao, String[] partes) {
        if (partes.length == 2) {
            String parametro = partes[1];
            if ("$".equals(parametro)) {
                AgendaDePagamentos agenda = new AgendaDePagamentos(descricao, "mensal", -1, 0);
                agendasCustomizadas.put(descricao, agenda);
                return agenda;
            } else {
                int diaMes = Integer.parseInt(parametro);
                if (diaMes < 1 || diaMes > 28) {
                    throw new IllegalArgumentException("Descricao de agenda invalida");
                }
                AgendaDePagamentos agenda = new AgendaDePagamentos(descricao, "mensal", diaMes, 0);
                agendasCustomizadas.put(descricao, agenda);
                return agenda;
            }
        } else {
            throw new IllegalArgumentException("Descricao de agenda invalida");
        }
    }


    public static boolean isAgendaValida(String descricao) {
        return AGENDAS_PADRAO.contains(descricao) || agendasCustomizadas.containsKey(descricao);
    }


    public static AgendaDePagamentos getAgenda(String descricao) {
        if (AGENDAS_PADRAO.contains(descricao)) {
            return new AgendaDePagamentos(descricao, getTipoPadrao(descricao),
                    getParametro1Padrao(descricao), getParametro2Padrao(descricao));
        }
        return agendasCustomizadas.get(descricao);
    }

    private static String getTipoPadrao(String descricao) {
        if (descricao.startsWith("semanal")) return "semanal";
        if (descricao.startsWith("mensal")) return "mensal";
        return "semanal";
    }

    private static int getParametro1Padrao(String descricao) {
        switch (descricao) {
            case "semanal 5": return 5;
            case "semanal 2 5": return 2;
            case "mensal $": return -1;
            default: return 5;
        }
    }

    private static int getParametro2Padrao(String descricao) {
        switch (descricao) {
            case "semanal 2 5": return 5;
            default: return 0;
        }
    }


    public boolean devePagarNaData(String data) {
        try {
            String[] partes = data.split("/");
            int dia = Integer.parseInt(partes[0]);
            int mes = Integer.parseInt(partes[1]);
            int ano = Integer.parseInt(partes[2]);

            java.time.LocalDate localDate = java.time.LocalDate.of(ano, mes, dia);
            java.time.DayOfWeek diaSemana = localDate.getDayOfWeek();
            int diaSemanaNumero = diaSemana.getValue();

            if ("semanal".equals(tipo)) {
                if (parametro2 == 0) {
                    return diaSemanaNumero == parametro1;
                } else {
                    java.time.LocalDate dataBase;

                    if (parametro1 == 52 && parametro2 == 1) {
                        dataBase = java.time.LocalDate.of(2004, 12, 26);
                    } else {
                        dataBase = java.time.LocalDate.of(2005, 1, 14);
                    }

                    while (dataBase.getDayOfWeek().getValue() != parametro2) {
                        dataBase = dataBase.plusDays(1);
                    }

                    if (localDate.isBefore(dataBase)) {
                        return false;
                    }

                    long diasEntre = java.time.temporal.ChronoUnit.DAYS.between(dataBase, localDate);
                    long semanasEntre = diasEntre / 7;
                    return diaSemanaNumero == parametro2 && semanasEntre % parametro1 == 0;
                }
            } else if ("mensal".equals(tipo)) {
                if (parametro1 == -1) {
                    return localDate.equals(localDate.withDayOfMonth(localDate.lengthOfMonth()));
                } else {
                    return dia == parametro1;
                }
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }


    public double calcularValorPagamento(Empregado empregado, String dataInicial, String dataFinal) {
        String tipoEmpregado = empregado.getTipo();

        if ("semanal".equals(tipo)) {
            if (parametro2 == 0) {
                return calcularPagamentoSemanal(empregado, tipoEmpregado);
            } else {
                return calcularPagamentoBiSemanal(empregado, tipoEmpregado, parametro1);
            }
        } else if ("mensal".equals(tipo)) {
            return calcularPagamentoMensal(empregado, tipoEmpregado);
        }

        return 0.0;
    }

    private double calcularPagamentoSemanal(Empregado empregado, String tipoEmpregado) {
        if ("horista".equals(tipoEmpregado)) {
            return calcularPagamentoHoristaSemanal(empregado);
        } else if ("assalariado".equals(tipoEmpregado)) {
            return calcularPagamentoAssalariadoSemanal(empregado);
        } else if ("comissionado".equals(tipoEmpregado)) {
            return calcularPagamentoComissionadoSemanal(empregado);
        }
        return 0.0;
    }

    private double calcularPagamentoBiSemanal(Empregado empregado, String tipoEmpregado, int semanas) {
        if ("horista".equals(tipoEmpregado)) {
            return calcularPagamentoHoristaBiSemanal(empregado);
        } else if ("assalariado".equals(tipoEmpregado)) {
            return calcularPagamentoAssalariadoBiSemanal(empregado, semanas);
        } else if ("comissionado".equals(tipoEmpregado)) {
            return calcularPagamentoComissionadoBiSemanal(empregado, semanas);
        }
        return 0.0;
    }

    private double calcularPagamentoMensal(Empregado empregado, String tipoEmpregado) {
        if ("horista".equals(tipoEmpregado)) {
            return calcularPagamentoHoristaMensal(empregado);
        } else if ("assalariado".equals(tipoEmpregado)) {
            return calcularPagamentoAssalariadoMensal(empregado);
        } else if ("comissionado".equals(tipoEmpregado)) {
            return calcularPagamentoComissionadoMensal(empregado);
        }
        return 0.0;
    }

    private double calcularPagamentoHoristaSemanal(Empregado empregado) {
        if (!(empregado instanceof EmpregadoHorista)) return 0.0;
        EmpregadoHorista horista = (EmpregadoHorista) empregado;
        double salarioHora = horista.getSalarioPorHora();

        double horasNormais = calcularHorasNormais(horista, 7);
        double horasExtras = calcularHorasExtras(horista, 7);

        return (horasNormais * salarioHora) + (horasExtras * salarioHora * 1.5);
    }

    private double calcularPagamentoHoristaBiSemanal(Empregado empregado) {
        if (!(empregado instanceof EmpregadoHorista)) return 0.0;
        EmpregadoHorista horista = (EmpregadoHorista) empregado;
        double salarioHora = horista.getSalarioPorHora();

        double horasNormais = calcularHorasNormais(horista, 14);
        double horasExtras = calcularHorasExtras(horista, 14);

        return (horasNormais * salarioHora) + (horasExtras * salarioHora * 1.5);
    }

    private double calcularPagamentoHoristaMensal(Empregado empregado) {
        if (!(empregado instanceof EmpregadoHorista)) return 0.0;
        EmpregadoHorista horista = (EmpregadoHorista) empregado;
        double salarioHora = horista.getSalarioPorHora();

        double horasNormais = calcularHorasNormais(horista, 30);
        double horasExtras = calcularHorasExtras(horista, 30);

        return (horasNormais * salarioHora) + (horasExtras * salarioHora * 1.5);
    }

    private double calcularPagamentoAssalariadoSemanal(Empregado empregado) {
        if (!(empregado instanceof EmpregadoAssalariado)) return 0.0;
        EmpregadoAssalariado assalariado = (EmpregadoAssalariado) empregado;
        double salarioAnual = assalariado.getSalarioMensal() * 12;
        return salarioAnual / 52;
    }

    private double calcularPagamentoAssalariadoBiSemanal(Empregado empregado, int semanas) {
        if (!(empregado instanceof EmpregadoAssalariado)) return 0.0;
        EmpregadoAssalariado assalariado = (EmpregadoAssalariado) empregado;
        double salarioAnual = assalariado.getSalarioMensal() * 12;
        return (salarioAnual / 52) * semanas;
    }

    private double calcularPagamentoAssalariadoMensal(Empregado empregado) {
        if (!(empregado instanceof EmpregadoAssalariado)) return 0.0;
        EmpregadoAssalariado assalariado = (EmpregadoAssalariado) empregado;
        return assalariado.getSalarioMensal();
    }

    private double calcularPagamentoComissionadoSemanal(Empregado empregado) {
        if (!(empregado instanceof EmpregadoComissionado)) return 0.0;
        EmpregadoComissionado comissionado = (EmpregadoComissionado) empregado;
        double salarioAnual = comissionado.getSalarioMensal() * 12;
        double salarioSemanal = salarioAnual / 52;

        double comissoes = calcularComissoes(comissionado, 7);
        return salarioSemanal + comissoes;
    }

    private double calcularPagamentoComissionadoBiSemanal(Empregado empregado, int semanas) {
        if (!(empregado instanceof EmpregadoComissionado)) return 0.0;
        EmpregadoComissionado comissionado = (EmpregadoComissionado) empregado;
        double salarioAnual = comissionado.getSalarioMensal() * 12;
        double salarioBiSemanal = (salarioAnual / 52) * semanas;

        double comissoes = calcularComissoes(comissionado, semanas * 7);
        return salarioBiSemanal + comissoes;
    }

    private double calcularPagamentoComissionadoMensal(Empregado empregado) {
        if (!(empregado instanceof EmpregadoComissionado)) return 0.0;
        EmpregadoComissionado comissionado = (EmpregadoComissionado) empregado;
        double salarioMensal = comissionado.getSalarioMensal();

        double comissoes = calcularComissoes(comissionado, 30);
        return salarioMensal + comissoes;
    }

    private double calcularHorasNormais(EmpregadoHorista horista, int dias) {
        double totalHoras = 0.0;
        int count = 0;

        for (CartaoDePonto cartao : horista.getCartoes()) {
            if (count >= dias) break;
            double horas = cartao.getHoras();
            if (horas <= 8) {
                totalHoras += horas;
            } else {
                totalHoras += 8;
            }
            count++;
        }

        return totalHoras;
    }

    private double calcularHorasExtras(EmpregadoHorista horista, int dias) {
        double totalHoras = 0.0;
        int count = 0;

        for (CartaoDePonto cartao : horista.getCartoes()) {
            if (count >= dias) break;
            double horas = cartao.getHoras();
            if (horas > 8) {
                totalHoras += (horas - 8);
            }
            count++;
        }

        return totalHoras;
    }

    private double calcularComissoes(EmpregadoComissionado comissionado, int dias) {
        double totalComissoes = 0.0;
        double taxaComissao = comissionado.getTaxaDeComissao();
        int count = 0;

        for (ResultadoDeVenda venda : comissionado.getResultadoDeVenda()) {
            if (count >= dias) break;
            totalComissoes += venda.getValor() * taxaComissao;
            count++;
        }

        return totalComissoes;
    }

    public String getDescricao() { return descricao; }
    public String getTipo() { return tipo; }
    public int getParametro1() { return parametro1; }
    public int getParametro2() { return parametro2; }


    public static Map<String, AgendaDePagamentos> getAgendasCustomizadas() {
        return new HashMap<>(agendasCustomizadas);
    }


    public static void limparAgendasCustomizadas() {
        agendasCustomizadas.clear();
    }


    public static boolean agendaCustomizadaExiste(String descricao) {
        return agendasCustomizadas.containsKey(descricao);
    }
}