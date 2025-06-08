package com.example.estrellastats;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StatsDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "estrellastats.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "estadisticas";

    public StatsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "fecha TEXT NOT NULL, " +         // formato: yyyy-MM-dd
                "clase TEXT NOT NULL, " +
                "attendance INTEGER DEFAULT 0, " +
                "onTime INTEGER DEFAULT 0, " +
                "bibles INTEGER DEFAULT 0, " +
                "chapters INTEGER DEFAULT 0, " +
                "visits INTEGER DEFAULT 0" +
                ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Si hay cambios de versión, se puede actualizar aquí.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
