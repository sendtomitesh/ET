package com.explorer.technologies;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Utility {
public static String ServerPath="http://smsc.xwireless.net/API/WebSMS/Http/v2.0/index.php";


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
