package com.example.service_y;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class SimpleService extends Service {
	boolean running=true;
	int mCount = 0;
	int save=0;
	int i=0;
	private static final int CHANGE_COUNT=0;
	public static final int MODE_INCREASE=0;
	public static final int MODE_DECREASE=1;
	private boolean mUseListener=true;
	private int mMode=MODE_INCREASE;
	private int S_DEFAULT=0;
	public static final String ACTION_BRAODCAST = "Using_broadcast";
	
	final RemoteCallbackList<ISimpleListener> mlist = new RemoteCallbackList<ISimpleListener>();
	
	@Override
	public void onDestroy() {

	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		int check=	intent.getIntExtra("select", -4);	
		switch(check){
			case MainActivity.S_INCREASE:
				mMode=MODE_INCREASE;
			case MainActivity.S_DECREASE:
				mMode=MODE_DECREASE;
			case MainActivity.S_STOP:
				mHandler.removeMessages(CHANGE_COUNT);
				stopSelf();
				return 0;
			}
			mHandler.sendMessageDelayed(mHandler.obtainMessage(CHANGE_COUNT),1000 );
		return super.onStartCommand(intent, flags, startId);
			//		return Service.START_NOT_STICKY;
		//	return super.onStartCommand(intent, flags, startId);
	}
		
/****서비스에서 인터페이스 구현할 차례********
 * IsimpleAIDL.Stud()을 사용하여 AIDL에서 정의한 메서드를 구현하고, 
 * 서비스에 바인드 될때 호출되는 onBind()메서든느 앞에서 구현한 바인더 객체를 반환하도록 구현한다* 
 *******/

	private ISimpleAIDL.Stub mBinder = new ISimpleAIDL.Stub() {
		
		@Override
		public void unregisterListener(ISimpleListener callback)
				throws RemoteException {
		if(callback!=null) mlist.unregister(callback);
		}
		
		@Override
		public void stopCounting() throws RemoteException {

			Toast.makeText(SimpleService.this, "stop-counting", Toast.LENGTH_SHORT).show();
			mHandler.removeMessages(CHANGE_COUNT);
		}
		
		@Override
		public void startCounting() throws RemoteException {
			Toast.makeText(SimpleService.this, "startcounting", Toast.LENGTH_SHORT).show();
			mHandler.sendMessageDelayed(mHandler.obtainMessage(CHANGE_COUNT), 1000);
		}
		
		@Override
		public void registerListener(ISimpleListener callback)
				throws RemoteException {
			if(callback!=null) mlist.register(callback);
		}
		
		@Override
		public int getvalue(int value) throws RemoteException {	
			return mCount;
		}
		
		@Override
		public boolean Change_Mode() throws RemoteException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void reset() throws RemoteException {
			// TODO Auto-generated method stub
			mCount=0;
		}

		@Override
		public void setMode(int mode) throws RemoteException {
			mMode = mode;			
			Log.i("SETMODE","MODE:"+mode);
		}
		

		@Override
		public int getMode() throws RemoteException {
			return mMode;
		}

		@Override
		public void selectIPC(boolean use) throws RemoteException {
			// TODO Auto-generated method stub

			Toast.makeText(SimpleService.this, "use:"+use, Toast.LENGTH_SHORT).show();
			mUseListener=use;
		}

	
	};
	
	private void updateCount(int count){
		if(mUseListener){
			Toast.makeText(SimpleService.this,"Listener",Toast.LENGTH_SHORT).show();
				int k=mlist.getRegisteredCallbackCount();
				for (int j=0; j< k ; j++ ){
					//one.Printing(count);
					onCallbackPrintLog(count);
				}
		}
		else{

			Toast.makeText(SimpleService.this,"Broadcast",Toast.LENGTH_SHORT).show();
			Intent intent = new Intent();
			intent.setAction(ACTION_BRAODCAST);
			intent.putExtra("counting",count);
			sendBroadcast(intent);
		}
	}
	// 이부분 잘 살펴보
	private synchronized void onCallbackPrintLog(int count) {
		int N = mlist.beginBroadcast();
		try {
			for( int i = 0; i < N; i++ ) {
					mlist.getBroadcastItem(i).Printing(count);
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mlist.finishBroadcast();
	}
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
		switch (msg.what){
			case CHANGE_COUNT:

				Log.i("Handler","mMODE:"+mMode);
				if(mMode==MODE_INCREASE){
					mCount++;
					}
				else if(mMode==MODE_DECREASE)
					mCount--;
				}
			updateCount(mCount);			
				mHandler.sendMessageDelayed(mHandler.obtainMessage(CHANGE_COUNT), 1000);
			}
			//super.handleMessage(msg);
		};
	
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	@Override
	public void onRebind(Intent intent) {
		super.onRebind(intent);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
		//return null;
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}
	
}
