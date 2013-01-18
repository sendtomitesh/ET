package com.explorer.technologies;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class InboxActivity extends Activity {
	Button btnDelete;
	private InboxAdapter adapter;
	private ListView listview;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inbox);
        btnDelete =(Button)findViewById(R.id.btn_delete);
        btnDelete.setVisibility(View.VISIBLE);
        initializeInbox();

    }
    
    
    private void initializeInbox(){
    	new AsyncTask<String, Integer, String>() {
    		ProgressDialog progressDialog;
    		protected void onPreExecute() {
    			progressDialog = new ProgressDialog(InboxActivity.this);
    			progressDialog.setMessage("Loading inbox...");
    			progressDialog.show();
    		};
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
		    	Inbox.setInbox(InboxActivity.this);
		    	return null;
			}
			protected void onPostExecute(String result) {
				try {
					loadInbox();
					progressDialog.dismiss();
				} catch (Exception e) {
					// TODO: handle exception
				}
			};
		}.execute();

    } 
    public void deleteAll(View v)
    {
    	deleteAllmsg();
    	Inbox.deleteAllMsg();
    	adapter.notifyDataSetChanged();
    	//loadInbox();
    }
    public void loadInbox()
    {
    	
    	
	   	    listview = (ListView) findViewById(R.id.listview_inbox);
	   		adapter = new InboxAdapter(InboxActivity.this,Inbox.sInboxList);
	   		listview.setAdapter(adapter);
	        listview.setClickable(true);
	    	listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

	    	  @Override
	    	  public void onItemClick(AdapterView<?> arg0, View v, final int position, long arg3) {
	    	
	    		  
	    		final String optionArr[];
	    		//cursor.moveToPosition(position);
	    		optionArr=new String[]{"Reply", "Forward","Delete"};
	    		
	    		AlertDialog multichoice;
	    		multichoice=new AlertDialog.Builder(InboxActivity.this)
	    			
	               .setTitle( "SMS Options" )
	               .setItems(optionArr, new OnClickListener() {

	                   @Override
	                   public void onClick(DialogInterface dialog, int which) {
	                	      if(which==2){
	                			   deleteMsg(Inbox.sInboxList.get(position).getId(),position);
	                			   
	                	       }
	                		   else{
	                			  moveToCompose(which,Inbox.sInboxList.get(position).getNumber(),Inbox.sInboxList.get(position).getMessage()); 
	                		   }
	                	   
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
    public void deleteMsg(final String pid,final int id)
    {
    	new AsyncTask<String, Integer, String>() {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				String uri = "content://sms/" + pid;
		        getContentResolver().delete(Uri.parse(uri), null, null);
		        Inbox.deleteMsg(id);
				return null;
			}
			protected void onPostExecute(String result) {
				adapter.notifyDataSetChanged();
			};
		}.execute();
    	
        
        
        //loadInbox();
    }
    private int deleteAllmsg()
    {
    	Uri inboxUri = Uri.parse("content://sms/inbox");
    	int count = 0;
    	Cursor c = getContentResolver().query(inboxUri , null, null, null, null);
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
    @SuppressWarnings("deprecation")
	@Override
    public void startManagingCursor(Cursor c) {
        if (Build.VERSION.SDK_INT < 11) {
            super.startManagingCursor(c);
        }
    }
    
}
