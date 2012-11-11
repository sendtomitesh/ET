package com.explorer.technologies;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	EditText et_Uname,et_Password;
	String uname,pass;
	
	SharedPreferences sp;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		getLayoutObjects();
		checkForSharedPrefValues();
	}
	

	private void getLayoutObjects() {
		// TODO Auto-generated method stub
		et_Uname = (EditText)findViewById(R.id.textbox_username);
		et_Password = (EditText)findViewById(R.id.textbox_password);
		
	}


	private void checkForSharedPrefValues() {
		// TODO Auto-generated method stub
		sp = getPreferences(MODE_PRIVATE);
		String sp_username = sp.getString("username", "");
		String sp_pass = sp.getString("pass", "");
		if(!sp_username.equals("") && !sp_pass.equals(""))
		{
			uname=sp_username;
			pass=sp_pass;
			new loginAPI().execute();
		}
	}


	public void loginUser(View v) {

		
		uname=et_Uname.getText().toString();
		pass=et_Password.getText().toString();
		
		new loginAPI().execute();
		
	
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
			apiresult=APICalls.userLogin(uname ,pass );
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			pd.dismiss();
			if(apiresult==0)
			{
				Editor editor = sp.edit();
				editor.putString("username", uname);
				editor.putString("pass", pass);
				editor.commit();
				movetoDashboard();
			}
			else
				Toast.makeText(getApplicationContext(), "error occured", Toast.LENGTH_LONG).show();
			super.onPostExecute(result);
		}
	}

}
