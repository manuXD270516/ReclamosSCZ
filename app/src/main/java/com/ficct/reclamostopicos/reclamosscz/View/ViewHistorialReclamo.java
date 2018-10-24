package com.ficct.reclamostopicos.reclamosscz.View;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ficct.reclamostopicos.reclamosscz.Model.CategoriaModel;
import com.ficct.reclamostopicos.reclamosscz.Model.ReclamoModel;
import com.ficct.reclamostopicos.reclamosscz.R;

import java.util.ArrayList;

public class ViewHistorialReclamo extends AppCompatActivity {

    public RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    private ReclamosAdapater adapter;
    private ArrayList<ReclamosAdapater> listaReclamos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_historial_reclamo);
        iniciar();
    }

    private void iniciar() {
        recyclerView = (RecyclerView) findViewById(R.id.rvCategorias);
        //layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        listaReclamos=new ArrayList<>();
        /*CategoriaModel x= new CategoriaModel();
        x.nombre="SEGURIDAD URBANA (PRUEBA)";
        x.setIdImg(R.drawable.ic_user_login);
        listaCategorias.add(x);*/
        setReclamos();
    }

    private void setReclamos() {


    }


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
    public class ReclamosAdapater extends RecyclerView.Adapter<ViewCategorias.myViewHolader>  {
        private ArrayList<ReclamoModel> reclamosList;
        //private ArrayList<CategoriaModel> publicacionesFilterList;
        private Context context;

        public ReclamosAdapater(Context context, ArrayList<ReclamoModel> arrayList) {
            reclamosList = new ArrayList<>(arrayList);
            this.context = context;
        }


        @Override
        public ViewCategorias.myViewHolader onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.categorias_card, viewGroup, false);
            ViewCategorias.myViewHolader vh = new ViewCategorias.myViewHolader(view);
            return vh;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onBindViewHolder(final ViewCategorias.myViewHolader viewHolder, final int i) {
            //Glide.with(ViewCategorias.this).load(publicacionesFilterList.get(i).getImagen()).into(viewHolder.imagen);
            /*viewHolder.imagen.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    //tocarItem(publicacionesFilterList.get(i).getTitulo(), publicacionesFilterList.get(i).getNombre(), publicacionesFilterList.get(i).getFecha());
                    return false;
                }
            });*/
            viewHolder.descripcion.setText(reclamosList.get(i).getTitulo());
            viewHolder.imagen.setImageDrawable(getDrawable(R.drawable.ic_img_categoria));
            viewHolder.imagen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(ViewCategorias.this,String.valueOf(i),Toast.LENGTH_SHORT).show();

                }
            });
            //progressDialog.dismiss();
        }



        @Override
        public int getItemCount() {
            return reclamosList.isEmpty()?0: reclamosList.size();
        }


    }



}
