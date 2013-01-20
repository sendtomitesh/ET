package com.explorer.technologies;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Login extends Activity {
	EditText et_Uname, et_Password, et_sender_id;
	Spinner countrySpinner;
	String uname, pass, sender_id,prefix;

	SharedPreferences sp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		getLayoutObjects();

	}

	private void getLayoutObjects() {
		// TODO Auto-generated method stub
		et_Uname = (EditText) findViewById(R.id.textbox_username);
		et_Password = (EditText) findViewById(R.id.textbox_password);
		et_sender_id = (EditText) findViewById(R.id.textbox_sender);
		countrySpinner = (Spinner) findViewById(R.id.spinner_country);
		loadSpinner();
	}

	private void loadSpinner() {


		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String countryCode = tm.getNetworkCountryIso();
		countryCode = countryCode.toUpperCase(Locale.getDefault());
		
		ArrayAdapter<Country> adapter = new ArrayAdapter<Country>(this,
				android.R.layout.simple_spinner_dropdown_item,
				Country.sCountryList);


		countrySpinner.setAdapter(adapter);
		int pos = 1;
		for(int i =0;i< Country.sCountryList.size();i++) {
		    if(Country.sCountryList.get(i).getCode().equals(countryCode)){
		          pos = i;
		          
		    }
		}
		countrySpinner.setSelection(pos);
		countrySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {

				prefix= Country.sCountryList.get(position).getPrefix();

			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}

		});

	}

	public void loginUser(View v) {

		uname = et_Uname.getText().toString();
		pass = et_Password.getText().toString();
		sender_id = et_sender_id.getText().toString();
		if (Utility.hasConnection(getApplicationContext())) {
			new LoginApi().execute();
		} else {
			Toast.makeText(getApplicationContext(),
					"No internet Connection Found!", Toast.LENGTH_LONG).show();
		}

	}

	public void register(View V) {
		Uri uri = Uri.parse("http://www.xwireless.net/register.php");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);

		/*
		 * Intent dashboardIntent = new Intent(getApplicationContext(),
		 * Register.class); startActivity(dashboardIntent); finish();
		 */
	}

	public void movetoDashboard() {
		Intent dashboardIntent = new Intent(getApplicationContext(), Main.class);
		startActivity(dashboardIntent);
		finish();
	}

	public class LoginApi extends AsyncTask<String, Integer, String> {
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
			apiresult = APICalls.userLogin(uname, pass);
			return null;
		}

		@SuppressWarnings("deprecation")
		@SuppressLint("WorldWriteableFiles")
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			pd.dismiss();
			if (apiresult == 0) {
				sp = getSharedPreferences("credentials", MODE_WORLD_WRITEABLE);
				Utility.storeCredentialsInSharedPref(sp, uname, pass, sender_id,prefix);
				// Toast.makeText(getApplicationContext(), sender_id,
				// Toast.LENGTH_LONG).show();
				movetoDashboard();
			} else if (apiresult == 1) {
				Toast.makeText(getApplicationContext(),
						"Username or Password Incorrect", Toast.LENGTH_LONG)
						.show();
			} else {
				Toast.makeText(getApplicationContext(), "error occured",
						Toast.LENGTH_LONG).show();
			}
			super.onPostExecute(result);
		}
	}

}
