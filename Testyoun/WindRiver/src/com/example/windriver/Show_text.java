package com.example.windriver;


import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;




public class Show_text extends Activity {
	private Button button3;
	public final static String EXTRA_MESSAGE = "com.example.windriver.MESSAGE";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_text);
		// Show the Up button in the action bar.
		
		button3 = (Button) findViewById(R.id.button3);
		
		
		button3.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	
	        	EditText editText = (EditText) findViewById(R.id.edit_message);
	        	String message = editText.getText().toString();
	        	Intent intent = new Intent(Show_text.this, WindRiver.class);
	        	intent.putExtra(EXTRA_MESSAGE, message);
	        	
	        	finish();
	        	
	        }
		});
	}
}

	



