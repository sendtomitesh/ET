package com.explorer.technologies;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.WindowManager;

public class SplashScreen extends Activity {

	 private Thread mSplashThread;
	 SharedPreferences sp;
	
	/** Called when the activity is first created. */
	@SuppressLint("WorldReadableFiles")
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
		 getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    setContentView(R.layout.splash_screen);
        
	    sp=getSharedPreferences("credentials", MODE_WORLD_READABLE);
        if(Utility.getSharedPrefValues(sp))
        {
        	new loginAPI().execute();
        }
        else
        {
        	loadSplash();
        }
        // The thread to wait for splash screen events
       	    // TODO Auto-generated method stub
	}
	private void loadSplash() {
		// TODO Auto-generated method stub
		final SplashScreen sPlashScreen = this;
		 mSplashThread =  new Thread(){
	            @Override
	            public void run(){
	                try {
	                    synchronized(this){
	                        // Wait given period of time or exit on touch
	                        wait(2000);
	                    }
	                }
	                catch(InterruptedException ex){                    
	                }

	                finish();
	                
	                // Run next activity
	                Intent intent = new Intent();
	                intent.setClass(sPlashScreen, Login.class);
	                startActivity(intent);
	                finish();               
	            }
	        };
	        
	        mSplashThread.start();        
	            

	}
	private void movetoDashboard()
	{
		Intent dashboardIntent = new Intent(getApplicationContext(), Main.class);
		startActivity(dashboardIntent);
		finish();
	}
	private void movetoLogin() {
		// TODO Auto-generated method stub
		Intent dashboardIntent = new Intent(getApplicationContext(), Login.class);
		startActivity(dashboardIntent);
		finish();
	}
	@Override
    public boolean onTouchEvent(MotionEvent evt)
    {
        if(evt.getAction() == MotionEvent.ACTION_DOWN)
        {
            synchronized(mSplashThread){
                mSplashThread.notifyAll();
            }
        }
        return true;
    }    
	
	
	public class loginAPI extends AsyncTask<String, Integer, String>
	{
		
		int apiresult;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			apiresult=APICalls.userLogin(Utility.username ,Utility.password );
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			
			if(apiresult==0)
			{
				
				movetoDashboard();
			}
			else if(apiresult==1)
			{
				movetoLogin();
			}
			
			super.onPostExecute(result);
		}
		
	}

}
