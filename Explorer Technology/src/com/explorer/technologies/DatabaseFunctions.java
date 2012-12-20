package com.explorer.technologies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseFunctions {

	// Global declarations
	static DbHelper dbHelper;
	static SQLiteDatabase db;
	// private static final String ROW_ID = "id";sms_to_complete
	private static final String TO = "sms_to";
	private static final String TO_COMPLETE = "sms_to_complete";
	private static final String MESSAGE = "message";

	// Save drats Message
	public static Boolean saveToDrafts(String to, String message,
			String toComplete) {

		ContentValues values = new ContentValues();
		values.put(TO, to);
		values.put(MESSAGE, message);
		values.put(TO_COMPLETE, toComplete);

		try {

			// Perform insert into Database
			db.insert(DbHelper.TABLE_DRAFTS, null, values);
			Log.d("DRAFT INSERT", "Record inserted!");

		} catch (Exception e) {
			Log.e("DRAFT INSERT", "error : " + e.toString());
			return false;
		}
		return true;

	}

	// Return drafts cursor
	public static Cursor getDraftCursor() {
		Cursor cursor = null;
		try {

			cursor = db
					.rawQuery(
							"SELECT  id AS _id,sms_to,message,sms_to_complete FROM drafts",
							null);

		} catch (Exception e) {
			Log.e("DRAFT SELECT", "error : " + e.toString());
			return null;
		}

		return cursor;
	}

	// Return true if draft message get deleted
	public static Boolean deleteDraftMessage(String id) {

		try {

			db.execSQL("DELETE FROM drafts WHERE id =" + id);
			Log.d("DRAFT DELETE", "Record deleted!");

		} catch (Exception e) {
			Log.e("DRAFT DELETE", "error : " + e.toString());
			return false;
		}
		return true;
	}

	// Save Auto Reply Message
	public static Boolean addAutoReply(String to, String message) {

		ContentValues values = new ContentValues();
		values.put(TO, to);
		values.put(MESSAGE, message);

		try {

			// Perform insert into Database
			db.insert(DbHelper.TABLE_AUTO_REPLY, null, values);
			Log.d("AUTO_REPLY INSERT", "Record inserted!");

		} catch (Exception e) {
			Log.e("AUTO_REPLY INSERT", "error : " + e.toString());
			return false;
		}
		return true;

	}

	// Save Auto Reply Detail
	public static Boolean addAutoReplyDetail(String autoReplyId, String to,
			String date) {

		ContentValues values = new ContentValues();
		values.put("autoReplyId", autoReplyId);
		values.put(TO, to);
		values.put("sentOn", date);

		try {

			// Perform insert into Database
			db.insert(DbHelper.TABLE_AUTO_REPLY_DETAIL, null, values);
			Log.d("AUTO_REPLY_DETAIL INSERT", "Record inserted!");

		} catch (Exception e) {
			Log.e("AUTO_REPLY_DETAIL", "error : " + e.toString());
			return false;
		}
		return true;

	}

	// Update Auto Reply Message
	public static Boolean updateAutoReply(String to, String message) {

		ContentValues values = new ContentValues();
		values.put(MESSAGE, message);

		try {

			// Perform Update
			String strFilter = "sms_to='" + to + "'";
			db.update(DbHelper.TABLE_AUTO_REPLY, values, strFilter, null);
			Log.d("AUTO_REPLY UPDATE", "Record Updated!");

		} catch (Exception e) {
			Log.e("AUTO_REPLY UPDATE", "error : " + e.toString());
			return false;
		}
		return true;

	}

	// Return true if AutoReply message get deleted
	public static Boolean deleteAutoReply(String id) {

		try {

			db.execSQL("DELETE FROM autoReply WHERE id =" + id);
			db.execSQL("DELETE FROM autoReplyDetail WHERE autoReplyId = " + id);
			Log.d("DRAFT AUTO_REPLY", "Record deleted!");

		} catch (Exception e) {
			Log.e("DRAFT AUTO_REPLY", "error : " + e.toString());
			return false;
		}
		return true;
	}

	// Return true if draft message get deleted
	public static Boolean deleteAllDraftMessage() {

		try {

			db.execSQL("DELETE FROM drafts");
			Log.d("DRAFT DELETE", "Record deleted!");

		} catch (Exception e) {
			Log.e("DRAFT DELETE", "error : " + e.toString());
			return false;
		}
		return true;
	}

	// Check Auto Reply esist or not
	public static Boolean checkAutoReply(String to) {

		Boolean check = false;

		try {
			Cursor cursor = db.rawQuery(
					"SELECT  sms_to FROM autoReply where sms_to='" + to + "'",
					null);

			if (cursor.getCount() > 0) {
				check = true;
			}

			cursor.close();
		} catch (Exception e) {
			Log.e("AUTO_REPLY SELECT", "error : " + e.toString());
		}

		return check;
	}

	// Get autoreply message for particular number set
	public static String getAutoReplyMessage(String to) {

		String message = "";

		try {
			Cursor cursor = db.rawQuery(
					"SELECT  message FROM autoReply where sms_to='" + to + "'",
					null);

			if (cursor.moveToFirst()) {
				message = cursor.getString(0);
			}

			cursor.close();
		} catch (Exception e) {
			Log.e("AUTO_REPLY SELECT", "error : " + e.toString());
		}

		return message;
	}

	// Return Auto Reply cursor
	public static Cursor getAutoReplyCursor() {
		Cursor cursor = null;
		try {

			// cursor = db.rawQuery(
			// "SELECT  id AS _id,sms_to,message FROM autoReply", null);

			cursor = db
					.rawQuery(
							"SELECT a.id as _id, a.sms_to,a.message,Count(d.id) as reply_count from autoReply as a "
									+ "LEFT JOIN autoReplyDetail as d on a.id = d.autoReplyId GROUP BY"
									+ " a.id , a.sms_to,a.message ORDER BY a.id DESC",
							null);

		} catch (Exception e) {
			Log.e("AUTO_REPLY SELECT", "error : " + e.toString());
			return null;
		}

		return cursor;
	}

	public static Cursor getAutoReplyDetailCursor(String id) {
		Cursor cursor = null;
		try {

			cursor = db.rawQuery("SELECT id AS _id,autoReplyId,sms_to,sentOn"
					+ " FROM autoReplyDetail Where autoReplyId = " + id
					+ " ORDER BY id DESC", null);

		} catch (Exception e) {
			Log.e("AUTO_REPLY SELECT", "error : " + e.toString());
			return null;
		}

		return cursor;
	}

	// Return true if all Auto Reply message gets deleted
	public static Boolean deleteAllAutoReply() {

		try {

			db.execSQL("DELETE FROM autoReply");
			Log.d("AUTO_REPLY DELETE", "Record deleted!");

		} catch (Exception e) {
			Log.e("AUTO_REPLY DELETE", "error : " + e.toString());
			return false;
		}
		return true;
	}

	// Return drafts cursor
	public static Cursor getSentMessageCursor() {
		Cursor cursor = null;
		try {

			cursor = db.rawQuery(
					"SELECT  id AS _id, sms_to , message , sms_to_complete , messageStatus "
							+ ", sentOn FROM sentItems ORDER BY id DESC", null);

		} catch (Exception e) {
			Log.e("TABLE_SENT_ITEM SELECT", "error : " + e.toString());
			return null;
		}

		return cursor;
	}

	// Return true if all Auto Reply message gets deleted
	public static Boolean deleteAllSentMessage() {

		try {

			db.execSQL("DELETE FROM sentItems");
			Log.d("TABLE_SENT_ITEM DELETE", "Record deleted!");

		} catch (Exception e) {
			Log.e("TABLE_SENT_ITEM DELETE", "error : " + e.toString());
			return false;
		}
		return true;
	}
	
	// Return true if all Auto Reply message gets deleted
	public static Boolean deleteSentMessage(String sentId) {

		try {

			db.execSQL("DELETE FROM sentItems where id = " + sentId);
			Log.d("TABLE_SENT_ITEM DELETE", "Record deleted!");

		} catch (Exception e) {
			Log.e("TABLE_SENT_ITEM DELETE", "error : " + e.toString());
			return false;
		}
		return true;
	}

	// Save Sent messages
	public static Boolean saveSentMessage(String to, String message,
			String toComplete, String messageStatus, String date) {

		ContentValues values = new ContentValues();
		values.put(TO, to);
		values.put(MESSAGE, message);
		values.put("sms_to_complete", toComplete);
		values.put("messageStatus", messageStatus);
		values.put("sentOn", date);

		try {

			// Perform insert into Database
			db.insert(DbHelper.TABLE_SENT_ITEMS, null, values);
			Log.d("TABLE_SENT_ITEM INSERT", "Record inserted!");

		} catch (Exception e) {
			Log.e("TABLE_SENT_ITEM", "error : " + e.toString());
			return false;
		}
		return true;

	}

	public static void openDb(Context context) {
		dbHelper = new DbHelper(context);
		db = dbHelper.getWritableDatabase();

	}

	public static void closeDb() {
		if (db.isOpen()) {
			db.close();
			dbHelper.close();
		}

	}

}
