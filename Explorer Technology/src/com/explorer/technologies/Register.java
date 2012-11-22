package com.explorer.technologies;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity {
	ProgressDialog pd;
	EditText textFullname,textUsername,textPassword,textMobile,textAddress,textRetypePassword;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        initializeGlobals();
        
    }
     
    private void initializeGlobals()
    {
    	textFullname = (EditText)findViewById(R.id.txt_fullname);
    	textUsername = (EditText)findViewById(R.id.txt_username);
    	textPassword = (EditText)findViewById(R.id.txt_password);
    	textRetypePassword = (EditText)findViewById(R.id.txt_retype_password);
    	textMobile = (EditText)findViewById(R.id.txt_mobile);
    	textAddress = (EditText)findViewById(R.id.txt_address);
    }
    
   
    public void getRegister(View v)
    {
    	String fullname = textFullname.getText().toString();
    	String username = textUsername.getText().toString();
    	String password = textPassword.getText().toString();
    	String retypePassword = textRetypePassword.getText().toString();
    	String mobile = textMobile.getText().toString();
    	String address = textAddress.getText().toString();
    	Boolean checkBlankFields = checkBlankString(new String[]{fullname,username,password,retypePassword,mobile,address});
    	if(!checkBlankFields){
    		Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_LONG).show();
    		
    	}else if (!password.equals(retypePassword)) {
    		Toast.makeText(getApplicationContext(), "Password mismatch!", Toast.LENGTH_LONG).show();
		}else{
			RegisterCall register = new RegisterCall();
	    	register.execute(username, password, fullname, address, mobile);
    	}
    	
    }
    private boolean checkBlankString(String[] fields){
        for(int i=0; i<fields.length; i++){
            String currentField=fields[i];
            if(currentField.toString().length()<=0){
                return false;
            }
        }
        return true;
    }
    
    public void gotoLogin(View v)
    {
    	Intent loginIntent = new Intent(getApplicationContext(),Login.class);
    	startActivity(loginIntent);
    	finish();
    }
    
    public class RegisterCall extends AsyncTask<String, Integer, Integer>
	{
		ProgressDialog pd;
		//int apiresult;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pd = new ProgressDialog(Register.this);
			pd.setTitle("Registration");
			pd.setMessage("Please wait...");
			pd.setIndeterminate(false);
			pd.show();
			super.onPreExecute();
		}
		@Override
		protected Integer doInBackground(String... params) {
			// TODO Auto-generated method stub
			return  APICalls.userRegistration(params[0],params[1],params[2],params[3],params[4]);
		}
		@SuppressLint("WorldWriteableFiles")
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			pd.dismiss();
			if(result==0){
				Toast.makeText(getApplicationContext(), "Registered!", Toast.LENGTH_LONG).show();
				Intent loginIntent = new Intent(getApplicationContext(),Login.class);
		    	startActivity(loginIntent);
		    	finish();
		    	
			}
			else if(result ==1){
				Toast.makeText(getApplicationContext(), "Could not register", Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(getApplicationContext(), "error occured", Toast.LENGTH_LONG).show();
			}
			super.onPostExecute(result);
		}
	}
}
