package com.explorer.technologies;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SMSBroadcastReceiver extends BroadcastReceiver {
	private static DbHelper dbHelper;
	private static SQLiteDatabase db;
	private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	private static final String TAG = "SMSBroadcastReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "Intent recieved: " + intent.getAction());

		if (intent.getAction().equals(SMS_RECEIVED)) {

			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				Object[] pdus = (Object[]) bundle.get("pdus");
				final SmsMessage[] messages = new SmsMessage[pdus.length];
				for (int i = 0; i < pdus.length; i++) {
					messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				}
				if (messages.length > -1) {
					dbHelper = new DbHelper(context);
					db = dbHelper.getReadableDatabase();
					Boolean check = DatabaseFunctions.checkAutoReply(context,
							messages[0].getOriginatingAddress(), db);
					Log.i(TAG,"Message recieved: " + messages[0].getMessageBody());
					// Add Notification here and remove toast
					Toast.makeText(
							context,
							"Message recieved from: "
									+ messages[0].getOriginatingAddress(),
							Toast.LENGTH_LONG).show();
					if (check) {
						String message = DatabaseFunctions.getAutoReplyMessage(context, messages[0].getOriginatingAddress(),db);
						
						SendMessage sendMessage = new SendMessage();
						sendMessage.execute(Utility.sender_id,messages[0].getOriginatingAddress(), message);
						
					}

				}
			}
		}
	}

	public class SendMessage extends AsyncTask<String, Void, Integer> {
	
			@Override
			protected Integer doInBackground(String... args) {
	
				return APICalls.sendMsg(args[0], args[1],args[2]);
	
			}
	
			@Override
			protected void onPostExecute(Integer result) {
	
				super.onPostExecute(result);
				if(result == 0){
					Log.d("AUTO REPLY", "Message sent successfully!");
				}
				else{
					Log.d("AUTO REPLY", "Message not sent");
				}
				
				
			}
	
	}

}
