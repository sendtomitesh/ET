package com.explorer.technologies;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Sent extends Activity {

	SentListAdapter sentAdapter;
	ImageView imgTitle;
	Button btnDelete;
	TextView txtTitle;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inbox);
        initilizeGlobals();
        loadSentMessages();
    }
    
    private void initilizeGlobals()
    {
    	imgTitle = (ImageView)findViewById(R.id.image_title);
        txtTitle = (TextView)findViewById(R.id.txt_title);
        imgTitle.setImageResource(R.drawable.sent);
        txtTitle.setText(getResources().getString(R.string.sent));
        btnDelete =(Button)findViewById(R.id.btn_delete);
        btnDelete.setVisibility(View.VISIBLE);
    }
    
    public void deleteAll(View v)
    {
    	deleteAllmsg();
    	loadSentMessages();
    }
    
    public boolean checkForValidNumber(String n)
    {
    	if(Utility.isLatinLetter(n.charAt(0)) || n.length()<10)
    		return false;
    	else
    		return true;
    }
    public void moveToCompose(int which,String to, String msg)
    {
    	Intent composeIntent = new Intent(getApplicationContext(), Compose.class);
    	//reply
    	if(which==0){
    		composeIntent.putExtra("to", to);
        }
    	//forward
    	else{
    		composeIntent.putExtra("msg", msg);
    	}
    	startActivity(composeIntent);
		finish();
    }
    private void loadSentMessages()
	{
	    	final ListView listview = (ListView) findViewById(R.id.listview_inbox);
	    	
    	
	    	Uri uriSMSURI = Uri.parse("content://sms/sent");
	        final Cursor cursor = getContentResolver().query(uriSMSURI, null, null, null,null);
	        
	        startManagingCursor(cursor);
	        
	        String[] from = new String[]{"address","body","date"};
	        int[] to = new int[]{R.id.txt_from_name,R.id.txt_message,R.id.txt_date};
	        
	        sentAdapter = new SentListAdapter(this, R.layout.inbox_item, cursor, from, to);		
	        listview.setAdapter(sentAdapter);
	        listview.setClickable(true);
	    	listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

	    	  @Override
	    	  public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
	    	    
	    		//  int pos = position+1;
	    		cursor.moveToPosition(position);
	    		  
		    	
		    		
		    		
		    		AlertDialog multichoice;
		    		multichoice=new AlertDialog.Builder(Sent.this)
		    			
		               .setTitle( "SMS Options" )
		               .setItems(new String[]{"Delete"}, new OnClickListener() {

		                   @Override
		                   public void onClick(DialogInterface dialog, int which) {
		                	   //if(optionArr.length==1)
		                	   //{
		                	//   Toast.makeText(getApplicationContext(), "Deleting at : "  + cursor.getString(1),Toast.LENGTH_LONG).show();
		                		   deleteMsg(cursor.getString(0));
		                	   //}
		                	   
		                   }
		               })	             
		               .create();
		    			
		    		multichoice.show();
	    		//Toast.makeText(getApplicationContext(), "You clicked at : " + pos, Toast.LENGTH_LONG).show();
	    		
	    	  }
	    	});

	  
	}
    public void deleteMsg(String pid)
    {
    	String uri = "content://sms/" + pid;
        getContentResolver().delete(Uri.parse(uri),null, null);
    	loadSentMessages();
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
