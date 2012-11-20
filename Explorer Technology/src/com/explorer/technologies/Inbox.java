package com.explorer.technologies;


import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class Inbox extends Activity {
	Button btnDelete;
	InboxListAdapter inboxAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inbox);
        btnDelete =(Button)findViewById(R.id.btn_delete);
        btnDelete.setVisibility(View.VISIBLE);
        loadInbox();
    }
    
    public void deleteAll(View v)
    {
    	Toast.makeText(getApplicationContext(), "Delete All", Toast.LENGTH_LONG).show();
    }
    public void loadInbox()
	{
	    	final ListView listview = (ListView) findViewById(R.id.listview_inbox);
	    	
    	
	    	Uri uriSMSURI = Uri.parse("content://sms/inbox");
	        final Cursor cursor = getContentResolver().query(uriSMSURI, null, null, null,null);
	        
	        startManagingCursor(cursor);
	        
	        String[] from = new String[]{"address","body","date"};
	        int[] to = new int[]{R.id.txt_from,R.id.txt_message,R.id.txt_date};
	        
	        inboxAdapter = new InboxListAdapter(this, R.layout.inbox_item, cursor, from, to);		
	        listview.setAdapter(inboxAdapter);
	        listview.setClickable(true);
	    	listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

	    	  @Override
	    	  public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
	    	    
	    		  int pos = position+1;
	    		cursor.moveToPosition(pos);
	    		
	    		
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
