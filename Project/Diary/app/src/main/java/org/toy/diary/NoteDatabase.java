package org.toy.diary;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class NoteDatabase{
    private static final String TAG ="NoteDatabase";

    private static NoteDatabase database;
    public static String TABLE_NOTE = "NOTE";
    public static int DATABASE_VERSION =1;

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Context context;

    private NoteDatabase(Context context){
        this.context =context;
    }

    public static NoteDatabase getInstance(Context context){
        if(database ==null){
            database = new NoteDatabase(context);
        }
        return database;
    }

    public boolean open(){
        println("opening db "+AppConstants.DATABASE_NAME);
        dbHelper = new DatabaseHelper(context);
        db =  dbHelper.getWritableDatabase();
        return true;
    }

    public void close(){
        println("closing db " + AppConstants.DATABASE_NAME);

        db.close();
        database = null;
    }

    public Cursor rawQuery(String SQL){
        println("\n executeQuery call\n" );

        Cursor cursor = null;
        try{
            cursor =db.rawQuery(SQL,null);
            println("cursor count : "+cursor.getCount());
        } catch (Exception e){
            Log.e(TAG,"Exception in executeQuery",e);
        }
        return cursor;
    }

    public boolean execSQL(String SQL){
        println("\nexecute called\n");

        try{
            Log.d(TAG,"SQL : "+SQL);
            db.execSQL(SQL);
        }catch (Exception e){
            Log.e(TAG,"Exception in executeQuery",e);
            return false;
        }
        return true;
    }





    private class DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper(Context context) {
            super(context, AppConstants.DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            println("creating database "+AppConstants.DATABASE_NAME);
            println("creating Table "+TABLE_NOTE);
            String DROP_SQL = "DROP TABLE IF EXISTS "+TABLE_NOTE;
            try{
                db.execSQL(DROP_SQL);
            }catch (Exception e) { Log.e(TAG,"Exception in CREATE_SQL",e); }
            String CREATE_SQL ="CREATE TABLE " + TABLE_NOTE + " ( "
                    +" _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT ,"
                    +" WEATHER TEXT DEFAULT '', "
                    +" ADDRESS TEXT DEFAULT '', "
                    +" LOCATION_X TEXT DEFAULT '', "
                    +" LOCATION_Y TEXT DEFAULT '', "
                    +" CONTENTS TEXT DEFAULT '', "
                    +" MOOD TEXT , "
                    +" PICTURE TEXT DEFAULT '' , "
                    +" CREATE_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP ,  "
                    +" MODIFY_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP  "
                    +" )";
            try{
                db.execSQL(CREATE_SQL);
            }catch (Exception e) { Log.e(TAG,"Exception in CREATE_SQL",e);}
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);
            println("opened database");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            println("update db"+oldVersion+ " to "+newVersion);
        }
    }
    private void println(String data){
        Log.d(TAG,data);
    }
}
