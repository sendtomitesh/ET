package com.explorer.technologies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SMSDialog extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification_dialog);
		
        checkNotification();
		
        
	}
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		checkNotification();
		super.onNewIntent(intent);
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
    	
    	TextView textFrom = (TextView)findViewById(R.id.txt_title);
    	TextView textMessage = (TextView)findViewById(R.id.txt_message);
		Button btnReply = (Button) findViewById(R.id.btn_reply);
		Button btnForward = (Button) findViewById(R.id.btn_forward);
		Button btnCancel = (Button) findViewById(R.id.btn_cancel);
		
		textFrom.setText("From : " + from );
		
		textMessage.setText(message);
		
		btnReply.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent composeIntent = new Intent(getApplicationContext(),Compose.class);
				composeIntent.putExtra("to", intent.getStringExtra("to"));
		    	startActivity(composeIntent);
				finish();
			}
		});
		
		btnForward.setOnClickListener(new OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				Intent composeIntent = new Intent(getApplicationContext(),Compose.class);
				composeIntent.putExtra("msg", intent.getStringExtra("msg"));
		    	startActivity(composeIntent);
				finish();
			}
		});
		
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
			
	}

}
