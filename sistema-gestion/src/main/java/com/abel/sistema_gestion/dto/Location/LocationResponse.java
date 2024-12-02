package com.abel.sistema_gestion.dto.Location;

import java.util.List;

public class LocationResponse {
    private int cantidad;
    private int inicio;
    private List<Location> localidades;
    private Parametros parametros;
    private int total;

    public LocationResponse() {
    }

    // Getters y setters
    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getInicio() {
        return inicio;
    }

    public void setInicio(int inicio) {
        this.inicio = inicio;
    }

    public List<Location> getLocalidades() {
        return localidades;
    }

    public void setLocalidades(List<Location> localidades) {
        this.localidades = localidades;
    }

    public Parametros getParametros() {
        return parametros;
    }

    public void setParametros(Parametros parametros) {
        this.parametros = parametros;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}