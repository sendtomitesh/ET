package com.explorer.technologies;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class SentListAdapter extends SimpleCursorAdapter {

	private Cursor dataCursor;
	
	private LayoutInflater mInflater;
	
	
	@SuppressWarnings("deprecation")
	public SentListAdapter(Context context, int layout, Cursor dataCursor, String[] from,
	        int[] to) {
	    super(context, layout, dataCursor, from, to);
	        this.dataCursor = dataCursor;
	        mInflater = LayoutInflater.from(context);
	       
	        //initial fill
	       
	}

	public View getView(int position, View convertView, ViewGroup parent) {

	    ViewHolder holder;
	    
	    if (convertView == null) {
	        convertView = mInflater.inflate(R.layout.inbox_item, null);

	        holder = new ViewHolder();
	        holder.txt_to = (TextView) convertView.findViewById(R.id.txt_from);
	        holder.txt_message = (TextView) convertView.findViewById(R.id.txt_message);
	        holder.txt_date = (TextView) convertView.findViewById(R.id.txt_date);
	        
	        convertView.setTag(holder);
	    } else {

	    	holder = (ViewHolder) convertView.getTag();
	    }

	    dataCursor.moveToPosition(position);
	    int from_index = dataCursor.getColumnIndex("address"); 
	    String label_to = dataCursor.getString(from_index);
	    
	    int message_index = dataCursor.getColumnIndex("body"); 
	    String label_message = dataCursor.getString(message_index);
	    
	    int date_index = dataCursor.getColumnIndex("date"); 
	    String label_date = dataCursor.getString(date_index);
	    
	    
	    Date date = ConvertToDate(label_date);
	    
	    holder.txt_to.setText("To : " + label_to);
	    holder.txt_message.setText(label_message);
	    holder.txt_date.setText(date.toString());
	    
	    return convertView;
	}

	
	private Date ConvertToDate(String dateString){
	    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
	    Date convertedDate = new Date();
	    try {
	        convertedDate = dateFormat.parse(dateString);
	    } catch (ParseException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	    return convertedDate;
	}
	public class ViewHolder {
	
		TextView txt_to,txt_message,txt_date;
	   
	}
}