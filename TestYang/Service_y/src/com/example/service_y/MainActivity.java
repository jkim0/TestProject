package com.example.service_y;

import com.example.service_y.MainActivity;
import com.example.service_y.R;
import com.example.service_y.SimpleService;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
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
	private Handler mHandler;
	
	int m_time=0;
	
	/****/
	private boolean isBound;
	ISimpleAIDL mSimpleService;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mHandler = new Handler();
		
		startServiceButton = (Button)findViewById(R.id.button1);		
		stopServiceButton = (Button)findViewById(R.id.button2);
		checkAliveButton = (Button)findViewById(R.id.button3);
		
		startServiceButton.setOnClickListener(this);
		stopServiceButton.setOnClickListener(this);
		checkAliveButton.setOnClickListener(this);
		mTextview = (TextView)findViewById(R.id.textView1);
	/*	checkAliveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});*/ //정신차리길!!!
		Intent intent = new Intent(this, SimpleService.class);
//		startService(intent); //바인드 서비스 해주고, 콜메서드 해줄거야
		bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
	
	}

	
/*****************대빵 중요해!*******************/
	
	private ServiceConnection mServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName pComponentName, IBinder pIBinder) {
			// TODO Auto-generated method stub
			Toast.makeText(MainActivity.this, "onService",Toast.LENGTH_LONG);
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
		//int time=0;
				switch(v.getId()){
					case R.id.button1: //
						//Start service : 1)바인드 서비스 2)메서드 호출 3)toast
						//bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
						Toast.makeText(this, "come5", Toast.LENGTH_LONG).show();
						isBound=true;
			//여기서 질문하기, 왜 for문으로 무한 못돌려?
						if(isBound){
					
								try {
								//	time=mSimpleService.add(0);
									Log.i("TEST", "mtime :" + mSimpleService.add(0));
									//Toast.makeText(this, "okay",Toast.LENGTH_SHORT).show();
									while(m_time<10){
									 m_time =mSimpleService.add(m_time);
										Toast.makeText(this," "+m_time, Toast.LENGTH_SHORT).show();
										mHandler.post(mRunnable);
										mHandler.postAtFrontOfQueue(mRunnable);
										mHandler.postDelayed(mRunnable, 1000);
										//mTextview.setText(""+m_time);
										
									}
									} catch (RemoteException e) { //catch Blcok
									// TODO Auto-generated catch block
								//	e.printStackTrace(); //error흔적 만들어주는 것	
								}
						}
						
						
						/*thread 구현 		
						Thread  t = new mThread();
						t.start(); */
						
						//	mTextview.setText(intent.getExtras().toString());
						break;
					case R.id.button2:
					//	unbindService(mServiceConnection);
						stopService(new Intent(this, SimpleService.class));
						break;
					case R.id.button3:
						sendBroadcast(new Intent("com.example.action.isAlive"));
						//i= intent.getExtras().getInt("printing_number");
						break;
					}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	/* thread구현부분 *
	public class mThread extends Thread {
		int mCount = 0;
		@Override
		public void run() {
			while(mCount <1000) {
				++mCount;
				Log.i("TEST", "mCount :" + mCount);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		*/
	
	private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mTextview.setText(""+m_time);
        }
    };
		
}
	
	

