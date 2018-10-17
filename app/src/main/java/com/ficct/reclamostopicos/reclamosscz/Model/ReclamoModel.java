package com.ficct.reclamostopicos.reclamosscz.Model;

public class ReclamoModel {

    private long ID;
    private String Titulo;
    private String Descripcion;
    private String Calle;
    private String Barrio;
    private String Zona;
    private double Latitud;
    private double Longitud;
    private String Imagen;
    private String Estado;
    private String Categoria;

    public ReclamoModel(long ID, String titulo, String descripcion, String calle, String barrio, String zona, double latitud, double longitud, String imagen, String estado, String categoria) {
        this.ID = ID;
        Titulo = titulo;
        Descripcion = descripcion;
        Calle = calle;
        Barrio = barrio;
        Zona = zona;
        Latitud = latitud;
        Longitud = longitud;
        Imagen = imagen;
        Estado = estado;
        Categoria = categoria;
    }

    public ReclamoModel() {
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getCalle() {
        return Calle;
    }

    public void setCalle(String calle) {
        Calle = calle;
    }

    public String getBarrio() {
        return Barrio;
    }

    public void setBarrio(String barrio) {
        Barrio = barrio;
    }

    public String getZona() {
        return Zona;
    }

    public void setZona(String zona) {
        Zona = zona;
    }

    public double getLatitud() {
        return Latitud;
    }

    public void setLatitud(double latitud) {
        Latitud = latitud;
    }

    public double getLongitud() {
        return Longitud;
    }

    public void setLongitud(double longitud) {
        Longitud = longitud;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }
}
