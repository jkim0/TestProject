package com.example.basic_project;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.os.Build;
import android.widget.EditText;
import android.widget.Button;
import android.content.Intent;

public class Input_data extends Activity {
	
	Button Button_Input;
	EditText Edit_Input;
	Intent act;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input_data);
		// Show the Up button in the action bar.
		setupActionBar();
		act = new Intent(Input_data.this, MainActivity.class);
		Edit_Input = (EditText)findViewById(R.id.EditText_Input_Message);
		Button_Input = (Button)findViewById(R.id.button_Input_Message);
		
		
		Button_Input.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Intent act = new Intent(Input_data.this, MainActivity.class);
				//위의 명령어가 onCreate 밖으로 이동하면 Input_data_Activity가 실행되지 않는다. 
				
				String message = Edit_Input.getText().toString();
	        	act.putExtra("Activity_str", message);
	        	setResult(RESULT_OK, act);				
	        	finish();
			}
		});
	}
	

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.input_data, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
