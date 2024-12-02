package com.abel.sistema_gestion.dto.Location;

public class Location {
    private String categoria;
    private Centroide centroide;
    private Departamento departamento;
    private String id;
    private LocalidadCensal localidad_censal;
    private Municipio municipio;
    private String nombre;
    private Provincia provincia;

    public Location() {
    }

    // Getters y setters
    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Centroide getCentroide() {
        return centroide;
    }

    public void setCentroide(Centroide centroide) {
        this.centroide = centroide;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalidadCensal getLocalidad_censal() {
        return localidad_censal;
    }

    public void setLocalidad_censal(LocalidadCensal localidad_censal) {
        this.localidad_censal = localidad_censal;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }
}
