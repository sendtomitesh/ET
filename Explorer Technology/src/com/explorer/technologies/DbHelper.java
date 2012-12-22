package com.explorer.technologies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {

	public static final int DB_VERSION = 2;
	public static final String DB_NAME = "expTechDB";
	public static final String TABLE_DRAFTS = "drafts";
	public static final String TABLE_AUTO_REPLY = "autoReply";
	public static final String TABLE_AUTO_REPLY_DETAIL = "autoReplyDetail";
	public static final String TABLE_SENT_ITEMS = "sentItems";
	public static final String TAG = DbHelper.class.getSimpleName();

	public DbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String createDrafts = "CREATE TABLE IF NOT EXISTS " + TABLE_DRAFTS
				+ "(id integer primary key autoincrement, "
				+ "sms_to VARCHAR,message VARCHAR,sms_to_complete VARCHAR);";

		String createAutoReply = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_AUTO_REPLY + "(id integer primary key autoincrement, "
				+ "sms_to VARCHAR,message VARCHAR);";
		
		String createAutoReplyDetail = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_AUTO_REPLY_DETAIL + "(id integer primary key autoincrement, "
				+ "autoReplyId integer,sms_to VARCHAR,sentOn DATETIME);";
		
		String createSentStatus = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_SENT_ITEMS + "(id integer primary key autoincrement, "
				+ "sms_to VARCHAR,message VARCHAR,sms_to_complete VARCHAR, "
				+"messageStatus VARCHAR,sentOn DATETIME);";

		db.execSQL(createDrafts);
		db.execSQL(createAutoReply);
		db.execSQL(createAutoReplyDetail);
		db.execSQL(createSentStatus);

		Log.d(TAG, "Database created successfully!");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRAFTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUTO_REPLY);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUTO_REPLY_DETAIL);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SENT_ITEMS);
		this.onCreate(db);
		//Log.d(TAG, "Database updated successfully!");
	}
	
	

}
