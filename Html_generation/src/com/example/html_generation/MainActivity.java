package com.example.html_generation;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import android.os.Bundle;
import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity {

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
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       
        try {
			File_Read();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        Log.i("Parsing_Result.html",""+write_str);
        		
        doCopy();
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
   
   public static final String to = "/data/data/com.example.html_generation/";
	public static final String copy_name = "index.html";

	public File doCopy() {

		File folder = new File(to + "files/");
		folder.mkdirs();
		File outfile = new File(to + "files/" + copy_name);
		
		
		try {
			// AssetManager assetManager = getResources().getAssets();
			// InputStream is = assetManager.open(HTML_NAME,
			// AssetManager.ACCESS_BUFFER);
			
			// make byte[] from inputstream
			
			//create file 
			outfile.createNewFile();
			//FileOutputstream (outfile) 에다가 tempdata를 쓴다.
		
			FileOutputStream fo = new FileOutputStream(outfile);
			byte[] buff2 = write_str.getBytes();
			
			fo.write(buff2);
			fo.close();
		
		} catch (IOException e) {
			e.printStackTrace();
		}

		return folder;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
