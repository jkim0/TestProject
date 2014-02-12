package com.example.emulator;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcel;
import android.os.PowerManager;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("NewApi")
public class EmulatorService extends Service {

	public static final String TAG = "EmulatorService";
	public final static int SCREEN_ON = 1;
	final RemoteCallbackList<EmulatorAIDLCallback> mCallbacks = new RemoteCallbackList<EmulatorAIDLCallback>();
	NotificationManager mNM;

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
		 try {
			File_Read();
			write_file(write_str);
		} catch (IOException e) {
			e.printStackTrace();
		}
		NanoHttpd();
	}

	/* copy from raw card */
	//public static final String from = "res/raw/index.html";
	public static final String to = "/data/data/com.example.emulator/";
	public static String copy_name = "index.html";
	public static String copy_php="upload_ok.php";

	public File doCopy() {
		
		File folder = new File(to + "files/");
		folder.mkdirs();
		File outfile = new File(to + "files/" + copy_name);
		File outfile2= new File(to+"files/"+copy_php);
		try {
			// AssetManager assetManager = getResources().getAssets();
			// InputStream is = assetManager.open(HTML_NAME,
			// AssetManager.ACCESS_BUFFER);
			Resources res = getResources();
			InputStream is = res.openRawResource(R.raw.index);
			InputStream is2=res.openRawResource(R.raw.upload_ok);
	
			long filesize = is.available();
			long filesize2= is2.available();
			byte[] tempdata = new byte[(int) filesize];
			byte[] tempdata2 = new byte[(int) filesize2];
			
			is.read(tempdata);
			is.close();

			is2.read(tempdata2);
			is2.close();
			// make byte[] from inputstream
			
			//create file 
			outfile.createNewFile();
			//FileOutputstream (outfile) 에다가 tempdata를 쓴다.
			FileOutputStream fo = new FileOutputStream(outfile);
			fo.write(tempdata);
			fo.close();
			
			outfile2.createNewFile();
			//FileOutputstream (outfile) 에다가 tempdata를 쓴다.
			FileOutputStream fo2 = new FileOutputStream(outfile2);
			fo2.write(tempdata2);
			fo2.close();
		
		} catch (IOException e) {
			e.printStackTrace();
		}

		return folder;
	}
	private NanoHTTPD mHttpd = null;
	private void NanoHttpd() {
		//여기서 파일 오픈해서 읽어서html 띄어주면 되는거잖아..
		File wwwroot = doCopy();
	
		try {
			//for tossing itself to class. thats why we need mHttpd;
			mHttpd = new NanoHTTPD(8091, wwwroot);
		 //mHttpd = new NanoHTTPD(this, 8091, wwwroot);
			mHttpd.registerCommandReceiver(mCommandReceiver);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//저기 클래스의 interface 를 받아와 (commandReceiver)
	private NanoHTTPD.CommandReceiver mCommandReceiver = new NanoHTTPD.CommandReceiver() {
		@Override
		public void onCommandReceived(String cmd, String value) {
			Log.d(TAG, "onCommandReceived cmd = " + cmd + " value = " + value);
			if (cmd.equalsIgnoreCase("screen")) {
				PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
				if (value.equalsIgnoreCase("1")) {
					//screen on
					pm.userActivity(SystemClock.uptimeMillis(), false);			
				} else if (value.equalsIgnoreCase("0")) {
					//screen off
					pm.goToSleep(2000);
					pm.wakeUp(2000);
				}
			}
			else if(cmd.equalsIgnoreCase("memosite")){
	
				doCopy();
		
				File folder = new File(to + "files/");
				folder.mkdirs();
				File outfile = new File(to + "files/" +"cmd.txt");
				try {
					outfile.createNewFile();
					//FileOutputstream (outfile) 에다가 tempdata를 쓴다.
					FileOutputStream fo = new FileOutputStream(outfile);
					fo.write(value.getBytes());
					fo.close();
			//		new NanoHTTPD(8090,folder);					
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				

			}
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		mNM.cancel(R.string.remote_service_started);
		mCallbacks.kill();
	}
	
	public void screenOnOff(String value) throws RemoteException {
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
	
		if (value.equalsIgnoreCase("1")) {
			//screen on
			pm.userActivity(SystemClock.uptimeMillis(), false);			
		} else if (value.equalsIgnoreCase("0")) {
			//screen off
			pm.goToSleep(2000);
			pm.wakeUp(2000);
		}
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

	byte buffer[];				//저장할 버퍼
	int rlen;						//파일 읽어오는 것의 크기(글자 수)
	int line_Cnt=1;					//Total line cnt
	ByteArrayInputStream bin_s=null;
	BufferedReader reader=null;
	String submmit_cmd;
	String write_str="<html>" +
			"<head>" +
			"<title>Emulator ver 0.1</title>" +
			"</head>" +
			"<body>";
	
	public void File_Read() throws IOException{
		 
		Toast.makeText(this, "File_Read Call", Toast.LENGTH_SHORT).show();
 
	   Resources res = getResources();							//res
	   InputStream in_s = res.openRawResource(R.raw.cmd);		//cmd.txt를 InputStream에
	   
	   rlen=in_s.available();			//Total Length
	   //Returns an estimated number of bytes that can be read or skipped without blocking for more input
	   byte[] buffer = new byte[in_s.available()];
	   in_s.read(buffer);
	   
	   bin_s = new ByteArrayInputStream(buffer, 0, rlen); //Parsing을	(bufferreader 사용)
	   reader = new BufferedReader( new InputStreamReader( bin_s )); //편하게 하기 위해서
	   
	   
	   Log.i("Buffer Length","#####rlen = "+rlen);
	   for(int i=0;i<rlen;i++)			//Check text Total line
	   {
		   if(buffer[i]== 10)
		   {
			   line_Cnt++;
		   }
		   //Log.i("buffer","buffer[i] = "+buffer[i]);
	   }
	   
	   int cnt = 0;		//Check for current readed line cnt
	   
		while(cnt < line_Cnt)
		{
			String str = reader.readLine();			//read one line
			
			cnt++;							//readed one line cnt increase +1
			if(str.length() == 0)			//Newline '\n'
			{
				Log.i("Parsing","Newline");
				write_str = write_str + "</select> <input type=\"submit\"" + "name = " + 
				"\"" + submmit_cmd + "\"" +"value =" +"\"Submit\"" + "/>" 
						+ "</form>";
			}
			
			else			//한 줄 띄기가 아닐 때만 Parsing을 호출
			{
				Parsing(str);
			}
		}
		
		write_str = write_str + "</select> <input type=\"submit\"" + "name = " + 
				"\"" + submmit_cmd + "\"" +"value =" +"\"Submit\"" + "/>" 
						+ "</form>";
		
		write_str = write_str + "</body></html>";
		
		Log.i("##########Check","line : "+cnt);
	}
    

    public void Parsing(String parsing){
		
    	
		Log.i("Parsing","Parsing Function Call");
		
		StringTokenizer stoken1 = new StringTokenizer( parsing, "#" );
		
		Log.i("Parsing","Parsing STR : "+parsing);
		
		String str_partition=null;
		
		if(stoken1.hasMoreTokens())
		{
			str_partition = stoken1.nextToken();
			if(parsing != str_partition)			//#이 있다는 것
			{
				submmit_cmd = str_partition;
				Log.i("Parsing","################");
				write_str = write_str + "<textarea rows=1 cols=10>"
						+ str_partition +"</textarea>";
			}
			
			else
			{
				stoken1 = new StringTokenizer( parsing, "-" );
				str_partition = stoken1.nextToken();
				if(parsing != str_partition)			//-이 있다는 것
				{
					Log.i("Parsing","--------------");
					write_str = write_str + "<text><br></text><text>"
							+ str_partition + "<br></text>" +
							"<form method=\"post\">" +
							"<select name=\"dropdown\">";
				}
				
				else									//command
				{
					int length = str_partition.length();
					int index = str_partition.indexOf('.');
					String forward = str_partition.substring(0, index-1);
					String backward = str_partition.substring(index+1, length);
					Log.i("Parsing","forward : "+forward);
					Log.i("Parsing","backward : "+backward);
					write_str = write_str + "<option value=\"" + backward + "\"" + "selected>"+ backward +"</option>";
					Log.i("Parsing","*****Command*****");			
				}
			}
		}	
	
	}
	private File write_file(String str){
		File out = new File("data/data/com.example.emulator/files/write.html");
		try {
			out.createNewFile();
			FileOutputStream fo = new FileOutputStream(out);
			fo.write(str.getBytes());
			fo.close();
			
			return out;
		} catch (IOException e) {
			return null;
		}
	}
}
