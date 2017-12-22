package com.example.lenovo_.hedeftantm.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.lenovo_.hedeftantm.UserObject;

/**
 * Created by Lenovo- on 16.12.2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME   = "ModaYakamoz";
    private static final String TABLE_USER      = "user";
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null,28);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_USER +
                "(" +
                "id INTEGER PRIMARY KEY," +
                "region            TEXT," +
                "summonerID        TEXT," +
                "email             TEXT," +
                "sifre             TEXT" +
                ")";
        db.execSQL(sql);


    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }
    public void deleteUser () {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_USER);
        db.close();
    }

    public void insertUser (String email, String sifre, String region, String summonerID) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " + TABLE_USER);

        ContentValues values = new ContentValues();
        values.put("region",region);
        values.put("summonerID",summonerID);
        values.put("email",email);
        values.put("sifre",sifre);
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    public UserObject getUser() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_USER, new String[]{
                "id", "region", "summonerID", "email", "sifre"},null, null, null, null, null);

        UserObject uo = new UserObject("","","","");

        while (cursor.moveToNext()) {
            uo = new UserObject(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            return uo;
        }
        return uo;
    }
}