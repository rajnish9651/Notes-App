package com.example.noteapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DataBaseHelper extends SQLiteOpenHelper {


    private static  final String DATABASE_NAME="NoteDB";
    private static  final int VERSION=1;
    private static final String TABLE_NOTE ="notes";
    private static final String KEY_ID="id";
    private static final String KEY_TITLE="title";
    private  static  final String KEY_CONTENT="content";
    private  static  final String KEY_DATE="date";

    public DataBaseHelper(@Nullable Context context ) {
//        super(context, name, factory, version);
        super(context, DATABASE_NAME,null,VERSION) ;
    }


    // this method is used to create table
    @Override
    public void onCreate(SQLiteDatabase db) {

        //create table noteDB (id INTEGER PRIMARY KEY AUTOINCREMENT,title TEXT,content TEXT)
        db.execSQL("CREATE TABLE " + TABLE_NOTE + " ( " +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_TITLE + " TEXT, " +
                KEY_CONTENT + " TEXT, " +KEY_DATE + " TEXT "+ ")");
    }


    // this method is used to drop the table if alredy exist
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NOTE);
        onCreate(db);
    }


    //for Adding/Creating new Note
    public void addNote(String title, String content){
        SQLiteDatabase db=this.getWritableDatabase();

        // for date and time
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat();
        Date date=new Date();
        String currentDate=simpleDateFormat.format(date);

        ContentValues values=new ContentValues();
        values.put(KEY_TITLE,title);
        values.put(KEY_CONTENT,content);
        values.put(KEY_DATE,currentDate);
        db.insert(TABLE_NOTE,null,values);


    }

    //for updating note
    public void updateNote(int id,String title, String content){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(KEY_TITLE,title);
        values.put(KEY_CONTENT,content);
        //UPDATE table_name
        //SET column1 = value1, column2 = value2...., columnN = valueN
        //WHERE [condition];
        db.update(TABLE_NOTE,values,KEY_ID+"= ?", new String[]{String.valueOf(id)});

    }

    //for fetching all note
    public ArrayList<NoteData> fetchData(){

        SQLiteDatabase db=this.getReadableDatabase();

        Cursor cursor= db.rawQuery("SELECT * FROM "+TABLE_NOTE,null);

        ArrayList<NoteData> arrNoteData =new ArrayList<>();
        if (cursor!=null && cursor.moveToFirst()){
            do{

                NoteData note =new NoteData();
                note.id=cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID));
                note.title=cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE));
                note.content=cursor.getString(cursor.getColumnIndexOrThrow(KEY_CONTENT));
                note.date=cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE));

                arrNoteData.add(note);
            }while(cursor.moveToNext());
            cursor.close();
        }


        return arrNoteData;
    }

    // for deleting note
    public void deleteNote(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTE, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });

    }


}
