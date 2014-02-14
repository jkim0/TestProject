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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	byte buffer[];				//저장할 버퍼
	int rlen;						//파일 읽어오는 것의 크기(글자 수)
	int line_Cnt=1;					//Total line cnt
	ByteArrayInputStream bin_s=null;
	BufferedReader reader=null;
	String submit_cmd;
	String status="";
	String write_str="<html>" +
			"<head>" +
			"<title>Emulator ver 0.1</title>" +
			"</head>" +
			"<body>";
	
	Button button1;
	
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
        
        button1 = (Button)findViewById(R.id.Button1);
        
        
        Log.i("Parsing_Result.html",""+write_str);
        		
        doCopy();
        
        button1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				write_str = write_str.replace("Keyboard : ","Keyboard : KeyCode_1");
				
				Log.i("Changed write_str","write_str : "+write_str);
			}
		});
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
				write_str = write_str + "</select> <input type=\"submit\"" +"value =" +"\"send\"" + "/>" 
						+ "</form>" +
						"<text><br><br></text>";
			}
			
			else			//한 줄 띄기가 아닐 때만 Parsing을 호출
			{
				Parsing(str);
			}
		}
		
		write_str = write_str + "</select> <input type=\"submit\"" + 
				"value =" +"\"send\"" + "/>" 
						+ "</form>";
		
		Add_status();
		
		//write_str = write_str + "</body></html>";
		
		Log.i("##########Check","line : "+cnt);
	}
    
    public void Add_status(){
    	
    	write_str = write_str + status; 
    	
    	write_str = write_str + "</body></html>";
    }
    
    public void Parsing(String parsing){
				
		StringTokenizer stoken1 = new StringTokenizer( parsing, "#" );
		
		String str_partition=null;
		
		if(stoken1.hasMoreTokens())
		{
			str_partition = stoken1.nextToken();
			if(parsing != str_partition)			//#이 있다는 것
			{
				int space_index=str_partition.indexOf(' ');
				if(space_index != -1)
				{
					str_partition = str_partition.trim();
				}
				
				submit_cmd = str_partition;
				Log.i("Parsing","################");
				write_str = write_str + "<textarea rows=1 cols=10>"
						+ str_partition + "</textarea>";
				status = status + "<text><br>" + str_partition + " : ";
				
				Log.i("######","String:"+str_partition);
			}
			
			else
			{
				stoken1 = new StringTokenizer( parsing, "-" );
				str_partition = stoken1.nextToken();
				if(parsing != str_partition)			//-이 있다는 것
				{
					int space_index=str_partition.indexOf(' ');
					if(space_index != -1)
					{
						str_partition = str_partition.trim();
					}
					
					
					Log.i("-------","String:"+str_partition);
					
					write_str = write_str + "<text><br></text><text>"
							+ str_partition + "<br></text>" +
							"<form method=\"post\">" +
							"<select name=\"" + submit_cmd + "\">";
				}
				
				else									//command
				{
					int length = str_partition.length();
					int index = str_partition.indexOf('.');
					int space_index=str_partition.indexOf(' ');
				
					
					String Cmd = str_partition.substring(index+1, length);
					
					if(space_index != -1)
					{
						Cmd = Cmd.trim();
					}
					
					Log.i("****Command*****","String:"+Cmd);
										
					write_str = write_str + "<option value=\"" + Cmd + "\"" + "selected>"+ 
							Cmd +"</option>";
					
					
					if(Cmd.equalsIgnoreCase("off"))
					{
						status = status + "off" + "</text>";	
					}
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
