package com.explorer.technologies;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utility {
	public static String ServerPath = "http://smsc.xwireless.net/API/WebSMS/Http/v2.0/?";
	public static String username;
	public static String password;
	public static String sender_id;
	public static String smsCredit;
	public static String sPrefix;

	public static void storeCredentialsInSharedPref(SharedPreferences sp,
			String uname, String pass, String send_id,String prefix) {
		Editor editor = sp.edit();
		editor.putString("username", uname);
		editor.putString("pass", pass);
		editor.putString("sender_id", send_id);
		editor.putString("prefix", prefix);
		editor.commit();

		// set values
		username = uname;
		password = pass;
		sender_id = send_id;
		sPrefix = prefix;
	}

	public static boolean hasConnection(Context cont) {
	    ConnectivityManager cm = (ConnectivityManager) cont.getApplicationContext().getSystemService(
	        Context.CONNECTIVITY_SERVICE);

	    NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	    if (wifiNetwork != null && wifiNetwork.isConnected()) {
	      return true;
	    }

	    NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	    if (mobileNetwork != null && mobileNetwork.isConnected()) {
	      return true;
	    }

	    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
	    if (activeNetwork != null && activeNetwork.isConnected()) {
	      return true;
	    }

	    return false;
	}
	
	public static boolean isLatinLetter(char c) {
		return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
	}

	public static void alert(Context context,String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setNeutralButton("OK", null);
        builder.create();
        builder.show();
    }
	
	public static void alert(Context context,String title,String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNeutralButton("OK", null);
        builder.create();
        builder.show();
    }
	public static boolean getSharedPrefValues(SharedPreferences sp) {
		// TODO Auto-generated method stub

		String sp_username = sp.getString("username", "");
		String sp_pass = sp.getString("pass", "");
		String sp_sender_id = sp.getString("sender_id", "");
		if (!sp_username.equals("") && !sp_pass.equals("")) {
			username = sp_username;
			password = sp_pass;
			sender_id = sp_sender_id;
			return true;

		}
		return false;
	}

	public static JSONObject getjsonFromInputStream(InputStream is) {
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
			//Log.e("log_tag", "Error converting result " + e.toString());
		}
		try {
			jArray = new JSONObject(result);
		} catch (JSONException e) {
		//	Log.e("log_tag", "Error parsing data " + e.toString());
		}

		return jArray;

	}

}
