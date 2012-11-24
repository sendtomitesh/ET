package com.explorer.technologies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Groups extends Activity {

	ProgressDialog pd;

	SentListAdapter sentAdapter;
	ImageView imgTitle;
	TextView txtTitle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inbox);
		imgTitle = (ImageView) findViewById(R.id.image_title);
		txtTitle = (TextView) findViewById(R.id.txt_title);
		imgTitle.setImageResource(R.drawable.groupsms);
		txtTitle.setText(getResources().getString(R.string.groups));
		new ShowGroups().execute();
	}

	public class ShowGroups extends
			AsyncTask<String, Void, ArrayList<HashMap<String, String>>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(Groups.this);
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

			showGroups(mylist);

			super.onPostExecute(mylist);

			try {
				pd.dismiss();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void showGroups(final ArrayList<HashMap<String, String>> mylist) {
		String[] from = new String[] { "id", "name" };
		int[] to = new int[] { R.id.contact_id, R.id.contact_name };
		final ListView listview = (ListView) findViewById(R.id.listview_inbox);

		ListAdapter adapter = new SimpleAdapter(this, mylist,
				R.layout.call_log_item, from, to);
		listview.setAdapter(adapter);

		listview.setTextFilterEnabled(true);
		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Map<String, String> map = mylist.get(position);
				String msg_Id = map.get("id");
				String group_name = map.get("name");
				moveToCompose(group_name + "(" + msg_Id + ")");

			}
		});

	}

	private void moveToCompose(String groupName) {
		Intent composeIntent = new Intent(getApplicationContext(),
				Compose.class);
		composeIntent.putExtra("to", groupName);
		startActivity(composeIntent);
		finish();
	}

}
