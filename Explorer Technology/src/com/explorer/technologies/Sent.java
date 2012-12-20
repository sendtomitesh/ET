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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Sent extends Activity {

	SentListAdapter sentAdapter;
	ImageView imgTitle;
	Button btnDelete;
	TextView txtTitle;
	Cursor sentCursor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inbox);
		initilizeGlobals();
		loadSentMessages();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		if (sentCursor != null) {
			sentCursor.close();
		}
		super.onStop();
	}


	private void initilizeGlobals() {
		imgTitle = (ImageView) findViewById(R.id.image_title);
		txtTitle = (TextView) findViewById(R.id.txt_title);
		imgTitle.setImageResource(R.drawable.sent);
		txtTitle.setText(getResources().getString(R.string.sent));
		btnDelete = (Button) findViewById(R.id.btn_delete);
		btnDelete.setVisibility(View.VISIBLE);
	}

	public void deleteAll(View v) {
		//deleteAllmsg();
		DatabaseFunctions.deleteAllSentMessage();
		loadSentMessages();
	}

	public boolean checkForValidNumber(String n) {
		if (Utility.isLatinLetter(n.charAt(0)) || n.length() < 10)
			return false;
		else
			return true;
	}

	public void moveToCompose(int which, String to, String msg) {
		Intent composeIntent = new Intent(getApplicationContext(),
				Compose.class);
		// reply
		if (which == 0) {
			composeIntent.putExtra("to", to);
		}
		// forward
		else {
			composeIntent.putExtra("msg", msg);
		}
		startActivity(composeIntent);
		finish();
	}

	private void loadSentMessages() {
		final ListView listview = (ListView) findViewById(R.id.listview_inbox);

		sentCursor = DatabaseFunctions.getSentMessageCursor();

		startManagingCursor(sentCursor);

		String[] from = new String[] { "sms_to", "message", "sentOn" };
		int[] to = new int[] { R.id.txt_from_name, R.id.txt_message,
				R.id.txt_date };

		sentAdapter = new SentListAdapter(this, R.layout.inbox_item,
				sentCursor, from, to);
		listview.setAdapter(sentAdapter);
		listview.setClickable(true);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				sentCursor.moveToPosition(position);

				AlertDialog multichoice;
				multichoice = new AlertDialog.Builder(Sent.this)

						.setTitle("SMS Options")
						.setItems(new String[] { "Delete" , "Status"},
								new OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										
										if(which == 0){
											DatabaseFunctions.deleteSentMessage(sentCursor.getString(0));
											loadSentMessages();
										}
										if(which == 1){
											Utility.alert(Sent.this,sentCursor.getString(4));
										}

									}
								}).create();

				multichoice.show();
			
			}
		});

	}

	@SuppressWarnings("deprecation")
	@Override
	public void startManagingCursor(Cursor c) {
		if (Build.VERSION.SDK_INT < 11) {
			super.startManagingCursor(c);
		}
	}

}
