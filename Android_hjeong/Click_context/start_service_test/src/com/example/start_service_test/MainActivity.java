package com.example.start_service_test;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	
	 Button startServiceButton;
	 Button stopServiceButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startServiceButton = (Button)findViewById(R.id.start_service);
		stopServiceButton = (Button)findViewById(R.id.stop_service);
		
		startServiceButton.setOnClickListener(this);
		stopServiceButton.setOnClickListener(this);
	}
	 /*
    //start the service
    public void onClickStartServie(View V)
    {
        //start the service from here //MyService is your service class name
        startService(new Intent(this, MyService.class));
    }
    //Stop the started service
    public void onClickStopService(View V)
    {
        //Stop the running service from here//MyService is your service class name
        //Service will only stop if it is already running.
        stopService(new Intent(this, MyService.class));
    }
    */
    
    public void onClick(View v) {
		// TODO Auto-generated method stub
	
			switch(v.getId()){
			case R.id.start_service: 
				startService(new Intent(this, MyService.class));
				break;
			case R.id.stop_service:
				stopService(new Intent(this, MyService.class));
				break;
			}
	}
    
    public void onReceive(Context context, Intent intent) {
         
        String name = intent.getAction();
         /*
        if(name.equals("arabiannight.tistory.com.sendreciver.gogogo")){
            Toast.makeText
            (context, "정상적으로 값을 받았습니다.", Toast.LENGTH_SHORT).show();
        }
        */
    }
	
    @Override
    public void onStart() {
    	super.onStart();
    	Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();	
    }

    @Override
    public void onResume() {
    	super.onResume();
    	Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();	
    }

    @Override
    public void onRestart() {
    	super.onRestart();
    	Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
	
    }

    @Override
    public void onPause() {
    	super.onPause();
    	Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onStop() {
    	super.onStop();
    	Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
    	super.onDestroy();
    	Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
    }
 
}