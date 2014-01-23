package com.example.hjeong_assignment3;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;
//쓰레드 만들기
public class ServiceExample01 extends Service{
	int mCount2=0;
	int mCount=0;
	Intent m_intent = new Intent("start");
	
	final RemoteCallbackList<ISend_Value>mCallbacks = new RemoteCallbackList<ISend_Value>();
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		IncreaingValue(mCount2);
		Log.e("onCreate","onCreate" );
	
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Toast.makeText(ServiceExample01.this, "OnDestroy", Toast.LENGTH_LONG).show();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		ADD_Thread add_Thread = new ADD_Thread();
//		add_Thread.setDaemon(true);
//		add_Thread.start();
		Toast.makeText(ServiceExample01.this, "OnCreate", Toast.LENGTH_LONG).show();	
		Toast.makeText(ServiceExample01.this, "OnStartCommand", Toast.LENGTH_LONG).show();			
		return START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		Log.e("OnBinder", "OnBinder");
		return (IBinder) mBinder;
	}
	
	private final IRemoteService.Stub mBinder =new IRemoteService.Stub() {
		
		@Override
		public void Unregistercallback(ISend_Value cb) throws RemoteException {
			// TODO Auto-generated method stub
			if(cb!= null)
				mCallbacks.unregister(cb);
			
		}
		
		@Override
		public void Registercallback(ISend_Value cb) throws RemoteException {
			// TODO Auto-generated method stub
			if(cb !=null)
				mCallbacks.register(cb);
			
		}
	};
	
	private void IncreaingValue(int mCount2) {
		// TODO Auto-generated method stub
		final int N = mCallbacks.beginBroadcast();
		for(int i=0; i<N; i++){
			
			try {
				mCallbacks.getBroadcastItem(i).Recieve_Value(mCount2);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		mCallbacks.finishBroadcast();
	}
	/*
	IBinder mBinder = new ISend_Value.Stub() {
		@Override
		public int Recieve_Value(int value) throws RemoteException {
			// TODO Auto-generated method stub
			return ++mCount;
		}
	};*/
	
	
	class ADD_Thread extends Thread{
		public void run() {
			
			while(true){
				try {
					mCount++;
					Log.i("thread", ""+mCount);
					m_intent.putExtra("counting",mCount);
					sendBroadcast(m_intent);
					Thread.sleep(1000);					
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
	}
	
}