package com.example.controller;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Process;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class RemoteService extends Service{

	
	
	final RemoteCallbackList<IRemoteServiceCallback> mCallbacks = new RemoteCallbackList<IRemoteServiceCallback>();
	
	int mValue = 0;
	NotificationManager mNM;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.e("onCreate", "startservice");
		mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		//Use with getSystemService(String) to retrieve a NotificationManager for informing the user of background events.
		
		showNotification();
		// Display a notification about us starting.
		mHandler.sendEmptyMessage(REPORT_MSG);
	}
	
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// Cancel the persistent notification.
		mNM.cancel(R.string.remote_service_started);
		//Cancel a previously shown notification. If it's transient, the view will be hidden. 
		//If it's persistent, it will be removed from the status bar.
		
		Toast.makeText(RemoteService.this, R.string.remote_service_stopped, Toast.LENGTH_SHORT).show();
		 // Unregister all callbacks.
		mCallbacks.kill();
		
		mHandler.removeMessages(REPORT_MSG);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		if(IRemoteService.class.getName().equals(intent.getAction())){
			return mBinder;
		}
		
		if(ISecondary.class.getName().equals(intent.getAction())){
			return mSecondaryBinder;
		}
		
		return null;
	}
	
	private final IRemoteService.Stub mBinder = new IRemoteService.Stub() {
		
		@Override
		public void unregisterCallback(IRemoteServiceCallback cb)
				throws RemoteException {
			// TODO Auto-generated method stub
			if (cb!=null) mCallbacks.unregister(cb);
			
		}
		@Override
		public void registerCallback(IRemoteServiceCallback cb)
				throws RemoteException {
			// TODO Auto-generated method stub
			if(cb !=null) mCallbacks.register(cb);
			
		}
	};
	
	private final ISecondary.Stub mSecondaryBinder = new ISecondary.Stub() {
		
		@Override
		public int getPid() throws RemoteException {
			// TODO Auto-generated method stub
			return Process.myPid();
		}
		
		@Override
		public void basicTypes(int anInt, long aLong, boolean aBoolean,
				float aFloat, double aDouble, String aString)
				throws RemoteException {
			// TODO Auto-generated method stub
			
		}
	};
	
	@SuppressLint("NewApi")
	@Override
	public void onTaskRemoved(Intent rootIntent) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Task removed: " + rootIntent, Toast.LENGTH_LONG).show();
		super.onTaskRemoved(rootIntent);
	}

	private static final int REPORT_MSG = 1;
	
	private final Handler mHandler = new Handler() {

		@Override	public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case REPORT_MSG:{
				int value = ++mValue;
				
				final int N = mCallbacks.beginBroadcast();
				Log.e("handler", ""+N);
				for(int i=0; i<N; i++){
					try {
						mCallbacks.getBroadcastItem(i).valueChanged(value);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				mCallbacks.finishBroadcast();
				
				sendMessageDelayed(obtainMessage(REPORT_MSG), 1*1000);
				
			} break;
			default:
				super.handleMessage(msg);
				
			}
			
		}	
	};
	private void showNotification() {
		// TODO Auto-generated method stub
		
		CharSequence text = getText(R.string.remote_service_started);
		Notification notification = new Notification(R.drawable.stat_sample, text, 
				System.currentTimeMillis());
		/**/
		//API 11부터 Notification은 빌더를 사용해 만들로록 바뀌었다.
		/*
		Notification notification = new Notification.Builder(getApplicationContext())
		.setContentTitle("Notification")
		.setContentText(text)
		.setSmallIcon(R.drawable.stat_sample)
		.setAutoCancel(true)
		.build();*/
		
		
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, 
				new Intent(this, Controller.class), 0);
		
		notification.setLatestEventInfo(this, getText(R.string.remote_service_label), 
				text, contentIntent);
		
		mNM.notify(R.string.remote_service_started, notification);
		
	}

}
