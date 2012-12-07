package com.explorer.technologies;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class CallLogAdaptor extends SimpleCursorAdapter{
	private Cursor dataCursor;
	//private int selectedItem;
	private LayoutInflater mInflater;
	
	
	@SuppressWarnings("deprecation")
	public CallLogAdaptor(Context context, int layout, Cursor dataCursor, String[] from,
	        int[] to) {
	    super(context, layout, dataCursor, from, to);
	        this.dataCursor = dataCursor;
	        mInflater = LayoutInflater.from(context);
	       
	        //initial fill
	       
	}
	
	//public void setSelectedItem(int position) {
	  //  selectedItem = position;
	//}
	
	public View getView(int position, View convertView, ViewGroup parent) {


	    ViewHolder holder;
	   
	    
	    if (convertView == null) {
	        convertView = mInflater.inflate(R.layout.call_log_item, null);

	        holder = new ViewHolder();
	        
	        
	        holder.contactId = (TextView) convertView.findViewById(R.id.contact_id);
	        holder.contactName = (TextView) convertView.findViewById(R.id.contact_name);
	        holder.contactNumber = (TextView) convertView.findViewById(R.id.contact_number);
	        
	        convertView.setTag(holder);
	    } else {
	        holder = (ViewHolder) convertView.getTag();
	    }

	    dataCursor.moveToPosition(position);
	    
	    int Contact_index = dataCursor.getColumnIndex(android.provider.CallLog.Calls._ID); 
	    String label_Contact = dataCursor.getString(Contact_index);
	    
	    int ContactName_index = dataCursor.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME); 
	    String label_ContactName = dataCursor.getString(ContactName_index);
	    
	    int ContactNumber_index = dataCursor.getColumnIndex(android.provider.CallLog.Calls.NUMBER); 
	    String label_ContactNumber = dataCursor.getString(ContactNumber_index);
	   
	    
	    holder.contactId.setText(label_Contact);
	    if(label_ContactName == null)
	    {
	    	holder.contactName.setText("Unknown");
	    }
	    else
	    {
	    	holder.contactName.setText(label_ContactName);
	    }
	        
	    holder.contactNumber.setText(label_ContactNumber);
	    return convertView;
	}

	 public class ViewHolder {
	    
		 TextView contactId,contactName,contactNumber;
	    
	}
}
