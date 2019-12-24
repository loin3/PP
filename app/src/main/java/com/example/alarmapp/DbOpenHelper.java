package com.example.alarmapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import androidx.annotation.Nullable;

public class DbOpenHelper implements BaseColumns {

    private static final String TABLE_NAME = "water_history";
    public static final String TIME = "time";
    private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"("+_ID+" Integer PRIMARY KEY AUTOINCREMENT, "+TIME+" Integer NOT NULL" + ");";

    public static SQLiteDatabase sqLiteDatabase;
    private DatabaseHelper databaseHelper;
    private Context context;

    private class DatabaseHelper extends SQLiteOpenHelper{
        public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    public DbOpenHelper(Context context){
        this.context = context;
    }

    public DbOpenHelper open() throws SQLException {
        databaseHelper = new DatabaseHelper(context, "DatabaseForWater(SQLite).db", null, 1);
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        return this;
    }

    public void create(){
        databaseHelper.onCreate(sqLiteDatabase);
    }

    public void close(){
        sqLiteDatabase.close();
    }

    public long insertColumn(int time){
        ContentValues contentValues = new ContentValues();
        contentValues.put(TIME, time);
        return sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
    }

    public Cursor selectColumns(){
        return sqLiteDatabase.query(TABLE_NAME, null, null, null, null, null, null);
    }

   public Cursor sortColumn(String standardColumn){
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM usertable ORDER BY " + standardColumn + ";", null);
        return cursor;
    }

    public void deleteAllColumns(){
        sqLiteDatabase.delete(TABLE_NAME, null,null);
    }

    public boolean deleteColumn(int id){
        return sqLiteDatabase.delete(TABLE_NAME, "_id="+id, null) > 0;
    }

    public int getIdFromTime(int time){
        Cursor cursor = this.selectColumns();
        while(cursor.moveToNext()){
            int _Id = cursor.getInt(cursor.getColumnIndex(_ID));
            int rawTime = cursor.getInt(cursor.getColumnIndex(TIME));
            if(rawTime == time){
                return _Id;
            }
        }
        return -1;
    }

    public int getTime(int time){
        Cursor cursor = this.selectColumns();
        while(cursor.moveToNext()){
            int rawTime = cursor.getInt(cursor.getColumnIndex(TIME));
            if(rawTime == time){
                return rawTime;
            }
        }
        return -1;
    }
}
