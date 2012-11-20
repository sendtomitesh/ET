package com.explorer.technologies;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
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
	        
	        final String[] from = new String[]{"address","body","date"};
	        int[] to = new int[]{R.id.txt_from,R.id.txt_message,R.id.txt_date};
	        
	        inboxAdapter = new InboxListAdapter(this, R.layout.inbox_item, cursor, from, to);		
	        listview.setAdapter(inboxAdapter);
	        listview.setClickable(true);
	    	listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

	    	  @Override
	    	  public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
	    	    
	    		final String optionArr[];
	    		cursor.moveToPosition(position);
	    		if(checkForValidNumber(cursor.getString(cursor.getColumnIndex("address"))))
	    		{
	    			optionArr=new String[]{"Reply", "Forward","Delete"};
	    		}
	    		else
	    			optionArr=new String[]{"Delete"};
	    		AlertDialog multichoice;
	    		multichoice=new AlertDialog.Builder(Inbox.this)
	    			
	               .setTitle( "SMS Options" )
	               .setItems(optionArr, new OnClickListener() {

	                   @Override
	                   public void onClick(DialogInterface dialog, int which) {
	                	   if(optionArr.length==1)
	                	   {
	                		   deleteMsg(cursor.getString(1));
	                	   }
	                	   else
	                	       moveToCompose(which,cursor.getString(cursor.getColumnIndex("address")),cursor.getString(cursor.getColumnIndex("body")));
	                   }
	               })
	               
	             
	               .create();
	    			
	    		multichoice.show();
	    	  }
	    	});

	  
	 }
    public boolean checkForValidNumber(String n)
    {
    	if(Utility.isLatinLetter(n.charAt(0)) || n.length()<10)
    		return false;
    	else
    		return true;
    }
    public void deleteMsg(String pid)
    {
    	String uri = "content://sms/conversations/" + pid;
        getContentResolver().delete(Uri.parse(uri), null, null);
        loadInbox();
    }
    public void moveToCompose(int which,String to, String msg)
    {
    	
    	Intent composeIntent = new Intent(getApplicationContext(), Compose.class);
    	//reply
    	if(which==0)
    	{
    		
        	
        	composeIntent.putExtra("to", to);
        	
    		
    	}
    	//forward
    	else
    	{
    		composeIntent.putExtra("msg", msg);
    	}
    	startActivity(composeIntent);
		finish();
    }
    @SuppressWarnings("deprecation")
	@Override
    public void startManagingCursor(Cursor c) {
        if (Build.VERSION.SDK_INT < 11) {
            super.startManagingCursor(c);
        }
    }
    
}
