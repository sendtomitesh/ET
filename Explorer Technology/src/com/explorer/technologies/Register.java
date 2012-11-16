package com.explorer.technologies;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Register extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        
        
    }
     
    public void getRegister(View v)
    {
    	Toast.makeText(getApplicationContext(), "Registered!", Toast.LENGTH_LONG).show();
    }
    
    public void gotoLogin(View v)
    {
    	Intent loginIntent = new Intent(getApplicationContext(),Login.class);
    	startActivity(loginIntent);
    	finish();
    }
}
