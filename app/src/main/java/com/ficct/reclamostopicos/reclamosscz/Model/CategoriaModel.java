package com.ficct.reclamostopicos.reclamosscz.Model;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

/**
 * Created by Manuel Saavedra
 */

public class CategoriaModel {

    public int ID;
    public String nombre;
    public String descripcion;
    public int idImg;


    public int getIdImg() {
        return idImg;
    }

    public void setIdImg(int idImg) {
        this.idImg = idImg;
    }

    public CategoriaModel() {
        this(-1,"","",-1);
    }

    public CategoriaModel(int ID, String nombre,String descripcion, int idimg) {
        this.ID = ID;
        this.nombre = nombre;
        this.descripcion=descripcion;
        this.idImg=idimg;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj!=null && obj instanceof CategoriaModel){
            return this.getID()==((CategoriaModel)obj).getID();
        }
        return false;
    }
}
