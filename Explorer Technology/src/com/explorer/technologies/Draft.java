package com.explorer.technologies;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class Draft extends Activity {

	SentListAdapter sentAdapter;
	ImageView imgTitle;
	Button btnDelete;
	TextView txtTitle;
	//SQLiteDatabase db;
	//DbHelper dbHelper;
	Cursor cursor;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inbox);
        initilizeGlobals();
        showDrafts();
        
    }
    
    
    @Override
    protected void onStop() {
    	if(cursor != null)
    	{
    		cursor.close();
    	}
    	super.onStop();
    	
    }
    
    @Override
    public void onBackPressed() {
    	super.onBackPressed();
    	finish();
    }
    
    private void initilizeGlobals()
    {
    	imgTitle = (ImageView)findViewById(R.id.image_title);
        txtTitle = (TextView)findViewById(R.id.txt_title);
        imgTitle.setImageResource(R.drawable.draft);
        txtTitle.setText(getResources().getString(R.string.drafts));
        btnDelete =(Button)findViewById(R.id.btn_delete);
        btnDelete.setVisibility(View.VISIBLE);
    }
    
    public void deleteAll(View v)
    {
    	deleteAllDrafts();
    	showDrafts();
    }
    
    @SuppressWarnings("deprecation")
	public void showDrafts()
    {
    	
    	
    	String[] from = new String[] {"sms_to","message","sms_to_complete"};
    	int[] to = new int[] { R.id.txt_from_name,R.id.txt_message ,R.id.txt_from};
    	final ListView listview = (ListView) findViewById(R.id.listview_inbox);
    	    	
    	cursor = DatabaseFunctions.getDraftCursor();
    	
    	if(cursor != null){
    		startManagingCursor(cursor);
        	
        	SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.inbox_item,cursor, from, to);
            listview.setAdapter(adapter);
        	
            listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
      		listview.setClickable(true);
        	listview.setOnItemClickListener(new OnItemClickListener() {
        		public void onItemClick(AdapterView<?> parent, View view,
        				int position, long id) {
        			
        		//	Toast.makeText(getApplicationContext(), cursor.getString(2), Toast.LENGTH_LONG).show();
        			
        			
        			cursor.moveToPosition(position);
        			
        			
        			
        			AlertDialog multichoice;
    	    		multichoice=new AlertDialog.Builder(Draft.this)
    	    			
    	               .setTitle( "SMS Options" )
    	               .setItems(new String[]{"Send Now","Discard"}, new OnClickListener() {

    	                   @Override
    	                   public void onClick(DialogInterface dialog, int which) {
    	                	if(which==0){
    	                		String msg_Id = cursor.getString(0);
    	            			String to = cursor.getString(1);
    	            			String msg = cursor.getString(2);
    	            			String toComplete = cursor.getString(3);
    	                		moveToCompose(msg_Id,to,msg,toComplete);
    	                	}
    	                	else
    	                	{
    	                		discardCurrent(cursor.getString(0));
    	                	}
    	                   }
    	               })
    	               
    	             
    	               .create();
    	    			
    	    		multichoice.show();
     			
        		}

    			
        	});

    	}
    		
    	    	
    }
    private void discardCurrent(String msgId)
    {
    	DatabaseFunctions.deleteDraftMessage(msgId);
    	showDrafts();
    }
    private void moveToCompose(String id,String to,String msg,String toComplete) {
		// TODO Auto-generated method stub
    	Intent dashboardIntent = new Intent(getApplicationContext(), Compose.class);
    	dashboardIntent.putExtra("id", id);
    	dashboardIntent.putExtra("to", to);
    	dashboardIntent.putExtra("msg", msg);
    	dashboardIntent.putExtra("toComplete", toComplete);
		startActivity(dashboardIntent);
		finish();
    	
	}
    
    private void deleteAllDrafts()
    {
    	DatabaseFunctions.deleteAllDraftMessage();
    }
	  @SuppressWarnings("deprecation")
	  @Override
	    public void startManagingCursor(Cursor c) {

	        // To solve the following error for honeycomb:
	        // java.lang.RuntimeException: Unable to resume activity 
	        // java.lang.IllegalStateException: trying to requery an already closed cursor
	        if (Build.VERSION.SDK_INT < 11) {
	            super.startManagingCursor(c);
	        }
	    }       
}
