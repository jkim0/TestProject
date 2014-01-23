package com.example.service_project;


import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

public class CountingService extends Service{
	//private final IBinder mBinder = new LocalBinder();
	
	int Count=0;
	boolean running = false;
	boolean B_mode = false;
	boolean L_mode = false;
	CountingThread add;
	Handler handler;
	public CountingListener mListen;
	
	
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
				if(running == true)
				{
					if(B_mode)
					{
						Broadcastmode();
					}
					if(L_mode)
					{
						Linstener();
					}
				}
			}
		};
	}


	public void Broadcastmode(){
		Intent intent = new Intent();
		intent.setAction("Count");
		sendBroadcast(intent);
	}
	
	public void Linstener(){
		try {
			mListen.Print_Count();
		} catch (RemoteException ex) {
		}
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		//Toast.makeText(this, "OnBind", Toast.LENGTH_SHORT).show();
		
		return mbinder;
	}
	

	   service.Stub mbinder = new service.Stub() {
		
		@Override
		public void register(CountingListener register) throws RemoteException {
			// TODO Auto-generated method stub
			mListen = register;
		}
		
		@Override
		public int getvalue() throws RemoteException {
			// TODO Auto-generated method stub
			return Count;
		}
		
		@Override
		public void Change_Mode() throws RemoteException {
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
				Count--;
			}
		}
		
		@Override
		public void Listener_Mode() throws RemoteException {
			// TODO Auto-generated method stub
			B_mode = false;
			L_mode = true;
		}
		
		
		@Override
		public void Broadcast_Mode() throws RemoteException {
			// TODO Auto-generated method stub
			B_mode = true;
			L_mode = false;
		}
		
		@Override
		public void Start() throws RemoteException {
			// TODO Auto-generated method stub
			running = true;
			add = new CountingThread(handler);
			add.start();
		}
	};
	
	
	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		//Toast.makeText(this, "OnUnbind", Toast.LENGTH_SHORT).show();
		return false;
		//return super.onUnbind(intent);
	}
}
