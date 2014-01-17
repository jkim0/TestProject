package com.example.basic_project;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;


public class MainActivity extends Activity 
{
	TextView view1;			//첫 번째 TextView선언
	TextView view2;			//두 번째 TextView선언
	Button button1;			//Dialog 버튼
	Button button2;			//Activity 버튼

	int start_dialog =0;		//Show_Dialog Id값 설정
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//위에서 선언한 변수와 실제 Layout과 연결
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
				showDialog(start_dialog);				//Dialog 호출		

			}
		});
		
		button2.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				startActivityForResult(intent,0);			//Activity 불러오기
			}
		});
	}

		
	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) 
	{
		// TODO Auto-generated method stub
		//return super.onCreateDialog(id);
		
		Dialog dialog=null;
		final EditText Dialog_Text = new EditText(this);
		/*
			final이 아니면 String Edit_Message = Dialog_Text.getEditableText().toString();
			위에 명령어 Error
		*/
		if(id == start_dialog)		//제대로 값을 전달받았으면 Dialog 생성
		{
			dialog = new AlertDialog.Builder(MainActivity.this)
			.setTitle("Input_Text")
			.setView(Dialog_Text)
			.setPositiveButton("ok", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface arg0, int arg1)
				{
					// TODO Auto-generated method stub
						String Edit_Message = Dialog_Text.getEditableText().toString();
						view1.setText(Edit_Message);
						Dialog_Text.setText(null);
				}
			})
			.setNegativeButton("cancel", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface arg0, int arg1)
				{
					// TODO Auto-generated method stub
					Dialog_Text.setText(null);
				}
			})
			.create();
		 }
		
		 return dialog;
	}


	//startActivityResult(); 에서 호출된 Activity가 비활성화되고 나서 호출되는함수
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
			if(requestCode == 0)
			{
				if(resultCode == RESULT_OK)
				{
					String Activity_Message = data.getStringExtra("Activity_str");
					/* 
						연결된 Intent data의 Input_data_activity에서 "Activity_str"에서 입력받은
					    문자를 가져온다.
					*/
					view2.setText(Activity_Message);
				}
				
				else
				{
					view2.setText("Fail");
				}
				
			}
	}
	
	//OptionMenu  
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}//최초 App 실행 시 1회 호출


	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// TODO Auto-generated method stub
		 switch (item.getItemId()) 		//Item의 Id를 가져온다
		 {  
		 		case R.id.Show_Dialog:
		 		{
		 			showDialog(start_dialog);
		 			return true;     
		 		}
		 		case R.id.Show_Activity:
		 		{
		 			final Intent intent = new Intent(this, Input_data.class);
		 			startActivityForResult(intent,0);
		 			return true;
		 		}

		 		default:
		 		{
		 			return super.onOptionsItemSelected(item);
		 		}
         }  
	}	//메뉴가 Select 될 때마다 호출
}





