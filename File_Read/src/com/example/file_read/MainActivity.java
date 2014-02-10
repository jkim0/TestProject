package com.example.file_read;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.os.Bundle;
import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	//final private String mFile_name = "Cmd.txt";
	final private String mFile_name = "cmd.txt";
	FileInputStream fo;
	byte buffer[];
	TextView view;
	String Input_data;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		view = (TextView)findViewById(R.id.FilePrintView);
		
		Toast.makeText(this, "OnCreate Call", Toast.LENGTH_SHORT).show();
		

		try {
			File_Read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		view.setText(Input_data);
	}

	public void File_Read() throws IOException{
		Toast.makeText(this, "File_Read Call", Toast.LENGTH_SHORT).show();
		
		Log.e("File_Read","File_Read_call");
 
	   Resources res = getResources();
	   InputStream in_s = res.openRawResource(R.raw.cmd);

	   byte[] b = new byte[in_s.available()];
	   in_s.read(b);
		        
	   Input_data = new String(b);	        
		        
	   
	   
		
		/*
		AssetManager assetManager = getResources().getAssets();
		InputStream is = assetManager.open(mFile_name,AssetManager.ACCESS_BUFFER);
	
	//	InputStream is = new FileInputStream(path);
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

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
