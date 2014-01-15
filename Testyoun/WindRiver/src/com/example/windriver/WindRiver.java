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


public class WindRiver extends Activity {
	
	final Context context = this;
	private Button button;
	private Button button2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wind_river);
		
		button = (Button) findViewById(R.id.button_show_alert_dialog);
		button2 = (Button) findViewById(R.id.text_call);
		
		
		
		//button click 
		button.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	final TextView view = (TextView)findViewById(R.id.text);
	            AlertDialog.Builder alert = new AlertDialog.Builder(context);
	            alert.setTitle("Youn YoungSik");
	            alert.setMessage("Enter Your Name");

	            final EditText input = new EditText(context);
	     //       view.setText(input);
	            alert.setView(input);

	            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {
	                   String srt = input.getEditableText().toString();
						view.setText(srt);
	            }
	       
	          });
	              AlertDialog alertDialog = alert.create();
	              alertDialog.show();
	              
	       }
	    });
		
		button2.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	final TextView view1 = (TextView)findViewById(R.id.print);
	        	final Intent intent = new Intent(WindRiver.this, Show_text.class);
	        	startActivity(intent);
	        	     	
	        	
	        	Intent yys = getIntent();
	    	    String message = yys.getStringExtra(Show_text.EXTRA_MESSAGE);
	    	    
	    	    view1.setText(message);
	       }
	    });
		
	   }//Onecreate();
		
	}
/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wind_river, menu);
		return true;
	}
	*/


