package com.explorer.technologies;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import com.explorer.technologies.Login.loginAPI;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class Utility {
public static String ServerPath="http://smsc.xwireless.net/API/WebSMS/Http/v2.0/index.php";
public static String username;
public static String password;

public static void storeCredentialsInSharedPref(SharedPreferences sp,String uname,String pass)
{
	Editor editor = sp.edit();
	editor.putString("username", uname);
	editor.putString("pass", pass);
	editor.commit();
}
public static void getSharedPrefValues(SharedPreferences sp) {
	// TODO Auto-generated method stub
	
	String sp_username = sp.getString("username", "");
	String sp_pass = sp.getString("pass", "");
	if(!sp_username.equals("") && !sp_pass.equals(""))
	{
		username=sp_username;
		password=sp_pass;
		
	}
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
