package com.example.myapplication.dbhandler;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.myapplication.model.Website;

import java.util.ArrayList;
import java.util.List;

public class MyDBSiteHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "sites.db";//name of file
    public static final String TABLE_SITES = "sites";//name of table
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "url";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_USER_ID = "userid";

    public MyDBSiteHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_SITES + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_USER_ID + " LONG " + ")";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SITES);
        onCreate(db);
    }

    public void addUrl(Website website, long id) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, website.get_url());
        values.put(COLUMN_TITLE, website.getTitle());
        values.put(COLUMN_USER_ID, id);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_SITES, null, values);
        db.close();
    }

    public void deleteUrl(String urlName, long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_SITES + " WHERE " + COLUMN_NAME + " =\"" + urlName + "\" AND " + COLUMN_USER_ID + " = " + id + " ;");
    }

    @SuppressLint("Range")
    public List<Website> databaseToString(long id) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_SITES + " WHERE " + COLUMN_USER_ID + " = " + id;

        List<Website> websites = new ArrayList<>();
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        if (c.moveToNext()) {
            do {
                if (c.getString(c.getColumnIndex(COLUMN_NAME)) != null) {
                    Website website = new Website(c.getString(c.getColumnIndex("url")), c.getString(c.getColumnIndex("title")));
                    websites.add(website);
                }
            } while (c.moveToNext());
        }
        return websites;
    }

    public void clearHistory(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_SITES + " WHERE " + COLUMN_USER_ID + " = " + id);
    }
}
