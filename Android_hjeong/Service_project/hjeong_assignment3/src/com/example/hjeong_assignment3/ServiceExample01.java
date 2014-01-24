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
		Toast.makeText(this, "Oncreate", Toast.LENGTH_SHORT);
		IncreaingValue(mCount2);
		Log.e("onCreate","onCreate" );
	}
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
//		ADD_Thread add_Thread = new ADD_Thread();
//		add_Thread.setDaemon(true);
//		add_Thread.start();
		//Toast.makeText(ServiceExample01.this, "OnCreate", Toast.LENGTH_LONG).show();	
		Toast.makeText(ServiceExample01.this, "OnStartCommand", Toast.LENGTH_LONG).show();
		//Toast  만들어주고  show를 하지 않으면 error
		return START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Onbind", Toast.LENGTH_SHORT).show();
		Log.e("OnBinder", "OnBinder");
		return mBinder;
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
		final int N = mCallbacks.beginBroadcast();//register에 등록된 함수의 계수를 받아온다.
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