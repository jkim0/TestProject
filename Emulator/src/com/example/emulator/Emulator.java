package com.example.emulator;

import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class Emulator extends Activity {

	private Button btn_start,btn_stop;
	private Intent intent = new Intent(Emulator.this, EmulatorService.class);
	private EmulatorAIDL mService = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_emulator);
		
		btn_start= (Button) findViewById(R.id.btn_start);
		btn_stop= (Button) findViewById(R.id.btn_stop);
		
		
		btn_start.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			
				bindService(intent,mConnection,Context.BIND_AUTO_CREATE);
			}
		});
		btn_stop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			
				unbindService(mConnection);
			}
		});
		
		
	private ServiceConnection mConnection = new ServiceConnection() {
			
			@Override
			public void onServiceDisconnected(ComponentName arg0) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onServiceConnected(ComponentName arg0, IBinder arg1) {
				// TODO Auto-generated method stub

				mService=Emulator.Stub.asInterface(arg1);
			}
		};
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.emulator, menu);
		return true;
	}

}
