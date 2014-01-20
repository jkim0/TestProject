package com.example.basic_project;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	
	TextView view1;
	TextView view2;
	Button button1;
	Button button2;

	int start_dialog =0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		view1 = (TextView)findViewById(R.id.text_Dialog);
		view2 = (TextView)findViewById(R.id.text_Activity);
		button1 = (Button)findViewById(R.id.button_Dialog);
		button2 = (Button)findViewById(R.id.button_Activity);
		final Intent intent = new Intent(this, Input_data.class);	
		//왜 final을 안붙이면 startActivityForResult();에서 Error가 나는가?
		
		
		button1.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				showDialog(start_dialog);
			}
		});
		
		button2.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				startActivityForResult(intent,0);
			}
		});
	}

		
	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		//return super.onCreateDialog(id);
		Toast.makeText(this, "OnCreateDialog", Toast.LENGTH_SHORT).show();
		Dialog dialog=null;
		final EditText Dialog_Text = new EditText(this);
		//final이 아니면 String Edit_Message = Dialog_Text.getEditableText().toString();
		//위에 명령어 Error
		if(id == start_dialog)
		{
			dialog = new AlertDialog.Builder(MainActivity.this)
			.setTitle("Input_Text")
			.setView(Dialog_Text)
			.setPositiveButton("ok", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface arg0, int arg1)
				{
					Toast.makeText(MainActivity.this, "Button_OK Click", Toast.LENGTH_SHORT).show();
					// TODO Auto-generated method stub
						String Edit_Message = Dialog_Text.getEditableText().toString();
						view1.setText(Edit_Message);
						Dialog_Text.setText(null);
				}
				})
			.setNegativeButton("cancel", null)
			.create();
		}
		return dialog;
	}


	//startActivityResult(); 에서 호출된 Activity가 비활성화되고 나서 호출되는함수
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
			if(requestCode == 0)
			{
				if(resultCode == RESULT_OK)
				{
					String Activity_Message = data.getStringExtra("Activity_str");
					view2.setText(Activity_Message);
				}
				
				else
				{
					view2.setText("Fail");
				}
				
			}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
