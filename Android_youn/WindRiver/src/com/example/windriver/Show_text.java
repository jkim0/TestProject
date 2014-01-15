package com.example.windriver;


import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import android.widget.Toast;

  

public class Show_text extends Activity {
	private Button button3;
	public final static String EXTRA_MESSAGE = "com.example.windriver.MESSAGE";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_text);
		// Show the Up button in the action bar.
		
		Toast.makeText(this, "OnCreate", Toast.LENGTH_SHORT).show();
		button3 = (Button) findViewById(R.id.button3);
		
		final EditText editText = (EditText) findViewById(R.id.edit_message);
		
		
		
		button3.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        
	        	String message = editText.getText().toString();
	        	Intent intent = new Intent(Show_text.this, WindRiver.class);
	        	intent.putExtra(EXTRA_MESSAGE, message);
	        
	        	finish();
	        	
	        }
		});
	}
	
	
	@Override
	public void onStart(){
		super.onResume();
		Toast.makeText(this, "OnStart", Toast.LENGTH_SHORT).show();
	}
	
	
	@Override
	public void onResume(){
		super.onResume();
		Toast.makeText(this, "OnResume", Toast.LENGTH_SHORT).show();
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

	



