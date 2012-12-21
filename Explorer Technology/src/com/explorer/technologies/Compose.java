package com.explorer.technologies;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class Compose extends Activity {

	// private static final int CONTACT_PICKER_RESULT = 1001;

	EditText textSender, textTo, textMessage;
	TextView txtViewCounter;
	Button btnSendMessage;
	ProgressDialog pd;
	int textCounter = 0;
	String groupIds = "";
	String msgId;
	ArrayList<HashMap<String, String>> groupList;
	Boolean isGroupListLoaded = false;
	Boolean isDraft = false;
	String scheduleDateTime = null; // To store scheduled message date and time
	private mItems[] itemss;

	List<String> contactList = new ArrayList<String>();
	String contactCount;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.compose);
		globalInitialize();

		textSender.setText(Utility.sender_id);
		checkIfComesFromDrafts();
		checkIfComesFromGroups();
	}

	private void checkIfComesFromDrafts() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();

		if (intent.hasExtra("id")) {
			msgId = intent.getStringExtra("id");
			isDraft = true;

		}

		if (intent.hasExtra("to")) {
			// textTo.setText(textTo.getText().toString()+","+intent.getStringExtra("to"));
			textTo.setText(intent.getStringExtra("to"));
			// setToNumber(intent.getStringExtra("to"));
		}
		if (intent.hasExtra("msg")) {
			textMessage.setText(intent.getStringExtra("msg"));
		}

		if (intent.hasExtra("toComplete")) {
			String[] contacts = intent.getStringExtra("toComplete").toString()
					.split(",");

			for (int i = 0; i < contacts.length; i++) {

				contactList.add(contacts[i]);
				// Toast.makeText(getApplicationContext(), contacts[i],
				// Toast.LENGTH_SHORT).show();
			}

		}

	}

	private void checkIfComesFromGroups() {
		Intent intent = getIntent();
		if (intent.hasExtra("groupName")) {

			textTo.setText(textTo.getText().toString() + ","
					+ intent.getStringExtra("groupName"));

		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		if (!isDraft) {
			if (!textTo.getText().toString().equals("")
					|| !textMessage.getText().toString().equals("")) {
				AlertDialog.Builder ab = new AlertDialog.Builder(Compose.this);
				ab.setMessage("Save it to drafts?")
						.setPositiveButton("Yes", dialogClickListener)
						.setNegativeButton("No", dialogClickListener).show();
			} else {
				super.onBackPressed();
			}
		} else {
			super.onBackPressed();
		}
	}

	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				// Yes button clicked

				if(saveMsgToDrafts())
					moveToDashboard();
				break;

			case DialogInterface.BUTTON_NEGATIVE:
				// No button clicked
				moveToDashboard();
				break;
			}
		}

	};

	private boolean saveMsgToDrafts() {
		String toComplete="";
		
		if(textTo.getText().length()>0)
			try
			{
			toComplete = setupAndGetFinalContactList();
			DatabaseFunctions.saveToDrafts(textTo.getText().toString(), textMessage
					.getText().toString(), toComplete);
			}
			catch(Exception e)
			{
				Toast.makeText(getApplicationContext(), "Wrong Characters in To field, please re-enter", Toast.LENGTH_SHORT).show();
				return false;
			}
		return true;
		

	}

	public void moveToDashboard() {
		isDraft = false;
		finish();

	}

	// Schedule Message
	public void scheduleMessage(View v) {
		// Set Scheduled date time to scheduledDataTime

		final Dialog DATE_PICKER_DIALOG = new Dialog(Compose.this,
				R.style.DialogWindowTitle);
		DATE_PICKER_DIALOG.setContentView(R.layout.date_time_dialog);

		final Button btnSetTime = (Button) DATE_PICKER_DIALOG
				.findViewById(R.id.btn_set_time);
		final Button btnSetDate = (Button) DATE_PICKER_DIALOG
				.findViewById(R.id.btn_set_date);
		final Button btnDone = (Button) DATE_PICKER_DIALOG
				.findViewById(R.id.btn_done);
		final LinearLayout layoutTime = (LinearLayout) DATE_PICKER_DIALOG
				.findViewById(R.id.layout_time);
		final LinearLayout layoutDate = (LinearLayout) DATE_PICKER_DIALOG
				.findViewById(R.id.layout_date);

		final DatePicker datePicker = (DatePicker) DATE_PICKER_DIALOG
				.findViewById(R.id.date_picker);
		final TimePicker timePicker = (TimePicker) DATE_PICKER_DIALOG
				.findViewById(R.id.time_picker);

		btnSetTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				layoutDate.setVisibility(View.INVISIBLE);
				layoutTime.setVisibility(View.VISIBLE);

			}
		});

		btnSetDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				layoutDate.setVisibility(View.VISIBLE);
				layoutTime.setVisibility(View.INVISIBLE);

			}
		});

		btnDone.setOnClickListener(new OnClickListener() {

			@SuppressLint("SimpleDateFormat")
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {

				Date date = new Date(datePicker.getYear() - 1900, datePicker
						.getMonth(), datePicker.getDayOfMonth(), timePicker
						.getCurrentHour(), timePicker.getCurrentMinute());
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss");
				String formatedDate = dateFormat.format(date);
				scheduleDateTime = formatedDate;
				// Toast.makeText(getApplicationContext(), scheduleDateTime,
				// Toast.LENGTH_LONG).show();
				DATE_PICKER_DIALOG.dismiss();

			}
		});

		DATE_PICKER_DIALOG.show();

	}

	public void sendMessage(View v) {

		// replaces ,, to , if exists
		String repairedNumbers = "";
		try {
			repairedNumbers = setupAndGetFinalContactList();
		} catch (Exception e) {
			Toast.makeText(Compose.this, "Error in generating Contact list",
					Toast.LENGTH_LONG).show();
			return;
		}

		
			//Toast.makeText(Compose.this, scheduleDateTime, Toast.LENGTH_LONG).show();
		
		if (repairedNumbers.equals("") && !groupIds.equals("")) {

			// Toast.makeText(Compose.this, groupIds, Toast.LENGTH_LONG).show();
			new SendMessageToGroup().execute(textSender.getText().toString(),
					groupIds, textMessage.getText().toString());
		} else if (!repairedNumbers.equals("")) {

			// Toast.makeText(Compose.this, repairedNumbers,
			// Toast.LENGTH_LONG).show();
			// Toast.makeText(Compose.this, groupIds, Toast.LENGTH_LONG).show();
			new SendMessage().execute(textSender.getText().toString(),
					repairedNumbers, textMessage.getText().toString(),
					scheduleDateTime);
		}

	}

	private String setupAndGetFinalContactList() {
		String repairedNumbers = "";
		String contactsFromToEditBox = removeContactTag(textTo.getText()
				.toString());

		String mixContacts = combineContact(contactsFromToEditBox);
		mixContacts = manageComma(mixContacts);

		String arr[] = mixContacts.split(",");

		for (int i = 0; i < arr.length; i++) {

			if (Utility.isLatinLetter(arr[i].charAt(0))) {
				groupIds = groupIds + "," + extractGroupId(arr[i]) + ",";
				continue;
			}

			// contactList.add(arr[i]);
			arr[i] = repairPhoneNumber(arr[i]);
			if (i == arr.length - 1) {
				repairedNumbers = repairedNumbers + "," + arr[i];

			} else {
				repairedNumbers = repairedNumbers + "," + arr[i] + ",";
			}
		}

		repairedNumbers = manageComma(repairedNumbers);

		groupIds = manageComma(groupIds);
		return repairedNumbers;

	}

	private String combineContact(String contactsFromToEditBox) {
		// TODO Auto-generated method stub\
		String numberListFromArray = "";
		for (int i = 0; i < contactList.size(); i++) {

			// contactList.set(i, repairPhoneNumber(contactList.get(i)));
			numberListFromArray = numberListFromArray + ","
					+ contactList.get(i) + ",";

		}
		return numberListFromArray + contactsFromToEditBox;

	}

	public String manageComma(String s) {
		if (s.length() > 0) {
			s = s.replaceAll(",,", ",");

			// removes last ,
			if (s.charAt(s.length() - 1) == ',') {
				s = s.substring(0, s.length() - 1);
			}
			// remove first ,
			if (s.charAt(0) == ',') {
				s = s.substring(1, s.length());
			}
		}
		return s;
	}

	public String extractGroupId(String combinedString) {
		String gId = "";

		int start = combinedString.indexOf("<");
		int end = combinedString.indexOf(">");

		gId = combinedString.substring(start + 1, end);

		return gId;

	}

	public void updateContactTag() {
		String[] contactArray = textTo.getText().toString().split(",");
		String contactsWeWant = "";
		if (isContactTagPresent(contactArray)) {
			for (int i = 0; i < contactArray.length; i++) {
				if (contactArray[i].contains("Contact"))
					contactArray[i] = "Contact(" + contactList.size() + ")";
				contactsWeWant = contactsWeWant + contactArray[i] + ",";

			}
			if (contactsWeWant.length() > 0) {
				contactsWeWant = contactsWeWant.substring(0,
						contactsWeWant.length() - 1);
				contactsWeWant = manageComma(contactsWeWant);
			}
			textTo.setText(contactsWeWant);
		} else {
			if (textTo.getText().length() > 0)
				textTo.setText("Contact(" + contactList.size() + ")" + ","
						+ textTo.getText().toString());
			else
				textTo.setText("Contact(" + contactList.size() + ")");
		}

	}

	private Boolean isContactTagPresent(String[] contactArray) {
		for (int i = 0; i < contactArray.length; i++) {
			if (contactArray[i].contains("Contact"))
				return true;
		}
		return false;
	}

	private String removeContactTag(String mixString) {
		String[] contactArray = mixString.split(",");
		String contactsWeWant = "";
		for (int i = 0; i < contactArray.length; i++) {
			if (contactArray[i].contains("Contact"))
				continue;

			contactsWeWant = contactsWeWant + contactArray[i] + ",";

		}
		contactsWeWant = manageComma(contactsWeWant);
		return contactsWeWant;

	}

	public String repairPhoneNumber(String num) {
		String repairedNumber = num;

		// get country code
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String countryCode = tm.getNetworkCountryIso();

		// get phone code from country code
		countryCode = Iso2Phone.getPhone(countryCode);

		// if number starts from + means it has code attached, nothing to do
		if (repairedNumber.startsWith("+")) {

			repairedNumber = repairedNumber.substring(1,
					repairedNumber.length());
			return repairedNumber;
		}

		// remove 0 if its a first digit
		if (repairedNumber.startsWith("0")) {
			repairedNumber = repairedNumber.substring(1,
					repairedNumber.length());

		}
		if (repairedNumber.length() == 10) // valid number in india
		{
			repairedNumber = countryCode + repairedNumber;
		} else if (repairedNumber.length() == 11) // valid number in nigeria
		{
			repairedNumber = countryCode + repairedNumber;
		}

		return repairedNumber;
	}

	public void globalInitialize() {
		// spinnerLevel = (Spinner) findViewById(R.id.spinner_level);
		textSender = (EditText) findViewById(R.id.txt_sender);
		textTo = (EditText) findViewById(R.id.txt_to);
		textMessage = (EditText) findViewById(R.id.txt_message);
		txtViewCounter = (TextView) findViewById(R.id.txtCounter);
		textMessage.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				txtViewCounter.setText("Count : "
						+ textMessage.getText().length());

			}
		});
		btnSendMessage = (Button) findViewById(R.id.btn_send_message);

	}

	public void insertSentMessage(String messageStatus) {
		String toComplete = setupAndGetFinalContactList();
		Long milliseconds = System.currentTimeMillis();
		DatabaseFunctions.saveSentMessage(textTo.getText().toString(),
				textMessage.getText().toString(), toComplete, messageStatus,
				milliseconds.toString());

	}

	public void insertSentMessage1() {
		ContentValues values = new ContentValues();
		values.put("address", textTo.getText().toString());
		values.put("body", textMessage.getText().toString());

		getApplicationContext().getContentResolver().insert(
				Uri.parse("content://sms/sent"), values);

	}

	public class UpdateCredits extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... args) {
			return APICalls.userLogin(Utility.username, Utility.password);
		}

		@Override
		protected void onPostExecute(Integer result) {

			super.onPostExecute(result);

		}

	}

	public class SendMessage extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(Compose.this);
			pd.setTitle("SMS Send");
			pd.setMessage("Sending SMS...");
			pd.show();

		}

		@Override
		protected String doInBackground(String... args) {

			String status;
			// status = APICalls.

			status = APICalls.sendMsg(args[0], args[1], args[2], args[3]);
			return status;

		}

		@Override
		protected void onPostExecute(String result) {

			// if (result == 0)
			// {
			insertSentMessage(result);
			new UpdateCredits().execute();
			if (isDraft) {
				new DeleteDraft().execute();
				// isDraft=false;
			}
			// Toast.makeText(getApplicationContext(),
			// "Message sent successfully ", Toast.LENGTH_LONG).show();
			Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG)
					.show();
			// }
			// else
			// {
			// Toast.makeText(getApplicationContext(),
			// "ERROR SENDING SMS. PLEASE CHECK YOUR INTERNET CONNECTION! ",
			// Toast.LENGTH_LONG).show();
			// }

			try {
				pd.dismiss();
			} catch (Exception e) {
			}

			// if groups exists
			if (groupIds != "") {
				new SendMessageToGroup()
						.execute(textSender.getText().toString(), groupIds,
								textMessage.getText().toString());

			} else
				moveToDashboard();
			super.onPostExecute(result);
		}

	}

	public class SendMessageToGroup extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(Compose.this);
			pd.setTitle("Send Group SMS");
			pd.setMessage("Sending sms to group..");
			pd.show();

		}

		@Override
		protected String doInBackground(String... args) {

			String status;
			// status = APICalls.

			status = APICalls.sendToGroup(args[0], args[1], args[2]);
			return status;

		}

		@Override
		protected void onPostExecute(String result) {

			// if (result == 0)
			// {
			insertSentMessage(result);
			new UpdateCredits().execute();
			// Toast.makeText(getApplicationContext(),
			// "Message sent successfully to group ", Toast.LENGTH_LONG).show();
			Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG)
					.show();
			if (isDraft) {
				new DeleteDraft().execute();
				moveToDashboard();
			}
			// }
			// else
			// {
			// Toast.makeText(getApplicationContext(),
			// "Error sending to group ", Toast.LENGTH_LONG).show();
			// }

			groupIds = "";
			try {
				pd.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
			moveToDashboard();

			super.onPostExecute(result);
		}

	}

	public class ShowGroups extends
			AsyncTask<String, Void, ArrayList<HashMap<String, String>>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(Compose.this);
			pd.setTitle("Groups");
			pd.setMessage("Loading Groups..");
			pd.show();

		}

		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(
				String... args) {
			return APICalls.getGroups();

		}

		@Override
		protected void onPostExecute(ArrayList<HashMap<String, String>> mylist) {

			groupList = mylist;
			isGroupListLoaded = true;
			showinList();

			try {
				pd.dismiss();
			} catch (Exception e) {
			}

			super.onPostExecute(mylist);
		}

	}

	public class DeleteDraft extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... params) {
			DatabaseFunctions.deleteDraftMessage(msgId);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}

	}

	@SuppressWarnings("deprecation")
	public void getCallLog(View v) {
		final Dialog CALL_LOG_DIALOG = new Dialog(Compose.this,
				R.style.DialogWindowTitle);
		CALL_LOG_DIALOG.setContentView(R.layout.contact_dialog);

		final TextView textTitle = (TextView) CALL_LOG_DIALOG
				.findViewById(R.id.txt_title);
		textTitle.setText(getString(R.string.call_log));

		final Button btnOk = (Button) CALL_LOG_DIALOG
				.findViewById(R.id.btn_contact_ok);
		final Button btnSelectAll = (Button) CALL_LOG_DIALOG
				.findViewById(R.id.btn_contact_select_all);
		final EditText textSerach = (EditText) CALL_LOG_DIALOG
				.findViewById(R.id.contactSearchBox);
		final Button btnSearch = (Button) CALL_LOG_DIALOG
				.findViewById(R.id.btnSearch);
		String order = android.provider.CallLog.Calls.DATE + " DESC";
		String[] PROJECTION = { android.provider.CallLog.Calls._ID,
				android.provider.CallLog.Calls.NUMBER,
				android.provider.CallLog.Calls.CACHED_NAME, };

		final Cursor contactCursor = getContentResolver().query(
				android.provider.CallLog.Calls.CONTENT_URI, PROJECTION, null,
				null, order);

		// Create and populate mItems.
		itemss = (mItems[]) getLastNonConfigurationInstance();
		final ArrayList<mItems> callLogs = new ArrayList<Compose.mItems>();
		contactCursor.moveToFirst();
		while (contactCursor.moveToNext()) {
			callLogs.add(new mItems(
					contactCursor.getString(contactCursor
							.getColumnIndex(android.provider.CallLog.Calls.NUMBER)),
					contactCursor.getString(contactCursor
							.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME))));
		}

		final SelectArralAdapter myAdapter = new SelectArralAdapter(
				Compose.this, callLogs);

		final ListView listview = (ListView) CALL_LOG_DIALOG
				.findViewById(R.id.contact_listview);
		listview.setAdapter(myAdapter);
		listview.setClickable(true);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parant, View view,
					int position, long Id) {
				// TODO Auto-generated method stub
				mItems planet = myAdapter.getItem(position);
				planet.toggleChecked();
				SelectViewHolder viewHolder = (SelectViewHolder) view.getTag();
				viewHolder.getCheckBox().setChecked(planet.isChecked());

			}
		});

		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				for (int i = 0; i < callLogs.size(); i++) {
					if (callLogs.get(i).checked) {

						contactList.add(callLogs.get(i).phoneNumber);
					}

				}
				updateContactTag();
				CALL_LOG_DIALOG.dismiss();
			}
		});

		btnSelectAll.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("unused")
			@Override
			public void onClick(View v) {
				// Toast.makeText(getApplicationContext(), "Items : " +
				// mycontacts.size(), Toast.LENGTH_LONG).show();
				String contact = "";

				for (int i = 0; i < callLogs.size(); i++) {
					contact += callLogs.get(i).contactName + "<"
							+ callLogs.get(i).phoneNumber + ">,";
					contactList.add(callLogs.get(i).phoneNumber);
				}
				// setToNumber(contact);
				updateContactTag();
				// Toast.makeText(getApplicationContext(), contact.length(),
				// Toast.LENGTH_LONG).show();
				CALL_LOG_DIALOG.dismiss();

			}
		});
		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String searchText = textSerach.getText().toString();
				String order = android.provider.CallLog.Calls.DATE + " DESC";
				String[] PROJECTION = { android.provider.CallLog.Calls._ID,
						android.provider.CallLog.Calls.NUMBER,
						android.provider.CallLog.Calls.CACHED_NAME, };

				final Cursor contactCursor = getContentResolver().query(
						android.provider.CallLog.Calls.CONTENT_URI,
						PROJECTION,
						android.provider.CallLog.Calls.CACHED_NAME + " like "
								+ "'" + searchText + "%'", null, order);
				// Create and populate planets.
				itemss = (mItems[]) getLastNonConfigurationInstance();
				final ArrayList<mItems> callLogs = new ArrayList<Compose.mItems>();
				contactCursor.moveToFirst();
				while (contactCursor.moveToNext()) {
					callLogs.add(new mItems(
							contactCursor
									.getString(contactCursor
											.getColumnIndex(android.provider.CallLog.Calls.NUMBER)),
							contactCursor.getString(contactCursor
									.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME))));
				}

				final SelectArralAdapter myAdapter = new SelectArralAdapter(
						Compose.this, callLogs);

				final ListView listview = (ListView) CALL_LOG_DIALOG
						.findViewById(R.id.contact_listview);
				listview.setAdapter(myAdapter);

			}
		});

		CALL_LOG_DIALOG.show();

	}

	public void getGroups(View v) {
		if (!isGroupListLoaded) {
			new ShowGroups().execute();
		} else {
			showinList();
		}

	}

	public void showinList() {
		final Dialog GROUP_DIALOG = new Dialog(Compose.this,
				R.style.DialogWindowTitle);
		GROUP_DIALOG.setContentView(R.layout.contact_dialog);
		final LinearLayout buttonLayout = (LinearLayout) GROUP_DIALOG
				.findViewById(R.id.button_layout);
		buttonLayout.setVisibility(View.GONE);
		final TextView textTitle = (TextView) GROUP_DIALOG
				.findViewById(R.id.txt_title);
		textTitle.setText("Select Group");
		
		final EditText contactSearchBox = (EditText)GROUP_DIALOG.findViewById(R.id.contactSearchBox);
		final Button btnSearch = (Button) GROUP_DIALOG
				.findViewById(R.id.btnSearch);
		contactSearchBox.setHint("Group Name");

		final String[] from = new String[] { "id", "name" };
		final int[] to = new int[] { R.id.contact_id, R.id.contact_name };
		final ListView listview = (ListView) GROUP_DIALOG
				.findViewById(R.id.contact_listview);

		ListAdapter adapter = new SimpleAdapter(this, groupList,
				R.layout.call_log_item, from, to);

		listview.setAdapter(adapter);

		listview.setTextFilterEnabled(true);
		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				@SuppressWarnings("unchecked")
				HashMap<String, String> o = (HashMap<String, String>) listview
						.getItemAtPosition(position);

				String name = o.get("name") + "<" + o.get("id") + ">";

				setToNumberforGroup(name);
				GROUP_DIALOG.dismiss();

			}
		});
		
		btnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String searchText = contactSearchBox.getText().toString().toLowerCase();
				ArrayList<HashMap<String, String>> filteredGroup = new ArrayList<HashMap<String,String>>();
				for(int i=0;i<groupList.size();i++)
				{
					String name=groupList.get(i).get("name").toLowerCase();
					if(name.contains(searchText))
					{
						
						HashMap<String, String> newHash = new HashMap<String, String>();
						newHash.put("id", groupList.get(i).get("id").toString());
						newHash.put("name", groupList.get(i).get("name").toString());
						filteredGroup.add(newHash);
						
					}
					
				}
				
				if(filteredGroup.size()>0)
				{
					ListAdapter adapter = new SimpleAdapter(Compose.this, filteredGroup,
							R.layout.call_log_item, from, to);
	
					listview.setAdapter(adapter);
				}

				
				
			}
		});
		GROUP_DIALOG.show();

	}

	public void setToNumberforGroup(String number) {
		textTo.setText(textTo.getText() + "," + number);

	}

	@SuppressWarnings("deprecation")
	public void getContacts(View v) {
		// Intent contactPickerIntent = new
		// Intent(Intent.ACTION_PICK,Contacts.CONTENT_URI);
		// startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
		final Dialog CONTACT_DIALOG = new Dialog(Compose.this,
				R.style.DialogWindowTitle);
		CONTACT_DIALOG.setContentView(R.layout.contact_dialog);

		final EditText textSerach = (EditText) CONTACT_DIALOG
				.findViewById(R.id.contactSearchBox);
		final Button btnOk = (Button) CONTACT_DIALOG
				.findViewById(R.id.btn_contact_ok);
		final Button btnSearch = (Button) CONTACT_DIALOG
				.findViewById(R.id.btnSearch);
		final Button btnSelectAll = (Button) CONTACT_DIALOG
				.findViewById(R.id.btn_contact_select_all);

		final String[] PROJECTION = new String[] { Contacts._ID,
				Contacts.DISPLAY_NAME, Phone.NUMBER };
		final Cursor contactCursor = getContentResolver().query(
				Phone.CONTENT_URI, PROJECTION, null, null, null);
		// Create and populate planets.
		itemss = (mItems[]) getLastNonConfigurationInstance();
		final ArrayList<mItems> mycontacts = new ArrayList<Compose.mItems>();
		contactCursor.moveToFirst();
		while (contactCursor.moveToNext()) {
			mycontacts.add(new mItems(contactCursor.getString(contactCursor
					.getColumnIndex(Phone.NUMBER)), contactCursor
					.getString(contactCursor
							.getColumnIndex(Contacts.DISPLAY_NAME))));
		}

		final SelectArralAdapter myAdapter = new SelectArralAdapter(
				Compose.this, mycontacts);

		final ListView listview = (ListView) CONTACT_DIALOG
				.findViewById(R.id.contact_listview);
		listview.setAdapter(myAdapter);
		listview.setClickable(true);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parant, View view,
					int position, long Id) {
				// TODO Auto-generated method stub
				mItems planet = myAdapter.getItem(position);
				planet.toggleChecked();
				SelectViewHolder viewHolder = (SelectViewHolder) view.getTag();
				viewHolder.getCheckBox().setChecked(planet.isChecked());

			}
		});

		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				for (int i = 0; i < mycontacts.size(); i++) {
					if (mycontacts.get(i).checked) {

						contactList.add(mycontacts.get(i).phoneNumber);
					}

				}
				updateContactTag();
				CONTACT_DIALOG.dismiss();
			}
		});

		btnSelectAll.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("unused")
			@Override
			public void onClick(View v) {
				// Toast.makeText(getApplicationContext(), "Items : " +
				// mycontacts.size(), Toast.LENGTH_LONG).show();
				String contact = "";

				for (int i = 0; i < mycontacts.size(); i++) {
					contact += mycontacts.get(i).contactName + "<"
							+ mycontacts.get(i).phoneNumber + ">,";
					contactList.add(mycontacts.get(i).phoneNumber);
				}
				// setToNumber(contact);
				updateContactTag();
				// Toast.makeText(getApplicationContext(), contact.length(),
				// Toast.LENGTH_LONG).show();
				CONTACT_DIALOG.dismiss();

			}
		});

		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String searchText = textSerach.getText().toString();
				Cursor contactCursor = getContentResolver().query(
						Phone.CONTENT_URI,
						PROJECTION,
						Contacts.DISPLAY_NAME + " like " + "'" + searchText
								+ "%'", null, null);
				// Create and populate planets.
				itemss = (mItems[]) getLastNonConfigurationInstance();
				final ArrayList<mItems> mycontacts = new ArrayList<Compose.mItems>();

				contactCursor.moveToFirst();
				while (contactCursor.moveToNext()) {
					mycontacts.add(new mItems(contactCursor
							.getString(contactCursor
									.getColumnIndex(Phone.NUMBER)),
							contactCursor.getString(contactCursor
									.getColumnIndex(Contacts.DISPLAY_NAME))));
				}

				final SelectArralAdapter myAdapter = new SelectArralAdapter(
						Compose.this, mycontacts);
				listview.setAdapter(myAdapter);

			}
		});

		CONTACT_DIALOG.show();

	}

	// for multiselect

	private static class mItems {
		private String phoneNumber = "";
		private String contactName = "";
		private boolean checked = false;

		@SuppressWarnings("unused")
		public mItems() {
		}

		@SuppressWarnings("unused")
		public mItems(String cName) {
			this.contactName = cName;
		}

		public mItems(String pN, String cName) {
			this.phoneNumber = pN;
			this.contactName = cName;
		}

		@SuppressWarnings("unused")
		public mItems(String pN, String cName, boolean checked) {
			this.phoneNumber = pN;
			this.contactName = cName;
			this.checked = checked;
		}

		public String getContactName() {
			return contactName;
		}

		public String getPhoneNumber() {
			return phoneNumber;
		}

		@SuppressWarnings("unused")
		public void setContactName(String cName) {
			this.contactName = cName;
		}

		@SuppressWarnings("unused")
		public void setPhoneNumber(String pNo) {
			this.phoneNumber = pNo;
		}

		public boolean isChecked() {
			return checked;
		}

		public void setChecked(boolean checked) {
			this.checked = checked;
		}

		/*
		 * public String toString() { return name; }
		 */

		public void toggleChecked() {
			checked = !checked;
		}
	}

	private static class SelectViewHolder {
		private CheckBox checkBox;
		private TextView textPhone;
		private TextView textname;

		@SuppressWarnings("unused")
		public SelectViewHolder() {
		}

		public SelectViewHolder(TextView tn, TextView tp, CheckBox checkBox) {
			this.checkBox = checkBox;
			this.textPhone = tp;
			this.textname = tn;
		}

		public CheckBox getCheckBox() {
			return checkBox;
		}

		@SuppressWarnings("unused")
		public void setCheckBox(CheckBox checkBox) {
			this.checkBox = checkBox;
		}

		public TextView getphoneText() {
			return textPhone;
		}

		public TextView getContactText() {
			return textname;
		}

		@SuppressWarnings("unused")
		public void setphoneText(TextView textView) {
			this.textPhone = textView;
		}

		@SuppressWarnings("unused")
		public void setContactText(TextView tv) {
			this.textname = tv;
		}
	}

	/** Custom adapter for displaying an array of Planet objects. */
	private static class SelectArralAdapter extends ArrayAdapter<mItems> {
		private LayoutInflater inflater;

		public SelectArralAdapter(Context context, List<mItems> contactList) {
			super(context, R.layout.contact_item, contactList);
			// Cache the LayoutInflate to avoid asking for a new one each time.
			inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// Planet to display
			mItems planet = (mItems) this.getItem(position);

			// The child views in each row.
			CheckBox checkBox;
			TextView textPhone;
			TextView textName;

			// Create a new row view
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.contact_item, null);

				// Find the child views.
				textPhone = (TextView) convertView
						.findViewById(R.id.txt_contact_number);
				textName = (TextView) convertView
						.findViewById(R.id.txt_contact_name);
				checkBox = (CheckBox) convertView
						.findViewById(R.id.checkbox_contact);
				// Optimization: Tag the row with it's child views, so we don't
				// have to
				// call findViewById() later when we reuse the row.
				convertView.setTag(new SelectViewHolder(textName, textPhone,
						checkBox));
				// If CheckBox is toggled, update the planet it is tagged with.
				checkBox.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						CheckBox cb = (CheckBox) v;
						mItems planet = (mItems) cb.getTag();
						planet.setChecked(cb.isChecked());
					}
				});
			}
			// Reuse existing row view
			else {
				// Because we use a ViewHolder, we avoid having to call
				// findViewById().
				SelectViewHolder viewHolder = (SelectViewHolder) convertView
						.getTag();
				checkBox = viewHolder.getCheckBox();
				textName = viewHolder.getContactText();
				textPhone = viewHolder.getphoneText();
			}

			// Tag the CheckBox with the Planet it is displaying, so that we can
			// access the planet in onClick() when the CheckBox is toggled.
			checkBox.setTag(planet);
			// Display planet data
			checkBox.setChecked(planet.isChecked());
			textPhone.setText(planet.getPhoneNumber());
			textName.setText(planet.getContactName());
			return convertView;
		}
	}

	public Object onRetainNonConfigurationInstance() {
		return itemss;
	}
}
