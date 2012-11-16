package com.explorer.technologies;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;



public class APICalls {

	
	//returns 0 for success, 1 for username or password incorrect, 2 for protocol error, 3 for IO error and 4 for JSon parsing error
	public static int userLogin(String username, String password) 
	{
		
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();

		HttpPost httppost = new HttpPost(Utility.ServerPath);

		try {
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			
			nameValuePairs.add(new BasicNameValuePair("method", "credit_check"));
			nameValuePairs.add(new BasicNameValuePair("username", username));
			nameValuePairs.add(new BasicNameValuePair("password", password));
			nameValuePairs.add(new BasicNameValuePair("format", "json"));
			
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			// Execute HTTP Post Request
		
			HttpResponse response = httpclient.execute(httppost);
			JSONObject jo=Utility.getjsonFromInputStream(response.getEntity().getContent());
			if(jo.getString("error").equals("0"))return 0;
			else return 1;
		} catch (ClientProtocolException e) {
		
			Log.e("Client error", e.toString());
			return 2;
		} catch (IOException e) {
			
			Log.e("IO Error", e.toString());
			return 3;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return 4;
			
		}

	}
	//returns 0 for success, 1 for error in registration1, 2 for protocol error, 3 for IO error and 4 for JSon parsing error
	public static int userRegistration(String username, String password, String fullname,String address,String mobile) 
		{
			
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppost = new HttpPost(Utility.ServerPath);

			try {
				
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				
				nameValuePairs.add(new BasicNameValuePair("method", "register"));
				nameValuePairs.add(new BasicNameValuePair("format", "json"));
				nameValuePairs.add(new BasicNameValuePair("login", username));
				nameValuePairs.add(new BasicNameValuePair("password", password));
				nameValuePairs.add(new BasicNameValuePair("name", fullname));
				nameValuePairs.add(new BasicNameValuePair("address", address));
				nameValuePairs.add(new BasicNameValuePair("mobile", mobile));
				
				
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				
				// Execute HTTP Post Request
			
				HttpResponse response = httpclient.execute(httppost);
				JSONObject jo=Utility.getjsonFromInputStream(response.getEntity().getContent());
				if(jo.getString("error").equals("0"))return 0;
				else return 1;
			} catch (ClientProtocolException e) {
			
				Log.e("Client error", e.toString());
				return 2;
			} catch (IOException e) {
				
				Log.e("IO Error", e.toString());
				return 3;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				return 4;
				
			}

		}

	//returns 0 for success, 1 for error in registration1, 2 for protocol error, 3 for IO error and 4 for JSon parsing error
	public static int sendMsg(String username, String password, String sender,String to,String message) 
			{
				
				// Create a new HttpClient and Post Header
				HttpClient httpclient = new DefaultHttpClient();

				HttpPost httppost = new HttpPost(Utility.ServerPath);

				try {
					
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
					
					nameValuePairs.add(new BasicNameValuePair("method", "compose"));
					
					nameValuePairs.add(new BasicNameValuePair("username", username));
					nameValuePairs.add(new BasicNameValuePair("password", password));
					nameValuePairs.add(new BasicNameValuePair("sender", sender));
					nameValuePairs.add(new BasicNameValuePair("to", to));
					nameValuePairs.add(new BasicNameValuePair("message", message));
					nameValuePairs.add(new BasicNameValuePair("international","1" ));
					nameValuePairs.add(new BasicNameValuePair("format", "json"));
					
					
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					
					// Execute HTTP Post Request
				
					HttpResponse response = httpclient.execute(httppost);
					JSONObject jo=Utility.getjsonFromInputStream(response.getEntity().getContent());
					if(jo.getString("error").equals("0"))return 0;
					else return 1;
				} catch (ClientProtocolException e) {
				
					Log.e("Client error", e.toString());
					return 2;
				} catch (IOException e) {
					
					Log.e("IO Error", e.toString());
					return 3;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					return 4;
					
				}

			}

	
}
