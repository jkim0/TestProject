package com.example.emulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
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
import android.widget.CompoundButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

public class Emulator extends Activity {
//
//	private ConnectivityManager mConnMnger;
//	private NetworkInfo wifiInfo;
//	private NetworkInfo mobileInfo;
//	private WifiManager mWifiMnger;
//	
//	public String[] from= new String[]{"bssid","ssid","capablities","frequency","level"};
//	public int[] to1 = new int[]{R.id.bssid, R.id.ssid, R.id.capabilities, R.id.frequency, R.id.level};
//    
//	
//	
	
	public final static int SCREEN_ON=1;
	public final static int SCREEN_OFF=2;
	public final static int FOR_SPINNER=3;
	private IntentFilter screenFilter;
	private Intent forSpinner;
	private Button btn_start,btn_stop;
//	private Button btn_wifi, btn_bluetooth, btn_broadcast;
	private EmulatorAIDL mService = null;	
	
	private Switch Mode_wifi;
	
	
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
//		btn_wifi=(Button) findViewById(R.id.btn_wifi);
//		btn_bluetooth=(Button) findViewById(R.id.btn_bluetooth);
		
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
//	
//		btn_wifi.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//					
//				 
//			}
//		});		
//
//		btn_bluetooth.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//			
//		
//			}
//		});		
//
//		btn_broadcast.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {	
//			}
//		});		
		
		Mode_wifi= (Switch) findViewById(R.id.wifi_switch);
		Mode_wifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				boolean status= isChecked;
				WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
				ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo mWifiInfo = mConnectivityManager.getNetworkInfo(mConnectivityManager.TYPE_WIFI);

				        if (status == true && !wifiManager.isWifiEnabled()) {
				            wifiManager.setWifiEnabled(true);				 
				            Toast.makeText(getApplicationContext(), "wifiInfo =" + mWifiInfo, Toast.LENGTH_LONG).show();
				             Log.d("WIFI_on","available = "+mWifiInfo.isAvailable());
				             Log.d("WIFI_on","available = "+mWifiInfo.isConnected());
				        } else if (status == false && wifiManager.isWifiEnabled()) {
				            wifiManager.setWifiEnabled(false);
				            Toast.makeText(getApplicationContext(), "wifiInfo = "+ mWifiInfo, Toast.LENGTH_LONG).show();
				            Log.d("WIFI_off","available = "+mWifiInfo.isAvailable());
				             Log.d("WIFI_off","available = "+mWifiInfo.isConnected()); 
				        
				        }			
				}
			
		});
//
//		public void toggle_wifi(boolean status){
//			 WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
//		        if (status == true && !wifiManager.isWifiEnabled()) {
//		            wifiManager.setWifiEnabled(true);
//		        } else if (status == false && wifiManager.isWifiEnabled()) {
//		            wifiManager.setWifiEnabled(false);
//		        }			
//		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.emulator, menu);
		return true;
	}
	


}
