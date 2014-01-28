package com.example.emulator;

import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class Emulator extends Activity {

	public final static int SCREEN_ON=1;
	public final static int SCREEN_OFF=2;
	private IntentFilter screenFilter;
	
	private Button btn_start,btn_stop,btn_screen_on,btn_screen_off;
	private EmulatorAIDL mService = null;
//질문, 여기에 intent 나 powermanager 할당하면, 에러나 ! 왜그럴까
	
	//////////////
	PowerManager mPm;
	PowerManager.WakeLock mWakeLock, mWakeLock2;
	
	private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			Toast.makeText(Emulator.this, "onserviceDisConnected" ,Toast.LENGTH_SHORT).show();
		}
		@Override
		public void onServiceConnected(ComponentName arg0, IBinder arg1) {
			Toast.makeText(Emulator.this, "onserviceConnected" ,Toast.LENGTH_SHORT).show();
			mService=EmulatorAIDL.Stub.asInterface(arg1);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_emulator);
		//setKeepScreenOn(true);
		
		mPm = (PowerManager) getApplication().getSystemService( Context.POWER_SERVICE );
		//ACQUIRED_로 받으면 또 에러...
		mWakeLock = mPm.newWakeLock(PowerManager.FULL_WAKE_LOCK , "power");
		mWakeLock2=  mPm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK ,"power");

//		mWakeLock = mPm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "power");
	//make Button	
		btn_start= (Button) findViewById(R.id.btn_start);
		btn_stop= (Button) findViewById(R.id.btn_stop);
		btn_screen_on=(Button) findViewById(R.id.btn_screen_on);
		btn_screen_off=(Button) findViewById(R.id.btn_screen_off);
	  // PowerManager pm = (PowerManager) getSystemService( Context.POWER_SERVICE );
	  // PowerManager.WakeLock wakeLock = pm.newWakeLock( PowerManager.SCREEN_DIM_WAKE_LOCK, "MY TAG" );
	//BindService	
		btn_start.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {		
				Intent intent = new Intent(Emulator.this, EmulatorService.class);
				bindService(intent,mConnection,Context.BIND_AUTO_CREATE);
				Toast.makeText(Emulator.this, "Bind()" ,Toast.LENGTH_SHORT).show();
			}
		});
	//unBindService
		btn_stop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				unbindService(mConnection);
				Toast.makeText(Emulator.this, "UnBind()" ,Toast.LENGTH_SHORT).show();
			}
		});

	
	
	//Screen_on
		btn_screen_on.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mWakeLock.acquire(); 
				boolean inScreenon= mPm.isScreenOn();
				Toast.makeText(Emulator.this, inScreenon+":",Toast.LENGTH_SHORT).show();
			//	getCurrentFocus().setKeepScreenOn(true);
			//	getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			}
		});
	//Screen_off	
		btn_screen_off.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {	
			
				Toast.makeText(Emulator.this,":-1",Toast.LENGTH_SHORT).show();
				Toast.makeText(Emulator.this,":0",Toast.LENGTH_SHORT).show();
				mWakeLock.release(); 
				if(mWakeLock==null){Toast.makeText(Emulator.this,"null",Toast.LENGTH_LONG).show();}
				Toast.makeText(Emulator.this,mWakeLock+":0",Toast.LENGTH_SHORT).show();
				
				Toast.makeText(Emulator.this,":1",Toast.LENGTH_SHORT).show();
				mWakeLock2.acquire();
				Toast.makeText(Emulator.this,":2",Toast.LENGTH_SHORT).show();
				boolean inScreenon= mPm.isScreenOn();
				Toast.makeText(Emulator.this,inScreenon+":4",Toast.LENGTH_SHORT).show();
			
//				wakeLock.release();
//				PowerManager pm = (PowerManager) getSystemService( Context.POWER_SERVICE );
//				PowerManager.WakeLock wakeLock = pm.newWakeLock( PowerManager.PARTIAL_WAKE_LOCK, EmulatorService.TAG );
//				wakeLock.acquire();
//				  // do something. 
//				  // the screen will stay on during this section.
//					wakeLock.release();
//					PARTIAL_WAKE_LOCK				
//				PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
//				pm.goToSleep(2000);
				// 안드로이드 상위버전에서는 슬립 안쓴다, wake 어쩌고저쩌고
			}
		});
	
		
	}	
	private BroadcastReceiver scrReceiver= new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
		String action = intent.getAction();
		if(action.equals(EmulatorService.TAG)){
			int cases = intent.getIntExtra("number",0);
			switch(cases){
			case SCREEN_ON:

				break;
			case SCREEN_OFF:
				  PowerManager pm= (PowerManager)getSystemService(Context.POWER_SERVICE);
				  PowerManager.WakeLock wakeLock= pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,EmulatorService.TAG);
		
				break;
			}
			
		}
			//	screenFilter = new IntentFilter(EmulatorService.TAG);
		}
	};

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.emulator, menu);
		return true;
	}

}
