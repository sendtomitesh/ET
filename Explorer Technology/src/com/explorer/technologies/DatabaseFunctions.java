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
	private static final String TO = "sms_to";
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
	public static Cursor getDraftCursor(Context context,SQLiteDatabase sqlDb)
	{
		Cursor cursor = null;
		try {
			
			cursor = sqlDb.rawQuery("SELECT  id AS _id,sms_to,message FROM drafts", null);
			
		} catch (Exception e) {
			Log.e("DRAFT SELECT", "error : " + e.toString());
			return null;
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
	
	//Save Auto Reply Message
	public static Boolean addAutoReply(Context context, String to, String message) {

			ContentValues values = new ContentValues();
			values.put(TO, to);
			values.put(MESSAGE, message);

			try {
				// Open the Database
				dbHelper = new DbHelper(context);
				db = dbHelper.getWritableDatabase();
			
				// Perform insert into Database
				db.insert(DbHelper.TABLE_AUTO_REPLY, null, values);
				Log.d("AUTO_REPLY INSERT", "Record inserted!");

			} catch (Exception e) {
				Log.e("AUTO_REPLY INSERT", "error : " + e.toString());
				return false;
			}finally{
				// Close the Database
				db.close();
				dbHelper.close();
			}
			return true;

	}
	
	//Check Auto Reply esist or not
	public static Boolean checkAutoReply(Context context, String to ,SQLiteDatabase sqlDb) {

		Boolean check = false;
		Cursor cursor = null;
		try {
			
			cursor = sqlDb.rawQuery("SELECT  message FROM autoReply WHERE sms_to = " + to, null);
			
			if(cursor.getCount() != 0){
				check = true;
			}
						
		} catch (Exception e) {
			Log.e("AUTO_REPLY SELECT", "error : " + e.toString());
		}
		return check;
	}
	
	//Return Auto Reply cursor
	public static Cursor getAutoReplyCursor(Context context,SQLiteDatabase sqlDb)
	{
			Cursor cursor = null;
			try {
				
				cursor = sqlDb.rawQuery("SELECT  id AS _id,sms_to,message FROM autoReply", null);
				
			} catch (Exception e) {
				Log.e("AUTO_REPLY SELECT", "error : " + e.toString());
				return null;
			}
			
			return cursor;
	}
	
	//Return true if  all Auto Reply message gets deleted
	public static Boolean deleteAllAutoReply(Context context)
	{
			
			try {
				
				dbHelper = new DbHelper(context);
				db = dbHelper.getReadableDatabase();
				db.execSQL("DELETE FROM autoReply");
				Log.d("AUTO_REPLY DELETE", "Record deleted!");
				
			} catch (Exception e) {
				Log.e("AUTO_REPLY DELETE", "error : " + e.toString());
				return false;
			}finally{
				// Close the Database
				db.close();
				dbHelper.close();
			}
			return true;
	}

}
