package com.example.service_example;

import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private ProgressBar mProgress;
    private Button myButton1, myButton2, myButton3;
    private Button myButton4, myButton5, myButton6;
    private IBinder i;
    TextView mValue = null;

    hyeon hy;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		mProgress = (ProgressBar)findViewById(R.id.progress_bar);		
		Button myButton1 = (Button)findViewById(R.id.button1);
		Button myButton2 = (Button)findViewById(R.id.button2);
       Button myButton3 = (Button)findViewById(R.id.button3);
       Button myButton4 = (Button)findViewById(R.id.button4);
       Button myButton5 = (Button)findViewById(R.id.button5);
       Button myButton6 = (Button)findViewById(R.id.button6);
       mValue = (TextView)findViewById(R.id.Number);
       
       myButton1.setOnClickListener(new View.OnClickListener() {   	   
    	
    	@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			 Intent bindIntent = new Intent(MainActivity.this, MyService.class);
           startService(bindIntent);
		}
	});
       
       myButton1.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			 Intent bindIntent = new Intent(MainActivity.this, MyService.class);
             startService(bindIntent);	
		}
	});

       myButton2.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			  Intent bindIntent = new Intent(MainActivity.this, MyService.class);
             bindService(bindIntent, mConnection, Context.BIND_AUTO_CREATE);			
		}
	});
       myButton3.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			unbindService(mConnection);
			
		}
	});
       
       myButton4.setOnClickListener(new View.OnClickListener() {
   		
   		@Override
   		public void onClick(View v) {
   			// TODO Auto-generated method stub
   			Intent bindIntent = new Intent(MainActivity.this, MyService.class);
           stopService(bindIntent);
   			
   		}
   	});
       
       myButton5.setOnClickListener(new View.OnClickListener() {
   		
   		@Override
   		public void onClick(View v) {
   			// TODO Auto-generated method stub
   	
   				int x = ((MyService.MyBinder)i).volumDown();
   	           mProgress.setProgress(x);
   	           mValue.setText(" "+x);
   	           //while 을 쓰게 되면 x값이 0으로 출력되고.
   	           //bind의 함수가 출력되지 않는다.
   	   			
   		}
   	});
       
       myButton6.setOnClickListener(new View.OnClickListener() {
   		
   		@Override
   		public void onClick(View v) {
   			// TODO Auto-generated method stub
   			int x = ((MyService.MyBinder)i).volumUp();
            mProgress.setProgress(x);
            mValue.setText(" "+x);
//            mValue.setText(x);
   		}
   	});
	}
	
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			 i = service;
			
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			 i = null;
		}       
    };
    
}
