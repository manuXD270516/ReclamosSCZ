package com.ficct.reclamostopicos.reclamosscz.WebServices;

/**
 * Created by USUARIO on 23/07/2017.
 */

public class Constantes {

    public static final int CODIGO_DETALLE = 100;

    /**
     * Transición Detalle -> Actualización
     */
    public static final int CODIGO_ACTUALIZACION = 101;
    /**
     * Puerto que utilizas para la conexión.
     * Dejalo en blanco si no has configurado esta carácteristica.
     */
    //LOCAL private static final String PUERTO_HOST = ":8080";//":8080";//:63343";
    private static final String PUERTO_HOST = "";//":8080";//:63343";
    /**
     * Dirección IP de genymotion o AVD
     */

    // LOCAL private static final String IP ="192.168.43.168";////www.pruebaws.hol.es";///"192.168.0.29" ;//"192.168.0.166";//"10.0.3.2";
    //private static final String IP ="www.pruebaws.hol.es";///"192.168.0.29" ;//"192.168.0.166";//"10.0.3.2";
    private static final String IP ="192.168.43.123";//"manueldeveloper.xyz";///"192.168.0.29" ;//"192.168.0.166";//"10.0.3.2";
    /**
     * URLs del Web Service
     */

    /*public static final String GET_CATEOGORIAS = "http://" + IP + PUERTO_HOST + "/I%20Wish/obtener_metas.php";
    public static final String GET_BY_ID = "http://" + IP + PUERTO_HOST + "/I%20Wish/obtener_meta_por_id.php";
    public static final String UPDATE = "http://" + IP + PUERTO_HOST + "/I%20Wish/actualizar_meta.php";
    public static final String DELETE = "http://" + IP + PUERTO_HOST + "/I%20Wish/borrar_meta.php";
    public static final String INSERT = "http://" + IP + PUERTO_HOST + "/I%20Wish/insertar_meta.php";*/

    public static final String URL_REGISTRODATOS_RECLAMOS = "http://" + IP + PUERTO_HOST + "/appReclamos/public/api/categorias";


    // CONSTANTES PARA LA BASE DE DATOS LOCAL
    public static final String TABLE_NAME = "Reclamo";
    public static String _ID="ID";
    public static final String COLUMN_TITULO = "Titulo";
    public static final String COLUMN_DESCRIPCION = "Descripcion";
    public static final String COLUMN_CALLE = "Calle";
    public static final String COLUMN_BARRIO = "Barrio";
    public static final String COLUMN_ZONA = "Zona";
    public static final String COLUMN_LATITUD = "Latitud";
    public static final String COLUMN_LONGITUD = "Longitud";
    public static final String COLUMN_IMAGEN = "Imagen";
    public static final String COLUMN_ESTADO = "Estado";
    public static final String COLUMN_CATEGORIA = "Categoria";




}
