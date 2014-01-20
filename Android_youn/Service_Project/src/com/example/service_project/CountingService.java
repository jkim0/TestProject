package com.example.service_project;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;


public class CountingService extends Service{
	private final IBinder mBinder = new LocalBinder();
	int num=0;
	int i;
	public class LocalBinder extends Binder{
		
		CountingService getService(){
			
			return CountingService.this;
		}
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "OnBind", Toast.LENGTH_SHORT).show();
		num++;
		return mBinder;
	}
	
	
	
	
	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}




	public int getCountingNumber()
	{
		//Toast.makeText(this, "GetCounter", Toast.LENGTH_SHORT).show();
		return num;
	}

}