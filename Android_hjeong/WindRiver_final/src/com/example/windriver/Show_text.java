package com.example.windriver;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;


public class Show_text extends Activity {
	
	String message=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_text);
		// Show the Up button in the action bar.
		Toast toast = Toast.makeText(getApplicationContext(),
 			   "onCreate", Toast.LENGTH_LONG);
 			toast.setGravity(0, 0, 0);
 			toast.show();
			sendMessage( );
	}
	
	public void sendMessage( ){
		
		final EditText editText = (EditText)findViewById(R.id.edit_mm);
		
		Button button = (Button)findViewById(R.id.button_send);
		
 		Toast.makeText(this, "sendMessage", Toast.LENGTH_SHORT).show();
 		
		button.setOnClickListener(new View.OnClickListener() {
	
	        public void onClick(View v) {
	        	
	        	message = editText.getEditableText().toString();
	        	
	        	Toast.makeText(Show_text.this, "click", Toast.LENGTH_SHORT).show();
	        	Toast.makeText(Show_text.this, message, Toast.LENGTH_SHORT).show();
	        	String_message str = new String_message();
	        	str.save(message);
	        	
	        	//Intent intent = new Intent(Show_text.this, MainActivity.class);
	    		//startActivity(intent);
	    		finish();
	       
	        }
	    });
	}
}