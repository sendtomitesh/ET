package com.explorer.technologies;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class InboxAdapter extends BaseAdapter {

	 private ArrayList<Inbox> mInboxList;
	 private LayoutInflater mInflater;
	 
	
	 public InboxAdapter(Context context,ArrayList<Inbox> inboxList){
	
		 this.mInboxList = inboxList;
		 mInflater = LayoutInflater.from(context);
		 
	 }
	 
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mInboxList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mInboxList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	@Override
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

    	holder.txt_from_name.setText(mInboxList.get(position).getName()); 
	    holder.txt_from.setText(mInboxList.get(position).getName());
	    holder.txt_message.setText(mInboxList.get(position).getMessage());
	    holder.txt_date.setText(mInboxList.get(position).getDate());
	    
	    return convertView;

	}
	
	public class ViewHolder {
		
		TextView txt_from,txt_message,txt_date,txt_from_name;
	   
	}

}
