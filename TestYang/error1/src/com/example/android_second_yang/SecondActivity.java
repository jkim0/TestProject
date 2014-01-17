package com.example.android_second_yang;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SecondActivity extends Activity {

	EditText mEdit=null;
//	EditText mEdit = (EditText)findViewById(R.id.editText1);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		
		mEdit = (EditText)findViewById(R.id.editText1);
		Button button1 = (Button)findViewById(R.string.button_send);
		button1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			   String value = mEdit.getText().toString();
				Intent intent = new Intent();
				intent.putExtra("text", value);
				finish(); //이거꼭해야줘야됨
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.second, menu);
		return true;
	}

}
