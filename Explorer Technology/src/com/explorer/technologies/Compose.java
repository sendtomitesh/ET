package com.explorer.technologies;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Compose extends Activity {

	//Spinner spinnerLevel;
	EditText textSender, textTo, textMessage;
	TextView txtViewCounter;
	Button btnSendMessage;
	//int level;
	int textCounter=0;
	ProgressDialog pd;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.compose);
		globalInitialize();
		
	}

	public void sendMessage(View v) {
		//String lev = Integer.toString(level);
		//String lev = Integer.toString(0);
		SendMessage message = new SendMessage();
		message.execute(Utility.username, Utility.password, textSender
				.getText().toString(), textTo.getText().toString(), textMessage
				.getText().toString());
	}

	public void globalInitialize() {
		//spinnerLevel = (Spinner) findViewById(R.id.spinner_level);
		
		textSender = (EditText) findViewById(R.id.txt_sender);
		textTo = (EditText) findViewById(R.id.txt_to);
		textMessage = (EditText) findViewById(R.id.txt_message);
		txtViewCounter = (TextView) findViewById(R.id.txtCounter);
		textMessage.addTextChangedListener(new TextWatcher() {

	          public void afterTextChanged(Editable s) {

	           
	          }

	          public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	          public void onTextChanged(CharSequence s, int start, int before, int count) 
	          {
	        	  textCounter=textCounter +count;
	        	  txtViewCounter.setText(""+textCounter);
	        	  
	          }
	       });
		btnSendMessage = (Button) findViewById(R.id.btn_send_message);
		textSender.setText(Utility.sender_id);
	}

	public void insertSentMessage() {
		ContentValues values = new ContentValues();
		values.put("address", textTo.getText().toString());
		values.put("body", textMessage.getText().toString());

		getApplicationContext().getContentResolver().insert(
				Uri.parse("content://sms/sent"), values);

	}

	public class SendMessage extends AsyncTask<String, Void, Integer> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(Compose.this);
			pd.setTitle("Sending Message...");
			pd.show();
			btnSendMessage.setText("Sending...");
		}

		@Override
		protected Integer doInBackground(String... args) {

			int status;
			// status = APICalls.
			status = APICalls.sendMsg(args[0], args[1], args[2], args[3],
					args[4]);
			return status;

		}

		@Override
		protected void onPostExecute(Integer result) {

			super.onPostExecute(result);
			try
			{
			pd.dismiss();
			}catch(Exception e)
			{}
			btnSendMessage.setText(getString(R.string.send_message));
			if (result == 0) {
				insertSentMessage();
				Toast.makeText(getApplicationContext(),
						"Message Sent Successfully!", Toast.LENGTH_LONG).show();
				
			} else {
				Toast.makeText(getApplicationContext(),
						"Error sending message", Toast.LENGTH_LONG).show();
			}

		}

	}

	public void getContacts(View v)
	{
		Toast.makeText(getApplicationContext(),"Get Contacts", Toast.LENGTH_LONG).show();
	}
	
	public void getCallLog(View v)
	{
		Toast.makeText(getApplicationContext(),"Get Call Log", Toast.LENGTH_LONG).show();
	}
	
	public void getGroups(View v)
	{
		Toast.makeText(getApplicationContext(),"Get Groups", Toast.LENGTH_LONG).show();
	}
	}
