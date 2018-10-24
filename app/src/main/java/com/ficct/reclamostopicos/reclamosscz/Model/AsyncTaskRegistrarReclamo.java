package com.ficct.reclamostopicos.reclamosscz.Model;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ficct.reclamostopicos.reclamosscz.Database.DatabaseReclamos;
import com.ficct.reclamostopicos.reclamosscz.WebServices.Constantes;
import com.ficct.reclamostopicos.reclamosscz.WebServices.VolleySingleton;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

// encargado de realizar las peticiones constantes
public class AsyncTaskRegistrarReclamo extends AsyncTask<Void, ReclamoModel, Boolean> {

    private boolean trackReclamos;
    private Context context;
    LinkedList<ReclamoModel> reclamosList;
    DatabaseReclamos dbReclamos;

    public AsyncTaskRegistrarReclamo() {
        this(null);
    }

    public AsyncTaskRegistrarReclamo(Context context) {
        this.context = context;
        this.trackReclamos=false;
        this.reclamosList=new LinkedList<>();
        dbReclamos= new DatabaseReclamos(context);
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
                Constantes.COLUMN_ID_CATEGORIA
        };
        //Ejecuta la sentencia devolviendo los resultados de los parámetros pasados de tabla, columnas, producto y orden de los resultados.
        Cursor cursor = sqlite.query(Constantes.TABLE_RECLAMO, columnas, Constantes.COLUMN_ESTADO + "=?" , new String[]{"no enviado"}, null, null, null);
        if (cursor!=null){
            if (cursor.moveToFirst()){
                do {
                    long ID = cursor.getLong(cursor.getColumnIndex(Constantes.COLUMN_ID));
                    String titulo = cursor.getString(cursor.getColumnIndex(Constantes.COLUMN_TITULO));
                    String descripcion = cursor.getString(cursor.getColumnIndex(Constantes.COLUMN_DESCRIPCION));
                    String calle = cursor.getString(cursor.getColumnIndex(Constantes.COLUMN_CALLE));
                    String barrio = cursor.getString(cursor.getColumnIndex(Constantes.COLUMN_BARRIO));
                    String zona = cursor.getString(cursor.getColumnIndex(Constantes.COLUMN_ZONA));
                    double latitud = cursor.getDouble(cursor.getColumnIndex(Constantes.COLUMN_LATITUD));
                    double longitud = cursor.getDouble(cursor.getColumnIndex(Constantes.COLUMN_LONGITUD));
                    byte[] imagen = null;
                    if (cursor.getBlob(cursor.getColumnIndex(Constantes.COLUMN_IMAGEN))!=null){
                        imagen=cursor.getBlob(cursor.getColumnIndex(Constantes.COLUMN_IMAGEN));
                    }
                    String estado = cursor.getString(cursor.getColumnIndex(Constantes.COLUMN_ESTADO));
                    int idCategoria = cursor.getInt(cursor.getColumnIndex(Constantes.COLUMN_ID_CATEGORIA));
                    ReclamoModel modelReclamo=new ReclamoModel(ID,titulo,descripcion,calle,barrio,zona,latitud,longitud,imagen,estado, idCategoria);
                    reclamosList.add(modelReclamo);
                } while (cursor.moveToNext());
                cursor.close();
            }
        }
        sqlite.close();
    }




    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected Boolean doInBackground(Void... voids) {
        while (true) {
            if (isTrackReclamos()){
                // realizar las consultas locales para obtener
                try {
                    Thread.sleep(1000);
                    selectReclamosNoEnviados();
                    while (!reclamosList.isEmpty()){
                        ReclamoModel reclamoAct=reclamosList.removeFirst();
                        if (existsConnectionInternet()){
                            enviarReclamo(reclamoAct);
                            //publishProgress(reclamoAct);
                        } else {
                            reclamosList.addFirst(reclamoAct);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                break;
            }
        }
        return true;
    }

    @Override
    protected void onProgressUpdate(ReclamoModel... values) {
        if (values[0]!=null){
            //enviarReclamo(values[0]);
        }
    }

    @Override
    protected void onPreExecute() {
        if (!isTrackReclamos()){
            onCancelled();
        }
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


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void enviarReclamo(final ReclamoModel reclamoModel){
        final String urlFinal=Constantes.URL_ADD_RECLAMO_WS;
        Map<String,String> params=new HashMap<>();
        //params.put(Constantes.COLUMN_ID,String.valueOf(reclamoModel.getID()));
        params.put(Constantes.COLUMN_TITULO.toLowerCase(),reclamoModel.getTitulo());
        params.put(Constantes.COLUMN_DESCRIPCION.toLowerCase(),reclamoModel.getDescripcion());
        params.put(Constantes.COLUMN_CALLE.toLowerCase(),reclamoModel.getCalle());
        params.put(Constantes.COLUMN_BARRIO.toLowerCase(),reclamoModel.getBarrio());
        params.put(Constantes.COLUMN_ZONA.toLowerCase(),reclamoModel.getZona());
        params.put(Constantes.COLUMN_LATITUD.toLowerCase(),String.valueOf(reclamoModel.getLatitud()));
        params.put(Constantes.COLUMN_LONGITUD.toLowerCase(),String.valueOf(reclamoModel.getLongitud()));
        params.put(Constantes.COLUMN_ESTADO.toLowerCase(),reclamoModel.getEstado());
        params.put(Constantes.COLUMN_IMAGEN.toLowerCase(),new String(reclamoModel.getImagen(),StandardCharsets.UTF_8));
        params.put(Constantes.COLUMN_ID_CATEGORIA.toLowerCase(), String.valueOf(reclamoModel.getIDCategoria()));
        JSONObject jsonObj=new JSONObject(params);
        JsonObjectRequest requestAct =new JsonObjectRequest(Request.Method.POST, urlFinal,jsonObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("resp").equals("SI")){
                                Log.i("DATO ENVIADO","%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                                // enviar al servidor de firebase
                                enviarImgServidor(reclamoModel.getImagen());
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
                });
        VolleySingleton.getInstance(context).addToRequestQueue(requestAct);
        /*RequestQueue requestQueue=Volley.newRequestQueue(context);
        requestQueue.add(requestAct);*/
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void enviarImgServidor(byte[] imgBytes) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference("IMAGENES_RECLAMOS");
        final StorageReference refImgReclamos = storageReference.child(String.valueOf(System.currentTimeMillis()));
        Uri imgUriSend=Uri.parse(new String(imgBytes,StandardCharsets.UTF_8));
        UploadTask uploadTask = refImgReclamos.putFile(imgUriSend);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                return refImgReclamos.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    try {
                        Uri downloadUri = task.getResult();
                        /*content.put(Constantes.COLUMN_IMAGEN, downloadUri.toString());
                        SQLiteDatabase sqlite = dbReclamos.getWritableDatabase();
                        sqlite.insert(Constantes.TABLE_RECLAMO, null, content);
                        sqlite.close();*/
                    } catch (Exception e) {
                        String error = e.getMessage();
                    }
                }
            }
        });

    }

    private  void updateEstadoReclamo(long idReclamoLocal){
        //Se inicializa la clase.
        //Se establecen permisos de escritura

        SQLiteDatabase sqlite = dbReclamos.getWritableDatabase();
        ContentValues content = new ContentValues();
        //Se añaden los valores introducidos de cada campo mediante clave(columna)/valor(valor introducido en el campo de texto)
        content.put(Constantes.COLUMN_ESTADO, "enviado");
        //Se establece la condición del _id del a modificar
        String selection = Constantes.COLUMN_ID + "=" + idReclamoLocal;

        int count = sqlite.update(Constantes.TABLE_RECLAMO, content,selection, null);
        //Se cierra la conexión abierta a la Base de Datos
        sqlite.close();
    }

}
