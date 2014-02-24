package com.example.emulator;


import com.example.emulator.EmulatorService.LocalBinder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
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

public class Emulator extends Activity {
	
	private Button btn_start,btn_stop;
	private Button mWifi_Setting;
	EmulatorService mService = null;
	NetworkInfo mWifiInfo;
	TextView wifiStatus;
	
	private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			Toast.makeText(Emulator.this, "onserviceDisConnected" ,Toast.LENGTH_SHORT).show();
			
		}
		@Override
		public void onServiceConnected(ComponentName arg0, IBinder service) {
			Toast.makeText(Emulator.this, "onserviceConnected" ,Toast.LENGTH_SHORT).show();
			LocalBinder binder = (LocalBinder) service;
			mService = binder.getService();
		}
	};
	
	private BroadcastReceiver wReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.d("ACTIVITY","action = "+action);
			if(WifiManager.WIFI_STATE_CHANGED_ACTION.equalsIgnoreCase(action)){	
				int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
				Log.d("ACTIVITY","state=="+state);
				if (state==WifiManager.WIFI_STATE_ENABLED){
					StringBuilder wifiString= new StringBuilder();
					wifiString.append("WIFI: ")	
					.append(mWifiInfo.isAvailable());
					wifiStatus.setText(wifiString);
					btn_start.setEnabled(true);
					btn_stop.setEnabled(true);
				}
			}
			}
		};

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		IntentFilter filter = new IntentFilter();
		filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);		
		registerReceiver(wReceiver,filter);
		
		setContentView(R.layout.activity_emulator);
		//make Button	
		btn_start= (Button) findViewById(R.id.btn_start);
		btn_stop= (Button) findViewById(R.id.btn_stop);
		mWifi_Setting = (Button)findViewById(R.id.wifi_setting);
		

		wifiStatus= (TextView) findViewById(R.id.wifi_state);
		ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		mWifiInfo = mConnectivityManager.getNetworkInfo(mConnectivityManager.TYPE_WIFI);
		
		StringBuilder wifiString= new StringBuilder();
		wifiString.append("WIFI: ")	
		.append(mWifiInfo.isAvailable());
	
		registerReceiver(wReceiver,filter);
		
		wifiStatus.setText(wifiString);
		mWifi_Setting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
				startActivity(gpsOptionsIntent); 
			}
		});

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
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		
		
	}

}