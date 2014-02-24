package com.example.emulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.example.emulator.EmulatorService.LocalBinder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Instrumentation;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
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
import android.widget.CompoundButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class Emulator extends Activity {
	
	public final static int SCREEN_ON=1;
	public final static int SCREEN_OFF=2;
	public final static int FOR_SPINNER=3;
	private IntentFilter screenFilter;
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
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_emulator);
		//make Button	
		btn_start= (Button) findViewById(R.id.btn_start);
		btn_stop= (Button) findViewById(R.id.btn_stop);
		mWifi_Setting = (Button)findViewById(R.id.wifi_setting);
		

		wifiStatus= (TextView) findViewById(R.id.wifi_state);
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		mWifiInfo = mConnectivityManager.getNetworkInfo(mConnectivityManager.TYPE_WIFI);
		
		StringBuilder wifiString= new StringBuilder();
		wifiString.append("WIFI: ")	
		.append(mWifiInfo.isAvailable());
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
		
		if (mWifiInfo.isAvailable()){
			StringBuilder wifiString= new StringBuilder();
			wifiString.append("WIFI: ")	
			.append(mWifiInfo.isAvailable());
			wifiStatus.setText(wifiString);
			btn_start.setEnabled(true);
			btn_stop.setEnabled(true);
		}
		
	}

}