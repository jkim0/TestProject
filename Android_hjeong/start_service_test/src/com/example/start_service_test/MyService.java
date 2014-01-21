package com.example.start_service_test;


	import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

	public class MyService extends Service {

	    private static final String TAG = "MyService";
	    
	    @Override
	    public IBinder onBind(Intent arg0) {
	        return null;
	    }
	 
	    @Override
	    public void onCreate() {
	        Toast.makeText(this, "Congrats! MyService Created", Toast.LENGTH_LONG).show();
	        Log.d(TAG, "onCreate");
	    }
	 
	    @Override
	    public void onStart(Intent intent, int startId) {
	        Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();
	        Log.d(TAG, "onStart");
	        push();
	        
	    //Note: You can start a new thread and use it for long background processing from here.
	    }
	 
	    private void push() {
			// TODO Auto-generated method stub
	    	Toast.makeText(this, "push", Toast.LENGTH_LONG).show();
	    	//sendBroadcast(new Intent("arabiannight.tistory.com.sendreciver.gogogo"));
	    	
			
		}
	    /*
	    @Override
	    public void onResume() {
	    	super.onResume();
	    	Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();	
	    }

	    @Override
	    public void onRestart() {
	    	super.onRestart();
	    	Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();	    	
	    }
	    @Override
	    public void onPause() {
	    	super.onPause();
	    	Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
	    }

	    @Override
	    public void onStop() {
	    	super.onStop();
	    	Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
	    }
*/
		@Override
	    public void onDestroy() {
	        Toast.makeText(this, "MyService Stopped", Toast.LENGTH_LONG).show();
	        Log.d(TAG, "onDestroy");
	    }
	}