package com.explorer.technologies;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class Main extends Activity {
	SQLiteDatabase db;
	DbHelper dbHelper;
	TextView textCredits;
	TextView textLink;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //Open the database
        DatabaseFunctions.openDb(getApplicationContext());
        globalInitialize();
        if(Utility.smsCredit == null){
        	textCredits.setText("Credit : 0" + Utility.smsCredit);
        }
        else{
        	textCredits.setText("Credit : " + Utility.smsCredit);
        }
        
        
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
    
    private void globalInitialize(){
    	 textCredits = (TextView)findViewById(R.id.txt_credits);
         textLink  = (TextView)findViewById(R.id.txt_link);
         textLink.setText(Html.fromHtml(
            "<a href=\"http://www.xwireless.net\">X-Wireless</a>"));
         textLink.setMovementMethod(LinkMovementMethod.getInstance());
         TextView textGetCredit=(TextView)findViewById(R.id.txtGetsmsCredit);
         textGetCredit.setText(Html.fromHtml(
            "<a href=\"http://xwireless.net/bulk_sms_how_to_pay.php\">Get Credit</a>"));
         textGetCredit.setMovementMethod(LinkMovementMethod.getInstance());
    }
   public void gotoInbox(View v)
    {
	   Intent inboxIntent = new Intent(getApplicationContext(),InboxActivity.class);
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
