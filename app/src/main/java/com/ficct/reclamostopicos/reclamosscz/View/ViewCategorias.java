package com.ficct.reclamostopicos.reclamosscz.View;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ficct.reclamostopicos.reclamosscz.Model.CategoriaModel;
import com.ficct.reclamostopicos.reclamosscz.R;
import com.ficct.reclamostopicos.reclamosscz.WebServices.Constantes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewCategorias extends AppCompatActivity {

    public RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    private CategoriaAdapter adapter;
    private ArrayList<CategoriaModel> listaCategorias;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_categorias);
        iniciar();
    }


    private void setCategorias() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String urlFinal= Constantes.URL_GET_CATEGORIAS;
        JsonObjectRequest objRequest=new JsonObjectRequest(
                Request.Method.GET, urlFinal,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array=response.getJSONArray("categorias");
                            for (int i=0;i<array.length();i++){
                                CategoriaModel x= new CategoriaModel();
                                x.setDescripcion(array.getJSONObject(i).getString("nombre"));
                                x.setIdImg(R.drawable.ic_img_categoria);
                                listaCategorias.add(x);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
            },new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "El usuario no se puedo regustrar en la base de datos", Toast.LENGTH_LONG).show();
                    }
        });
        queue.add(objRequest);
    }


    private void iniciar() {
        recyclerView = (RecyclerView) findViewById(R.id.rvCategorias);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        listaCategorias=new ArrayList<>();

        CategoriaModel x= new CategoriaModel();
        x.descripcion="SEGURIDAD URBANA (PRUEBA)";
        x.setIdImg(R.drawable.ic_user_login);
        listaCategorias.add(x);
        //setCategorias();

        adapter = new CategoriaAdapter(this, listaCategorias);
        recyclerView.setAdapter(adapter);



    }

    /*private void tocarItem(final String titulo, final String descripcion, final String fecha) {
        final ArrayList<String> listItems = new ArrayList<>();
        listItems.add("Editar Datos");
        listItems.add("Eliminar");
        new AlertDialog.Builder(Informacion.this)
                .setTitle("Elija una Opción:")
                .setCancelable(false)
                .setAdapter(new ArrayAdapter<String>(Informacion.this, android.R.layout.simple_list_item_1, listItems),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                switch (item) {
                                    case 0:
                                        editarItem(fecha, titulo, descripcion);
                                        break;
                                    case 1:
                                        eliminarItem(fecha, titulo);
                                        break;
                                }
                            }
                        }).setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        }).show();
    }*/


    /*private void editarItem(final String fecha, final String titulo, final String descripcion) {
        final ArrayList<String> listItems = new ArrayList<>();
        listItems.add("Editar Titulo");
        listItems.add("Editar Descripcion");
        // listItems.add("Editar Imagen");
        new AlertDialog.Builder(Informacion.this)
                .setTitle("Elija una Opción:")
                .setCancelable(false)
                .setAdapter(new ArrayAdapter<String>(Informacion.this, android.R.layout.simple_list_item_1, listItems),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                switch (item) {
                                    case 0:
                                        editarVentana(titulo, fecha, "titulo");
                                        break;
                                    case 1:
                                        editarVentana(descripcion, fecha, "descripcion");
                                        break;
                                    case 2:
                                        //  elegirEstado(fecha);
                                        break;
                                }
                            }
                        }).setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        }).show();
    }*/

    public static class myViewHolader extends RecyclerView.ViewHolder {

        TextView descripcion;
        ImageView imagen;
        LinearLayout linearLayout;
        public Context mContext;

        public myViewHolader(View itemView) {
            super(itemView);
            descripcion = (TextView) itemView.findViewById(R.id.cardICategoriaDescripcion);
            imagen = (ImageView) itemView.findViewById(R.id.cardCategoriaImagen);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.cardInformacionLinerLayout);

           /* itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext = v.getContext();
                    Intent intent = new Intent(v.getContext(), ImagenesFolder.class);
                    intent.putExtra("folder", fecha.getText().toString().trim());
                    mContext.startActivity(intent);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return false;
                }
            });*/
        }
    }


    // CLASE ADAPTER PARA LAS PUBLICACIONES
    public class CategoriaAdapter extends RecyclerView.Adapter<myViewHolader>  {
        private ArrayList<CategoriaModel> categoriasList;
        //private ArrayList<CategoriaModel> publicacionesFilterList;
        private Context context;

        public CategoriaAdapter(Context context, ArrayList<CategoriaModel> arrayList) {
            categoriasList = new ArrayList<>(arrayList);
            this.context = context;
        }


        @Override
        public myViewHolader onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.categorias_card, viewGroup, false);
            myViewHolader vh = new myViewHolader(view);
            return vh;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onBindViewHolder(final myViewHolader viewHolder, final int i) {
            //Glide.with(ViewCategorias.this).load(publicacionesFilterList.get(i).getImagen()).into(viewHolder.imagen);
            /*viewHolder.imagen.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    //tocarItem(publicacionesFilterList.get(i).getTitulo(), publicacionesFilterList.get(i).getDescripcion(), publicacionesFilterList.get(i).getFecha());
                    return false;
                }
            });*/
            viewHolder.descripcion.setText(categoriasList.get(i).getDescripcion());
            viewHolder.imagen.setImageDrawable(getDrawable(R.drawable.ic_user_login));
            viewHolder.imagen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        //Toast.makeText(ViewCategorias.this,String.valueOf(i),Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(ViewCategorias.this,ViewReclamo.class);
                        intent.putExtra("categoria", listaCategorias.get(i).getDescripcion());
                        startActivity(intent);
                }
            });
            //progressDialog.dismiss();
        }



        @Override
        public int getItemCount() {
            return categoriasList.isEmpty()?0:categoriasList.size();
        }


        }


}
