package com.ficct.reclamostopicos.reclamosscz.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.ficct.reclamostopicos.reclamosscz.Model.ReclamoModel;
import com.ficct.reclamostopicos.reclamosscz.Model.TaskAsynkRegistrarReclamo;
import com.ficct.reclamostopicos.reclamosscz.R;
import com.ficct.reclamostopicos.reclamosscz.WebServices.Constantes;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewIndex extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private ImageView photoImageView;
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView idTextView;
    private SharedPreferences sharedPreferences;
    private GoogleApiClient googleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("PRUEBA DE DATOS ","###################################################"+contador+"######################");
                contador++;
            }
        }).start();*/


        sharedPreferences=getSharedPreferences("credenciales_user",Context.MODE_PRIVATE);
        photoImageView = (ImageView) findViewById(R.id.photoImageView);
        nameTextView = (TextView) findViewById(R.id.nameTextView);
        emailTextView = (TextView) findViewById(R.id.emailTextView);
        idTextView = (TextView) findViewById(R.id.idTextView);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()) {
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            nameTextView.setText(account.getDisplayName());
            emailTextView.setText(account.getEmail());
            idTextView.setText(account.getId());
            Glide.with(this).load(account.getPhotoUrl()).into(photoImageView);
        } else {
            if (sharedPreferences.getString("access","").length()==0){
                goLogInScreen();
            }

        }
    }

    private void goLogInScreen() {
        Intent intent = new Intent(this, ViewLogin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void logOutGoogle(View view) {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    goLogInScreen();
                } else {
                    Toast.makeText(getApplicationContext(), "Error al cerrar sesion!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void logOutFacebook(View view) {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if (isLoggedIn){
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(this, ViewLogin.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    public void irCategorias(View view) {
        TaskAsynkRegistrarReclamo taskAsync=new TaskAsynkRegistrarReclamo(getApplicationContext());
        taskAsync.setTrackReclamos(true);
        taskAsync.execute();

        /*enviarReclamo(new ReclamoModel(
                1,"titulo","desci","b","b","d",
                0.3223,0.039392,"sasda","valido","1"));*/

        Intent intent = new Intent(this, ViewCategorias.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }



    // METODO DE PRUEBA
    /*public void enviarReclamo(final ReclamoModel reclamoModel){
        String urlFinal=Constantes.URL_ADD_RECLAMO_WS;
        Map<String,String> params=new HashMap<>();
        params.put(Constantes.COLUMN_TITULO,reclamoModel.getTitulo());
        params.put(Constantes.COLUMN_DESCRIPCION,reclamoModel.getNombre());
        params.put(Constantes.COLUMN_CALLE,reclamoModel.getCalle());
        params.put(Constantes.COLUMN_BARRIO,reclamoModel.getBarrio());
        params.put(Constantes.COLUMN_ZONA,reclamoModel.getZona());
        params.put(Constantes.COLUMN_LATITUD,String.valueOf(reclamoModel.getLatitud()));
        params.put(Constantes.COLUMN_LONGITUD,String.valueOf(reclamoModel.getLongitud()));
        params.put(Constantes.COLUMN_IMAGEN,reclamoModel.getImagen());
        params.put(Constantes.COLUMN_ESTADO,reclamoModel.getEstado());
        params.put(Constantes.COLUMN_ID_CATEGORIA,reclamoModel.getIDCategoria());
        JSONObject jsonObj=new JSONObject(params);
        JsonObjectRequest peticion=new JsonObjectRequest(Request.Method.POST, urlFinal,jsonObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(ViewIndex.this  ,"rrrrrrrrrrrrrrrr",Toast.LENGTH_SHORT).show();
                            //Log.i("VALIDAR ","############################"+response.getString("resp")+"##############################");
                            if (response.getString("resp").equals("SI")){
                                //updateEstadoReclamo(reclamoModel.getID());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        int x=error.networkResponse.statusCode;
                        Log.i("VALIDAR ","############################ ERROR ##############################");
                    }
                });
        RequestQueue requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(peticion);
        requestQueue.getCache().clear();
    }*/






}
