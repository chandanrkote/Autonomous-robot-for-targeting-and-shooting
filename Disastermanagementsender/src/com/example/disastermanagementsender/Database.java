package com.example.disastermanagementsender;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper{

	public static final int DATABASE_VERSION=1;
	public static final String DATABASE_NAME="Refrigeration.db";
	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}   
   
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		final String User=   
                "CREATE TABLE Chat" + 
                         "(_id integer primary key autoincrement"
                        + ", MyIp TEXT  "
                        + ", FromIp TEXT" +
                        ", MyText TEXT" +
                        ", FromText TEXT" +
                        ",Dept TEXT" +
                        ", Password TEXT" +
                        ")";      
        db.execSQL(User);        
     
      
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	
	}
	
	
	
	public void insert(String myIp, String fromIp, String text, String fromText,String dept){
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv = new ContentValues();
		
		cv.put("MyIp", ChatActivity.myIP);
		cv.put("FromIp", fromIp);
		cv.put("MyText", text);
		cv.put("FromText",fromText);
		cv.put("Dept",dept);			
		db.insert("Chat", null, cv);

	
	}

}
