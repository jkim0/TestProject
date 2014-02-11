package com.example.file_read;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import android.os.Bundle;
import android.app.Activity;
//import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	byte buffer[];				//저장할 버퍼
	int rlen;						//파일 읽어오는 것의 크기(글자 수)
	int line_Cnt=1;					//Total line cnt
	ByteArrayInputStream bin_s=null;
	BufferedReader reader=null;
	LinearLayout layout;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Toast.makeText(this, "OnCreate Call", Toast.LENGTH_SHORT).show();
		
		layout = (LinearLayout)findViewById(R.id.my_Layout);
		
		try {
			File_Read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

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
				AddView(" ",null);
				
			}
			
			else			//한 줄 띄기가 아닐 때만 Parsing을 호출
			{
				Parsing(str);
			}
			
		}
		
		Log.i("##########Check","line : "+cnt);
		
		
		/* assets 파일에 있는 .txt 파일 읽어오기
		AssetManager assetManager = getResources().getAssets();
		InputStream is = assetManager.open(mFile_name,AssetManager.ACCESS_BUFFER);
	
	
		long filesize = is.available();
		byte[] data = new byte[(int) filesize];
		is.read(data);
        {
    	   
       } //txt 내용을 바이트 배열로 기록
       is.close();
      
       Input_data = new String(data);
       Log.e("Input data view","Input_data"+Input_data); 
	*/
	}

	public void Parsing(String parsing){
		
		//Log.i("Parsing","Parsing Function Call");
		
		StringTokenizer stoken1 = new StringTokenizer( parsing, "#" );
		
		Log.i("Parsing","Parsing STR : "+parsing);
		
		String str_partition=null;
		
		TextView view = new TextView(this);
		
		if(stoken1.hasMoreTokens())
		{
			str_partition = stoken1.nextToken();
			if(parsing != str_partition)			//#이 있다는 것
			{
				//Log.i("Parsing","################");
				
				view.setBackgroundColor(Color.RED);
			}
			
			else
			{
				stoken1 = new StringTokenizer( parsing, "-" );
				str_partition = stoken1.nextToken();
				if(parsing != str_partition)			//-이 있다는 것
				{
					//Log.i("Parsing","--------------");
					
					view.setBackgroundColor(Color.BLUE);
				}
				
				else									//command
				{
					//Log.i("Parsing","*****Command*****");
					
					view.setBackgroundColor(Color.GREEN);
				}
			}
		}
		
		AddView(str_partition,view);
		
		/*
		char[] div = {'#','-','1','2','e'};
		
		StringTokenizer st = new StringTokenizer( Input_data, div[0]);
		
		int start , end;
		
		
		
		for(int i=0;i<3;i++)
		{
			start=FindChar(div[i]);
			end = FindChar(div[i+1]);
			String Print = Input_data.substring(start,end);
			Log.i("Parsing","Print:"+Print);
			AddView(Print);
		}
		*/
		/*
		while ( st.hasMoreTokens())
		{
			String e = st.nextToken();
			Log.e("Parsing",""+e);
			int sep = e.indexOf( '-' );
			
			Log.e("Parsing","sep:"+sep);
		}
		*/	
	}
	/*
	public int FindChar(char div){
		
		int find=0;
		
		//find = Input_data.indexOf(div);
		
		Log.d("FindChar","find"+find);
		return find;
	}
	*/
	
	public void AddView(String st, TextView view){
		
		if(view != null)
		{
			view.setText(st);
			layout.addView(view);			//addView - TextView 추가하기
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
