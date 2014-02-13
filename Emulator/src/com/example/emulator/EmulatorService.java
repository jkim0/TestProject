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
import java.util.Hashtable;
import java.util.Properties;
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
		try {
			File_Read();
			File wwwroot = write_file(write_str);
			Log.d("wwwroot","wwwroot="+wwwroot);
			mHttpd = new NanoHTTPD(8091,wwwroot);
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
					
				} catch (IOException e) {
					e.printStackTrace();
				}	

			}
			else if(cmd.equalsIgnoreCase("keyboard")){
				String find = new String();
				find=(String) theKeyBoard.get(value);
				Log.d("KEYBOADR","cmd= "+ cmd +"(value = "+value);
				Log.d("KEYBOADR","find (keynumber) ::: "+ find);
				
				
			}
			else if(cmd.equalsIgnoreCase("wifi")){
				wifiManager();	
			}
			else if(cmd.equalsIgnoreCase("broadcast")){
				
			}
			else if(cmd.equalsIgnoreCase("bluetooth")){
				
			}
			else if(cmd.equalsIgnoreCase("getStatus")){
				
			}
			
		}

		
	};
	private void wifiManager(){
	 	


		 

		// wifi 검색

		WifiManager mwifi = (WifiManager) this.getSystemService(WIFI_SERVICE);

		List Aplist = mwifi.getScanResults();

		 

		ScanResult ap = (ScanResult) Aplist.get(index);﻿

		 

		ap.SSID // ap의 ssid

		ap.level // ap의 신호크기

		 

		//wifi 연결

		WifiConfiguration wfc;

		 

		if(ap.capabilities.contains("WEP") == true)

		{

		wfc = ConnectWifi.ConnectWEP(ap.SSID);

		wfc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

		wfc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);

		wfc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);

		wfc.wepKeys[0] = password;

		}

		 else if(ap.capabilities.contains("WPA") == ture)

		{

		wfc = ConnectWifi.ConnectWPA(ap.SSID);

		wfc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);  

		wfc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);  

		wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);  

		wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);

		wfc.preSharedKey = password;

		}

		 else

		 wfc = ConnectWifi.ConnectOpenCapabilites(ap.SSID);


		int networkID = mwifi.addNetwork(wfc);

		mwifi.enableNetwork(networkID, true);



		// wifi 관련 브로드캐스트 액션

		IntentFilter intentFilter = new IntentFiliter();

		 

		intentfilter.addAction(WifiManager.NETWORK_IDS_CHANGED_ACTION);

		intentfilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION); // 와이파이상태

		intentfilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION); // AP 리스트 검색

		intentfilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);

		intentfilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION); // AP가 검색이 되면 이벤트가 들어옮

		intentfilter.addAction(WifiManager.EXTRA_SUPPLICANT_ERROR);

		intentfilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION); //와이파이 활성화

		intentfilter.addAction(WifiManager.RSSI_CHANGED_ACTION); // 안테나 감도 변경

		 

		registerReceiver(만든 리시버, intentfilter)




		* ConnectWifi 클래스 열어 드립니다. *


		public class ConnectWifi {


		// 암호 필요 없을경우 

		public static WifiConfiguration ConnectOpenCapabilites( String ssid ) {

		WifiConfiguration wfc = new WifiConfiguration();

		wfc.SSID = "\"".concat(ssid).concat("\"");

		wfc.priority = 40;

		wfc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);  

		wfc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);  

		wfc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);  

		wfc.allowedAuthAlgorithms.clear();  

		wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);  

		 

		wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);  

		wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);  

		wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);  

		wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

		//connect(wfc);

		return wfc;

		}

		/**

		* WEP  방식 일 때 설정

		 

		*/

		public static WifiConfiguration ConnectWEP( String ssid ) {

		WifiConfiguration wfc = new WifiConfiguration();

		wfc.SSID = "\"".concat(ssid).concat("\"");

		wfc.priority = 40;

		String password = "123456789";

		wfc.status = WifiConfiguration.Status.DISABLED; 

		   wfc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

		   wfc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);

		   wfc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);

		       

		        int length = password.length();

		        if ((length == 10 || length == 26 || length == 58) && password.matches("[0-9A-Fa-f]*")) {

		            wfc.wepKeys[0] = password;

		        } else {

		        wfc.wepKeys[0] = '"' + password + '"';

		        }

		        //connect(wfc);

		        return wfc;

		}

		/**

		* WPA, WPA2 방식 일 때 설정

		 

		*/

		public static WifiConfiguration ConnectWPA( String ssid ) {

		// 공통 부분

		WifiConfiguration wfc = new WifiConfiguration();

		wfc.SSID = "\"".concat(ssid).concat("\"");

		wfc.priority = 40;

		wfc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);  

		wfc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);  

		wfc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);  

		wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);  

		wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);  

		wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);  

		wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

		 

		String password = "123456789";

		wfc.preSharedKey = "\"".concat(password).concat("\"");

		//connect(wfc);

		return wfc;

		}

		/**

		* 원하는 네트워크 아이디에 AP 에 연결

		*/

		public static void connect(WifiConfiguration wfc, WifiManager wifi, String ssid){

		boolean isId = false;

		int networkID = 0;

		int tempID = 0;

		String tempSSID;

		List<WifiConfiguration> wifiConfigurationList;

		wifiConfigurationList = wifi.getConfiguredNetworks();

		for(WifiConfiguration w : wifiConfigurationList){

		if(w.SSID.equals("\""+ssid+"\"")){

		isId = true;

		tempID = w.networkId;

		tempSSID = w.SSID;

		break;

		} else {

		//Log.e("check", "else : id = "+w.SSID);

		}

		}

		if (isId == true) {

		networkID = tempID;

		} else {

		networkID = wifi.addNetwork(wfc);

		}

		boolean bEnableNetwork = wifi.enableNetwork(networkID, true);

		   if (bEnableNetwork) {

		    Log.d(TAG, "Connected!");

		   } else {

		    Log.d(TAG, "Disconnected!");

		   }

		}

		}


	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mNM.cancel(R.string.remote_service_started);
		mCallbacks.kill();
	}
	
   
    private void dealing_key(){
    
    	String tmp11=new String("KEYCODE_SOFT_LEFT");
    
    	Properties key_Value = new Properties();
    //	key_Value.put(key, value)
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
				write_str = write_str + "</select> <input type=\"submit\"" + 
				"\"" +"value =" +"\"send\"" + "/>" 
						+ "</form>";
			}
			
			else			//한 줄 띄기가 아닐 때만 Parsing을 호출
			{
				Parsing(str);
			}
		}
		
		write_str = write_str + "</select> <input type=\"submit\"" + 
				"\"" +"value =" +"\"send\"" + "/>" 
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
									"<text><br></text>" +
							"<form method=\"post\">" +
							"<select name=\"" + submmit_cmd + "\">";
				}
				
				else									//command
				{
					int length = str_partition.length();
					int index = str_partition.indexOf('.');
					String forward = str_partition.substring(0, index-1);
					String backward = str_partition.substring(index+1, length);
					Log.i("Parsing","forward : "+forward);
					Log.i("Parsing","backward : "+backward);
					write_str = write_str + "<option value=\"" + forward + "\"" + "selected>"+ 
					backward +"</option>";
					Log.i("Parsing","*****Command*****");			
				}
			}
		}	
	
	}

    

	private File write_file(String str){
		File dir = new File("data/data/com.example.emulator/files/");
		dir.mkdirs();
		File out = new File("data/data/com.example.emulator/files/index.html");
		try {
			out.createNewFile();
			FileOutputStream fo = new FileOutputStream(out);
			fo.write(str.getBytes());
			fo.close();
			return dir;
		} catch (IOException e) {
			return null;
		}
	}

	 private static Hashtable theKeyBoard = new Hashtable();
	    static
	    {
			StringTokenizer st = new StringTokenizer(
				
				" KEYCODE_UNKNOWN         0"+
			   " KEYCODE_SOFT_LEFT         1"+
			   " KEYCODE_SOFT_RIGHT        2"+
			   " KEYCODE_HOME              3"+
			   " KEYCODE_BACK              4"+
			   " KEYCODE_CALL              5"+
			   " KEYCODE_ENDCALL           6"+
			   " KEYCODE_0                 7"+
			   " KEYCODE_1                 8"+
			   " KEYCODE_2                 9"+
			   " KEYCODE_3                 10"+
			   " KEYCODE_4                 11"+
			   " KEYCODE_5                 12"+
			   " KEYCODE_6                 13"+
			   " KEYCODE_7                 14"+
			   " KEYCODE_8                 15"+
			   " KEYCODE_9                 16"+
			   " KEYCODE_STAR              17"+
			   " KEYCODE_POUND             18"+
			   " KEYCODE_DPAD_UP           19"+
			   " KEYCODE_DPAD_DOWN         20"+
			   " KEYCODE_DPAD_LEFT         21"+
			   " KEYCODE_DPAD_RIGHT        22"+
			   " KEYCODE_DPAD_CENTER       23"+
			   " KEYCODE_VOLUME_UP         24"+
			   " KEYCODE_VOLUME_DOWN       25"+
			   " KEYCODE_POWER             26"+
			   " KEYCODE_CAMERA            27"+
			   " KEYCODE_CLEAR             28"+
			   " KEYCODE_A                 29"+
			   " KEYCODE_B                 30"+
			   " KEYCODE_C                 31"+
			   " KEYCODE_D                 32"+
			   " KEYCODE_E                 33"+
			   " KEYCODE_F                 34"+
			   " KEYCODE_G                 35"+
			   " KEYCODE_H                 36"+
			   " KEYCODE_I                 37"+
			   " KEYCODE_J                 38"+
			   " KEYCODE_K                 39"+
			   " KEYCODE_L                 40"+
			   " KEYCODE_M                 41"+
			   " KEYCODE_N                 42"+
			   " KEYCODE_O                 43"+
			   " KEYCODE_P                 44"+
			   " KEYCODE_Q                 45"+
			   " KEYCODE_R                 46"+
			   " KEYCODE_S                 47"+
			   " KEYCODE_T                 48"+
			   " KEYCODE_U                 49"+
			   " KEYCODE_V                 50"+
			   " KEYCODE_W                 51"+
			   " KEYCODE_X                 52"+
			   " KEYCODE_Y                 53"+
			   " KEYCODE_Z                 54"+
			   " KEYCODE_COMMA             55"+
			   " KEYCODE_PERIOD            56"+
			   " KEYCODE_ALT_LEFT          57"+
			   " KEYCODE_ALT_RIGHT         58"+
			   " KEYCODE_SHIFT_LEFT        59"+
			   " KEYCODE_SHIFT_RIGHT       60"+
			   " KEYCODE_TAB               61"+
			   " KEYCODE_SPACE             62"+
			   " KEYCODE_SYM               63"+
			   " KEYCODE_EXPLORER          64"+
			   " KEYCODE_ENVELOPE          65"+
			   " KEYCODE_ENTER             66"+
			   " KEYCODE_DEL               67"+
			   " KEYCODE_GRAVE             68"+
			   " KEYCODE_MINUS             69"+
			   " KEYCODE_EQUALS            70"+
			   " KEYCODE_LEFT_BRACKET      71"+
			   " KEYCODE_RIGHT_BRACKET     72"+
			   " KEYCODE_BACKSLASH         73"+
			   " KEYCODE_SEMICOLON         74"+
			   " KEYCODE_APOSTROPHE        75"+
			   " KEYCODE_SLASH             76"+
			   " KEYCODE_AT                77"+
			   " KEYCODE_NUM               78"+
			   " KEYCODE_HEADSETHOOK       79"+
			   " KEYCODE_FOCUS             80"+   
			   " KEYCODE_PLUS              81"+
			   " KEYCODE_MENU              82"+
			   " KEYCODE_NOTIFICATION      83"+
			   " KEYCODE_SEARCH            84"+
			   " KEYCODE_MEDIA_PLAY_PAUSE  85"+
			   " KEYCODE_MEDIA_STOP        86"+
			   " KEYCODE_MEDIA_NEXT        87"+
			   " KEYCODE_MEDIA_PREVIOUS    88"+
			   " KEYCODE_MEDIA_REWIND      89"+
			   " KEYCODE_MEDIA_FAST_FORWARD   90"+
			   " KEYCODE_MUTE              91"+
			   " KEYCODE_PAGE_UP           92"+
			   " KEYCODE_PAGE_DOWN         93"+
			   " KEYCODE_PICTSYMBOLS       94"+   
			   " KEYCODE_SWITCH_CHARSET    95"+   
			   " KEYCODE_BUTTON_A          96"+
			   " KEYCODE_BUTTON_B          97"+
			   " KEYCODE_BUTTON_C          98"+
			   " KEYCODE_BUTTON_X          99"+
			   " KEYCODE_BUTTON_Y          100"+
			   " KEYCODE_BUTTON_Z          101"+
			   " KEYCODE_BUTTON_L1         102"+
			   " KEYCODE_BUTTON_R1         103"+
			   " KEYCODE_BUTTON_L2         104"+
			   " KEYCODE_BUTTON_R2         105"+
			   " KEYCODE_BUTTON_THUMBL     106"+
			   " KEYCODE_BUTTON_THUMBR     107"+
			   " KEYCODE_BUTTON_START      108"+
			   " KEYCODE_BUTTON_SELECT     109"+
			   " KEYCODE_BUTTON_MODE       110"+
			   " KEYCODE_ESCAPE            111"+
			   " KEYCODE_FORWARD_DEL       112"+
			   " KEYCODE_CTRL_LEFT         113"+
			   " KEYCODE_CTRL_RIGHT        114"+
			   " KEYCODE_CAPS_LOCK         115"+
			   " KEYCODE_SCROLL_LOCK       116"+
			   " KEYCODE_META_LEFT         117"+
			   " KEYCODE_META_RIGHT        118"+
			   " KEYCODE_FUNCTION          119"+
			   " KEYCODE_SYSRQ             120"+
			   " KEYCODE_BREAK             121"+
			   " KEYCODE_MOVE_HOME         122"+
			   " KEYCODE_MOVE_END          123"+
			   " KEYCODE_INSERT            124"+
			   " KEYCODE_FORWARD           125"+
			   " KEYCODE_MEDIA_PLAY        126"+
			   " KEYCODE_MEDIA_PAUSE       127"+
			   " KEYCODE_MEDIA_CLOSE       128"+
			   " KEYCODE_MEDIA_EJECT       129"+
			   " KEYCODE_MEDIA_RECORD      130"+
			   " KEYCODE_F1                131"+
			   " KEYCODE_F2                132"+
			   " KEYCODE_F3                133"+
			   " KEYCODE_F4                134"+
			   " KEYCODE_F5                135"+
			   " KEYCODE_F6                136"+
			   " KEYCODE_F7                137"+
			   " KEYCODE_F8                138"+
			   " KEYCODE_F9                139"+
			   " KEYCODE_F10               140"+
			   " KEYCODE_F11               141"+
			   " KEYCODE_F12               142"+
			   " KEYCODE_NUM_LOCK          143"+
			   " KEYCODE_NUMPAD_0          144"+
			   " KEYCODE_NUMPAD_1          145"+
			   " KEYCODE_NUMPAD_2          146"+
			   " KEYCODE_NUMPAD_3          147"+
			   " KEYCODE_NUMPAD_4          148"+
			   " KEYCODE_NUMPAD_5          149"+
			   " KEYCODE_NUMPAD_6          150"+
			   " KEYCODE_NUMPAD_7          151"+
			   " KEYCODE_NUMPAD_8          152"+
			   " KEYCODE_NUMPAD_9          153"+
			   " KEYCODE_NUMPAD_DIVIDE     154"+
			   " KEYCODE_NUMPAD_MULTIPLY   155"+
			   " KEYCODE_NUMPAD_SUBTRACT   156"+
			   " KEYCODE_NUMPAD_ADD        157"+
			   " KEYCODE_NUMPAD_DOT        158"+
			   " KEYCODE_NUMPAD_COMMA      159"+
			   " KEYCODE_NUMPAD_ENTER      160"+
			   " KEYCODE_NUMPAD_EQUALS     161"+
			   " KEYCODE_NUMPAD_LEFT_PAREN   162"+
			   " KEYCODE_NUMPAD_RIGHT_PAREN   163"+
			   " KEYCODE_VOLUME_MUTE       164"+
			   " KEYCODE_INFO              165"+
			   " KEYCODE_CHANNEL_UP        166"+
			   " KEYCODE_CHANNEL_DOWN      167"+
			   " KEYCODE_ZOOM_IN           168"+
			   " KEYCODE_ZOOM_OUT          169"+
			   " KEYCODE_TV                170"+
			   " KEYCODE_WINDOW            171"+
			   " KEYCODE_GUIDE             172"+
			   " KEYCODE_DVR               173"+
			   " KEYCODE_BOOKMARK          174"+
			   " KEYCODE_CAPTIONS          175"+
			   " KEYCODE_SETTINGS          176"+
			   " KEYCODE_TV_POWER          177"+
			   " KEYCODE_TV_INPUT          178"+
			   " KEYCODE_STB_POWER         179"+
			   " KEYCODE_STB_INPUT         180"+
			   " KEYCODE_AVR_POWER         181"+
			   " KEYCODE_AVR_INPUT         182"+
			   " KEYCODE_PROG_RED          183"+
			   " KEYCODE_PROG_GREEN        184"+
			   " KEYCODE_PROG_YELLOW       185"+
			   " KEYCODE_PROG_BLUE         186"+
			   " KEYCODE_APP_SWITCH        187"+
			   " KEYCODE_BUTTON_1          188"+
			   " KEYCODE_BUTTON_2          189"+
			   " KEYCODE_BUTTON_3          190"+
			   " KEYCODE_BUTTON_4          191"+
			   " KEYCODE_BUTTON_5          192"+
			   " KEYCODE_BUTTON_6          193"+
			   " KEYCODE_BUTTON_7          194"+
			   " KEYCODE_BUTTON_8          195"+
			   " KEYCODE_BUTTON_9          196"+
			   " KEYCODE_BUTTON_10         197"+
			   " KEYCODE_BUTTON_11         198"+
			   " KEYCODE_BUTTON_12         199"+
			   " KEYCODE_BUTTON_13         200"+
			   " KEYCODE_BUTTON_14         201"+
			   " KEYCODE_BUTTON_15         202"+
			   " KEYCODE_BUTTON_16         203"+
			   " KEYCODE_LANGUAGE_SWITCH   204"+
			   " KEYCODE_MANNER_MODE       205"+
			   " KEYCODE_3D_MODE           206"+
			   " KEYCODE_CONTACTS          207"+
			   " KEYCODE_CALENDAR          208"+
			   " KEYCODE_MUSIC             209"+
			   " KEYCODE_CALCULATOR        210"+
			   " KEYCODE_ZENKAKU_HANKAKU   211"+
			   " KEYCODE_EISU              212"+
			   " KEYCODE_MUHENKAN          213"+
			   " KEYCODE_HENKAN            214"+
			   " KEYCODE_KATAKANA_HIRAGANA   215"+
			   " KEYCODE_YEN               216"+
			   " KEYCODE_RO                217"+
			   " KEYCODE_KANA              218"+
			   " KEYCODE_ASSIST            219"+
			   " KEYCODE_BRIGHTNESS_DOWN   220"+
			   " KEYCODE_BRIGHTNESS_UP     221"+
			   " KEYCODE_MEDIA_AUDIO_TRACK   222"+
			   "LAST_KEYCODE             KEYCODE_MEDIA_AUDIO_TRACK"
					);
					while ( st.hasMoreTokens())
				theKeyBoard.put( st.nextToken(), st.nextToken());
		}


}

