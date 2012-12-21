package com.explorer.technologies;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSBroadcastReceiver extends BroadcastReceiver {
	
	private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	private static final String TAG = "SMSBroadcastReceiver";
	int count = 0;
	Context appContext;
	private ArrayList<AutoReplyData> autoReplyList;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "Intent recieved: " + intent.getAction());
		this.appContext = context;
		if (intent.getAction().equals(SMS_RECEIVED)) {
			DatabaseFunctions.openDb(appContext);

			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				Object[] pdus = (Object[]) bundle.get("pdus");
				final SmsMessage[] messages = new SmsMessage[pdus.length];
				for (int i = 0; i < pdus.length; i++) {
					messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				}
				if (messages.length > -1) {
					
					Log.i(TAG,"Message recieved: " + messages[0].getMessageBody());
				
					// Add Notification Dialog
					movetoDialog(context, messages[0].getOriginatingAddress(), messages[0].getMessageBody());
					sendAutoReply(messages[0].getOriginatingAddress(), messages[0].getMessageBody());
					
				}
			}
		}
	}
		
	@SuppressWarnings("unused")
	public void sendAutoReply(String to,String message){
		
		Date date = new Date();
		autoReplyList = getAutoReplyList();
		
		for (int i = 0; i < autoReplyList.size(); i++) {
			AutoReplyData data = new AutoReplyData();
			data = autoReplyList.get(i);
			if(checkKeyword(data.keyword,message)){
				//Toast.makeText(appContext,"Id : " + data.id + "\nKeyword : " + data.keyword + "\n" + data.message, Toast.LENGTH_SHORT).show();
				new SendMessage().execute(Utility.sender_id,to,data.message);
				boolean check = DatabaseFunctions.addAutoReplyDetail(data.id,to,date.toString());
				
			}
		}
		
	}
	public void movetoDialog(Context context,String title,String message)
	{
		
		Intent resultIntent = new Intent(context,SMSDialog.class);
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		resultIntent.putExtra("notify", "notify");
		resultIntent.putExtra("to", title);
		resultIntent.putExtra("msg", message);
		context.startActivity(resultIntent);
		
	}

	private ArrayList<AutoReplyData> getAutoReplyList(){
		
		Cursor cursor = DatabaseFunctions.getAutoReplyCursor();
		ArrayList<AutoReplyData> replyList = new ArrayList<AutoReplyData>();
		
		while (cursor.moveToNext()) {
			AutoReplyData data = new AutoReplyData(cursor.getString(0),cursor.getString(1),cursor.getString(2));
			replyList.add(data);
		}
		if(!cursor.isClosed()){
			cursor.close();
		}
		return replyList;
	}
	
	private boolean checkKeyword(String keyword,String message){
		String msg = message.toLowerCase(Locale.getDefault());
		String key = keyword.toLowerCase(Locale.getDefault());
		return msg.contains(key);
	}
		
	public class SendMessage extends AsyncTask<String, Void, String> {
	
			String to = "";
			String message = "";
			@Override
			protected String doInBackground(String... args) {
				to = args[1];
				message = args[2];
				return APICalls.sendMsg(args[0], args[1],args[2],null);
	
			}
	
			@Override
			protected void onPostExecute(String result) {
	
				super.onPostExecute(result);
				
				Long milliseconds = System.currentTimeMillis();

				DatabaseFunctions.saveSentMessage(to,
						message,to,result,
						milliseconds.toString());
				
			}
	
	}
	
}
