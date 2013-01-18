package com.explorer.technologies;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Country {
	
	private String mId;
	private String mName;
	private String mPrefix;
	
	
	public static ArrayList<Country> sCountryList;
	
	public Country(String id,String name,String prefix){
		this.mId = id;
		this.mName = name;
		this.mPrefix = prefix;
		
	}
	
	public static void initalizeList() {
        sCountryList = new ArrayList<Country>();
    }
	
	//public getters
	public String getId() {
        return this.mId;
    }
	
	public String getName() {
        return this.mName;
    }
	
	public String getPrefix() {
        return this.mPrefix;
    }
	
	
	public static ArrayList<Country> getInboxList() {
        return sCountryList;
    }
	
	//public setters
	public void setId(String id) {
        this.mId = id;
    }
	
	public void setName(String name) {
        this.mName = name;
    }
	
	public void setPrefix(String prefix) {
        this.mPrefix = prefix;
    }
	
	public static ArrayList<String> getCountryNames(){
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < sCountryList.size(); i++) {
			list.add(sCountryList.get(i).getName());
		}
		return list;
	}
    public static boolean setCountries() {
    	
    	// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();

		HttpPost httppost = new HttpPost("http://xwireless.net/countries-json.php");

		try {
			
			
			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			JSONObject jo= Utility.getjsonFromInputStream(response.getEntity().getContent());
			
			
			JSONArray countryList = jo.getJSONArray("Countries");
			
			String id = "";
			String name = "";
			String prefix = "";
			for (int i = 0; i < countryList.length(); i++) {
				JSONObject country = countryList.getJSONObject(i);
				id =country.getString("id");
				name = country.getString("name");
				prefix = country.getString("prefix");
				Country myCountry = new Country(id, name, prefix);
				sCountryList.add(myCountry);

			}

			return true;
			
		} catch (ClientProtocolException e) {
			return false;
		} catch (IOException e) {
			return false;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return false;
			
		}

    }

}
