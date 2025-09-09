package br.ufal.ic.p2.wepayu.models.sistemaponto;

import java.time.LocalDate;


public class CartaoDePonto {

    private String idEmpregado;
    private LocalDate data;
    private String horas;

    public CartaoDePonto(LocalDate data, String horas, String idEmpregado) throws Exception {
        this.data = data;
        this.idEmpregado = idEmpregado;
        setHoras(validaHora(horas));
    }

    public void setHoras(String horas) {
        this.horas = horas;
    }

    public String getIdEmpregado() {
        return idEmpregado;
    }

    public LocalDate getData() {
        return data;
    }

    public String getHoras() {
        return horas;
    }

    public String validaHora (String hora) throws Exception {
            if (hora.equalsIgnoreCase("0")) {
                throw  new Exception("O numero de horas nao pode ser nulo");
            }
            int horasEmInt = Integer.parseInt(hora);
            if (horasEmInt < 0) {
                throw  new Exception("O numero de horas nao pode ser negativo");
            }
            return hora;
    }
}
