package com.explorer.technologies;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class SentListAdapter extends SimpleCursorAdapter {

	private Cursor dataCursor;
	//private Context contextLocal;
	private LayoutInflater mInflater;
	
	
	@SuppressWarnings("deprecation")
	public SentListAdapter(Context context, int layout, Cursor dataCursor, String[] from,
	        int[] to) {
	    super(context, layout, dataCursor, from, to);
	        this.dataCursor = dataCursor;
	        mInflater = LayoutInflater.from(context);
	        //contextLocal = context;
	        //initial fill
	       
	}

	public View getView(int position, View convertView, ViewGroup parent) {

	    ViewHolder holder;
	    
	    if (convertView == null) {
	        convertView = mInflater.inflate(R.layout.inbox_item, null);

	        holder = new ViewHolder();
	        holder.txt_to = (TextView) convertView.findViewById(R.id.txt_from);
	        holder.txt_from_name = (TextView) convertView.findViewById(R.id.txt_from_name);
	        holder.txt_message = (TextView) convertView.findViewById(R.id.txt_message);
	        holder.txt_date = (TextView) convertView.findViewById(R.id.txt_date);
	        
	        convertView.setTag(holder);
	    } else {

	    	holder = (ViewHolder) convertView.getTag();
	    }

	    dataCursor.moveToPosition(position);
	    int from_index = dataCursor.getColumnIndex("sms_to"); 
	    String label_to = dataCursor.getString(from_index);
	    
	    int message_index = dataCursor.getColumnIndex("message"); 
	    String label_message = dataCursor.getString(message_index);
	    
	    int date_index = dataCursor.getColumnIndex("sentOn"); 
	    String label_date = dataCursor.getString(date_index);
	    
	    
	    String date = ConvertToDate(label_date);
	   
	    //String contactName = Utility.getContactName(label_to, contextLocal);
	    //if(contactName == ""){
	    	holder.txt_from_name.setText(label_to); 
	    //}
	    //else{
	    	//holder.txt_from_name.setText(contactName);
	    //}
	    holder.txt_to.setText("To : " + label_to);
	    holder.txt_message.setText(label_message);
	    holder.txt_date.setText(date);
	    
	    
	    
	    return convertView;
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
	
		TextView txt_to,txt_message,txt_date,txt_from_name;;
	   
	}
}