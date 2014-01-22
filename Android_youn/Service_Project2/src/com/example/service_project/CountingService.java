package com.example.service_project;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


public class CountingService extends Service{
	//private final IBinder mBinder = new LocalBinder();
	
	int Count=1;
	boolean running = true;
	CountingThread add;
	Handler handler;
	
	
	class CountingThread extends Thread{
		Handler handler;
		public void run()
		{
			while(running == true)
			{
				Count++;
				try{
				Thread.sleep(1000);
				}catch (InterruptedException e){
				}
				
				Message msg = new Message();
				handler.sendMessage(msg);
			}
		}
		CountingThread(Handler handler)				//생성자
		{this.handler = handler;}
	}


	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		
		handler = new Handler(){
			public void handleMessage(Message msg){
				
				Intent intent = new Intent();
				intent.setAction("Count");
				sendBroadcast(intent);
				
			}
		};
		
		
		Toast.makeText(this, "ServiceOnCreate", Toast.LENGTH_SHORT).show();
		
		add = new CountingThread(handler);
		add.start();
	}


	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "OnBind", Toast.LENGTH_SHORT).show();
		
		return mbinder;
	}
	

	service.Stub mbinder = new service.Stub() {
		
		@Override
		public int getvalue() throws RemoteException {
			// TODO Auto-generated method stub
			return Count;
		}
		
		@Override
		public boolean Change_Mode() throws RemoteException {
			// TODO Auto-generated method stub
			if(running==true)
			{
				running = false;
			}
			
			else
			{
				running = true;
				add = new CountingThread(handler);
				add.start();
			}
			
			return running;
		}
	};
	
	
	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "OnUnbind", Toast.LENGTH_SHORT).show();
		return false;
		//return super.onUnbind(intent);
	}
}