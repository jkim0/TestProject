package com.example.windriver2;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Write_text extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write_text);
		final EditText editText = (EditText)findViewById(R.id.EditText1);
		Button send_button = (Button)findViewById(R.id.Button1);
		
		send_button.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String message=editText.getText().toString();  
              Intent intent=new Intent();  
              intent.putExtra("MESSAGE",message);  
                 
              setResult(2,intent);
              finish();	
			}
		});
	}
}
