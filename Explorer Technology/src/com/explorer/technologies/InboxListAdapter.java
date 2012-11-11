package com.explorer.technologies;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class InboxListAdapter extends SimpleCursorAdapter {

	private Cursor dataCursor;
	
	private LayoutInflater mInflater;
	
	
	@SuppressWarnings("deprecation")
	public InboxListAdapter(Context context, int layout, Cursor dataCursor, String[] from,
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
	        holder.txt_from = (TextView) convertView.findViewById(R.id.txt_from);
	        holder.txt_message = (TextView) convertView.findViewById(R.id.txt_message);
	        
	        convertView.setTag(holder);
	    } else {

	    	holder = (ViewHolder) convertView.getTag();
	    }

	    dataCursor.moveToPosition(position);
	    int from_index = dataCursor.getColumnIndex("address"); 
	    String label_from = dataCursor.getString(from_index);
	    
	    int message_index = dataCursor.getColumnIndex("body"); 
	    String label_message = dataCursor.getString(message_index);
	    
	    holder.txt_from.setText("From : " + label_from);
	    holder.txt_message.setText(label_message);
	    
	    return convertView;
	}

	public class ViewHolder {
	
		TextView txt_from,txt_message;
	   
	}
}