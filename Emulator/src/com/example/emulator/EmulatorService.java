package com.example.emulator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("NewApi")
public class EmulatorService extends Service {

	public static final String TAG = "SCREEN";
	public final static int SCREEN_ON = 1;
	final RemoteCallbackList<EmulatorAIDLCallback> mCallbacks = new RemoteCallbackList<EmulatorAIDLCallback>();
	NotificationManager mNM;
	PowerManager pm;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return mBinder;
	}

	private final EmulatorAIDL.Stub mBinder = new EmulatorAIDL.Stub() {
		@Override
		public void unregisterCallback(EmulatorAIDLCallback cb)
				throws RemoteException {
			// TODO Auto-generated method stub
			if (cb != null)
				mCallbacks.unregister(cb);
		}

		@Override
		public void registerCallback(EmulatorAIDLCallback cb)
				throws RemoteException {
			if (cb != null)
				mCallbacks.register(cb);
		}

		@Override
		public void openfile() throws RemoteException {
		}

		@Override
		public void closefile() throws RemoteException {
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		showNotification();
		Log.i("Power", "check1");
		pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
		Toast.makeText(getBaseContext(), "pm = " + pm, Toast.LENGTH_LONG)
				.show();
		NanoHttpd();
		Log.i("Power", "check2");
	}

	public static final String from = "sdcard/index.html";
	public static final String to = "/data/data/com.example.emulator/";
	public static final String copy_name = "index.html";

	public File doCopy() {

		File folder = new File(to + "files/");
		folder.mkdirs();
		File outfile = new File(to + "files/" + copy_name);
		try {
			// AssetManager assetManager = getResources().getAssets();
			// InputStream is = assetManager.open(HTML_NAME,
			// AssetManager.ACCESS_BUFFER);
			InputStream is = new FileInputStream(from);
			long filesize = is.available();
			byte[] tempdata = new byte[(int) filesize];
			is.read(tempdata);
			is.close();
			outfile.createNewFile();
			FileOutputStream fo = new FileOutputStream(outfile);
			fo.write(tempdata);
			fo.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return folder;
	}

	private void NanoHttpd() {
		Log.e(TAG, "############## come####  ");
		File wwwroot = doCopy();

		try {
			NanoHTTPD Nano = new NanoHTTPD(8091, wwwroot);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mNM.cancel(R.string.remote_service_started);
		mCallbacks.kill();
	}

	private void showNotification() {
		// TODO Auto-generated method stub
		CharSequence text = getText(R.string.remote_service_started);

		Notification notification = new Notification(R.drawable.stat_sample,
				text, System.currentTimeMillis());

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, Emulator.class), 0);

		notification.setLatestEventInfo(this,
				getText(R.string.remote_service_label), text, contentIntent);

		mNM.notify(R.string.remote_service_started, notification);
	}

	public void ScreenOnOff(String value) {
		// AndroidManifext.xml에 android:sharedUserId="android.uid.system"를 지우지
		// 않고 실행하면.
		// 시스템 권한이 없어서 emulator 실행이 되지 않는다.

		int Screen_value = Integer.parseInt(value);
		Log.e("Service & NanoHttpd", "" + value);
		Log.e("Service & NanoHttpd", "OK");

		if (Screen_value == 1) {
			Log.e("Screen_on", "screenon:" + Screen_value);

			Log.e("Screen_on", "1)screenon:" + Screen_value);
			pm.userActivity(SCREEN_ON, true);
			Log.e("Screen_on", "2screenon:" + Screen_value);
		}

		else if (Screen_value == 0) {
			Log.e("Screen_off", "screenoff:" + Screen_value);
			Log.e("Screen_off", "screenoff1:" + Screen_value);
			Log.e("ss", "pm = " + pm);
			pm.goToSleep(2000);
			Log.e("Screen_off", "screenoff2:" + Screen_value);
			pm.wakeUp(2000);
			Log.e("Screen_off", "screenoff3:" + Screen_value);
		}
	}
}
