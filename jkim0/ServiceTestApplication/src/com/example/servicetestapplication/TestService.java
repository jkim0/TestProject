package com.example.servicetestapplication;

import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

public class TestService extends Service {
	private static final String TAG = "TestService";
	
	private int mCount = 0;
	private boolean mUseBroadcast = true;	
	
	private static final int MSG_CHANGE_COUNT = 0;
	public static final int MODE_INCREASE = 0;
	public static final int MODE_DECREASE = 1;
	private int mMode = MODE_INCREASE;
	
	public static final String ACTION_NOTIFY_COUNT_CHANGED = "com.example.servicetestapplication.COUNT_CHANGED";
	
	final ArrayList<ICountingListener> mListeners = new ArrayList<ICountingListener>();
	
	private final ITestService.Stub mBinder = new ITestService.Stub() {
		@Override
		public void registerCountingListener(ICountingListener cb) {
			Log.e(TAG, "registerCounringListener");
			if (cb != null) mListeners.add(cb);
		}
		
		@Override
		public void unregisterCountingListener(ICountingListener cb) {
			Log.e(TAG, "unregisterCountingListener");
			if (cb != null) mListeners.remove(cb);
		}

		@Override
		public void setMode(int mode) throws RemoteException {
			// TODO Auto-generated method stub
			Log.e(TAG, "setMode() mode = " + mode);
			mMode = mode;
		}

		@Override
		public int getMode() throws RemoteException {
			// TODO Auto-generated method stub
			Log.e(TAG, "getMode()");
			return mMode;
		}

		@Override
		public void setUseListener(boolean use) throws RemoteException {
			// TODO Auto-generated method stub
			Log.e(TAG, "setUseListener() use listener : " + use);
			mUseBroadcast = !use;
		}

		@Override
		public void startCounting() throws RemoteException {
			// TODO Auto-generated method stub
			Log.e(TAG, "startCounting()");
			mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_CHANGE_COUNT), 1000);
		}

		@Override
		public void stopCounting() throws RemoteException {
			// TODO Auto-generated method stub
			Log.e(TAG, "stopCounting()");
			mHandler.removeMessages(MSG_CHANGE_COUNT);
		}

		@Override
		public void reset() throws RemoteException {
			// TODO Auto-generated method stub
			mCount = 0;
		}
	};
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_CHANGE_COUNT:
				if (mMode == MODE_INCREASE) {
					mCount++;
				} else {
					mCount--;
				}
				onCountChanged(mCount);
				mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_CHANGE_COUNT), 1000);
				break;
			}
		}
	};
	
	public void onCountChanged(int count) {
		Log.e(TAG, "onCountChanged count = " + count);
		if (mUseBroadcast) {
			Intent intent = new Intent();
			intent.setAction(ACTION_NOTIFY_COUNT_CHANGED);
			intent.putExtra("count", count);
			sendBroadcast(intent);
		} else {
			// implements
			for (int i = 0; i < mListeners.size(); i++) {
				ICountingListener cl = mListeners.get(i);
				try {
					cl.onCountChanged(count);
				} catch (RemoteException ex) {
					Log.e(TAG, "exception occured ex = ", ex);
				}
			}
		}
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onRebind(Intent intent) {
		// TODO Auto-generated method stub
		super.onRebind(intent);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.e(TAG, "onStartCommand intent = " + intent + " flags = " + flags + " startId = " + startId);
		boolean toIncrease = intent.getBooleanExtra("increase", true);
		
		boolean toStop = intent.getBooleanExtra("stop", false);
				mHandler.removeMessages(MSG_CHANGE_COUNT);		

		if (toStop) {
			stopSelf();
			return 0;
		}
		mMode = toIncrease ? MODE_INCREASE : MODE_DECREASE;
		mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_CHANGE_COUNT), 1000);
		
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}
}
