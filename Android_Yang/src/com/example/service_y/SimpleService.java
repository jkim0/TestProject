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
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class SimpleService extends Service {
	boolean running=true;
	int mCount = 0;
	int i=0;
	Intent intent2 =new Intent("com.example.action.update");
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			//Broadcast가 호출되면 여기로 오도록 하는거야.	
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
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	// TODO Auto-generated method stub
//		Toast.makeText(this, "onStartCommand", Toast.LENGTH_LONG).show();
		IntentFilter filter = new IntentFilter("com.example.action.isAlive");
		registerReceiver(mReceiver, filter);
	
		mThread sThread = new mThread();
		sThread.start();
		
		

		return Service.START_NOT_STICKY;
		//	return super.onStartCommand(intent, flags, startId);
	}
		
/****서비스에서 인터페이스 구현할 차례********
 * IsimpleAIDL.Stud()을 사용하여 AIDL에서 정의한 메서드를 구현하고, 
 * 서비스에 바인드 될때 호출되는 onBind()메서든느 앞에서 구현한 바인더 객체를 반환하도록 구현한다* 
 *****/
	
	private IBinder mBinder = new ISimpleAIDL.Stub(){
		@Override
		
		public int add(int value) throws RemoteException {
			// TODO Auto-generated method stub
			mCount = value;
			return mCount;
		}
		public String add2(long timeInMillis)throws RemoteException{
			Date date = new Date(timeInMillis);
			SimpleDateFormat sdf= new SimpleDateFormat("MM/dd HH:mm");
			return sdf.format(date);
		}
		@Override
		public boolean Change_Mode() throws RemoteException {
			// TODO Auto-generated method stub
				if (running==true){
						running=false;}
				else {
					running=true;
			    	mThread sThread2= new mThread();
					sThread2.start();
				}
				return running;
			}
	};
	
	public class mThread extends Thread {
		
		@Override
		public void run() {
			while(running==true) {
				++mCount;
			//브로드캐스트 계속날리기
				intent2.putExtra("counting", mCount);
				sendBroadcast(intent2);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	
	}
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return mBinder;
		//return null;
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
  //      Toast.makeText(this, "onUnbind()", Toast.LENGTH_LONG).show();
		return super.onUnbind(intent);
	}
	
	
}
