package br.ufal.ic.p2.wepayu.services.implementation;

import br.ufal.ic.p2.wepayu.models.empregado.Empregado;
import br.ufal.ic.p2.wepayu.models.pagamento.AgendaDePagamentos;
import br.ufal.ic.p2.wepayu.models.sindicato.MembroSindicato;
import br.ufal.ic.p2.wepayu.services.PersistenciaService;

import java.util.Map;
import java.beans.XMLEncoder;
import java.beans.XMLDecoder;
import java.io.*;


public class PersistenciaServiceImpl implements PersistenciaService {

    private Map<String, Empregado> empregados;
    private Map<String, MembroSindicato> membrosSindicato;
    private int id;

    private static final String FILE_EMPREGADOS = "empregados.xml";
    private static final String FILE_SINDICATO = "sindicato.xml";
    private static final String FILE_AGENDAS = "agendas.xml";

    public PersistenciaServiceImpl(Map<String, Empregado> empregados,
                                   Map<String, MembroSindicato> membrosSindicato,
                                   int id) {
        this.empregados = empregados;
        this.membrosSindicato = membrosSindicato;
        this.id = id;
    }

    @Override
    public void salvarSistema() {
        try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(FILE_EMPREGADOS)))) {
            encoder.writeObject(empregados);
            encoder.writeObject(id);
        } catch (Exception e) {
            System.err.println("Erro ao salvar sistema: " + e.getMessage());
        }

        try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(FILE_SINDICATO)))) {
            encoder.writeObject(membrosSindicato);
        } catch (Exception e) {
            System.err.println("Erro ao salvar membros do sindicato: " + e.getMessage());
        }

        try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(FILE_AGENDAS)))) {
            java.util.Set<String> descricoesAgendas = new java.util.HashSet<>();
            for (AgendaDePagamentos agenda : AgendaDePagamentos.getAgendasCustomizadas().values()) {
                descricoesAgendas.add(agenda.getDescricao());
            }
            encoder.writeObject(descricoesAgendas);
        } catch (Exception e) {
            System.err.println("Erro ao salvar agendas customizadas: " + e.getMessage());
        }
    }

    @Override
    public void carregarSistema() {
        File agendas = new File(FILE_AGENDAS);
        if (agendas.exists()) {
            try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(FILE_AGENDAS)))) {
                @SuppressWarnings("unchecked")
                java.util.Set<String> descricoesAgendas = (java.util.Set<String>) decoder.readObject();

                for (String descricao : descricoesAgendas) {
                    try {
                        AgendaDePagamentos.criarAgenda(descricao);
                    } catch (Exception e) {
                    }
                }
            } catch (Exception e) {
                System.err.println("Erro ao carregar agendas customizadas: " + e.getMessage());
            }
        }

        File emps = new File(FILE_EMPREGADOS);
        if (!emps.exists()) return;

        try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(FILE_EMPREGADOS)))) {
            empregados.putAll((Map<String, Empregado>) decoder.readObject());
            id = (Integer) decoder.readObject();
        } catch (Exception e) {
            System.err.println("Erro ao carregar sistema: " + e.getMessage());
        }

        File sin = new File(FILE_SINDICATO);
        if (!sin.exists()) return;

        try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(FILE_SINDICATO)))) {
            membrosSindicato.putAll((Map<String, MembroSindicato>) decoder.readObject());
        } catch (Exception e) {
            System.err.println("Erro ao carregar membros do sindicato: " + e.getMessage());
        }
    }

    @Override
    public void zerarSistema() {
        empregados.clear();
        membrosSindicato.clear();
        AgendaDePagamentos.limparAgendasCustomizadas();
        id = 0;
    }

    @Override
    public void encerrarSistema() {
        salvarSistema();
    }
}