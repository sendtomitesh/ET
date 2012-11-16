package com.explorer.technologies;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	EditText et_Uname,et_Password,et_sender_id;
	String uname,pass,sender_id;
	
	SharedPreferences sp;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		getLayoutObjects();
		
	}
	

	private void getLayoutObjects() {
		// TODO Auto-generated method stub
		et_Uname = (EditText)findViewById(R.id.textbox_username);
		et_Password = (EditText)findViewById(R.id.textbox_password);
		et_sender_id =(EditText)findViewById(R.id.textbox_sender);
		
	}


	
	public void loginUser(View v) {

		
		uname=et_Uname.getText().toString();
		pass=et_Password.getText().toString();
		sender_id= et_sender_id.getText().toString();
		new loginAPI().execute();
		
	
	}
	public void register(View V)
	{
		Intent dashboardIntent = new Intent(getApplicationContext(), Register.class);
		startActivity(dashboardIntent);
		finish();
	}
	public void movetoDashboard()
	{
		Intent dashboardIntent = new Intent(getApplicationContext(), Main.class);
		startActivity(dashboardIntent);
		finish();
	}
	
	public class loginAPI extends AsyncTask<String, Integer, String>
	{
		ProgressDialog pd;
		int apiresult;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pd = new ProgressDialog(Login.this);
			pd.setTitle("Login");
			pd.setMessage("Authenticating...");
			pd.setIndeterminate(false);
			pd.show();
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			apiresult=APICalls.userLogin(uname,pass);
			return null;
		}
		@SuppressLint("WorldWriteableFiles")
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			pd.dismiss();
			if(apiresult==0)
			{
				sp= getSharedPreferences("credentials", MODE_WORLD_WRITEABLE);
				Utility.storeCredentialsInSharedPref(sp,uname, pass,sender_id);
				//Toast.makeText(getApplicationContext(), sender_id, Toast.LENGTH_LONG).show();
				movetoDashboard();
			}
			else if(apiresult==1)
			{
				Toast.makeText(getApplicationContext(), "Username or Password Incorrect", Toast.LENGTH_LONG).show();
			}
			else
				Toast.makeText(getApplicationContext(), "error occured", Toast.LENGTH_LONG).show();
			super.onPostExecute(result);
		}
	}

}
