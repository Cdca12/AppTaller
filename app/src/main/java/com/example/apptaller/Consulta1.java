package com.example.apptaller;

public class Consulta1 {

    public static final String[] nombreColumnas = {"Ciudad", "Ingreso Total",
            "Ingreso Menor", "Ingreso Mayor", "Ingreso Promedio"};

    private String ciudad, ingresoTotal, ingresoMenor, ingresoMayor, ingresoPromedio;

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getIngresoTotal() {
        return ingresoTotal;
    }

    public void setIngresoTotal(String ingresoTotal) {
        this.ingresoTotal = ingresoTotal;
    }

    public String getIngresoMenor() {
        return ingresoMenor;
    }

    public void setIngresoMenor(String ingresoMenor) {
        this.ingresoMenor = ingresoMenor;
    }

    public String getIngresoMayor() {
        return ingresoMayor;
    }

    public void setIngresoMayor(String ingresoMayor) {
        this.ingresoMayor = ingresoMayor;
    }

    public String getIngresoPromedio() {
        return ingresoPromedio;
    }

    public void setIngresoPromedio(String ingresoPromedio) {
        this.ingresoPromedio = ingresoPromedio;
    }
}
