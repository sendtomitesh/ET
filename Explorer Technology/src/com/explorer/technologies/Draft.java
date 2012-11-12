package com.explorer.technologies;


import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Draft extends Activity {

	SentListAdapter sentAdapter;
	ImageView imgTitle;
	TextView txtTitle;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inbox);
        imgTitle = (ImageView)findViewById(R.id.image_title);
        txtTitle = (TextView)findViewById(R.id.txt_title);
        imgTitle.setImageResource(R.drawable.draft);
        txtTitle.setText(getResources().getString(R.string.drafts));
        
    }
           
}
