package com.explorer.technologies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper{

	
	public static final int DB_VERSION = 1;
	public static final String DB_NAME = "expTechDB";
	public static final String TABLE_DRAFTS = "drafts";
	public static final String TAG = DbHelper.class.getSimpleName();
	
	public DbHelper(Context context) {
		super(context, DB_NAME, null,DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table if not exists " + TABLE_DRAFTS + "(id integer primary key autoincrement, "
				+ "to VARCHAR,message VARCHAR);";
		db.execSQL(sql);
		Log.d(TAG, "Database created successfully!");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exist" + TABLE_DRAFTS);
		this.onCreate(db);
		Log.d(TAG, "Database updated successfully!");
	}
	
}
