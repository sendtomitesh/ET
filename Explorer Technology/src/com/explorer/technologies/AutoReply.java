package com.explorer.technologies;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class AutoReply extends Activity {

	SentListAdapter sentAdapter;
	ImageView imgTitle;
	TextView txtTitle;
	Button btnNew, btnDelete;
	// SQLiteDatabase db;
	// DbHelper dbHelper;
	Cursor cursor, detailCursor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inbox);
		initilizeGlobals();
		showAutoReplyList();

	}

	@Override
	protected void onResume() {
		showAutoReplyList();
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (cursor != null) {
			cursor.close();
		}
		if (detailCursor != null) {
			detailCursor.close();
		}
	}

	private void initilizeGlobals() {
		imgTitle = (ImageView) findViewById(R.id.image_title);
		txtTitle = (TextView) findViewById(R.id.txt_title);
		imgTitle.setImageResource(R.drawable.autoreply);
		txtTitle.setText(getResources().getString(R.string.autoreply));
		btnNew = (Button) findViewById(R.id.btn_new);
		btnDelete = (Button) findViewById(R.id.btn_delete);
		btnNew.setVisibility(View.VISIBLE);
		btnDelete.setVisibility(View.VISIBLE);
	}

	public void addNew(View v) {

		// Toast.makeText(getApplicationContext(), "Add New",
		// Toast.LENGTH_LONG).show();
		Intent newReplyIntent = new Intent(getApplicationContext(),
				AddAutoReply.class);
		startActivity(newReplyIntent);

	}

	private void discardCurrent(String msgId) {
		DatabaseFunctions.deleteAutoReply(msgId);
		showAutoReplyList();
	}

	public void deleteAll(View v) {

		DatabaseFunctions.deleteAllAutoReply();
		showAutoReplyList();
		Toast.makeText(getApplicationContext(), "All Records Deleted",
				Toast.LENGTH_LONG).show();
	}

	@SuppressWarnings("deprecation")
	private void showAutoReplyList() {

		String[] from = new String[] { "sms_to", "message", "reply_count" };
		int[] to = new int[] { R.id.txt_from_name, R.id.txt_message,
				R.id.txt_total };
		final ListView listview = (ListView) findViewById(R.id.listview_inbox);

		// dbHelper = new DbHelper(getApplicationContext());
		// db = dbHelper.getReadableDatabase();
		cursor = DatabaseFunctions.getAutoReplyCursor();

		if (cursor != null) {
			startManagingCursor(cursor);
			SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
					R.layout.auto_reply_item, cursor, from, to);
			listview.setAdapter(adapter);

			listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			listview.setClickable(true);
			listview.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					final String itemId = String.valueOf(id);
					
					cursor.moveToPosition(position);
					String[] items = new String[] { "Update", "Delete",
							"Details" };

					AlertDialog.Builder optionBuilder = new AlertDialog.Builder(
							AutoReply.this);
					optionBuilder.setTitle("Options").setItems(items,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									if (which == 0) {

										String to = cursor.getString(1);
										String msg = cursor.getString(2);
										openUpdateDialog(to, msg);
									}
									if (which == 1) {
										discardCurrent(itemId);
									}

									if (which == 2) {
										openDetailDialog(itemId);

									}

								}
							});
					optionBuilder.create();
					optionBuilder.show();

				}

			});

		}

	}

	private void openUpdateDialog(String to, String message) {
		final Dialog dialog = new Dialog(this, R.style.DialogWindowTitle);
		dialog.setContentView(R.layout.update_auto_reply_dialog);
		final EditText textTo = (EditText) dialog.findViewById(R.id.txt_to);
		final EditText textMessage = (EditText) dialog
				.findViewById(R.id.txt_message);
		final Button btnUpdate = (Button) dialog.findViewById(R.id.btn_update);
		textTo.setText(to);
		final String smsTo = to;
		textMessage.setText(message);
		btnUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String msg = textMessage.getText().toString();
				if (msg.length() == 0) {
					Toast.makeText(getApplicationContext(),
							"Please set message" + msg, Toast.LENGTH_LONG)
							.show();
				} else {

					DatabaseFunctions.updateAutoReply(smsTo, msg);
					showAutoReplyList();
					dialog.dismiss();
				}

			}
		});
		dialog.show();

	}

	@SuppressWarnings("deprecation")
	private void openDetailDialog(String sms_to) {
		final Dialog GROUP_DIALOG = new Dialog(AutoReply.this,
				R.style.DialogWindowTitle);
		GROUP_DIALOG.setContentView(R.layout.contact_dialog);
		final LinearLayout buttonLayout = (LinearLayout) GROUP_DIALOG
				.findViewById(R.id.button_layout);
		buttonLayout.setVisibility(View.GONE);

		final RelativeLayout layoutSearch = (RelativeLayout) GROUP_DIALOG
				.findViewById(R.id.layoutSearchBox);
		layoutSearch.setVisibility(View.INVISIBLE);
		final TextView textTitle = (TextView) GROUP_DIALOG
				.findViewById(R.id.txt_title);
		textTitle.setText("AutoReply Details");

		String[] from = new String[] { "_id", "sms_to", "sentOn" };
		int[] to = new int[] { R.id.contact_id, R.id.contact_name,
				R.id.contact_number };
		final ListView listview = (ListView) GROUP_DIALOG
				.findViewById(R.id.contact_listview);

		detailCursor = DatabaseFunctions.getAutoReplyDetailCursor(sms_to);

		if (detailCursor != null) {
			startManagingCursor(detailCursor);
			SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
					R.layout.call_log_item, detailCursor, from, to);
			listview.setAdapter(adapter);
		}
		GROUP_DIALOG.show();

	}

	@SuppressWarnings("deprecation")
	@Override
	public void startManagingCursor(Cursor c) {

		// To solve the following error for honeycomb:
		// java.lang.RuntimeException: Unable to resume activity
		// java.lang.IllegalStateException: trying to requery an already closed
		// cursor
		if (Build.VERSION.SDK_INT < 11) {
			super.startManagingCursor(c);
		}
	}
}
