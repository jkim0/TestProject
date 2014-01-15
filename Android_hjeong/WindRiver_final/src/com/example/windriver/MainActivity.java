package com.example.windriver;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	final Context context = this;
	private Button button;
	private Button button2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		button = (Button) findViewById(R.id.button_show_alert_dialog);
		button2 = (Button) findViewById(R.id.text_call);
		final Intent intent = new Intent(this, Show_text.class);
		
		
		button.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	final TextView view = (TextView)findViewById(R.id.text);
	            AlertDialog.Builder alert = new AlertDialog.Builder(context);
	            alert.setTitle("Krishthili");
	            alert.setMessage("Enter Your Name");

	            final EditText input = new EditText(context);
	          
	            alert.setView(input);
	            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	            
	            	public void onClick(DialogInterface dialog, int whichButton) {
	                   String srt = input.getEditableText().toString();
						view.setText(srt);
	             
	            }
	        }); 
	            alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                 
	                    dialog.cancel();
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
		
		
		
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		TextView view2 = (TextView)findViewById(R.id.text2);
		Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
		String_message str = new String_message();
    	String message2= str.send();
		view2.setText(message2);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onStop() {
		super.onStop();
		TextView view2 = (TextView)findViewById(R.id.text2);
		view2.setText("");
		Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		TextView view2 = (TextView)findViewById(R.id.text2);
		view2.setText("");
		Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
	}
}
