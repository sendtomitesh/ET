package com.explorer.technologies;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class InboxListAdapter extends SimpleCursorAdapter {

	private Cursor dataCursor;
	private Context contextLocal;
	private LayoutInflater mInflater;
	
	
	@SuppressWarnings("deprecation")
	public InboxListAdapter(Context context, int layout, Cursor dataCursor, String[] from,
	        int[] to) {
	    super(context, layout, dataCursor, from, to);
	        this.dataCursor = dataCursor;
	        contextLocal =  context;
	        mInflater = LayoutInflater.from(context);
	       
	        //initial fill
	       
	}

	public View getView(int position, View convertView, ViewGroup parent) {

	    ViewHolder holder;
	    
	    if (convertView == null) {
	        convertView = mInflater.inflate(R.layout.inbox_item, null);

	        holder = new ViewHolder();
	        holder.txt_from = (TextView) convertView.findViewById(R.id.txt_from);
	        holder.txt_from_name = (TextView) convertView.findViewById(R.id.txt_from_name);
	        holder.txt_message = (TextView) convertView.findViewById(R.id.txt_message);
	        holder.txt_date = (TextView) convertView.findViewById(R.id.txt_date);
	        
	        convertView.setTag(holder);
	    } else {

	    	holder = (ViewHolder) convertView.getTag();
	    }

	    dataCursor.moveToPosition(position);
	    int from_index = dataCursor.getColumnIndex("address"); 
	    String label_from = dataCursor.getString(from_index);
	    
	    int message_index = dataCursor.getColumnIndex("body"); 
	    String label_message = dataCursor.getString(message_index);
	    
	    int date_index = dataCursor.getColumnIndex("date"); 
	    String label_date = dataCursor.getString(date_index);
	    
	    
	    String date = ConvertToDate(label_date);
	    
	    //String contactName = getContactNameFromNumber(label_from);
	    //if(contactName == ""){
	    	holder.txt_from_name.setText(label_from); 
	    //}
	    //else{
	    	//holder.txt_from_name.setText(contactName);
	    //}
	    holder.txt_from.setText(label_from);
	    holder.txt_message.setText(label_message);
	    holder.txt_date.setText(date.toString());
	    
	    return convertView;
	}

	@SuppressWarnings("unused")
	private String getContactNameFromNumber(String phoneNumber)
	{
		String name = "";
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
		//return resolver.query(uri, new String[]{PhoneLookup.DISPLAY_NAME});
		Cursor cursor = null;
		try {

			cursor = contextLocal.getContentResolver().query(uri,new String[]{PhoneLookup.DISPLAY_NAME},null,null,null);
			 name = cursor.getString(1);
			 if(name.length() > 0){
				 return name;
			 }
			 else{
				 name = phoneNumber;
				 return name;
			 }
			 
			
		} catch (Exception e) {
			Log.e("CONTACT NAME", "error : " + e.toString());
			return null;
		}

		
	}
	@SuppressLint("SimpleDateFormat")
	private String ConvertToDate(String dateString){
	
		long timestamp = Long.parseLong(dateString);    
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd hh:mm a");
		return dateFormat.format(calendar.getTime());
	
	}
	
	public class ViewHolder {
	
		TextView txt_from,txt_message,txt_date,txt_from_name;
	   
	}
}