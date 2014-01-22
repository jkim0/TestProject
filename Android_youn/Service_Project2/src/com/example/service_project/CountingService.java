package com.example.service_project;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;


public class CountingService extends Service{
	//private final IBinder mBinder = new LocalBinder();
	int num=1;

	boolean running = true;
	
	WorkerThread add;
	
	class WorkerThread extends Thread{
		public void run()
		{
			while(running == true)
			{
				num++;
				try{
				Thread.sleep(1000);
				}catch (InterruptedException e){
				}
			}
		}
	}
	
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "OnBind", Toast.LENGTH_SHORT).show();
		
		add = new WorkerThread();
		add.start();
		return mbinder;
	}
	

	service.Stub mbinder = new service.Stub() {
		
		@Override
		public int getvalue() throws RemoteException {
			// TODO Auto-generated method stub
			return num;
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
				add = new WorkerThread();
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