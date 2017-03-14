package com.app.dorsh.imovies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String cmd = "CREATE TABLE "+Constants.DB_TABLE_NAME+" ("+
                Constants.DB_CLM_NAME_id+" INTEGER PRIMARY KEY, " +
                Constants.DB_CLM_NAME_Title + " TEXT, " +
                Constants.DB_CLM_NAME_Year + " TEXT, " +
                Constants.DB_CLM_NAME_Rated + " TEXT, " +
                Constants.DB_CLM_NAME_Released + " TEXT, " +
                Constants.DB_CLM_NAME_Runtime + " TEXT, " +
                Constants.DB_CLM_NAME_Genre + " TEXT, " +
                Constants.DB_CLM_NAME_Director + " TEXT, " +
                Constants.DB_CLM_NAME_Writer + " TEXT, " +
                Constants.DB_CLM_NAME_Actors + " TEXT, " +
                Constants.DB_CLM_NAME_Plot + " TEXT, " +
                Constants.DB_CLM_NAME_Language + " TEXT, " +
                Constants.DB_CLM_NAME_Country + " TEXT, " +
                Constants.DB_CLM_NAME_Awards + " TEXT, " +
                Constants.DB_CLM_NAME_Poster + " TEXT, " +
                Constants.DB_CLM_NAME_Metascore + " TEXT, " +
                Constants.DB_CLM_NAME_imdbRating + " TEXT, " +
                Constants.DB_CLM_NAME_imdbVotes + " TEXT, " +
                Constants.DB_CLM_NAME_imdbID + " TEXT, " +
                Constants.DB_CLM_NAME_Type + " TEXT, " +
                Constants.DB_CLM_NAME_Response + " TEXT, " +
                Constants.DB_CLM_NAME_Watched + " TEXT, " +
                Constants.DB_CLM_NAME_Favorite + " TEXT, " +
                Constants.DB_CLM_NAME_URating + " REAL)";
        try {
            db.execSQL(cmd);
        } catch (SQLiteException e) {
            e.getMessage();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
