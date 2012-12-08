package com.explorer.technologies;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity {
	SQLiteDatabase db;
	DbHelper dbHelper;
	TextView textCredits;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //Open the database 
        DatabaseFunctions.openDb(getApplicationContext());
        
        textCredits = (TextView)findViewById(R.id.txt_credits);
        if(Utility.smsCredit == null){
        	textCredits.setText("Credit : 0" + Utility.smsCredit);
        }
        else{
        	textCredits.setText("Credit : " + Utility.smsCredit);
        }
        
        //checkNotification();
        Intent intent = getIntent();
        if (intent.hasExtra("notify")) {
			checkNotification();
		}
        //checkNotification();
    }
    
    @Override
	protected void onResume() {
    	textCredits.setText("Credit : " + Utility.smsCredit);
		super.onResume();
	   
	}
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	DatabaseFunctions.closeDb();
    	super.onDestroy();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
    	final Dialog NOTIFICATION_DIALOG = new Dialog(Main.this,R.style.DialogWindowTitle);
    	NOTIFICATION_DIALOG.setContentView(R.layout.nitification_dialog);

    	final TextView textFrom = (TextView) NOTIFICATION_DIALOG.findViewById(R.id.txt_title);
    	final TextView textMessage = (TextView) NOTIFICATION_DIALOG.findViewById(R.id.txt_message);
		final Button btnReply = (Button) NOTIFICATION_DIALOG
				.findViewById(R.id.btn_reply);
		final Button btnForward = (Button) NOTIFICATION_DIALOG
				.findViewById(R.id.btn_forward);
		
		textFrom.setText("From : " + from );
		
		textMessage.setText(message);
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
		
		btnReply.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent composeIntent = new Intent(getApplicationContext(),Compose.class);
				composeIntent.putExtra("to", intent.getStringExtra("to"));
		    	startActivity(composeIntent);
				NOTIFICATION_DIALOG.dismiss();
			}
		});
		
		btnForward.setOnClickListener(new OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				Intent composeIntent = new Intent(getApplicationContext(),Compose.class);
				composeIntent.putExtra("msg", intent.getStringExtra("msg"));
		    	startActivity(composeIntent);
				NOTIFICATION_DIALOG.dismiss();
			}
		});
		
		NOTIFICATION_DIALOG.show();
			
	}
    
    public void gotoInbox(View v)
    {
    	Intent inboxIntent = new Intent(getApplicationContext(),Inbox.class);
    	startActivity(inboxIntent);
    	
    	
    }
    
    public void gotoCompose(View v)
    {
    	Intent composeIntent = new Intent(getApplicationContext(),Compose.class);
    	startActivity(composeIntent);
    }
    public void gotoSent(View v)
    {
    	Intent sentIntent = new Intent(getApplicationContext(),Sent.class);
    	startActivity(sentIntent);
    }
    public void gotoDraft(View v)
    {
    	Intent draftIntent = new Intent(getApplicationContext(),Draft.class);
    	startActivity(draftIntent);
    }
    public void gotoGroups(View v)
    {
    	Intent groupsIntent = new Intent(getApplicationContext(),Groups.class);
    	startActivity(groupsIntent);
    }
    public void gotoAutoReply(View v)
    {
    	Intent autoReplyIntent = new Intent(getApplicationContext(),AutoReply.class);
    	startActivity(autoReplyIntent);
    }
    
  
}
