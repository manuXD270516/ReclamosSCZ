package com.ficct.reclamostopicos.reclamosscz.View;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ficct.reclamostopicos.reclamosscz.Database.DatabaseReclamos;
import com.ficct.reclamostopicos.reclamosscz.R;
import com.ficct.reclamostopicos.reclamosscz.WebServices.Constantes;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ViewReclamo extends AppCompatActivity {

    private TextView tvCategoria;
    private EditText etBarrio, etZona, etCalle, etTitulo, etDescripcion;
    private Switch swAnonimo;
    private DatabaseReclamos dbReclamos;
    private ViewPager vpImgReclamos;
    LocationManager locationManager;
    private ImagePagerAdapter adaptadorImgSlide;
    private static final int PICK_IMAGE = 100;
    private Uri[] imgsUri;
    private StorageReference storageReference;
    private FirebaseStorage storage;
    private double latitud, longitud;
    private int idCategoriaSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reclamo);

        iniciarInstancias();
    }

    private void iniciarInstancias() {
        tvCategoria = (TextView) findViewById(R.id.tvCategoria_ReclamosView);
        /*etBarrio = (EditText) findViewById(R.id.etBarrio_ReclamosView);
        etCalle = (EditText) findViewById(R.id.etCalle_ReclamosView);
        etZona = (EditText) findViewById(R.id.etZona_ReclamosView);*/
        etTitulo = (EditText) findViewById(R.id.etTitulo_ReclamosView);
        etDescripcion = (EditText) findViewById(R.id.etDescripcion_ReclamosView);

        swAnonimo = (Switch) findViewById(R.id.swAnonimo_ReclamosView);
        vpImgReclamos = (ViewPager) findViewById(R.id.vpImagenes_ViewReclamos);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        dbReclamos = new DatabaseReclamos(this);
        Bundle datosRecibidos=getIntent()!=null?getIntent().getExtras():null;
        if (datosRecibidos!= null) {
            tvCategoria.setText(datosRecibidos.getString("nombre_categoria").toUpperCase());
            idCategoriaSelect = datosRecibidos.getInt("id_categoria");
        }
        storage = FirebaseStorage.getInstance();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void guardarReclamoLocal(final View view) throws IOException {
        //Se inicializa la clase.
        //final SQLiteDatabase sqlite = dbReclamos.getWritableDatabase();
        String titulo = etTitulo.getText().toString();
        String descripcion = etDescripcion.getText().toString();
        /*String calle = etCalle.getText().toString();
        String zona = etZona.getText().toString();
        String barrio = etBarrio.getText().toString();*/
        int idCategoria = idCategoriaSelect;

        //storageReference = storage.getReference("IMAGENES_RECLAMOS");
        //final StorageReference refImgReclamos = storageReference.child(String.valueOf(System.currentTimeMillis()));
        /*byte[] inputData=null;
        if (imgsUri.length>0){
            InputStream iStream =   getContentResolver().openInputStream(imgsUri[0]);
            inputData= getBytes(iStream);
        }*/

        if (titulo.equals("") || descripcion.equals("") || imgsUri.length == 0) {
            Toast.makeText(this, "Revise los datos introducidos. Todos los campos son obligatorios.", Toast.LENGTH_SHORT).show();
        } else {
            final ContentValues content = new ContentValues();
            //Se añaden los valores introducidos de cada campo mediante clave(columna)/valor(valor introducido en el campo de texto)
            content.put(Constantes.COLUMN_TITULO, titulo);
            content.put(Constantes.COLUMN_DESCRIPCION, descripcion);
            content.put(Constantes.COLUMN_CALLE, "calle prueba");
            content.put(Constantes.COLUMN_BARRIO, "barrio prueba");
            content.put(Constantes.COLUMN_ZONA, "zona prueba");
            content.put(Constantes.COLUMN_LATITUD, latitud);
            content.put(Constantes.COLUMN_LONGITUD, longitud);
            content.put(Constantes.COLUMN_ESTADO, "no enviado");
            content.put(Constantes.COLUMN_ID_CATEGORIA, String.valueOf(idCategoria));
            byte[] dataImg=getBytes(getContentResolver().openInputStream(imgsUri[0]));
            content.put(Constantes.COLUMN_IMAGEN,dataImg);
            SQLiteDatabase sqlite = dbReclamos.getWritableDatabase();
            sqlite.insert(Constantes.TABLE_RECLAMO, null, content);
            sqlite.close();
            /*UploadTask uploadTask = refImgReclamos.putFile(imgsUri[0]);
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
                            content.put(Constantes.COLUMN_IMAGEN, downloadUri.toString());
                            SQLiteDatabase sqlite = dbReclamos.getWritableDatabase();
                            sqlite.insert(Constantes.TABLE_RECLAMO, null, content);
                            sqlite.close();
                        } catch (Exception e) {
                            String error = e.getMessage();
                        }
                    }
                }
            });*/
            Snackbar.make(view, "Reclamo guardado correctamente!!!", Snackbar.LENGTH_LONG).show();
            etTitulo.setText("");
            etDescripcion.setText("");
            /*etBarrio.setText("");
            etCalle.setText("");
            etZona.setText("");*/
            //selectAllReclamo(view);
        }
    }

    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Su ubicación esta desactivada.\npor favor active su ubicación " +
                        "usa esta app")
                .setPositiveButton("Configuración de ubicación", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    private final LocationListener locationListenerGPS = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitud = location.getLongitude();
            latitud = location.getLatitude();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    /*longitud.setText(longitudeGPS + "");
                    latitud.setText(latitudeGPS + "");
                    Toast.makeText(MainActivity.this, "GPS Provider update", Toast.LENGTH_SHORT).show();*/
                }
            });
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void selectImgReclamos(View v) {
        Intent gallery = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        gallery.setType("image/*");
        gallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            //imguri = data.getData();
            //isPublicacionWithImg = true;
            try {
                vpImgReclamos.setVisibility(View.VISIBLE);
                if (data == null) {
                    Toast.makeText(this, "DEBE SELECCIOAR UNA O MAS IMAGENES ... PRESIONAR 2 SEGUNDOS SOBRE LA(S) IMAGENES QUE SELECCIONE", Toast.LENGTH_LONG).show();
                    return;
                }
                if (data.getClipData() != null) {
                    imgsUri = new Uri[data.getClipData().getItemCount()];
                    ClipData clipData = data.getClipData();
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        Uri uriImagenActual = clipData.getItemAt(i).getUri();
                        imgsUri[i] = uriImagenActual;

                    }
                    adaptadorImgSlide = new ImagePagerAdapter(imgsUri);
                    vpImgReclamos.setAdapter(adaptadorImgSlide);
                    return;
                }
                if (data.getData() != null) {
                    imgsUri = new Uri[1];
                    imgsUri[0] = data.getData();
                    adaptadorImgSlide = new ImagePagerAdapter(imgsUri);
                    vpImgReclamos.setAdapter(adaptadorImgSlide);
                    return;
                }
            } catch (Exception e) {
                String error = e.getMessage();
                System.out.println(error);
            }
        }
    }

    // CLASE PARA EL VIEW PAGER DE IMAGENES
    public class ImagePagerAdapter extends PagerAdapter {

        //private Bitmap[] mImages;
        private Uri[] urlImages;

        public ImagePagerAdapter(Uri[] urlImages) {
            //this.mImages=imagenes;
            this.urlImages = urlImages;
        }

        @Override
        public int getCount() {
            return urlImages.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            //return false;
            //return view == ((ImageView) object);
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = LayoutInflater.from(ViewReclamo.this);
            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.slide_images, container, false);
            container.addView(layout);
            ImageView image = (ImageView) layout.findViewById(R.id.imageSlide);
            image.setImageURI(urlImages[position]);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return layout;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
            //super.destroyItem(container, position, object);
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void selectAllReclamo(View view) {
        //Se inicializa la clase.
        dbReclamos = new DatabaseReclamos(this);
        //Se establecen permisos de lectura
        SQLiteDatabase sqlite = dbReclamos.getReadableDatabase();
        //Columnas que devolverá la consulta.
        String[] columnas = {
                Constantes.COLUMN_ID,
                Constantes.COLUMN_TITULO,
                Constantes.COLUMN_DESCRIPCION,
                Constantes.COLUMN_LATITUD,
                Constantes.COLUMN_LONGITUD,
                Constantes.COLUMN_IMAGEN
        };

        //Ejecuta la sentencia devolviendo los resultados de los parámetros pasados de tabla, columnas, producto y orden de los resultados.
        Cursor cursor = sqlite.query(Constantes.TABLE_RECLAMO, columnas, null, null, null, null, null);

        if (cursor.getCount() != 0) {
            cursor.moveToLast();
            String tituloReg = cursor.getString(cursor.getColumnIndex(Constantes.COLUMN_TITULO));
            long identificador = cursor.getLong(cursor.getColumnIndex(Constantes.COLUMN_ID));
            double lati, longi;
            String imgRuta = cursor.getString(cursor.getColumnIndex(Constantes.COLUMN_IMAGEN));

            lati = cursor.getLong(cursor.getColumnIndex(Constantes.COLUMN_LATITUD));
            longi = cursor.getLong(cursor.getColumnIndex(Constantes.COLUMN_LONGITUD));

            TextView x = (TextView) findViewById(R.id.tvPrueba);
            x.setText("ID : " + String.valueOf(identificador) + "\nTITULO : " + tituloReg + "\nRUTA >" + imgRuta);//+"\nLATITUD :"+String.valueOf(lati)+" LONGITUD :"+String.valueOf(longi));
        }
        //Se cierra la conexión abierta a la Base de Datos
        sqlite.close();

    }




}
