package com.ficct.reclamostopicos.reclamosscz.Database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.ficct.reclamostopicos.reclamosscz.WebServices.Constantes;


public class DatabaseReclamos extends SQLiteOpenHelper{


	private static final String TEXT_TYPE = " TEXT";
	private static final String INTEGER_TYPE = " INTEGER";
	private static final String REAL_TYPE = " REAL";
	private static final String BLOB_TYPE = " BLOB";

	private static final String COMMA_SEP = ",";
	private static final String SQL_CREATE_ENTRIES =
	    "CREATE TABLE " + Constantes.TABLE_NAME + " (" +
				Constantes._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				Constantes.COLUMN_TITULO + TEXT_TYPE + COMMA_SEP +
				Constantes.COLUMN_DESCRIPCION + TEXT_TYPE + COMMA_SEP +
				Constantes.COLUMN_CALLE+ TEXT_TYPE + COMMA_SEP +
				Constantes.COLUMN_BARRIO + TEXT_TYPE + COMMA_SEP +
				Constantes.COLUMN_ZONA + TEXT_TYPE + COMMA_SEP +
				Constantes.COLUMN_LATITUD + REAL_TYPE + COMMA_SEP +
				Constantes.COLUMN_LONGITUD + REAL_TYPE + COMMA_SEP +
				Constantes.COLUMN_IMAGEN + BLOB_TYPE + COMMA_SEP +
				Constantes.COLUMN_CATEGORIA + TEXT_TYPE + COMMA_SEP +
				Constantes.COLUMN_ESTADO + TEXT_TYPE + " )";

	private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + Constantes.TABLE_NAME;
	public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ReclamosDBLocal.sqlite";

	
	public DatabaseReclamos(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	//M�todo para crear la Tabla que recibe la consulta Transact-SQL
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_ENTRIES);
	}

	//M�todo que elimina la tabla y vuelve a llamar al m�todo que la crea
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_ENTRIES);
		onCreate(db);
	}

}
