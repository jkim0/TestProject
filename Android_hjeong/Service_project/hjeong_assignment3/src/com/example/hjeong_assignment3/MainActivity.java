package com.example.hjeong_assignment3;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	
	Button m_Button_start = null;
	Button m_Button_stop = null;
	Button m_Button_bindSatrt=null;
	Button m_Button_getValue=null;
	Intent m_intent=null;
	TextView m_ValuePrint=null;
	//ISend_Value mISend_Value;
	int mValue=0;
		
	private IRemoteService mService = null;
	private Handler handler = new Handler(){
		public void handleMessage(Message msg){
			if(msg.what == 1)
				m_ValuePrint.setText(mValue);
			else
				m_ValuePrint.setText("error");
			
		}
	};
	
	private ISend_Value mISend_Value = new ISend_Value.Stub() {
		
		@Override
		public int Recieve_Value(int value) throws RemoteException {
			// TODO Auto-generated method stub
			mValue = value;
			handler.sendEmptyMessage(1);
			return 0;
		}
	};
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		m_Button_start = (Button)findViewById(R.id.start);
		m_Button_stop = (Button)findViewById(R.id.stop);
		m_Button_bindSatrt=(Button)findViewById(R.id.bind_start);
		m_Button_getValue=(Button)findViewById(R.id.getValue);
		m_ValuePrint = (TextView)findViewById(R.id.write_value);
		
		m_Button_start.setOnClickListener(this);
		m_Button_stop.setOnClickListener(this);
		m_Button_bindSatrt.setOnClickListener(this);
		m_Button_getValue.setOnClickListener(this);
		
		m_intent = new Intent(MainActivity.this, ServiceExample01.class);
		
		
		IntentFilter filter = new IntentFilter("start");//filter 정의
		registerReceiver(Receiver, filter);
	}
	
private BroadcastReceiver Receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			int getValue;
			getValue= intent.getIntExtra("counting", -3); //defaul  null로 하면 error
			m_ValuePrint.setText(""+getValue);
		}
	};
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId())
		{
		case R.id.start:
			Toast.makeText(MainActivity.this, "Start Service", Toast.LENGTH_SHORT).show();
			startService(m_intent);
			Log.e("Start", "Start");
			break;
			
		case R.id.stop:
			Toast.makeText(MainActivity.this, "Stop Service", Toast.LENGTH_SHORT).show();
			stopService(m_intent);
			break;	
			
		case R.id.bind_start:
			Toast.makeText(MainActivity.this, "bind Service", Toast.LENGTH_LONG).show();
			bindService(m_intent, mConnection, BIND_AUTO_CREATE);
			break;
			
		case R.id.getValue:
			try {
				mValue=mISend_Value.Recieve_Value(mValue);
				Toast.makeText(MainActivity.this, " "+mValue, Toast.LENGTH_SHORT).show();
				Log.e("getValue", ""+mValue);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}
	/*
	ServiceConnection bindService = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub			
			mISend_Value = null;
		}		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			
			mISend_Value = ISend_Value.Stub.asInterface(service);
		}
	};*/
	
	ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub			
			mService = null;
		}		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			
			mService = IRemoteService.Stub.asInterface(service);
			try {
				mService.Registercallback(mISend_Value);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				Log.e("TAG", "mConnection", e);
			}
		}
	};
	
}

