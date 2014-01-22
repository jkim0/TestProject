package com.example.service_y;

import com.example.service_y.MainActivity;
import com.example.service_y.R;
import com.example.service_y.SimpleService;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Resources.NotFoundException;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	
	TextView mTextview = null; 
	private Button startServiceButton;
	private Button stopServiceButton;
	private Button checkAliveButton;
	int time_save=0;
	int time=0;
	/****/
	private boolean isBound;
	ISimpleAIDL mSimpleService;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		startServiceButton = (Button)findViewById(R.id.button1);		
		stopServiceButton = (Button)findViewById(R.id.button2);
		checkAliveButton = (Button)findViewById(R.id.button3);
		
		startServiceButton.setOnClickListener(this);
		stopServiceButton.setOnClickListener(this);
		checkAliveButton.setOnClickListener(this);

		
		IntentFilter filter2 = new IntentFilter("com.example.action.update");
		registerReceiver(mReceiver, filter2);
		
		Intent intent = new Intent(this, SimpleService.class);
		 //정신차리길!!!		
		startService(intent); //바인드 서비스 해주고, 콜메서드 해줄거야
		bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
	
	}
	
/*****************대빵 중요해!*******************/
	
	private ServiceConnection mServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName pComponentName, IBinder pIBinder) {
			// TODO Auto-generated method stub
			mSimpleService = ISimpleAIDL.Stub.asInterface(pIBinder);
			// register listener
		}
		@Override
		public void onServiceDisconnected(ComponentName pComponentName) {
			// TODO Auto-generated method stub
			mSimpleService = null;
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		Intent intent = new Intent(this, SimpleService.class);
				switch(v.getId()){
					case R.id.button1:
						isBound=true;
						if(isBound){				
								try {	
										if(time!=0){mSimpleService.Change_Mode();}
										mSimpleService.add(time);											
									} catch (RemoteException e) { //catch Blcok
									// TODO Auto-generated catch block
									e.printStackTrace(); //error흔적 만들어주는 것	
								}
						}
						break;
					case R.id.button2:
					try {
						mSimpleService.Change_Mode();
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						stopService(new Intent(this, SimpleService.class));
						break;
					case R.id.button3:
						sendBroadcast(new Intent("com.example.action.isAlive"));
						break;
					}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			//Broadcast가 호출되면 여기로 오도록 하는거야.	
			int i=0;
	//		int count=0;
			TextView printing = (TextView)findViewById(R.id.textView1);
			
			i=intent.getIntExtra("counting", -3);
		//	Toast.makeText(MainActivity.this, "value:"+i, Toast.LENGTH_SHORT).show();
			printing.setText(i+"");
			time++;
		}
	};
	
	}
	
	

