package com.example.remoteservice2;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class RemoteService extends Activity implements OnClickListener {
	
	private int m_value=0;
	Button mB_StratService = null;
	Button mB_BindService = null;
	Intent m_intent;
	
	private IRemoteService mService = null;
	
	private IRemoteServiceCallback mCallback = new IRemoteServiceCallback.Stub() {
		
		@Override
		public void Increasing_Value(int value) throws RemoteException {
			// TODO Auto-generated method stub
			m_value=value;
			Toast.makeText(RemoteService.this, ""+m_value, Toast.LENGTH_SHORT).show();
			
		}
	};
	
	private ServiceConnection mConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			mService= null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Toast.makeText(RemoteService.this, "OnServiceConnected", Toast.LENGTH_SHORT).show();
			// TODO Auto-generated method stub
			mService = IRemoteService.Stub.asInterface(service);
	
			try {
				Toast.makeText(RemoteService.this, "onservice", Toast.LENGTH_SHORT).show();
				mService.registerCallback(mCallback);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				Log.e("onServiceConeccted", "onServiceConnected");
				e.printStackTrace();
			}
	
		}
	};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remote_service);
		Toast.makeText(RemoteService.this, "OnCreate", Toast.LENGTH_SHORT).show();
		mB_StratService = (Button)findViewById(R.id.StartService);
		mB_BindService = (Button)findViewById(R.id.BindService);
		
		
		Button btn1= (Button)findViewById(R.id.btn1);
		btn1.setOnClickListener(RemoteService.this);
		mB_BindService.setOnClickListener(RemoteService.this);
		mB_StratService.setOnClickListener(RemoteService.this);
		
		m_intent= new Intent(this, MessengerService.class);

		Toast.makeText(RemoteService.this, "1:m_intent"+m_intent, Toast.LENGTH_LONG).show();
		Toast.makeText(RemoteService.this, "1:mConnection"+mConnection, Toast.LENGTH_LONG).show();
		startService(m_intent);
		Toast.makeText(RemoteService.this, "2:m_intent"+m_intent, Toast.LENGTH_LONG).show();
		bindService(m_intent, mConnection, Context.BIND_AUTO_CREATE);

		Toast.makeText(RemoteService.this, "2:cm_intent"+m_intent, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub		
		switch (v.getId())
		{
		case R.id.StartService: 
			Toast.makeText(RemoteService.this, "StartService1", Toast.LENGTH_SHORT).show();
			startService(m_intent);
			Toast.makeText(RemoteService.this, "StartService2", Toast.LENGTH_SHORT).show();
			
		break;
		
		case R.id.BindService:
			Toast.makeText(RemoteService.this, "BindService1", Toast.LENGTH_SHORT).show();
			Log.i("Activity", "BindService");
			bindService(m_intent, mConnection, Context.BIND_AUTO_CREATE);

			Toast.makeText(RemoteService.this, "BindService2", Toast.LENGTH_SHORT).show();
			Log.i("Activity", "BindService");
		break;
		case R.id.btn1:
			try {
				int i= mService.example(0);
				Toast.makeText(RemoteService.this, "int::"+i, Toast.LENGTH_LONG).show();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
