package com.explorer.technologies;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SMSDialog extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nitification_dialog);
		
        checkNotification();
		
        
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		super.onBackPressed();
	}
	private void checkNotification(){
    	final Intent intent = getIntent();
    	String from = "";
    	String message = "";
    	if (intent.hasExtra("msg")) {
				message = intent.getStringExtra("msg").toString();
		}
    	if (intent.hasExtra("to")) {
			from = intent.getStringExtra("to").toString();
    	}
    	final Dialog NOTIFICATION_DIALOG = new Dialog(SMSDialog.this,R.style.DialogWindowTitle);
    	NOTIFICATION_DIALOG.setContentView(R.layout.nitification_dialog);

    	final TextView textFrom = (TextView) NOTIFICATION_DIALOG.findViewById(R.id.txt_title);
    	final TextView textMessage = (TextView) NOTIFICATION_DIALOG.findViewById(R.id.txt_message);
		final Button btnReply = (Button) NOTIFICATION_DIALOG
				.findViewById(R.id.btn_reply);
		final Button btnForward = (Button) NOTIFICATION_DIALOG
				.findViewById(R.id.btn_forward);
		
		textFrom.setText("From : " + from );
		
		textMessage.setText(message);
		
		
		btnReply.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent composeIntent = new Intent(getApplicationContext(),Compose.class);
				composeIntent.putExtra("to", intent.getStringExtra("to"));
		    	startActivity(composeIntent);
				NOTIFICATION_DIALOG.dismiss();
				finish();
			}
		});
		
		btnForward.setOnClickListener(new OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				Intent composeIntent = new Intent(getApplicationContext(),Compose.class);
				composeIntent.putExtra("msg", intent.getStringExtra("msg"));
		    	startActivity(composeIntent);
				NOTIFICATION_DIALOG.dismiss();
				finish();
			}
		});
		
		NOTIFICATION_DIALOG.show();
			
	}


}
