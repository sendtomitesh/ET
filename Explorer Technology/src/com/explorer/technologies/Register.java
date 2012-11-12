package com.explorer.technologies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;



public class Register extends Activity {

	ProgressDialog pd;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void on_register_click(View v)
    {
    	new registerAPI().execute();
    	//Toast.makeText(getApplicationContext(), "I m clicked", Toast.LENGTH_LONG).show();
    	
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_register, menu);
        return true;
    }
    
    
    public class registerAPI extends AsyncTask<String, Integer, String>
	{
		ProgressDialog pd;
		int apiresult;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pd = new ProgressDialog(Register.this);
			pd.setTitle("User Registration");
			pd.setMessage("Loading...");
			pd.setIndeterminate(false);
			pd.show();
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			apiresult=APICalls.userRegistration("mitesh", "welcome", "mitesh patel", "Udwada Gujarat India", "+919925724547");
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			pd.dismiss();
			if(apiresult==0)
			{
				
				Toast.makeText(getApplicationContext(), "User Registered Successfully", Toast.LENGTH_LONG).show();
			}
			else if(apiresult==1)
			{
				Toast.makeText(getApplicationContext(), "Given Data incorrect", Toast.LENGTH_LONG).show();
			}
			else
				Toast.makeText(getApplicationContext(), "error occured", Toast.LENGTH_LONG).show();
			super.onPostExecute(result);
		}
	}

}
