package com.example.service_project;

import com.example.service_project.CountingService.LocalBinder;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

	CountingService mService;
	boolean mBound = false;
	int num=0;
	
	private Button button;
	private TextView view;
	
	
	private ServiceConnection mConnection = new ServiceConnection(){
		@Override
		public void onServiceConnected(ComponentName classname, IBinder service)
		{
			LocalBinder binder = (LocalBinder)service;
			mService = binder.getService();
			mBound = true;
		}
		
		@Override
		public void onServiceDisconnected(ComponentName arg0){
			mService = null;
			mBound = false;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toast.makeText(MainActivity.this, "OnCreate", Toast.LENGTH_SHORT).show();
		
		
		button = (Button)findViewById(R.id.StartService);
		view = (TextView)findViewById(R.id.Text);
		
		button.setOnClickListener(new View.OnClickListener() {
	       public void onClick(View v) {
	    	   Toast.makeText(MainActivity.this, "Onclick", Toast.LENGTH_SHORT).show();
	    	   
	    	   if(mBound)
	    	   {
	    			num = mService.getCountingNumber();
	    		   	view.setText(""+num);
	    		  
	    		   //Toast.makeText(MainActivity.this, "number"+num, Toast.LENGTH_SHORT).show();
	    	   }
	    	  
	    	   else
	    	   {
	    		   view.setText("Fail");
	    	   }
	    	   
	        	
	        }
	    });
	
	}
		
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		Toast.makeText(MainActivity.this, "OnStart", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(this, CountingService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}
	
	
	
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Toast.makeText(MainActivity.this, "OnStop", Toast.LENGTH_SHORT).show();
		if(mBound)
		{
			unbindService(mConnection);
			mBound = false;
		}
	}




	

/*

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
*/
}