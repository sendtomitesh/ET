package com.explorer.technologies;


import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Sent extends Activity {

	SentListAdapter sentAdapter;
	ImageView imgTitle;
	TextView txtTitle;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inbox);
        imgTitle = (ImageView)findViewById(R.id.image_title);
        txtTitle = (TextView)findViewById(R.id.txt_title);
        imgTitle.setImageResource(R.drawable.sent);
        txtTitle.setText(getResources().getString(R.string.sent));
        loadInbox();
    }
    
    public void loadInbox()
	{
	    	final ListView listview = (ListView) findViewById(R.id.listview_inbox);
	    	
    	
	    	Uri uriSMSURI = Uri.parse("content://sms/sent");
	        final Cursor cursor = getContentResolver().query(uriSMSURI, null, null, null,null);
	        
	        startManagingCursor(cursor);
	        
	        String[] from = new String[]{"address","body","date"};
	        int[] to = new int[]{R.id.txt_from,R.id.txt_message,R.id.txt_date};
	        
	        sentAdapter = new SentListAdapter(this, R.layout.inbox_item, cursor, from, to);		
	        listview.setAdapter(sentAdapter);
	        listview.setClickable(true);
	    	listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

	    	  @Override
	    	  public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
	    	    
	    		  int pos = position+1;
	    		cursor.moveToPosition(pos);
	    		//Toast.makeText(getApplicationContext(), "You clicked at : " + pos, Toast.LENGTH_LONG).show();
	    		
	    	  }
	    	});

	  
	 }
    
    private int deleteAllmsg()
    {
    	Uri sentUri = Uri.parse("content://sms/sent");
    	int count = 0;
    	Cursor c = getContentResolver().query(sentUri , null, null, null, null);
    	while (c.moveToNext()) {
    	    try {
    	        // Delete the SMS
    	        String pid = c.getString(0); // Get id;
    	        String uri = "content://sms/" + pid;
    	        count = getContentResolver().delete(Uri.parse(uri),
    	                null, null);
    	    } catch (Exception e) {
    	    }
    	}
    	return count;
    }
    
    @SuppressWarnings("deprecation")
	@Override
    public void startManagingCursor(Cursor c) {
        if (Build.VERSION.SDK_INT < 11) {
            super.startManagingCursor(c);
        }
    }
    
}
