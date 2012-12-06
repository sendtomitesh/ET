package com.explorer.technologies;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSBroadcastReceiver extends BroadcastReceiver {
	private static DbHelper dbHelper;
	private static SQLiteDatabase db;
	private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	private static final String TAG = "SMSBroadcastReceiver";
	int count = 0;
	
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
					
					createNotification(context, messages[0].getOriginatingAddress(), messages[0].getMessageBody());
					//Toast.makeText(context,"Message recieved from: "
						//			+ messages[0].getOriginatingAddress(),
							//Toast.LENGTH_LONG).show();
					if (check) {
						String message = DatabaseFunctions.getAutoReplyMessage(context, messages[0].getOriginatingAddress(),db);
						
						SendMessage sendMessage = new SendMessage();
						sendMessage.execute(Utility.sender_id,messages[0].getOriginatingAddress(), message);
						
					}

				}
			}
		}
	}

	private void createNotification(Context context,String title,String message){
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
		.setSmallIcon(R.drawable.ic_launcher)
		.setContentTitle(title)
		.setContentText(message);
		
		Intent resultIntent = new Intent(context,Main.class);
		resultIntent.putExtra("notify", "notify");
		resultIntent.putExtra("to", title);
		resultIntent.putExtra("msg", message);
		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(Main.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
		    (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		mBuilder.setSound(uri);
		mBuilder.setAutoCancel(true);
		mBuilder.setNumber(count);
		mBuilder.setAutoCancel(true);
		
		// count allows you to update the notification later on.
		mNotificationManager.notify(count, mBuilder.build());
		
		
		count++;
	}
		
	public class SendMessage extends AsyncTask<String, Void, Integer> {
	
			@Override
			protected Integer doInBackground(String... args) {
	
				return APICalls.sendMsg(args[0], args[1],args[2],null);
	
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
