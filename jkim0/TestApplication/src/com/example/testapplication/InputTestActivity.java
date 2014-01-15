package com.example.testapplication;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InputTestActivity extends Activity {
	EditText mEdit = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input_test);
		
		mEdit = (EditText)findViewById(R.id.edit_box_activity);
		
		Button button = (Button)findViewById(R.id.ok);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendResult();
			}
		});
	}
	
	private void sendResult() {
		final String value = mEdit.getText().toString();
		if (value != null && value.length() > 0) {
			//Bundle data = new Bundle(); 
			//data.putString("text", value);
			Intent intent = new Intent();
			intent.putExtra("text", value);
			//intent.putExtras(data);
			setResult(RESULT_OK, intent);
			finish();
		} else {
			new AlertDialog.Builder(InputTestActivity.this)
			.setMessage(R.string.msg_no_input)
			.create()
			.show();
		}
	}
}
