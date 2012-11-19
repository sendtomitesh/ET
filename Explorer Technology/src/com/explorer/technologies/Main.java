package com.explorer.technologies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class Main extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
}
