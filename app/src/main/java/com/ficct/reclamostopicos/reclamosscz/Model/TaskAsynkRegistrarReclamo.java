package com.ficct.reclamostopicos.reclamosscz.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ficct.reclamostopicos.reclamosscz.Database.DatabaseReclamos;
import com.ficct.reclamostopicos.reclamosscz.WebServices.Constantes;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// encargado de realizar las peticiones constantes
public class TaskAsynkRegistrarReclamo extends AsyncTask<Void, ReclamoModel, Boolean> {


    int contador;
    private boolean trackReclamos;
    private Context context;
    List<ReclamoModel> reclamosList;

    public TaskAsynkRegistrarReclamo() {
        this(null);
    }

    public TaskAsynkRegistrarReclamo(Context context) {
        this.context = context;
        this.trackReclamos=false;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean isTrackReclamos() {
        return trackReclamos;
    }

    public void setTrackReclamos(boolean trackReclamos) {
        this.trackReclamos = trackReclamos;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void selectReclamosNoEnviados() {
        //Se inicializa la clase.
        DatabaseReclamos dbReclamos = new DatabaseReclamos(context);
        //Se establecen permisos de lectura
        SQLiteDatabase sqlite = dbReclamos.getReadableDatabase();
        //Columnas que devolverá la consulta.
        String[] columnas = {
                Constantes.COLUMN_ID,
                Constantes.COLUMN_TITULO,
                Constantes.COLUMN_DESCRIPCION,
                Constantes.COLUMN_CALLE,
                Constantes.COLUMN_BARRIO,
                Constantes.COLUMN_ZONA,
                Constantes.COLUMN_LATITUD,
                Constantes.COLUMN_LONGITUD,
                Constantes.COLUMN_IMAGEN,
                Constantes.COLUMN_ESTADO,
                Constantes.COLUMN_CATEGORIA
        };

        //Ejecuta la sentencia devolviendo los resultados de los parámetros pasados de tabla, columnas, producto y orden de los resultados.
        Cursor cursor = sqlite.query(Constantes.TABLE_RECLAMOS, columnas, Constantes.COLUMN_ESTADO + "='NO ENVIADO'" , null, null, null, null);
        while (cursor.moveToNext()) {
            long ID = cursor.getLong(cursor.getColumnIndex(Constantes.COLUMN_ID));
            String titulo = cursor.getString(cursor.getColumnIndex(Constantes.COLUMN_TITULO));
            String descripcion = cursor.getString(cursor.getColumnIndex(Constantes.COLUMN_DESCRIPCION));
            String calle = cursor.getString(cursor.getColumnIndex(Constantes.COLUMN_CALLE));
            String barrio = cursor.getString(cursor.getColumnIndex(Constantes.COLUMN_BARRIO));
            String zona = cursor.getString(cursor.getColumnIndex(Constantes.COLUMN_ZONA));
            double latitud = cursor.getDouble(cursor.getColumnIndex(Constantes.COLUMN_LATITUD));
            double longitud = cursor.getDouble(cursor.getColumnIndex(Constantes.COLUMN_LONGITUD));
            String imagen = cursor.getString(cursor.getColumnIndex(Constantes.COLUMN_IMAGEN));
            String estado = cursor.getString(cursor.getColumnIndex(Constantes.COLUMN_ESTADO));
            String categoria = cursor.getString(cursor.getColumnIndex(Constantes.COLUMN_CATEGORIA));
            ReclamoModel modelReclamo=new ReclamoModel(ID,titulo,descripcion,calle,barrio,zona,latitud,longitud,imagen,estado,categoria);
            reclamosList.add(modelReclamo);
        }
    }




    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected Boolean doInBackground(Void... voids) {

        while (true) {
            if (isTrackReclamos()){
                // realizar las consultas locales para obtener
                selectReclamosNoEnviados();
                int i=0;
                while (i<reclamosList.size()){
                    if (existsConnectionInternet()){
                        publishProgress(reclamosList.get(i));
                        reclamosList.remove(i);
                    } else {
                        i++;
                    }
                }
                /*Iterator<ReclamoModel> iterator=reclamosList.iterator();
                while (iterator.hasNext()){
                    publishProgress(iterator.next());
                }*/
                // realizar las peticiones de datos pendientes para su envio
                //publishProgress("existe conexion");

            } else {
                break;
            }

        }

            /*for (;;){
                try {
                    Thread.sleep(1000);
                    publishProgress(contador*10);
                    if (isCancelled()){
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }*/
        return true;
    }

    @Override
    protected void onProgressUpdate(ReclamoModel... values) {
        if (values[0]!=null){
            enviarReclamo(values[0]);
        }
    }

    @Override
    protected void onPreExecute() {
        contador = 0;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            Log.i("FINALIZACION", "SE TERMINO DE EJECUTAR LA PRUEBA");
        }
    }

            /*@Override
        protected void onProgressUpdate(... values) {
            int cant=values[0].intValue();
            Log.i("VALOR PARA IMPRIMIR","-----------------------"+cant+"--------------");
            contador+=1;
        }*/

    @Override
    protected void onCancelled(Boolean aBoolean) {
        super.onCancelled(aBoolean);
    }

    @Override
    protected void onCancelled() {
        Log.i("CANCELACION", "Tarea fue cancelada");
    }

    private boolean existsConnectionInternet() {
        if (context != null) {
            ConnectivityManager cm;
            NetworkInfo ni;
            cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            ni = cm.getActiveNetworkInfo();
            if (ni != null) {
                ConnectivityManager connManager1 = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netWifi = connManager1.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                ConnectivityManager connManager2 = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netMobile = connManager2.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                boolean conexionWifi = netWifi.isConnected() ? true : false;
                boolean conexionMobile = netMobile.isConnected() ? true : false;
                return conexionWifi || conexionMobile;
            }
        }
        return false;
    }


    public void enviarReclamo(final ReclamoModel reclamoModel){

        String urlFinal=Constantes.URL_ADD_RECLAMO;
        JsonObjectRequest peticion=new JsonObjectRequest(Request.Method.POST, urlFinal,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i("VALIDAR ","############################"+response.getString("resp")+"##############################");
                            if (response.getString("resp").equals("SI")){
                                updateEstadoReclamo(reclamoModel.getID());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put(Constantes.COLUMN_TITULO,reclamoModel.getTitulo());
                params.put(Constantes.COLUMN_DESCRIPCION,reclamoModel.getDescripcion());
                params.put(Constantes.COLUMN_CALLE,reclamoModel.getCalle());
                params.put(Constantes.COLUMN_BARRIO,reclamoModel.getBarrio());
                params.put(Constantes.COLUMN_ZONA,reclamoModel.getZona());
                params.put(Constantes.COLUMN_LATITUD,String.valueOf(reclamoModel.getLatitud()));
                params.put(Constantes.COLUMN_LONGITUD,String.valueOf(reclamoModel.getLongitud()));
                params.put(Constantes.COLUMN_IMAGEN,reclamoModel.getImagen());
                params.put(Constantes.COLUMN_ESTADO,reclamoModel.getEstado());
                params.put(Constantes.COLUMN_CATEGORIA,reclamoModel.getCategoria());
                return params;

            }
        };
        RequestQueue requestQueue=Volley.newRequestQueue(context);
        requestQueue.add(peticion);
    }

    private  void updateEstadoReclamo(long idReclamoLocal){
        //Se inicializa la clase.
        DatabaseReclamos dbReclamosInstance = new DatabaseReclamos(context);

        //Se establecen permisos de escritura
        SQLiteDatabase sqlite = dbReclamosInstance.getWritableDatabase();


        ContentValues content = new ContentValues();
        //Se añaden los valores introducidos de cada campo mediante clave(columna)/valor(valor introducido en el campo de texto)
        content.put(Constantes.COLUMN_ESTADO, "ENVIADO");
        //Se establece la condición del _id del producto a modificar
        String selection = Constantes.COLUMN_ID + " = " + idReclamoLocal;

        //Se llama al método update pasándole los parámetros para modificar el producto con el identificado como condición de busqueda
        int count = sqlite.update(Constantes.TABLE_RECLAMOS, content,selection, null);
        //Se cierra la conexión abierta a la Base de Datos
        sqlite.close();
    }

}
