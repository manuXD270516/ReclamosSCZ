package com.ficct.reclamostopicos.reclamosscz.View;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ficct.reclamostopicos.reclamosscz.Model.CategoriaModel;
import com.ficct.reclamostopicos.reclamosscz.R;
import com.ficct.reclamostopicos.reclamosscz.WebServices.Constantes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.ficct.reclamostopicos.reclamosscz.WebServices.*;

public class ViewCategorias extends AppCompatActivity {
    public RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    private CategoriaAdapter adapter;
    private ArrayList<CategoriaModel> listaCategorias;
    private AsyncTaskGetCategorias taskGetCategorias;
    //private CategoriaAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_categorias);
        iniciar();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //iniciar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        taskGetCategorias.setRunTask(false);
        taskGetCategorias.cancel(true);
    }

    /*private void setCategorias() {
        //RequestQueue requestQueue=VolleySingleton.getInstance(this).addToRequestQueue();
        String urlFinal = Constantes.URL_GET_CATEGORIAS_WS;
        JsonObjectRequest objRequest = new JsonObjectRequest(
                Request.Method.GET, urlFinal,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("categorias");
                            for (int i = 0; i < array.length(); i++) {
                                CategoriaModel categoriaAct = new CategoriaModel();
                                categoriaAct.setID(array.getJSONObject(i).getInt("id"));
                                categoriaAct.setNombre(array.getJSONObject(i).getString("nombre"));
                                categoriaAct.setDescripcion(array.getJSONObject(i).getString("descripcion"));
                                categoriaAct.setIdImg(R.drawable.ic_img_categoria); // modificable
                                listaCategorias.add(categoriaAct);
                            }
                            adapter = new CategoriaAdapter(ViewCategorias.this, listaCategorias);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
            }
        });
        VolleySingleton.getInstance(this).addToRequestQueue(objRequest);
        //requestQueue.add(objRequest);
        //queue.add(objRequest);
    }*/

    // metodo de actualizacion constante de los eventos

    public class AsyncTaskGetCategorias extends AsyncTask<Void,Integer,Boolean>{

        private RecyclerView recyclerView;
        private Context context;

        private ArrayList<CategoriaModel> listaCategorias;
        private boolean runTask;

        public AsyncTaskGetCategorias() {
            this(null,null);
        }

        public AsyncTaskGetCategorias(Context context,RecyclerView recyclerView) {
            this.context = context;
            this.recyclerView=recyclerView;
        }


        public boolean isRunTask() {
            return runTask;
        }

        public void setRunTask(boolean runTask) {
            this.runTask = runTask;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        protected Boolean doInBackground(Void... voids) {
            while (true){
                try {
                    Thread.sleep(1000);
                    if (isRunTask()){
                        String urlFinal = Constantes.URL_GET_CATEGORIAS_WS;
                        JsonObjectRequest objRequest = new JsonObjectRequest(
                                Request.Method.GET, urlFinal,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            List<CategoriaModel> bckgrList=new ArrayList<>();
                                            JSONArray array = response.getJSONArray("categorias");
                                            for (int i = 0; i < array.length(); i++) {
                                                CategoriaModel categoriaAct = new CategoriaModel();
                                                categoriaAct.setID(array.getJSONObject(i).getInt("id"));
                                                categoriaAct.setNombre(array.getJSONObject(i).getString("nombre"));
                                                categoriaAct.setDescripcion(array.getJSONObject(i).getString("descripcion"));
                                                categoriaAct.setIdImg(R.drawable.ic_img_categoria); // modificable
                                                bckgrList.add(categoriaAct);
                                            }
                                            if (listaCategorias.size()!=bckgrList.size()){

                                                listaCategorias=new ArrayList<>((bckgrList));
                                                adapter.refreshMyList(bckgrList);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("VALIDACION","ERROR EN LA PETICION DE CATEGORIAS");
                            }
                        });
                        VolleySingleton.getInstance(context).addToRequestQueue(objRequest);
                    } else {
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        @Override
        protected void onPreExecute() {
            this.setRunTask(true);
            this.listaCategorias=new ArrayList<>();
            String urlFinal = Constantes.URL_GET_CATEGORIAS_WS;
            JsonObjectRequest objRequest = new JsonObjectRequest(
                    Request.Method.GET, urlFinal,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray array = response.getJSONArray("categorias");
                                for (int i = 0; i < array.length(); i++) {
                                    CategoriaModel categoriaAct = new CategoriaModel();
                                    categoriaAct.setID(array.getJSONObject(i).getInt("id"));
                                    categoriaAct.setNombre(array.getJSONObject(i).getString("nombre"));
                                    categoriaAct.setDescripcion(array.getJSONObject(i).getString("descripcion"));
                                    categoriaAct.setIdImg(R.drawable.ic_img_categoria); // modificable
                                    listaCategorias.add(categoriaAct);
                                }
                                adapter=new CategoriaAdapter(context,listaCategorias);
                                recyclerView.setAdapter(adapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("VALIDACION","ERROR EN LA PETICION DE CATEGORIAS");
                    //Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                }
            });
            VolleySingleton.getInstance(context).addToRequestQueue(objRequest);

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        iniciar();
        /*recyclerView = (RecyclerView) findViewById(R.id.rvCategorias);
        //layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        taskGetCategorias=new AsyncTaskGetCategorias(getApplicationContext(),recyclerView);
        taskGetCategorias.execute();*/

    }

    private void iniciar() {
        recyclerView = (RecyclerView) findViewById(R.id.rvCategorias);
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        taskGetCategorias=new AsyncTaskGetCategorias(getApplicationContext(),recyclerView);
        taskGetCategorias.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        /*recyclerView = (RecyclerView) findViewById(R.id.rvCategorias);
        //layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        //listaCategorias=new ArrayList<>();
        /*CategoriaModel x= new CategoriaModel();
        x.nombre="SEGURIDAD URBANA (PRUEBA)";
        x.setIdImg(R.drawable.ic_user_login);
        listaCategorias.add(x);*/
        //setCategorias();
    }

    /*private void tocarItem(final String titulo, final String nombre, final String fecha) {
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
                                        editarItem(fecha, titulo, nombre);
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


    /*private void editarItem(final String fecha, final String titulo, final String nombre) {
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
                                        editarVentana(nombre, fecha, "nombre");
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

        public void refreshMyList(List<CategoriaModel> list){
            this.categoriasList.clear();
            this.categoriasList.addAll(list);
            this.notifyDataSetChanged();
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onBindViewHolder(final myViewHolader viewHolder, final int i) {
            //Glide.with(ViewCategorias.this).load(publicacionesFilterList.get(i).getImagen()).into(viewHolder.imagen);
            /*viewHolder.imagen.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    //tocarItem(publicacionesFilterList.get(i).getTitulo(), publicacionesFilterList.get(i).getNombre(), publicacionesFilterList.get(i).getFecha());
                    return false;
                }
            });*/
            viewHolder.descripcion.setText(categoriasList.get(i).getNombre());
            viewHolder.imagen.setImageDrawable(context.getDrawable(R.drawable.ic_user_login));
            viewHolder.imagen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        //Toast.makeText(ViewCategorias.this,String.valueOf(i),Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(context,ViewReclamo.class);
                        Bundle datosSendActivity=new Bundle();
                        datosSendActivity.putString("nombre_categoria", categoriasList.get(i).getNombre());
                        datosSendActivity.putInt("id_categoria", categoriasList.get(i).getID());
                        intent.putExtras(datosSendActivity);
                        context.startActivity(intent);
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
