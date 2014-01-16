package com.example.android_1;

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
import android.os.Build;


public class SecondActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		// Show the Up button in the action bar.
		
		final EditText user_input2=(EditText)findViewById(R.id.editText1);
		Button button=(Button)findViewById(R.id.button1);
		
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//나 일단 스트링 템프_텍스트 자체가, 전역변수야 이거 이렇게 쓸수있나 보자.
				
				String temp_text = user_input2.getEditableText().toString();
				Toast.makeText(SecondActivity.this, "click", Toast.LENGTH_SHORT).show();
				Toast.makeText(SecondActivity.this, temp_text,Toast.LENGTH_SHORT).show();
		////////////////////////여기 주목!///
				
				second_line temp=new second_line();
				temp.CommandFunction_save(temp_text);
			//여기다가 입력받았던걸 저장하고 가지고있을게 필요해 알겠니?아겠음알겟어?알겟어??????????????
				finish();
			}
		});
		//
		//setupActionBar();
	}

}
