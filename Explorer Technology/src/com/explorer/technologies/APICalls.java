package com.explorer.technologies;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class APICalls {

	
	//returns 0 for success, 1 for username or password incorrect, 2 for protocol error, 3 for IO error and 4 for JSon parsing error
	public static int userLogin(String username, String password) 
	{
		
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();

		HttpPost httppost = new HttpPost(Utility.ServerPath);

		try {
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			
			nameValuePairs.add(new BasicNameValuePair("method", "credit_check"));//here is change
			nameValuePairs.add(new BasicNameValuePair("username", username));
			nameValuePairs.add(new BasicNameValuePair("password", password));
			nameValuePairs.add(new BasicNameValuePair("format", "json"));
			
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			// Execute HTTP Post Request
		
			HttpResponse response = httpclient.execute(httppost);
			JSONObject jo=Utility.getjsonFromInputStream(response.getEntity().getContent());
			if(jo.getString("error").equals("0")){
				
				JSONArray jsonArray = jo.getJSONArray("credits");
				JSONObject objCredits = jsonArray.getJSONObject(0);
				Utility.smsCredit = String.valueOf(objCredits.getDouble("credit"));
				return 0;
			}
			else{
				return 1;
			}
		} catch (ClientProtocolException e) {
		
			//Log.e("Client error", e.toString());
			return 2;
		} catch (IOException e) {
			
			//Log.e("IO Error", e.toString());
			return 3;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return 4;
			
		}

	}
	
	

		public static ArrayList<HashMap<String, String>> getGroups() 
		{
			ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();

			JSONObject jo=null;
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppost = new HttpPost(Utility.ServerPath);

			try {
					
				
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				
				nameValuePairs.add(new BasicNameValuePair("method", "show_groups"));//here is change
				nameValuePairs.add(new BasicNameValuePair("username",Utility.username));
				nameValuePairs.add(new BasicNameValuePair("password", Utility.password));
				nameValuePairs.add(new BasicNameValuePair("format", "json"));
				
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				
				// Execute HTTP Post Request
			
				HttpResponse response = httpclient.execute(httppost);
				jo=Utility.getjsonFromInputStream(response.getEntity().getContent());
				
				JSONArray GroupsList = jo.getJSONArray("data");
				for (int i = 0; i < GroupsList.length(); i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					JSONObject Groups = GroupsList.getJSONObject(i);
					map.put("id", Groups.getString("id"));
					map.put("name", Groups.getString("name"));
					mylist.add(map);

				}
			//	if(jo.getString("error").equals("0"))return jo;
			//	else return 1;
			} catch (ClientProtocolException e) {
			
				//Log.e("Client error", e.toString());
				//return 2;
			} catch (IOException e) {
				
				//Log.e("IO Error", e.toString());
				//return 3;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return mylist;
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
			
				//Log.e("Client error", e.toString());
				return 2;
			} catch (IOException e) {
				
				//Log.e("IO Error", e.toString());
				return 3;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				return 4;
				
			}

		}

		//returns 0 for success, 1 for error in registration1, 2 for protocol error, 3 for IO error and 4 for JSon parsing error
		public static String sendMsg(String sender,String to,String message,String sheduleDate) 
		{
					// Create a new HttpClient and Post Header
					HttpClient httpclient = new DefaultHttpClient();

					HttpPost httppost = new HttpPost(Utility.ServerPath);

					try {
						
						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
						
						nameValuePairs.add(new BasicNameValuePair("method", "compose"));
						
						nameValuePairs.add(new BasicNameValuePair("username",Utility.username));
						nameValuePairs.add(new BasicNameValuePair("password", Utility.password));
						nameValuePairs.add(new BasicNameValuePair("sender", sender));
						nameValuePairs.add(new BasicNameValuePair("to", to));
						nameValuePairs.add(new BasicNameValuePair("message", message));
						nameValuePairs.add(new BasicNameValuePair("international","1" ));
						nameValuePairs.add(new BasicNameValuePair("format", "json"));
						
						if(sheduleDate != null){
							nameValuePairs.add(new BasicNameValuePair("sendondate", sheduleDate));
						}
						
						
						httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
						
						// Execute HTTP Post Request
					
						HttpResponse response = httpclient.execute(httppost);
						JSONObject jo=Utility.getjsonFromInputStream(response.getEntity().getContent());
						return jo.getString("status");
						
					} catch (ClientProtocolException e) {
					
						//Log.e("Client error", e.toString());
						return "Client error";
					} catch (IOException e) {
						
						//Log.e("IO Error", e.toString());
						return "IO error";
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						return "JSON error";
						
					}

				}
		//returns 0 for success, 1 for error in registration1, 2 for protocol error, 3 for IO error and 4 for JSon parsing error
			public static String sendToGroup(String sender,String groupIds,String message) 
					{
						
						
						// Create a new HttpClient and Post Header
						HttpClient httpclient = new DefaultHttpClient();

						HttpPost httppost = new HttpPost(Utility.ServerPath);

						try {
							
							List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
							
							nameValuePairs.add(new BasicNameValuePair("method", "compose"));
							
							nameValuePairs.add(new BasicNameValuePair("username",Utility.username));
							nameValuePairs.add(new BasicNameValuePair("password", Utility.password));
							nameValuePairs.add(new BasicNameValuePair("sender", sender));
							nameValuePairs.add(new BasicNameValuePair("source", "group"));
							nameValuePairs.add(new BasicNameValuePair("groupid", groupIds));
							nameValuePairs.add(new BasicNameValuePair("message", message));
							nameValuePairs.add(new BasicNameValuePair("international","0" ));
							nameValuePairs.add(new BasicNameValuePair("format", "json"));
							
							
							httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
							
							// Execute HTTP Post Request
						
							HttpResponse response = httpclient.execute(httppost);
							JSONObject jo=Utility.getjsonFromInputStream(response.getEntity().getContent());
							return jo.getString("status");
							
						} catch (ClientProtocolException e) {
							
							//Log.e("Client error", e.toString());
							return "Client error";
						} catch (IOException e) {
							
							//Log.e("IO Error", e.toString());
							return "IO error";
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							return "JSON error";
							
						}


					}


	
}
