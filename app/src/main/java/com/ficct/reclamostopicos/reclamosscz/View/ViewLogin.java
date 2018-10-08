package com.ficct.reclamostopicos.reclamosscz.View;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.ficct.reclamostopicos.reclamosscz.Interfaces.IPresenterViewLogin;
import com.ficct.reclamostopicos.reclamosscz.Interfaces.IViewPresenterLogin;
import com.ficct.reclamostopicos.reclamosscz.Presenter.LoginPresenter;
import com.ficct.reclamostopicos.reclamosscz.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.auth.api.signin.SignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.ficct.reclamostopicos.reclamosscz.WebServices.*;

public class ViewLogin extends AppCompatActivity implements IViewPresenterLogin, GoogleApiClient.OnConnectionFailedListener {

    private EditText tvUser, tvPass;
    private IPresenterViewLogin loginPresenter;

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private GoogleApiClient googleApiClient;
    private SignInButton signInButton;

    private static final int CODE_SIGN_GOOGLE = 777;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_login);
        iniciarInstancias();

    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            goIndex();
        } else {
            Toast.makeText(this, "Logueo invalido, intenter nuevamente porfavor", Toast.LENGTH_SHORT).show();
        }
    }

    private void goIndex() {
        Intent intent = new Intent(this, ViewIndex.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void iniciarInstancias() {
        tvUser = (EditText) findViewById(R.id.tvEmail_Login);
        tvPass = (EditText) findViewById(R.id.tvPassword_Login);
        loginButton = (LoginButton) findViewById(R.id.SignLogin_button_Login);

        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("user_friends");
        loginButton.setReadPermissions("public_profile");
        loginButton.setReadPermissions("email");
        loginButton.setReadPermissions("user_birthday");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                goIndex();
            }

            @Override
            public void onCancel() {
                Toast.makeText(ViewLogin.this ,"Error de Logueo con Facebook, porfavor intente nuevamente",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(ViewLogin.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        loginPresenter = new LoginPresenter();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();


        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, CODE_SIGN_GOOGLE);
            }
        });
    }

    private void loginGoogle() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String urlFinal= Constantes.URL_REGISTRODATOS_RECLAMOS;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlFinal, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "El usuario no se puedo regustrar en la base de datos", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
    }

    private void loginFacebook() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String urlFinal="";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlFinal, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "El usuario no se puedo regustrar en la base de datos", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void navigateActivityIndex() {
        startActivity(new Intent(ViewLogin.this,ViewIndex.class));
    }

    // verificar si el metodo pertenece a esta seccion
    @Override
    public boolean isAuthenticatedUser() {
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODE_SIGN_GOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

}
