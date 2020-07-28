package com.tauseef.mybills;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Info.db";
    public static final String TABLE_NAME = "INFORMATION";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "DATE";
    public static final String COL_3 = "STYLE";
    public static final String COL_4 = "STYLENO";
    public static final String COL_5 = "DESCRIPTION";
    public static final String COL_6 = "PRICE";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_2 + " TEXT, " + COL_3 + " TEXT, " + COL_4 + " TEXT, " + COL_5 + " TEXT, " + COL_6 + " INTEGER )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(String date, String style, String styleno, String description, long price)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, date);
        contentValues.put(COL_3, style);
        contentValues.put(COL_4, styleno);
        contentValues.put(COL_5, description);
        contentValues.put(COL_6, price);
        long res = db.insert(TABLE_NAME, null, contentValues);
        if (res == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllItems()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ TABLE_NAME, null);
        return cursor;
    }

    public Cursor getOneRow(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cur = db.rawQuery("SELECT * FROM "+ TABLE_NAME, )
        Cursor cursor = db.rawQuery("SELECT * FROM "+ TABLE_NAME+" WHERE ID = "+id, null);
        cursor.moveToFirst();
        return cursor;
    }

    public boolean updateRow(String id, String date, String style, String styleno, String description, long price)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, date);
        contentValues.put(COL_3, style);
        contentValues.put(COL_4, styleno);
        contentValues.put(COL_5, description);
        contentValues.put(COL_6, price);
        db.update(TABLE_NAME,contentValues,"ID = ?", new String[] {id});
        return true;
    }

    public int deleteData(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[] {id});
    }
}
