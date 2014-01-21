package com.example.service_project;


import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
	CountingService mService;
	
	int num;
	
	service Iservice;
	private Button button;
	private Button button2;
	public TextView view;
	boolean mode = true;
	
	PrintThread print;
	
	public class PrintThread extends Thread{
		Handler handler;
		int number=0;
		TextView view = (TextView)findViewById(R.id.Text);
		public void run()
		{
			while(mode == true)
			{
				try{
					Thread.sleep(1000);
				}catch (InterruptedException e){
				}
				
				try{   
	    		    number = Iservice.getvalue();
					} catch (RemoteException e){
					e.printStackTrace();
					}
			
	    	   Log.i("Number","number:"+number);
			}
		}
		
		PrintThread(Handler handler)
		{this.handler = handler;}
	}
	
	
	private ServiceConnection mConnection = new ServiceConnection(){
		@Override
		public void onServiceConnected(ComponentName classname, IBinder service1)
		{
			Iservice = service.Stub.asInterface(service1);
		}
		
		@Override
		public void onServiceDisconnected(ComponentName arg0){
			Iservice = null;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toast.makeText(MainActivity.this, "OnCreate", Toast.LENGTH_SHORT).show();
 	  
		button = (Button)findViewById(R.id.StartService);
		view = (TextView)findViewById(R.id.Text);
		button2 = (Button)findViewById(R.id.Change_Mode);		
		
		button.setOnClickListener(new View.OnClickListener() {
	       public void onClick(View v) {
	   			print  = new PrintThread();
	   			print.start();
	    	   Toast.makeText(MainActivity.this, "Onclick", Toast.LENGTH_SHORT).show();
	    	 
	    	   
	    	   
	    	   //view.setText(" "+num);   
	        }
	    });
		
		
		button2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try{
		    		   //Iservice.add();
		    		   mode = Iservice.Change_Mode();
						} catch (RemoteException e){
						e.printStackTrace();
						}
			}
		});
	}
		
	
	
	
	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Intent intent = new Intent(this, CountingService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		Toast.makeText(MainActivity.this, "OnStart", Toast.LENGTH_SHORT).show();
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}
		
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Toast.makeText(MainActivity.this, "OnPause", Toast.LENGTH_SHORT).show();
	
		unbindService(mConnection);
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