package com.explorer.technologies;

import java.util.ArrayList;
import java.util.HashMap;

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
import android.widget.Toast;

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
		new ShowGroups().execute("mobiled", "1234");
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
			return APICalls.getGroups(args[0], args[1]);

		}

		@Override
		protected void onPostExecute(ArrayList<HashMap<String, String>> mylist) {

			showGroups(mylist);

			super.onPostExecute(mylist);

			try {
				pd.dismiss();
			} catch (Exception e) {
			}
		}

	}

	public void showGroups(ArrayList<HashMap<String, String>> mylist) {
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
				@SuppressWarnings("unchecked")
				HashMap<String, String> o = (HashMap<String, String>) listview
						.getItemAtPosition(position);

				String name = o.get("name") + "(" + o.get("id") + ")";
				Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG)
						.show();
				gotoCompose(name);

			}
		});

	}
	
	private void gotoCompose(String groupName)
	{	
		Intent composeIntent = new Intent(getApplicationContext(), Compose.class);
		composeIntent.putExtra("groupName", groupName);
		startActivity(composeIntent);
		finish();
	}

}
