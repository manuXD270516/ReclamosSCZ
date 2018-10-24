package com.ficct.reclamostopicos.reclamosscz.Model;

import android.widget.ImageButton;

public class ReclamoModel {

    private long ID;
    private String Titulo;
    private String Descripcion;
    private String Calle;
    private String Barrio;
    private String Zona;
    private double Latitud;
    private double Longitud;
    private byte[] Imagen;
    private String Estado;
    private int IDCategoria;

    public ReclamoModel(long ID, String titulo, String descripcion, String calle, String barrio, String zona, double latitud, double longitud, byte[] imagen, String estado, int IDCategoria) {
        this.ID = ID;
        this.Titulo = titulo;
        this.Descripcion = descripcion;
        this.Calle = calle;
        this.Barrio = barrio;
        this.Zona = zona;
        this.Latitud = latitud;
        this.Longitud = longitud;
        this.Imagen = imagen;
        this.Estado = estado;
        this.IDCategoria = IDCategoria;
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

    public byte[] getImagen() {
        return this.Imagen;
    }

    public void setImagen(byte[] imagen) {
        Imagen = imagen;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public int getIDCategoria() {
        return IDCategoria;
    }

    public void setIDCategoria(int IDCategoria) {
        this.IDCategoria = IDCategoria;
    }
}
