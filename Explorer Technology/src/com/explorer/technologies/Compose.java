package com.explorer.technologies;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class Compose extends Activity {

	Spinner spinnerLevel;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compose);
        loadLevel();
    }
    
    public void sendMessage(View v)
    {
    	Toast.makeText(getApplicationContext(), "Send Clicked",Toast.LENGTH_LONG).show();
    }
    
    public void globalInitialize()
    {
    	spinnerLevel = (Spinner)findViewById(R.id.spinner_level);
    }
	public void loadLevel() {

		spinnerLevel = (Spinner)findViewById(R.id.spinner_level);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.level_array,
				android.R.layout.simple_spinner_item);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinnerLevel.setAdapter(adapter);
		spinnerLevel.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				Log.d("Selected Item", "Position : " + position);
			
				Toast.makeText(getApplicationContext(), "Level : " + position,Toast.LENGTH_LONG).show();

			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}

		});

	}
}
