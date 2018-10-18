package com.ficct.reclamostopicos.reclamosscz.WebServices;

/**
 * Created by USUARIO on 23/07/2017.
 */

public class Constantes {

    //LOCAL private static final String PUERTO_HOST = ":8080";//":8080";//:63343";
    private static final String PUERTO_HOST = "";//":8080";//:63343";


    private static final String IP ="192.168.100.97";//"192.168.43.123";//"manueldeveloper.xyz";///"192.168.0.29" ;//"192.168.0.166";//"10.0.3.2";
    /**
     * URLs del Web Service
     */


    public static final String IP_YPFBAVIACION="172.20.10.71";
    public static final String IP_WIFI_MANUEL ="192.168.100.97";
    public static final String IP_HUAWEI_ALEX="192.168.43.123";



    public static final String URL_GET_CATEGORIAS_WS = "http://" + IP_WIFI_MANUEL + PUERTO_HOST + "/app-Reclamos/public/api/categorias";
    public static final String URL_ADD_RECLAMO_WS = "http://" + IP_WIFI_MANUEL + PUERTO_HOST + "/app-Reclamos/public/api/guardarReclamo";


    // CONSTANTES PARA LA BASE DE DATOS LOCAL
    public static final String TABLE_RECLAMO = "Reclamo";
    public static String COLUMN_ID ="ID";
    public static final String COLUMN_TITULO = "Titulo";
    public static final String COLUMN_DESCRIPCION = "Descripcion";
    public static final String COLUMN_CALLE = "Calle";
    public static final String COLUMN_BARRIO = "Barrio";
    public static final String COLUMN_ZONA = "Zona";
    public static final String COLUMN_LATITUD = "Latitud";
    public static final String COLUMN_LONGITUD = "Longitud";
    public static final String COLUMN_IMAGEN = "Imagen";
    public static final String COLUMN_ESTADO = "Estado";
    public static final String COLUMN_ID_CATEGORIA = "ID_Categoria";




}
