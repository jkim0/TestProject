package com.example.emulator;

import java.io.File;
import java.io.IOException;

<<<<<<< HEAD
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
=======
import com.example.emulator.NanoHTTPD;
import com.example.emulator.R;

>>>>>>> 3e938b24639f369e410dd5a42ab19c3cce853f1d
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
<<<<<<< HEAD
=======
import android.widget.Toast;
>>>>>>> 3e938b24639f369e410dd5a42ab19c3cce853f1d


public class EmulatorService extends Service {
	public static final String TAG = "SCREEN";
	private EmulatorAIDL.Stub mBinder = new EmulatorAIDL.Stub() {
		
		@Override
		public void openfile() throws RemoteException {
	
			File path =Environment.getExternalStorageDirectory();
	     //   Log.e("F_OPEN", "############## path = " + path);
	        File wwwroot = path.getAbsoluteFile();
			Toast.makeText(EmulatorService.this, ""+wwwroot, Toast.LENGTH_LONG).show();
				try {
					new NanoHTTPD(8090, wwwroot);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

		@Override
		public void closefile() throws RemoteException {
		
		}

		@Override
		public void unregisterCallback(EmulatorAIDLCallback cb)
				throws RemoteException {
			unregisterCallback(cb);
		}
		
		@Override
		public void registerCallback(EmulatorAIDLCallback cb)
				throws RemoteException {
			registerCallback(cb);
		}
<<<<<<< HEAD
	};

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		showNotification();
		NanoHttpd();
	}
	
	private void NanoHttpd() {
		// TODO Auto-generated method stub
		Log.e("onCreate","onCreate");
	       // File path = Environment.getExternalStorageDirectory();
	        //
	        File path = new File("/mnt");
	        Log.e("Nano_Server", "############## path = " + path);
	        File wwwroot = path.getAbsoluteFile();
	        //File file = new File(path, "filename");
	        /**/
	        try {
				NanoHTTPD Nano = new NanoHTTPD(8090, wwwroot);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		
	}

=======
		
		
	};
      
>>>>>>> 3e938b24639f369e410dd5a42ab19c3cce853f1d
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

<<<<<<< HEAD
	private void showNotification() {
		// TODO Auto-generated method stub
		CharSequence text = getText(R.string.remote_service_started);
		
		Notification notification = new Notification(R.drawable.stat_sample, text, 
				System.currentTimeMillis());
		
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, 
				new Intent(this, Emulator.class), 0);	
		
		notification.setLatestEventInfo(this, getText(R.string.remote_service_label),
				text, contentIntent);
		
		mNM.notify(R.string.remote_service_started, notification);
	}
=======
>>>>>>> 3e938b24639f369e410dd5a42ab19c3cce853f1d
}
