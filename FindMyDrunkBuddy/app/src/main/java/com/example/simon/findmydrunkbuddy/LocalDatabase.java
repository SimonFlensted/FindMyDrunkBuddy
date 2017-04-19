package com.example.simon.findmydrunkbuddy;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Simon-PC on 19-04-2017.
 */

public class LocalDatabase extends SQLiteOpenHelper {

    private static final String DB_NAME = "userdata"; // the name of our database
    private static final int DB_VERSION = 2; // the version of the database

    public LocalDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    public static void insertUser(SQLiteDatabase db, String username,
                                  String password, int userId){
        ContentValues userValues = new ContentValues();
        userValues.put("USERID", userId);
        userValues.put("USERNAME", username);
        userValues.put("PASSWORD", password);
        db.insert("USERDATA", null, userValues);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("CREATE TABLE USERDATA (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "USERID INTEGER, "
                    + "USERNAME TEXT, "
                    + "PASSWORD TEXT);");
    }
}
