package com.example.emulator;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class Emulator extends Activity {

	public final static int SCREEN_ON=1;
	public final static int SCREEN_OFF=2;
	public final static int FOR_SPINNER=3;
	private IntentFilter screenFilter;
	private Intent forSpinner;
	private Button btn_start,btn_stop,btn_screen_on,btn_screen_off;
	private Button btn_spinner;
	private EmulatorAIDL mService = null;	
	
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

		//make Button	
		btn_start= (Button) findViewById(R.id.btn_start);
		btn_stop= (Button) findViewById(R.id.btn_stop);
		btn_screen_on=(Button) findViewById(R.id.btn_screen_on);
		btn_screen_off=(Button) findViewById(R.id.btn_screen_off);
		btn_spinner=(Button) findViewById(R.id.btn_spinner);
		
//BindService	
		btn_start.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {		
				Intent intent = new Intent(Emulator.this, EmulatorService.class);
				bindService(intent,mConnection,Context.BIND_AUTO_CREATE);
				Toast.makeText(Emulator.this, "Bind()" ,Toast.LENGTH_SHORT).show();
				Intent i = new Intent(Intent.ACTION_VIEW);
				Uri u= Uri.parse("http://localhost:8091");
				i.setData(u);
				startActivity(i);	
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
	
		btn_spinner.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				forSpinner= new Intent(Emulator.this, EmulatorSpinner.class);
				startActivity(forSpinner);
			}
		});		
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.emulator, menu);
		return true;
	}
	
	

}
