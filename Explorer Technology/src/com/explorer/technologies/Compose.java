package com.explorer.technologies;

import java.util.ArrayList;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Compose extends Activity {

	private static final int CONTACT_PICKER_RESULT = 1001;
	//Spinner spinnerLevel;
	EditText textSender, textTo, textMessage;
	TextView txtViewCounter;
	Button btnSendMessage;
	//int level;
	int textCounter=0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.compose);
		globalInitialize();
		//loadLevel();
	}

	public void sendMessage(View v) {
		//String lev = Integer.toString(level);
		String lev = Integer.toString(0);
		SendMessage message = new SendMessage();
		message.execute(Utility.username, Utility.password, textSender
				.getText().toString(), textTo.getText().toString(), textMessage
				.getText().toString(), lev);
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
			btnSendMessage.setText(getString(R.string.send_message));
			if (result == 0) {
				insertSentMessage();
				Toast.makeText(getApplicationContext(), "Message sent!",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(),
						"Error sending message", Toast.LENGTH_LONG).show();
			}

		}

	}

	public void getContacts(View v)
	{
		Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,Contacts.CONTENT_URI);  
	    startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
		Toast.makeText(getApplicationContext(),"Get Contacts", Toast.LENGTH_LONG).show();
	}
	


	public void getCallLog(View v)
	{

		
		final Dialog selectContactDialog = new Dialog(Compose.this);
		selectContactDialog.setTitle("Select recent contact");
		selectContactDialog.setContentView(R.layout.call_log_dialog);
			    
			    final CallLogAdaptor cursorAdapter;
			    String[] from = { android.provider.CallLog.Calls._ID,android.provider.CallLog.Calls.NUMBER,
		                android.provider.CallLog.Calls.CACHED_NAME, };
				int[] to = new int[]{R.id.contact_id,R.id.contact_number,R.id.contact_name};
				ListView listview = (ListView) selectContactDialog.findViewById(R.id.list_call_log);
				
		        String order = android.provider.CallLog.Calls.DATE + " DESC";
		        final Cursor tempCursor = getContentResolver().query(android.provider.CallLog.Calls.CONTENT_URI, from,
		                null, null, order);
				
			
		            
			        
			        cursorAdapter = new CallLogAdaptor(this, R.layout.call_log_item, tempCursor, from, to);
			        
			        listview.setAdapter(cursorAdapter);
			    	listview.setItemsCanFocus(false);
			    	listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View view,	int position, long id) {
							tempCursor.moveToPosition(position);
							String number =tempCursor.getString(tempCursor.getColumnIndex(android.provider.CallLog.Calls.NUMBER)); 
			                Toast.makeText(Compose.this,number,Toast.LENGTH_LONG).show();
			                setToNumber(number);
			                tempCursor.close();
			                
			                selectContactDialog.dismiss();
						}
			    		
					});
			    	selectContactDialog.show();


		
		
	}
	
	public void getGroups(View v)
	{
		Toast.makeText(getApplicationContext(),"Get Groups", Toast.LENGTH_LONG).show();
	}
	
	public void setToNumber(String number)
	{
		if(textTo.getText().length() < 0)
   	 {
   		 textTo.setText(number + ",");
   	 }
   	 else
   	 {
   		 String beforeContacts = textTo.getText().toString();
   		 textTo.setText(beforeContacts + number+",");
   	 }
	}
	/**public void loadLevel() {

		spinnerLevel = (Spinner) findViewById(R.id.spinner_level);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter
				.createFromResource(this, R.array.level_array,
						android.R.layout.simple_spinner_item);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinnerLevel.setAdapter(adapter);
		spinnerLevel.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				level = position;

			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				level = 0;
			}

		});

	}**/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK)
        { 
             if (data != null) {
                 Uri contactData = data.getData();

                 try {

                     String id = contactData.getLastPathSegment();
                    String[] columns = {Phone.DATA,Phone.DISPLAY_NAME};
                     Cursor phoneCur = getContentResolver()
                             .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    columns ,
                                     ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                             + " = ?", new String[] { id },
                                     null);

                     final ArrayList<String> phonesList = new ArrayList<String>();
                     String Name = null ;
                     if(phoneCur.moveToFirst())
                     {
                         do{
                             Name = phoneCur.getString(phoneCur.getColumnIndex(Phone.DISPLAY_NAME));
                             String phone = phoneCur
                             .getString(phoneCur
                                     .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA));
                                 phonesList.add(phone);

                           }   while (phoneCur.moveToNext());

                     }


                     phoneCur.close();

                     if (phonesList.size() == 0) {
                         Toast.makeText(
                                 this,"This contact does not contain any number",
                                 Toast.LENGTH_LONG).show();
                     } else if (phonesList.size() == 1) {
                    	
                    	 setToNumber(phonesList.get(0));
                    	 
                     } else {

                         final String[] phonesArr = new String[phonesList
                                 .size()];
                         for (int i = 0; i < phonesList.size(); i++) {
                             phonesArr[i] = phonesList.get(i);
                         }

                         AlertDialog.Builder dialog = new AlertDialog.Builder(Compose.this);
                         dialog.setTitle("Name : " + Name);
                         ((Builder) dialog).setItems(phonesArr,
                                 new DialogInterface.OnClickListener() {
                                     public void onClick(
                                             DialogInterface dialog,
                                             int which) {
                                         String selectedEmail = phonesArr[which];
                                         setToNumber(selectedEmail);
                                     }
                                 }).create();
                         dialog.show();
                     }
                 } catch (Exception e) {
                     Log.e("FILES", "Failed to get phone data", e);
                 }
             }

        }  		
	}
}
