package com.example.service_project;



import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
	IBinder activitybinder;
	
	
	private ServiceConnection mConnection = new ServiceConnection(){
		@Override
		public void onServiceConnected(ComponentName classname, IBinder service1)
		{
			Iservice = service.Stub.asInterface(service1);			
			//onBind에서 Return받은 Ibinder값으로 aidl의 Interface와 연동.
			try{   
    		    Iservice.register(mListen);
				} catch (RemoteException e){
				//e.printStackTrace();
				}
		}
		
		@Override
		public void onServiceDisconnected(ComponentName arg0){
			Iservice = null;
		}
	};

	private BroadcastReceiver MyReceiver = new BroadcastReceiver() {
			
			@Override
		   public void onReceive(Context context, Intent intent) {
				if (intent.getAction() == "Count")
				{
					try{   
		    		    num = Iservice.getvalue();
						} catch (RemoteException e){
						e.printStackTrace();
						}
					view.setText(" "+num);
					//Toast.makeText(context, "Intent Detected.", Toast.LENGTH_LONG).show();
				}
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

		/*
		button.setOnClickListener(new View.OnClickListener() {
	       public void onClick(View v) {
	    	   
	        }
	    });
		*/
		
		button2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try{
		    		   //Iservice.add();
		    		   Iservice.Change_Mode();
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
		
		 IntentFilter filter = new IntentFilter();
		 filter.addAction("Count");
	    registerReceiver(MyReceiver, filter);
		
		Intent intent = new Intent(this, CountingService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		Toast.makeText(MainActivity.this, "OnStart", Toast.LENGTH_SHORT).show();
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Toast.makeText(MainActivity.this, "OnResume", Toast.LENGTH_SHORT).show();
		
	}
		
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Toast.makeText(MainActivity.this, "OnPause", Toast.LENGTH_SHORT).show();
	
		unbindService(mConnection);
	}


	CountingListener.Stub mListen = new CountingListener.Stub() {
		@Override
		public void Print_Count() throws RemoteException {
			// TODO Auto-generated method stub
			//Toast.makeText(MainActivity.this, "Listen", Toast.LENGTH_SHORT).show();
			try{   
    		    num = Iservice.getvalue();
				} catch (RemoteException e){
				e.printStackTrace();
				}
			view.setText(" "+num);
		}
	}; 
	
	
	
/*

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
*/
}