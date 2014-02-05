package com.example.service_y;

import com.example.service_y.MainActivity;
import com.example.service_y.R;
import com.example.service_y.SimpleService;

import android.R.integer;
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
	private Button resetButton;
	private Button increase;
	private Button decrease;
	private Button reset;
	private Button useListener;
	private Button useBroadcast;
	private Button stop;
	private Button bind_plus;
	private Button bind_minus;
	private Button unbind;
	private Button bind;
	
	int time_save=0;
	int time=0;
	public static final int S_INCREASE=1;
	public static final int S_DECREASE=2;
	public static final int S_STOP=3;
	
	private static final int CHANGE_COUNT=0;
	/****/
	private boolean isBound;
	ISimpleAIDL mSimpleService=null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		startServiceButton = (Button)findViewById(R.id.start_counting);		
		stopServiceButton = (Button)findViewById(R.id.stop_counting);
		resetButton = (Button)findViewById(R.id.btn_reset);
		increase = (Button)findViewById(R.id.btn_increase);
		decrease = (Button)findViewById(R.id.btn_decrease);
		useListener = (Button)findViewById(R.id.btn_listener);
		useBroadcast=(Button)findViewById(R.id.btn_broadcast);
		stop=(Button)findViewById(R.id.btn_stop);
		
		bind_plus=(Button)findViewById(R.id.btn_bind_plus);
		bind_minus=(Button)findViewById(R.id.btn_bind_minus);
		unbind=(Button)findViewById(R.id.btn_unbind);
		bind =(Button)findViewById(R.id.btn_bind);
		
		startServiceButton.setOnClickListener(this);
		stopServiceButton.setOnClickListener(this);
		resetButton.setOnClickListener(this);
		increase.setOnClickListener(this);
		decrease.setOnClickListener(this);
		useListener.setOnClickListener(this);
		useBroadcast.setOnClickListener(this);
		stop.setOnClickListener(this);
		bind_plus.setOnClickListener(this);
		bind_minus.setOnClickListener(this);
		unbind.setOnClickListener(this);
		bind.setOnClickListener(this);
		
		mTextview = (TextView) findViewById(R.id.textView1);
		
		Intent intent = new Intent(this, SimpleService.class);		
		startService(intent); //바인드 서비스 해주고, 콜메서드 해줄거야
		bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);	

	}
	
	@Override
	public void onClick(View v) {
		int check=0;
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, SimpleService.class);
				switch(v.getId()){
					case R.id.start_counting:
						//when presseing start_counting button
						isBound=true;
						if(isBound){				
								try {	
									mSimpleService.startCounting();
									} catch (RemoteException e) { //catch Blcok				
									}
						}	
							break;
					case R.id.stop_counting:
						//when pressing stop_counting button
						try {
							mSimpleService.stopCounting();
						} catch (RemoteException e) {
							e.printStackTrace();
						}
						stopService(intent);
					break;
					case R.id.btn_reset:
						//when presseing reset button 
						try {
							mSimpleService.reset();
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					break;
					case R.id.btn_bind:
						bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);	
						break;
					case R.id.btn_unbind:
						try {
							mSimpleService.unregisterListener(mListener);
							unbindService(mServiceConnection);
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
		//Button implementation with startSERVICE
					case R.id.btn_increase:
					{
						intent.putExtra("select", S_INCREASE);
						startService(intent);}
						break;
					case R.id.btn_decrease:{
						intent.putExtra("select", S_DECREASE);
						startService(intent);}
						break;
					case R.id.btn_stop:
						intent.putExtra("select", S_STOP);
						startService(intent);
						break;
				//until this, we use startservice + onstartCommand		
					case R.id.btn_bind_plus:
						try {
							mSimpleService.setMode(SimpleService.MODE_INCREASE);
						} catch (RemoteException e) {
							e.printStackTrace();
						}
						//			sendBroadcast(new Intent("com.example.action.isAlive"));	
					break;
					case R.id.btn_bind_minus:
						try {
							mSimpleService.setMode(SimpleService.MODE_DECREASE);
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					break;					
					case R.id.btn_listener:
					try {
						mSimpleService.selectIPC(true);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
					case R.id.btn_broadcast:
					try {
						mSimpleService.selectIPC(false);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					break;
				
				}	
					
	}
	
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
			case CHANGE_COUNT:
				mTextview.setText(Integer.toString(msg.arg1));
			}
			//super.handleMessage(msg);
		}
	};
	
	private ISimpleListener mListener= new ISimpleListener.Stub() {
		@Override
		public void Printing(int count) throws RemoteException {
			// TODO Auto-generated method stub.
			mHandler.sendMessage(mHandler.obtainMessage(CHANGE_COUNT,count,-1));
		}
	};
	
	
	private ServiceConnection mServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName pComponentName, IBinder pIBinder) {
			// TODO Auto-generated method stub
			mSimpleService = ISimpleAIDL.Stub.asInterface(pIBinder);
			try {
				mSimpleService.registerListener(mListener);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			// register listener
		}
		@Override
		public void onServiceDisconnected(ComponentName pComponentName) {
			mSimpleService = null;
		}
	};


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
		
			String action = intent.getAction();
			if(action.equalsIgnoreCase(SimpleService.ACTION_BRAODCAST)){
				int value= intent.getIntExtra("counting",-4);
				mTextview.setText(Integer.toString(value));
			}
		}
	};
	
	}
	
	

