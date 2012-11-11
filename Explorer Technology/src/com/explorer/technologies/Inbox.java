package com.explorer.technologies;


import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class Inbox extends Activity {

	InboxListAdapter inboxAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inbox);
        loadInbox();
    }
    
    public void loadInbox()
	{
	    	final ListView listview = (ListView) findViewById(R.id.listview_inbox);
	    	
    	
	    	Uri uriSMSURI = Uri.parse("content://sms/inbox");
	        final Cursor cursor = getContentResolver().query(uriSMSURI, null, null, null,null);
	        
	        startManagingCursor(cursor);
	        
	        String[] from = new String[]{"address","body"};
	        int[] to = new int[]{R.id.txt_from,R.id.txt_message};
	        
	        inboxAdapter = new InboxListAdapter(this, R.layout.inbox_item, cursor, from, to);		
	        listview.setAdapter(inboxAdapter);
	        listview.setClickable(true);
	    	listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

	    	  @Override
	    	  public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
	    	    
	    		  int pos = position+1;
	    		cursor.moveToPosition(pos);
	    		Toast.makeText(getApplicationContext(), "You clicked at : " + pos, Toast.LENGTH_LONG).show();
	    		
	    	  }
	    	});

	  
	 }
    
    @SuppressWarnings("deprecation")
	@Override
    public void startManagingCursor(Cursor c) {
        if (Build.VERSION.SDK_INT < 11) {
            super.startManagingCursor(c);
        }
    }
    
}
