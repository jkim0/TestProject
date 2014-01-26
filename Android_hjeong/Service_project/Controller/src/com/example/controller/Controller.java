package com.example.controller;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.os.Process;

public class Controller extends Activity {
	
	IRemoteService mService = null;
	ISecondary mSecondaryService = null;
	
	Button mKillButtion;
	TextView mCallbackText;
	
	private boolean mIsBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remote_service_controller);
        
        Button startservice_button = (Button)findViewById(R.id.start);
        startservice_button.setOnClickListener(mStartListener);
        startservice_button = (Button)findViewById(R.id.stop);
        startservice_button.setOnClickListener(mStopListener);
        Button bindservice_button = (Button)findViewById(R.id.bind);
        bindservice_button.setOnClickListener(mBindListener);
        bindservice_button = (Button)findViewById(R.id.unbind);
        bindservice_button.setOnClickListener(mUnbindListener);
        mKillButtion = (Button)findViewById(R.id.kill);
        mKillButtion.setOnClickListener(mKillListener);
        mKillButtion.setEnabled(false);
        
        mCallbackText = (TextView)findViewById(R.id.callback);
        mCallbackText.setText("Not attached.");
        
        
    }
    
    private ServiceConnection mConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			mService=null;
			mKillButtion.setEnabled(false);
			mCallbackText.setText("Disconnected.");
			
			Toast.makeText(Controller.this, R.string.remote_service_disconnected, 
					Toast.LENGTH_SHORT).show();
			
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			mService = IRemoteService.Stub.asInterface(service);
			mKillButtion.setEnabled(true);
			mCallbackText.setText("Attached.");
			
			try {
				mService.registerCallback(mCallback);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Toast.makeText(Controller.this, R.string.remote_service_connected, 
					Toast.LENGTH_SHORT).show();
			
		}
	};
	
	private ServiceConnection mSecondaryConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			mSecondaryService = null;
			mKillButtion.setEnabled(false);
			
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			mSecondaryService = ISecondary.Stub.asInterface(service);
			mKillButtion.setEnabled(true);
			
		}
	};
    
    private OnClickListener mBindListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			bindService(new Intent(IRemoteService.class.getName()), 
					mConnection, Context.BIND_AUTO_CREATE);
			bindService(new Intent(IRemoteService.class.getName()), 
					mSecondaryConnection, Context.BIND_AUTO_CREATE);
			mIsBound = true;
			mCallbackText.setText("Binding.");
		}
	};
	
	private OnClickListener mUnbindListener  = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(mIsBound){
				if(mService !=null){
					try {
						mService.unregisterCallback(mCallback);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				unbindService(mConnection);
				unbindService(mSecondaryConnection);
				mKillButtion.setEnabled(false);
				mIsBound = false;
				mCallbackText.setText("Unbinding.");
			}
		}
	};
    
    private OnClickListener mStartListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			startService(new Intent("com.example.controller.RemoteService"));
			Log.e("startservice", "startservice");
			
		}
    };
    
    private OnClickListener mStopListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			stopService(new Intent("com.example.controller.RemoteService"));
			Log.e("stopservice", "stopservice");
			
		}
	};
	
	private OnClickListener mKillListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(mSecondaryService != null){
				try {
					int pid = mSecondaryService.getPid();
					Process.killProcess(pid);
					mCallbackText.setText("Killed serviced process.");
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(Controller.this, R.string.remote_call_failed, 
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	};
	
	private IRemoteServiceCallback mCallback = new IRemoteServiceCallback.Stub() {
		
		@Override
		public void valueChanged(int value) throws RemoteException {
			// TODO Auto-generated method stub
			mHandler.sendMessage(mHandler.obtainMessage(BUMP_MSG, value, 0));
		}
	};

	private static final int BUMP_MSG =1;
	
	private Handler mHandler = new Handler(){

		@Override 	public void handleMessage(Message msg) {
			switch(msg.what){
				case BUMP_MSG:
					mCallbackText.setText("Received from service: "+msg.arg1);
				break;
				default:
					super.handleMessage(msg);
					
			}			
		}
	
	};
}
