package com.example.cardview_test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_USER = "create table User ("
            + "id integer primary key autoincrement, "
            + "name text, "
            + "phone text, "
            + "address text,"+ "image blob)";

    public static final String CREATE_Family = "create table Family (" +
            "_id integer primary key autoincrement,"
            + "name text,"
            + "phone text,"
            + "more text)";
//            + "image blob)";

    private Context mContext;

    public MyDatabaseHelper(Context context, String name,
                            SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER );
        db.execSQL(CREATE_Family );
        Toast.makeText(mContext, "Initial succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists User");
        db.execSQL("drop table if exists Family");
        onCreate(db);
    }

}
