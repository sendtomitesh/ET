package com.explorer.technologies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseFunctions {

	//Global declarations
	private static DbHelper dbHelper; 
	private static SQLiteDatabase db ;
	//private static final String ROW_ID = "id";
	private static final String TO = "to";
	private static final String MESSAGE = "message";

	
	//Save drats Message
	public static Boolean saveToDrafts(Context context, String to, String message) {

		ContentValues values = new ContentValues();
		values.put(TO, to);
		values.put(MESSAGE, message);

		try {
			// Open the Database
			dbHelper = new DbHelper(context);
			db = dbHelper.getWritableDatabase();
		
			// Perform insert into Database
			db.insert(DbHelper.TABLE_DRAFTS, null, values);
			Log.d("DRAFT INSERT", "Record inserted!");

		} catch (Exception e) {
			Log.e("DRAFT INSERT", "error : " + e.toString());
			return false;
		}finally{
			// Close the Database
			db.close();
			dbHelper.close();
		}
		return true;

	}
	
	//Return drafts cursor
	public static Cursor getDraftCursor(Context context)
	{
		Cursor cursor = null;
		try {
			dbHelper = new DbHelper(context);
			db = dbHelper.getReadableDatabase();
			cursor = db.rawQuery("SELECT * FROM drafts", null);
			
		} catch (Exception e) {
			Log.e("DRAFT SELECT", "error : " + e.toString());
			return null;
		}finally{
			// Close the Database
			db.close();
			dbHelper.close();
		}
		
		return cursor;
	}
	
	//Return true if  draft message get deleted
	public static Boolean deleteDraftMessage(Context context,String id)
	{
		
		try {
			
			dbHelper = new DbHelper(context);
			db = dbHelper.getReadableDatabase();
			db.execSQL("DELETE FROM drafts WHERE id =" + id);
			Log.d("DRAFT DELETE", "Record deleted!");
			
		} catch (Exception e) {
			Log.e("DRAFT DELETE", "error : " + e.toString());
			return false;
		}finally{
			// Close the Database
			db.close();
			dbHelper.close();
		}
		return true;
	}
	
	

}
