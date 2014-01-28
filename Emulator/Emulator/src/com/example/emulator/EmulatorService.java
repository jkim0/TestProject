package com.example.emulator;

import java.io.File;
import java.io.IOException;

import com.example.emulator.NanoHTTPD;
import com.example.emulator.R;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;


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
		
		
	};
      
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
