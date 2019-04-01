/*
 * Copyright (c) 2019 Created by Adit Chauhan
 */

package tutorial.adit.com.onelabsdemo.CacheHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class UrlCache extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String URL_TABLE_NAME = "images";
    public static final String URL_COLUMN_ID = "id";
    public static final String URL_COLUMN_UID = "uid";
    public static final String URL_COLUMN_NAME = "category";
    public static final String URL_COLUMN_URL = "url";
    private HashMap hp;

    public UrlCache(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table images " +
                        "(id integer primary key, uid text, category text,url text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS images");
        onCreate(db);
    }

    public boolean inserturl(String category, String url) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("category", category);
        contentValues.put("url", url);
        db.insert("images", null, contentValues);
        return true;
    }

    public Cursor getData(String  cate) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from images where category=" + cate + "", null);
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, URL_TABLE_NAME);
        return numRows;
    }




    public void updateMethod(Integer ids, String category, String url){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("category", category);
        values.put("url", url);
        db.update("images", values, URL_COLUMN_ID +" = " + ids+" AND "+URL_COLUMN_NAME+" = "+category, null);
    }

    public Integer deleteContact(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("images",
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    public ArrayList<String> getAllurls(String s ) {
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d(" db sent token :",s);
        String query = "SELECT * FROM images WHERE category = " + s;
        Cursor res = db.rawQuery("SELECT * FROM " + URL_TABLE_NAME,null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            array_list.add(res.getString(res.getColumnIndex(URL_COLUMN_URL)));
            res.moveToNext();
        }
        return array_list;
    }
}