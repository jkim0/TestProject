package com.example.android_1;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
	final Context context=this;
	private Button button1,button2;
	static String temp="No";
	@Override
	
	//When onCreate mode starts
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//여기는 레이아웃의 xml넘겨주는거양 ㅠ0ㅠ	
		setContentView(R.layout.activity_main);
		// 여기까지는 그냥 주어진것, 왠지는 나중에 공부해보기
		button1=(Button)findViewById(R.id.button1);
		button2=(Button)findViewById(R.id.button2);
		//Activity 두번째꺼로 넘겨주는 인텐트!
		final Intent intent = new Intent(this, SecondActivity.class);
		
		button1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//이건, 내가 버튼밑에 있던 텍스트뷰를 뷰로 가져오는거야!!
				final TextView screen_text = (TextView)findViewById(R.id.text11);
				AlertDialog.Builder popup = new AlertDialog.Builder(context);
				popup.setTitle("Input Box");
				popup.setMessage("Write down your message");
	//여기서 인풋을 저장해오네오네오네오네getting words from 
				final EditText user_input= new EditText(context);
				// 입력된걸 화면에 보여주기 
				popup.setView(user_input);
				popup.setPositiveButton("Input", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
					//만약 입력버튼눌렀다면?
						String str = user_input.getEditableText().toString();
						screen_text.setText(str);
						
						
					}
				});
				
				popup.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				});
				
				AlertDialog popup2= popup.create();
				//checkj사항, 여기가 이상해요~~
				popup2.show();
				//		alertDialog.show();
				
			}
		});
		
		button2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//이것같은경우에는 새로운 클래스 extends activity이므로 넘긴다
				startActivity(intent);
			}
		});
	}
	@Override
	public void onStart(){
			super.onStart();
			Toast.makeText(this,"Onstart Mode", Toast.LENGTH_LONG).show();
	}
	@Override
	public void onResume(){
		super.onResume();
		second_line temp=new second_line();
		String printout_line=temp.CommandFunction_call();
		TextView text2=(TextView)findViewById(R.id.text22);
		
		text2.setText(printout_line);
		
	
	}
	public void onPause(){
		super.onPause();
	}
	public void onDestroy(){
		super.onDestroy();
		TextView text1 = (TextView)findViewById(R.id.text11);
		TextView text2 = (TextView)findViewById(R.id.text22);
//이부분이해안감//
		text1.setText(null);
		text2.setText(null);
	}
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	*/

}
