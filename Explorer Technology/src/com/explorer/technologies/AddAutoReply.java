package com.explorer.technologies;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;



public class AddAutoReply extends Activity {

	SQLiteDatabase db;
	DbHelper dbHelper;
	private static final int CONTACT_PICKER_RESULT = 1001;
	
	EditText textTo, textMessage;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_auto_reply);
		globalInitialize();
		
	}

	@Override
    protected void onStop() {
    	super.onStop();
    	if(db != null)
    	{
    		if(db.isOpen())
    		{
    			db.close();
    			dbHelper.close();
    		}
    	}
    }
    
	public void createAutoReply(View v)
    {
		dbHelper = new DbHelper(getApplicationContext());
		db = dbHelper.getReadableDatabase();
		String to = textTo.getText().toString();
		String message = textMessage.getText().toString();
		
		if(to == null || message == null){
			Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_LONG).show();
		}
		else{
			Boolean checkAleradyExist;
			
			checkAleradyExist = DatabaseFunctions.checkAutoReply(AddAutoReply.this,to,db);
			//Toast.makeText(AddAutoReply.this, checkAleradyExist.toString(), Toast.LENGTH_LONG).show();
			if(!checkAleradyExist)
			{
				DatabaseFunctions.addAutoReply(getApplicationContext(),to, message);
				Toast.makeText(AddAutoReply.this, "Auto Reply Created Successfully", Toast.LENGTH_LONG).show();
				finish();
			}
			else{
				
				Toast.makeText(AddAutoReply.this, "Auto Reply already set for this number", Toast.LENGTH_LONG).show();
			}
			
		}
		
    }
	
	public void globalInitialize() {
		textTo = (EditText) findViewById(R.id.txt_to);
		textMessage = (EditText) findViewById(R.id.txt_message);
	}
	
	public void getContacts(View v)
	{
		Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,Contacts.CONTENT_URI);  
	    startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
		
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
                	
                	 textTo.setText(phonesList.get(0));
                	 
                	 
                 } else {

                     final String[] phonesArr = new String[phonesList
                             .size()];
                     for (int i = 0; i < phonesList.size(); i++) {
                         phonesArr[i] = phonesList.get(i);
                     }

                     AlertDialog.Builder dialog = new AlertDialog.Builder(AddAutoReply.this);
                     dialog.setTitle("Name : " + Name);
                     ((Builder) dialog).setItems(phonesArr,
                             new DialogInterface.OnClickListener() {
                                 public void onClick(
                                         DialogInterface dialog,
                                         int which) {
                                     String selectedEmail = phonesArr[which];
                                     textTo.setText(selectedEmail);
                                     
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
