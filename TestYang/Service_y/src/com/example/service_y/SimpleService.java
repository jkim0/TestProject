package com.example.service_y;

import java.sql.Date;
import java.text.SimpleDateFormat;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class SimpleService extends Service {
	int mCount = 0;
	int i=0;
	
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Toast.makeText(context,"onRECEIVE" ,Toast.LENGTH_LONG).show();
		}
	}; 

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
	if(mReceiver!= null){
		unregisterReceiver(mReceiver);
	}
	Toast.makeText(this, "onDestroy", Toast.LENGTH_LONG).show();
	//	super.onDestroy();
	}
	
	/*
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//TODO Auto-generated method stub
		Toast.makeText(this, "onStartCommand", Toast.LENGTH_LONG).show();
		IntentFilter filter = new IntentFilter("com.example.action.isAlive");
		registerReceiver(mReceiver, filter);
	
		mThread sThread = new mThread();
		sThread.start();
		
		for(int i=0; i<3; i++){
			Toast.makeText(this, i, Toast.LENGTH_LONG).show();
		}
		
		
		return Service.START_NOT_STICKY;
		//	return super.onStartCommand(intent, flags, startId);
	}*/
		
/****서비스에서 인터페이스 구현할 차례********
 * IsimpleAIDL.Stud()을 사용하여 AIDL에서 정의한 메서드를 구현하고, 
 * 서비스에 바인드 될때 호출되는 onBind()메서든느 앞에서 구현한 바인더 객체를 반환하도록 구현한다* 
 *****/
	
	private IBinder mBinder = new ISimpleAIDL.Stub() {
		//RemoteException?이게 뭔지 모르겟당! 질문
		@Override
		
		public int add(int value) throws RemoteException {
			// TODO Auto-generated method stub
			mCount = value;
	//		Toast.makeText(SimpleService.this, value +"", Toast.LENGTH_SHORT).show();
			//Toast.makeText(SimpleService.this, mCount +":add", Toast.LENGTH_SHORT).show();
			Log.i("TEXT","333mcount:"+mCount);
			return ++mCount;
		}
		public String add2(long timeInMillis)throws RemoteException{
			Date date = new Date(timeInMillis);
			SimpleDateFormat sdf= new SimpleDateFormat("MM/dd HH:mm");
			return sdf.format(date);
		}
	};
	/*
	public class mThread extends Thread {
		boolean mCheck=true;
		@Override
		public void run() {
			while(mCheck=true) {
				++mCount;
				Log.i("TEST","mcheck:"+mCheck);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}*/
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "onBind()", Toast.LENGTH_LONG).show();
		return mBinder;
		//return null;
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
        Toast.makeText(this, "onUnbind()", Toast.LENGTH_LONG).show();
		return super.onUnbind(intent);
	}
	
	
}
