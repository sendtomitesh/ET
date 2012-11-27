package com.explorer.technologies;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ContactsAdapter extends SimpleCursorAdapter {

	private Cursor dataCursor;
	//private Context contextLocal;
	private LayoutInflater mInflater;
	
	
	@SuppressWarnings("deprecation")
	public ContactsAdapter(Context context, int layout, Cursor dataCursor, String[] from,
	        int[] to) {
	    super(context, layout, dataCursor, from, to);
	        this.dataCursor = dataCursor;
	    //    contextLocal =  context;
	        mInflater = LayoutInflater.from(context);
	       
	        //initial fill
	       
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder HOLDER;
		
	    if (convertView == null) {
	        convertView = mInflater.inflate(R.layout.contact_item, null);

	        HOLDER = new ViewHolder();
	        HOLDER.txt_name = (TextView) convertView.findViewById(R.id.txt_contact_name);
	        HOLDER.txt_number = (TextView) convertView.findViewById(R.id.txt_contact_number);
	        HOLDER.check_contact = (CheckBox) convertView.findViewById(R.id.checkbox_contact);
	        
	        convertView.setTag(HOLDER);
	    } else {

	    	HOLDER = (ViewHolder) convertView.getTag();
	    }

	    dataCursor.moveToPosition(position);
	    int name_index = dataCursor.getColumnIndex(Contacts.DISPLAY_NAME); 
	    String label_name = dataCursor.getString(name_index);
	    
	    int number_index = dataCursor.getColumnIndex(Phone.NUMBER); 
	    String label_number = dataCursor.getString(number_index);
	    
	    HOLDER.txt_name.setText(label_name);
	    HOLDER.txt_number.setText(label_number);

	    return convertView;
	}

	public class ViewHolder {
	
		TextView txt_name,txt_number;
		CheckBox check_contact;
	   
	}
}