package com.explorer.technologies;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.util.Log;

public class Utility {
public static String ServerPath="http://smsc.xwireless.net/API/WebSMS/Http/v2.0/?";
public static String username;
public static String password;
public static String sender_id;

public static void storeCredentialsInSharedPref(SharedPreferences sp,String uname,String pass,String send_id)
{
	Editor editor = sp.edit();
	editor.putString("username", uname);
	editor.putString("pass", pass);
	editor.putString("sender_id", send_id);
	editor.commit();
	
	//set values
	username=uname;
	password=pass;
	sender_id=send_id;  
}

//Return contact Name from number
@SuppressWarnings("deprecation")
public static String getContactName(final String phoneNumber,Context context) {
    Uri uri;
    String[] projection;
    Uri mBaseUri = Contacts.CONTENT_FILTER_URI;
    projection = new String[] { android.provider.Contacts.People.NAME };
    try {
        Class<?> c = Class
                .forName("android.provider.ContactsContract$PhoneLookup");
        mBaseUri = (Uri) c.getField("CONTENT_FILTER_URI").get(mBaseUri);
        projection = new String[] { "display_name" };
    } catch (Exception e) {
    }
    uri = Uri.withAppendedPath(mBaseUri, Uri.encode(phoneNumber));
    Cursor cursor = context.getContentResolver().query(uri, projection, null,
            null, null);

    String contactName = "";

    if (cursor.moveToFirst()) {
        contactName = cursor.getString(0);
    }
    cursor.close();
    cursor = null;
    return contactName;
}


public static boolean isLatinLetter(char c) {
    return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
}
public static boolean getSharedPrefValues(SharedPreferences sp) {
	// TODO Auto-generated method stub
	
	String sp_username = sp.getString("username", "");
	String sp_pass = sp.getString("pass", "");
	String sp_sender_id=sp.getString("sender_id","");
	if(!sp_username.equals("") && !sp_pass.equals(""))
	{
		username=sp_username;
		password=sp_pass;
		sender_id=sp_sender_id;
		return true;
		
	}
	return false;
}

public static JSONObject getjsonFromInputStream(InputStream is)
{
	String result = "";
	JSONObject jArray = null;

	try {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				is, "iso-8859-1"), 8);
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}
		is.close();
		result = sb.toString();
	} catch (Exception e) {
		Log.e("log_tag", "Error converting result " + e.toString());
	}
	try {
		jArray = new JSONObject(result);
	} catch (JSONException e) {
		Log.e("log_tag", "Error parsing data " + e.toString());
	}

	return jArray;

}


	
}
