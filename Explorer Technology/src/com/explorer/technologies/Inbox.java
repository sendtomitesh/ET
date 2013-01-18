package com.explorer.technologies;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

public class Inbox {
	
	private String mId;
	private String mName;
	private String mNumber;
	private String mMessage;
	private String mDate;
	
	
	public static ArrayList<Inbox> sInboxList;
	
	public Inbox(String id,String name,String number,String message,String date){
		this.mId = id;
		this.mName = name;
		this.mNumber = number;
		this.mMessage = message;
		this.mDate = date;
	}
	
	public static void initalizeList() {
        sInboxList = new ArrayList<Inbox>();
    }
	
	//public getters
	public String getId() {
        return this.mId;
    }
	
	public String getNumber() {
        return this.mNumber;
    }
	
	public String getName() {
        return this.mName;
    }
	
	public String getMessage() {
        return this.mMessage;
    }
	
	public String getDate() {
        return this.mDate;
    }
	
	public static ArrayList<Inbox> getInboxList() {
        return sInboxList;
    }
	public static void deleteMsg(int position)
	{
		sInboxList.remove(position);
	}
	
	public static void deleteAllMsg()
	{
		sInboxList.removeAll(sInboxList);
	}
	//public setters
	public void setId(String id) {
        this.mId = id;
    }
	
	public void setNumber(String number) {
        this.mNumber = number;
    }
	
	public void setName(String name) {
        this.mName = name;
    }
	
	public void setMessage(String message) {
        this.mMessage = message;
    }
	
	public void setDate(String date) {
        this.mDate = date;
    }
	
	
    public static boolean setInbox(Context context) {
    	initalizeList();
    	
    	//get contacts from phone
        Uri uriSMSURI = Uri.parse("content://sms/inbox");
        final Cursor inboxCursor = context.getContentResolver().query(uriSMSURI, null, null, null,null);
        
        String id = "";
        String number = "";
        String message = "";
        String date = "";
        String name = "";
        
        try {
            while (inboxCursor.moveToNext()) {
                // This would allow you get several email addresses
                // if the email addresses were stored in an array
                id = inboxCursor.getString(0);
            	number = inboxCursor.getString(inboxCursor.getColumnIndex("address"));
            	message = inboxCursor.getString(inboxCursor.getColumnIndex("body"));
            	date = inboxCursor.getString(inboxCursor.getColumnIndex("date"));
                //create object and add it to list.
            	name = getContactNameFromNumber(number, context);
            	date =  convertToDate(date);
            	
                Inbox inbox = new Inbox(id,name, number, message, date);
                sInboxList.add(inbox);

            }
            inboxCursor.close();
            return true;

        } catch (Exception e) {
            return false;
        }
    }
    
	
	private static String getContactNameFromNumber(String phoneNumber,Context context)
	{
		String name = "";
		//Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
		Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
		//return resolver.query(uri, new String[]{PhoneLookup.DISPLAY_NAME});
		Cursor cursor = null;
		try {

			cursor = context.getContentResolver().query(uri,new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME},null,null,null);
			 
			 if (cursor.moveToFirst()) {
				 name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
				 return name;
			 }
			 else{
				 return phoneNumber;
			 }
			
		} catch (Exception e) {
			return phoneNumber;
		}

		
	}
	
	@SuppressLint("SimpleDateFormat")
	private static String convertToDate(String dateString){
		long timestamp = Long.parseLong(dateString);    
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd hh:mm a");
		return dateFormat.format(calendar.getTime());
	
	}


}
