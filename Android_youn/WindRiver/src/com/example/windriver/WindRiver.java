package com.example.windriver;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



public class WindRiver extends Activity {
	
	final Context context = this;
	private Button button;
	private Button button2;
	public String message="start";
	public String srt = "start";
	public TextView view1;
	public TextView view2;
	public Intent intent;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wind_river);
		
		Toast.makeText(this, "OnCreate", Toast.LENGTH_SHORT).show();
		
		button = (Button) findViewById(R.id.button_show_alert_dialog);
		button2 = (Button) findViewById(R.id.text_call);
		view1 = (TextView)findViewById(R.id.print);
		view2 = (TextView)findViewById(R.id.text);
		intent = new Intent(WindRiver.this, Show_text.class);
		
		//button click 
		button.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	      
	            AlertDialog.Builder alert = new AlertDialog.Builder(context);
	            alert.setTitle("Youn YoungSik");
	            alert.setMessage("Enter Your Name");

	            final EditText input = new EditText(context);
	     //       view.setText(input);
	            alert.setView(input);

	            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {
	                   srt = input.getEditableText().toString();
						  view2.setText(srt);
	            }
	       
	          });
	              AlertDialog alertDialog = alert.create();
	              alertDialog.show();
	       }
	    });
		
		
		button2.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	startActivity(intent);
	       }
	    });
		
		
	}//OnCreate();
		
	@Override
	public void onStart(){
		super.onResume();
		Toast.makeText(this, "OnStart", Toast.LENGTH_SHORT).show();
	}
	
	
	@Override
	public void onResume(){
		super.onResume();
		Toast.makeText(this, "OnResume", Toast.LENGTH_SHORT).show();
		
		Intent intent = getIntent();
	    message = intent.getStringExtra(Show_text.EXTRA_MESSAGE);
	    view1.setText(message);
	    view2.setText(srt);
	}
	
	@Override
	public void onPause(){
		super.onResume();
		Toast.makeText(this, "OnPause", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onStop(){
		super.onResume();
		Toast.makeText(this, "OnStop", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onDestroy(){
		super.onResume();
		Toast.makeText(this, "OnDestroy", Toast.LENGTH_SHORT).show();
	}
}


