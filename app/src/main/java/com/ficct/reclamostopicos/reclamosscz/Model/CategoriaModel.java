package com.ficct.reclamostopicos.reclamosscz.Model;

/**
 * Created by Manuel Saavedra
 */

public class CategoriaModel {

    public String codigo;
    public String descripcion;
    public int idImg;

    public int getIdImg() {
        return idImg;
    }

    public void setIdImg(int idImg) {
        this.idImg = idImg;
    }

    public CategoriaModel() {

    }

    public CategoriaModel(String codigo, String descripcion,int idimg) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.idImg=idimg;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


}
