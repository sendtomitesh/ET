package com.explorer.technologies;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;



public class Compose extends Activity {

	private static final int CONTACT_PICKER_RESULT = 1001;
	
	EditText textSender, textTo, textMessage;
	TextView txtViewCounter;
	Button btnSendMessage;
	ProgressDialog pd;
	int textCounter=0;
	String  groupIds="";
	ArrayList<HashMap<String, String>> groupList;
	Boolean isGroupListLoaded=false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.compose);
		globalInitialize();
		textSender.setText(Utility.sender_id);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//Toast.makeText(getApplicationContext(), textMessage.getText().toString(), Toast.LENGTH_LONG).show();
		if (!textTo.getText().toString().equals("") || !textMessage.getText().toString().equals(""))
		{
			AlertDialog.Builder ab = new AlertDialog.Builder(Compose.this);
	        ab.setMessage("Save it to drafts?").setPositiveButton("Yes", dialogClickListener)
	        .setNegativeButton("No", dialogClickListener).show();
		}
		else
			super.onBackPressed();
	}
	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
	        switch (which){
	        case DialogInterface.BUTTON_POSITIVE:
	            //Yes button clicked
	        	
	        	saveMsgToDrafts();
	        	moveToDashboard();
	            break;

	        case DialogInterface.BUTTON_NEGATIVE:
	            //No button clicked
	        	moveToDashboard();
	            break;
	        }
	    }

		
	};
	private void saveMsgToDrafts() {
		//Toast.makeText(getApplicationContext(), "Saving to Drafts", Toast.LENGTH_LONG).show();
		
		DatabaseFunctions.saveToDrafts(getApplicationContext(), textTo.getText().toString(), textMessage.getText().toString());
		
	}
	public void moveToDashboard()
	{
		Intent dashboardIntent = new Intent(getApplicationContext(), Main.class);
		startActivity(dashboardIntent);
		finish();
		
	}
	public void sendMessage(View v) {
	
		//Toast.makeText(getApplicationContext(), extractGroupId("my group(1234),sdfomthing"), Toast.LENGTH_LONG).show();
		
		String repairedNumbers="";
		String arr[]= textTo.getText().toString().split(",");
		
		for(int i=0;i<arr.length;i++)
		{
			
			
			if(Utility.isLatinLetter(arr[i].charAt(0)))
			{
			   groupIds=groupIds+","+extractGroupId(arr[i])+",";
			   continue;
			}
			
			arr[i]=repairPhoneNumber(arr[i]);
			
			if(i== arr.length-1)
				repairedNumbers=repairedNumbers+","+arr[i];
			else
				repairedNumbers= repairedNumbers+","+arr[i]+",";
		}
		
		//replaces ,, to , if exists
		repairedNumbers=manageComma(repairedNumbers);
		groupIds=manageComma(groupIds);
		
		
       if(repairedNumbers.equals("") && !groupIds.equals(""))
       {
    	  
    	   Toast.makeText(getApplicationContext(), groupIds, Toast.LENGTH_LONG).show();
    	 /*  new SendMessageToGroup().execute(Utility.username, Utility.password, textSender
					.getText().toString(), groupIds, textMessage
					.getText().toString());  */
       }
       else if(!repairedNumbers.equals(""))
    	   new SendMessage().execute(Utility.username, Utility.password, textSender
				.getText().toString(), repairedNumbers, textMessage
				.getText().toString());
		
		
		  
	}
	public String manageComma(String s)
	{
		if(s.length()>0)
		{
			s= s.replaceAll(",,",",");
			
			
			//removes last ,
			if(s.charAt(s.length()-1)==',')
				s= s.substring(0,s.length()-1);
			
			//remove first ,
			if(s.charAt(0)==',')
				s= s.substring(1,s.length());
		}
		return s;
	}
	
	
	public String extractGroupId(String combinedString)
	{
		String gId="";
		
		int start= combinedString.indexOf("(");
		int end=combinedString.indexOf(")");
		
		gId=combinedString.substring(start+1, end);
		
		return gId;
		
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
			pd = new ProgressDialog(Compose.this);
			pd.setTitle("Sending sms..");
			pd.show();
			
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
			
			
			if (result == 0) {
				insertSentMessage();
				Toast.makeText(getApplicationContext(), "Message sent successfully ",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(),
						"Error sending message ", Toast.LENGTH_LONG).show();
			}
			
			try
			{
				pd.dismiss();
			}catch(Exception e){}
			
			//if groups exists
			if(groupIds!="")
			{
				new SendMessageToGroup().execute(Utility.username, Utility.password, textSender
						.getText().toString(), groupIds, textMessage
						.getText().toString());
				
			}

		}

	}

public class SendMessageToGroup extends AsyncTask<String, Void, Integer> {

		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(Compose.this);
			pd.setTitle("Sending sms to group..");
			pd.show();
			
		}

		@Override
		protected Integer doInBackground(String... args) {

			int status;
			// status = APICalls.
			
			
			status = APICalls.sendToGroup(args[0], args[1], args[2], args[3],
					args[4]);
			return status;

		}

		@Override
		protected void onPostExecute(Integer result) {

			super.onPostExecute(result);
			
			
			if (result == 0) {
				insertSentMessage();
				Toast.makeText(getApplicationContext(), "Message sent successfully ",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(),
						"Error sending message ", Toast.LENGTH_LONG).show();
			}
			
			groupIds="";
			try
			{
				pd.dismiss();
			}catch(Exception e){}
			
			


		}

	}
	
	public class ShowGroups extends AsyncTask<String, Void, ArrayList<HashMap<String, String>>> {

		
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(Compose.this);
			pd.setTitle("Groups");
			pd.setMessage("Loading Groups..");
			pd.show();
			
		}

		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(String... args) {
			return APICalls.getGroups(args[0],args[1]);
			

		}

		@Override
		protected void onPostExecute(ArrayList<HashMap<String, String>> mylist) {

			groupList=mylist;
			isGroupListLoaded=true;
			showinList();
			super.onPostExecute(mylist);
			
			try
			{
				pd.dismiss();
			}catch(Exception e){}
		}

	}

	public void getContacts(View v)
	{
		Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,Contacts.CONTENT_URI);  
	    startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
		
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
			              //  Toast.makeText(Compose.this,number,Toast.LENGTH_LONG).show();
			                setToNumber(number);
			                tempCursor.close();
			                
			                selectContactDialog.dismiss();
						}
			    		
					});
			    	selectContactDialog.show();


	
	}
	
	public void getGroups(View v)
	{
		if(!isGroupListLoaded)
			new ShowGroups().execute("mobiled", "1234");
		else
			showinList();
			
		
	}
	
	public void showinList()
	{
		final Dialog groupsDialog = new Dialog(Compose.this);
		groupsDialog.setTitle("Select Group");
		groupsDialog.setContentView(R.layout.call_log_dialog);
		
		String[] from = new String[] {"id","name"};
		int[] to = new int[] { R.id.contact_id,R.id.contact_name };
		final ListView listview = (ListView) groupsDialog.findViewById(R.id.list_call_log);

					
		ListAdapter adapter = new SimpleAdapter(this, groupList,R.layout.call_log_item, from, to);

		
				
		
		listview.setAdapter(adapter);
	
		listview.setTextFilterEnabled(true);
		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				@SuppressWarnings("unchecked")
				HashMap<String, String> o = (HashMap<String, String>) listview.getItemAtPosition(position);

				String name = o.get("name")+"("+o.get("id")+")";
			
				setToNumber(name);
				groupsDialog.dismiss();

			}
		});
		groupsDialog.show();

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
		
		
		public String repairPhoneNumber(String num)
		{
			String repairedNumber=num;
			
			//get country code
			TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
			String countryCode = tm.getNetworkCountryIso();
			
			//get phone code from country code
			countryCode=Iso2Phone.getPhone(countryCode);
			
			
			//remove 0 if its a first digit
			if(num.startsWith("0"))
				num=num.substring(1);
			
			else if(num.length()==10)  //valid number in india
			{
				repairedNumber=countryCode+num;
			}
			else if(num.length()==11)  //valid number in nigeria
			{
				repairedNumber=countryCode+num;
			}
			
			//if number starts from + means it has code attached, nothing to do
			if(repairedNumber.startsWith("+"))
			{
				repairedNumber=repairedNumber.substring(1);
				
			}
			return repairedNumber;
		}
}
