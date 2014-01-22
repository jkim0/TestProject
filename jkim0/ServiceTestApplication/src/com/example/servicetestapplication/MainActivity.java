package com.example.servicetestapplication;

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
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	private static final int MSG_UPDATE_TEXT = 0;
	private ITestService mService = null;
	private TextView mText = null;
	private Button mBind = null;
	private Button mUnBind = null;
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			Log.e(TAG, "onReceive action = " + action);
			if (action.equals(TestService.ACTION_NOTIFY_COUNT_CHANGED)) {
				int value = intent.getIntExtra("count", 0);
				if (mText != null)
					mText.setText(Integer.toString(value));
			}
		}
	};
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_UPDATE_TEXT:
				if (mText != null) {
					mText.setText(Integer.toString(msg.arg1));
				}
				break;
			}
		}
	};
	
	private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			Log.e(TAG, "onServiceConnected");
			mService = ITestService.Stub.asInterface(service);
			try {
				mService.registerCountingListener(mCountingListener);
				mService.startCounting();
			} catch (RemoteException ex) {
				Log.e(TAG, "exception occured when register listener.", ex);
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated metICountingListenerhod stub
			Log.e(TAG, "onServiceDisconnected");
			mService = null;			
		}
	};
	
	private ICountingListener mCountingListener = new ICountingListener.Stub() {		
		@Override
		public void onCountChanged(int count) throws RemoteException {
			// TODO Auto-generated method stub
			Log.e(TAG, "ICountingListener::onCountChanged() count = " + count);
			mHandler.sendMessage(mHandler.obtainMessage(MSG_UPDATE_TEXT, count, -1));
		}
	};
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mText = (TextView)findViewById(R.id.value);
        
        Button increase = (Button)findViewById(R.id.increase);
        increase.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, TestService.class);
				intent.putExtra("increase", true);
				startService(intent);
				
			}
		});
        
        Button decrease = (Button)findViewById(R.id.decrease);
        decrease.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, TestService.class);
				intent.putExtra("increase", false);
				startService(intent);
				
			}
		});
        
        Button stop = (Button)findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, TestService.class);
				intent.putExtra("stop", true);
				startService(intent);
			}
		});
        
        Button bind = (Button)findViewById(R.id.bind);
        bind.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, TestService.class);
				bindService(intent, mConnection, Context.BIND_AUTO_CREATE);				
			}
		});
        mBind = bind;
        
        Button unBind = (Button)findViewById(R.id.unbind);
        unBind.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					mService.unregisterCountingListener(mCountingListener);
				} catch (RemoteException ex) {
					Log.e(TAG, "exception occured while unregister listener.", ex);
				}
				
				unbindService(mConnection);				
			}
		});
        mUnBind = unBind;
        
        Button btnIncrease = (Button)findViewById(R.id.increase2);
        btnIncrease.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					mService.setMode(TestService.MODE_INCREASE);
				} catch (RemoteException ex) {
					Log.e(TAG, "exception occured while set increase mode.", ex);
				}				
			}
		});
        
        Button btnDecrease = (Button)findViewById(R.id.decrease2);
        btnDecrease.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					mService.setMode(TestService.MODE_DECREASE);
				} catch (RemoteException ex) {
					Log.e(TAG, "exception occured while set decrease mode.", ex);
				}
			}
		});
        
        Button btnBroadcast = (Button)findViewById(R.id.broadcast);
        btnBroadcast.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					mService.setUseListener(false);
				} catch (RemoteException ex) {
					Log.e(TAG, "exception occured while set use broadcast.", ex);
				}
			}
		});
        
        Button btnListener = (Button)findViewById(R.id.listener);
        btnListener.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					mService.setUseListener(true);
				} catch (RemoteException ex) {
					Log.e(TAG, "exception occured while set use listener.", ex);
				}
			}
		});
        
        Button startCounting = (Button)findViewById(R.id.start_count);
        startCounting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					mService.startCounting();
				} catch (RemoteException ex) {
					Log.e(TAG, "exception occured while start counting.", ex);
				}
				
			}
		});
        
        Button stopCounting = (Button)findViewById(R.id.stop_count);
        stopCounting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					mService.stopCounting();
				} catch (RemoteException ex) {
					Log.e(TAG, "exception occured while stop counting.", ex);
				}
			}
		});
        
        Button reset = (Button)findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					mService.reset();
				} catch (RemoteException ex) {
					Log.e(TAG, "exception occured while reset.", ex);
				}
			}
		});
    }

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		unregisterReceiver(mBroadcastReceiver);
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
       IntentFilter filter = new IntentFilter();
       filter.addAction(TestService.ACTION_NOTIFY_COUNT_CHANGED);
       registerReceiver(mBroadcastReceiver, filter);
		super.onResume();
	}
}
