package br.ufal.ic.p2.wepayu.utils;

import br.ufal.ic.p2.wepayu.models.empregado.Empregado;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Utils {

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
            System.out.printf("Erro: Nao foi possivel ler os dados");
        }
        return empregados;
    }

}
