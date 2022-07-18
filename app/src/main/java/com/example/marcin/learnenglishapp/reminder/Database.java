package com.example.marcin.learnenglishapp.reminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    private static String dbname = "Reminders";

    public Database(@Nullable Context context){ super(context, dbname, null, 1); }

    public void onCreate(SQLiteDatabase sqLiteDatabase){
        String query = "create table tbl_reminder(id integer primary key autoincrement, title text, date text, time text)";
        sqLiteDatabase.execSQL(query);
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i,int i1){
        String query = "DROP TABLE IF EXISTS tbl_reminder";
        sqLiteDatabase.execSQL(query);
        onCreate(sqLiteDatabase);
    }

    public String addreminder(String title, String date, String time){
        SQLiteDatabase database = this.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("date", date);
        contentValues.put("time", time);

        float result = database.insert("tbl_reminder", null, contentValues);    //returns -1 if data successfully inserts into database

        if (result == -1) {
            return "Failed";
        } else {
            return "Successfully inserted";
        }
    }

    public Cursor readallreminders(){
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "select * from tbl_reminder order by id desc";
        Cursor cursor = database.rawQuery(query,null);
        return cursor;
    }



}
