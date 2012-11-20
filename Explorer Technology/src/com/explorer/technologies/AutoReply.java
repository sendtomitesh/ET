package com.explorer.technologies;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Toast;

public class AutoReply extends Activity {

	SentListAdapter sentAdapter;
	ImageView imgTitle;
	TextView txtTitle;
	Button btnNew,btnDelete;
	SQLiteDatabase db;
	DbHelper dbHelper;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inbox);
        initilizeGlobals();
    	showAutoReplyList();
        
    }
    
	@Override
	protected void onResume() {
		super.onResume();
		showAutoReplyList();
		
	}
    
    @Override
    protected void onStop() {
    	super.onStop();
    	if(db != null)
    	{
    		if(db.isOpen())
    		{
    			db.close();
    			dbHelper.close();
    		}
    	}
    }
    
    private void initilizeGlobals()
    {
    	imgTitle = (ImageView)findViewById(R.id.image_title);
        txtTitle = (TextView)findViewById(R.id.txt_title);
        imgTitle.setImageResource(R.drawable.autoreply);
        txtTitle.setText(getResources().getString(R.string.autoreply));
        btnNew =(Button)findViewById(R.id.btn_new);
        btnDelete =(Button)findViewById(R.id.btn_delete);
        btnNew.setVisibility(View.VISIBLE);
        btnDelete.setVisibility(View.VISIBLE);
    }
    
    public void addNew(View v)
    {
    	//Toast.makeText(getApplicationContext(), "Add New", Toast.LENGTH_LONG).show();
    	Intent newReplyIntent = new Intent(getApplicationContext(),AddAutoReply.class);
    	startActivity(newReplyIntent);
    	
    }
    
    public void deleteAll(View v)
    {
    	
    	DatabaseFunctions.deleteAllAutoReply(getApplicationContext());
    	showAutoReplyList();
    	Toast.makeText(getApplicationContext(), "All Records Deleted", Toast.LENGTH_LONG).show();
    }
    
    
    
    @SuppressWarnings("deprecation")
	private void showAutoReplyList()
    {
    	
    	String[] from = new String[] {"sms_to","message"};
    	int[] to = new int[] { R.id.txt_from,R.id.txt_message };
    	final ListView listview = (ListView) findViewById(R.id.listview_inbox);
    	    	
    	dbHelper = new DbHelper(getApplicationContext());
    	db = dbHelper.getReadableDatabase();
    	final Cursor cursor = DatabaseFunctions.getAutoReplyCursor(getApplicationContext(),db);
    	startManagingCursor(cursor);
    	
    	SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.inbox_item,cursor, from, to);
        listview.setAdapter(adapter);
    	
        listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
  		listview.setClickable(true);
    	listview.setOnItemClickListener(new OnItemClickListener() {
    		public void onItemClick(AdapterView<?> parent, View view,
    				int position, long id) {
    			
    			Cursor cur = ((SimpleCursorAdapter)listview.getAdapter()).getCursor();
    			cur.moveToPosition(position);
    			String to = cur.getString(1).toString();
    			String msg = cur.getString(2).toString();
    			Toast.makeText(getApplicationContext(), to + "\n" + msg,Toast.LENGTH_LONG).show();
    			
    			//moveToCompose(id,to,msg);    			
    			
    			
    		}

			
    	});
    	
    }
    private void moveToCompose(long id,String to,String msg) {
		// TODO Auto-generated method stub
    	Intent dashboardIntent = new Intent(getApplicationContext(), Compose.class);
    	dashboardIntent.putExtra("id", id);
    	dashboardIntent.putExtra("to", to);
    	dashboardIntent.putExtra("msg", msg);
		startActivity(dashboardIntent);
		finish();
    	
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
