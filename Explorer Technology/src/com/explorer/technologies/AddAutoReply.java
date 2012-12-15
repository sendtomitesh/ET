package com.explorer.technologies;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;



public class AddAutoReply extends Activity {

	EditText textTo, textMessage;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_auto_reply);
		globalInitialize();
		
	}

	public void createAutoReply(View v)
    {
		String to = textTo.getText().toString();
		String message = textMessage.getText().toString();
		
		if(to.length() < 0 || message.length() < 0){
			Utility.alert(getApplicationContext(), "Please fill all fields");
			//Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_LONG).show();
		}
		else{
			Boolean checkAleradyExist;
			
			checkAleradyExist = DatabaseFunctions.checkAutoReply(to);
			//Toast.makeText(AddAutoReply.this, checkAleradyExist.toString(), Toast.LENGTH_LONG).show();
			if(!checkAleradyExist)
			{
				DatabaseFunctions.addAutoReply(to, message);
				Toast.makeText(AddAutoReply.this, "Auto Reply Created Successfully", Toast.LENGTH_LONG).show();
				finish();
			}
			else{
				Utility.alert(getApplicationContext(), "Auto Reply already set for this keyword");
				//Toast.makeText(AddAutoReply.this, "Auto Reply already set for this keyword", Toast.LENGTH_LONG).show();
			}
			
		}
		
    }
	
	public void globalInitialize() {
		textTo = (EditText) findViewById(R.id.txt_to);
		textMessage = (EditText) findViewById(R.id.txt_message);
	}

		
}
