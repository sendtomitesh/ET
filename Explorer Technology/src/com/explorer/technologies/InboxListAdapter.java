package com.explorer.technologies;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
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
	    
	    
	    //Date date = ConvertToDate(label_date);
	    String date = ConvertToDate(label_date);
	    
	    String contactName = Utility.getContactName(label_from, contextLocal);
	    if(contactName == ""){
	    	holder.txt_from_name.setText(label_from); 
	    }
	    else{
	    	holder.txt_from_name.setText(contactName);
	    }
	    holder.txt_from.setText(label_from);
	    holder.txt_message.setText(label_message);
	    holder.txt_date.setText(date.toString());
	    
	    return convertView;
	}

	
	private String ConvertToDate(String dateString){
		
		Long timestamp = Long.parseLong(dateString);    
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		Date finaldate = calendar.getTime();
		String smsDate = finaldate.toString();
		SimpleDateFormat dateFormat = new SimpleDateFormat("E MM hh:mm a");
	    Date convertedDate = new Date();
	    try {
	    	
	        convertedDate = dateFormat.parse(smsDate);
	    } catch (ParseException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	    
		DateFormat df = new SimpleDateFormat("E MM hh:mm a");
	   return  df.format(convertedDate);
		
	}
	
	public class ViewHolder {
	
		TextView txt_from,txt_message,txt_date,txt_from_name;
	   
	}
}