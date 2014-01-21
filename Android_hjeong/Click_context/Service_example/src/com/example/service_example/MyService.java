package com.example.service_example;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

public class MyService extends Service{
	/*
//	aidl 추가 하는 방법!
	hyeon.Stub ibiner = new hyeon.Stub() {
		
		@Override
		public int getvalue() throws RemoteException {
			// TODO Auto-generated method stub
			return num;
		}
	};*/
	LayoutInflater mInflater = null;

	private final IBinder binder = new MyBinder();
	public int num = 0;
    public int volum = 0;
	
    @Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "onBind()", Toast.LENGTH_SHORT).show();
		return binder;
	}
	
	  public void onCreate() {
	        super.onCreate();
	        Toast.makeText(this, "onCreate()", Toast.LENGTH_SHORT).show();
	    }

	public void onDestroy() {
	        super.onDestroy();
	        Toast.makeText(this, "onDestroy()", Toast.LENGTH_SHORT).show();
	    }
	   
	  public int onStartCommand(Intent intent, int flags, int startId) {
		  
		  super.onStartCommand(intent, flags, startId);
		  
//	        int i;
//	        Save_Number increase = new Save_Number();
	        
	        
//	        for (i = 0; i < 40; i++) {
//	            num++;
//	            increase.save(i);
	            
//	            if(num%10==0){
	            	
//	                Toast.makeText(this, "startcommand num : "+num,
//	                                             Toast.LENGTH_SHORT).show();               
//	            }
//	        }
	        return START_STICKY;
	    }
	  
	  public boolean onUnbind(Intent intent) {
	        Toast.makeText(this, "onUnbind()", Toast.LENGTH_SHORT).show();
	        return super.onUnbind(intent);
	    }
	  	  
	  public class MyBinder extends Binder{

	        int getService(){
	            Toast.makeText(MyService.this, "binder num : " + num, Toast.LENGTH_SHORT).show();
	            return num;
	        }
	        int volumUp(){
	            if(volum!=100){
	                volum = volum+10;
	                
	            }
	            return volum;
	        }
	        int volumDown(){
	            if(volum!=0){
	                volum = volum-10;
	               
	            }
	            return volum;
	        }
	       
	    }
}


