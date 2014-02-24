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
//
//	private ConnectivityManager mConnMnger;
//	private NetworkInfo wifiInfo;
//	private NetworkInfo mobileInfo;
//	private WifiManager mWifiMnger;
//	
//	public String[] from= new String[]{"bssid","ssid","capablities","frequency","level"};
//	public int[] to1 = new int[]{R.id.bssid, R.id.ssid, R.id.capabilities, R.id.frequency, R.id.level};

	
	public final static int SCREEN_ON=1;
	public final static int SCREEN_OFF=2;
	public final static int FOR_SPINNER=3;
	private IntentFilter screenFilter;
	private Intent forSpinner;
	private Button btn_start,btn_stop;
	private Button mWifi_Setting;
//	private Button btn_wifi, btn_bluetooth, btn_broadcast;
	EmulatorService mService = null;
	NetworkInfo mWifiInfo;
	TextView wifiStatus;
	
	
//List for Device Info
	private class DeviceInfo{
		String name;
		String address;
		private DeviceInfo(String _name, String _addr){

			this.name = _name;
			this.address = _addr;
		}
		private String getName(){
			return name;
		}
		private String getAddress(){
			return address;
		}
	}
	
	
	private BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
	private ArrayList<DeviceInfo> mBTList,mWifiList = null;
	private Switch Mode_wifi,Mode_blueTooth;
	
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
	

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(BluetoothDevice.ACTION_FOUND.equalsIgnoreCase(action)){
				BluetoothDevice device =intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if(device.getBondState() !=BluetoothDevice.BOND_BONDED){
				DeviceInfo di = new DeviceInfo("device.getName()","device.getAddress()");
				mBTList.add(di);
				}
			}
			else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equalsIgnoreCase(action)){
				setProgressBarIndeterminateVisibility(false);
				if( mBTList.size()==0){
					// 검색되는 장치가 하나도없습니다.
					Log.e("BlueTooth","There is no any device able to connected");
//					String noDevices = getText(R.string.none_found).toString();
//					mNewDevicesArrayAdapter.add(noDevices);
				}
			}
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

		/**/
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
		
	
		Mode_blueTooth= (Switch) findViewById(R.id.bluetooth_switch);
		Mode_blueTooth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// 연결을 클릭한다면
				
				if(isChecked==true){
					mBtAdapter.cancelDiscovery();
					//이제 호출 그만하고, 연결할꺼야
					//getremotedevice 
					Log.d("BT","howmany bluetooth connections?= "+ mBTList.size());
					//연결?
					String addr=null;
					
					for(int i=0; i< mBTList.size();i++){	
					 addr= mBTList.get(i).getAddress();
					}
					BluetoothDevice device = mBtAdapter.getRemoteDevice(addr);
					Boolean check=true;
					while(check){
						if (mBtAdapter.isEnabled()){
							Toast.makeText(Emulator.this,"BLUETOOTH is connected " , Toast.LENGTH_LONG).show();
							break;
						}
					}
				}
				else{
					
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
