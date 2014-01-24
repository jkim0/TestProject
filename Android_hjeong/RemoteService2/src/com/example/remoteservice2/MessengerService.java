package com.example.remoteservice2;

import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class MessengerService extends Service{

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Toast.makeText(MessengerService.this, "onstartcommand", Toast.LENGTH_SHORT).show();
		return super.onStartCommand(intent, flags, startId);
	}

	int m_value;
	private static final String TAG = "MessengerService";
	
	//final RemoteCallbackList<IRemoteServiceCallback> mCallbacks = new RemoteCallbackList<IRemoteServiceCallback>();
	final ArrayList<IRemoteServiceCallback> mCallbacks = new ArrayList<IRemoteServiceCallback>();
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub

		Toast.makeText(MessengerService.this, "onbind", Toast.LENGTH_LONG).show();
		/*
		if(IRemoteService.class.getName().equals(intent.getAction())){
			return mBinder;	
		}*/
		return mBinder;
	}
	
	private final IRemoteService.Stub mBinder = new IRemoteService.Stub() {
		
		@Override
		public void unregisterCallback(IRemoteServiceCallback cb)
				throws RemoteException {
			// TODO Auto-generated method stub
			Log.e(TAG, "unregisterCounringListener");
			if(cb != null) mCallbacks.remove(cb);
		}
		
		@Override
		public void registerCallback(IRemoteServiceCallback cb)
				throws RemoteException {
			// TODO Auto-generated method stub
			Log.e(TAG, "registerCounringListener");
			if(cb != null) mCallbacks.add(cb);
		}

		@Override
		public int example(int i) throws RemoteException {
			// TODO Auto-generated method stub
			return i;
		}
		
	};
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Toast.makeText(MessengerService.this, "3333onCreate", Toast.LENGTH_SHORT).show();
		RemoteServiceStart();
	}

	private void RemoteServiceStart() {
		// TODO Auto-generated method stub
		int N = mCallbacks.size();
		
		for(int i=0; i<N; i++)
			try {
				mCallbacks.get(i).Increasing_Value(m_value);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//mCallbacks.finishBroadcast();
	}
	
}
