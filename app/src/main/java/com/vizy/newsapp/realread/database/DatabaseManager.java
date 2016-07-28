package com.vizy.newsapp.realread.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseManager {
    public static final String TABLE_NAME = "Contacts";


    public static final String NEWS_ID = "_id";
    public static final String NEWS_TITLE = "news_title";
    public static final String NEWS_DESCRIPTION = "news_description";

    public static final String CREATE_TABLE = "create table " + TABLE_NAME + " ("
            + NEWS_ID + " integer primary key autoincrement,"
            + NEWS_TITLE + " text not null,"
            + NEWS_DESCRIPTION + " text);";


    private DBHelper mHelper;
    private SQLiteDatabase mDb;
    private String[] mColumns = new String[]{NEWS_ID, NEWS_TITLE, NEWS_DESCRIPTION};

    public DatabaseManager(Context context) {

        mHelper = new DBHelper(context);
        mDb = mHelper.getWritableDatabase();
    }

    public void insert(String name, String news) {

        mDb.insert(TABLE_NAME, null, generateContentValues(name, news));

    }

    private ContentValues generateContentValues(String name, String news) {
        ContentValues values = new ContentValues();
        values.put(NEWS_TITLE, name);
        values.put(NEWS_DESCRIPTION, news);
        return values;
    }

    public void deleteFromDB(String name) {
        mDb.delete(TABLE_NAME, NEWS_TITLE + "=?", new String[]{name});
    }

    public void modifyPhone(String name, String newphone) {
        mDb.update(TABLE_NAME, generateContentValues(name, newphone), NEWS_TITLE + "=?", new String[]{name});
    }

    public Cursor loadContactsCursor() {
        return mDb.query(TABLE_NAME, mColumns, null, null, null, null, null);
    }

    public Cursor searchNews(String name) {
        try {//added this try and catch to simulate a search in a large database that would take time and freeze the app without background task
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mDb.query(TABLE_NAME, mColumns, NEWS_TITLE + "=?", new String[]{name}, null, null, null);
    }


}
