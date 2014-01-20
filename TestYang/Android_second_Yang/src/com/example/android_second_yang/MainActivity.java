package com.example.android_second_yang;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	// showDialog 변수 넘겨주기 위한 수
	static final int ZERO=0;	
	// menu를 위한 변수
	public static final int MENU_ONE = Menu.FIRST+1;
	public static final int MENU_TWO = Menu.FIRST+2;
	public static final int MENU_Three = Menu.FIRST+3;
	// 뷰 변수, setcontetnview
	View mLayoutforView = null;
	// xml이 여러개일때, 나중에 여기서.inflate로 view봅아낸다
	LayoutInflater mInflater =null;
	//화면에 찍어줄 textview2개
	TextView mTextview1=null;
	TextView mTextview2=null;
	
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//xml 파일을 가지고 뷰 그릴걸 준비
		setContentView(R.layout.activity_main);
		
		Button button1= (Button)findViewById(R.id.button1_name);
		Button button2= (Button)findViewById(R.id.button2_name);
		LinearLayout center =(LinearLayout)findViewById(R.id.center);
		//final Button button3= (Button)findViewById(R.id.button3_name);
		mTextview1 = (TextView)findViewById(R.id.textview1);
		mTextview2 = (TextView)findViewById(R.id.textview2);
		//Button_Listener
	//contextmenu를 위한 버튼 설정
		//registerForContextMenu(button3);
//레이아웃 인플레이터 객체
		mInflater = getLayoutInflater();
		//뷰타입의 레이아웃?
		mLayoutforView= mInflater.inflate(R.layout.dialoglayout, null);
		button1.setOnClickListener(new View.OnClickListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// dialog fragment로 어떻게구현해야하지????
				showDialog(ZERO);
			}
		});
		
		button2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mIntent2= new Intent(MainActivity.this, SecondActivity.class);
			///여기가 진짜 중요한부분, 그전에는 startActivity로 짯기때문에, 리쥼에서 고쳐줘야하는데 
		    //이것덕분에 알아서 이함수가 호출되고난뒤에 리턴으로 
				startActivityForResult(mIntent2, 0);
			}
		});
		
		registerForContextMenu(center);
		
	}
	

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		//Toast.makeText(context, "들어옴",TOAST_)
		Log.d("tag","onCreateDialog"+id);
		Dialog dialog = new AlertDialog.Builder(MainActivity.this)
		.setView(mLayoutforView)
		.setPositiveButton(R.string.button_send, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				final EditText edit= (EditText)mLayoutforView.findViewById(R.id.editText1);
				final String text = edit.getText().toString();
				mTextview1.setText(text);
			}
		})
		.setNegativeButton(R.string.button_quit, null)
		.setCancelable(false) //뒤로가기안되
		.create();

		return dialog;
	}
	


	@Override
	@Deprecated
	protected void onPrepareDialog(int id, Dialog dialog) {
		// TODO Auto-generated method stub
		//final에서 에러남~
		final EditText edit = (EditText)mLayoutforView.findViewById(R.id.editText1);
		edit.setText(null);
		super.onPrepareDialog(id, dialog);
		
		Log.d("tag","onCreateDialog"+id);
		
	}


	

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		String str=null;
		Log.i("message", "OncreateContext_ll");
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(Menu.NONE, MENU_ONE, Menu.NONE, "First Choice");
		menu.add(Menu.NONE, MENU_TWO, Menu.NONE, "Second Choice");
		menu.add(Menu.NONE, MENU_Three, Menu.NONE, "Third Choice");
	}


	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		String selectedMenu = null;
		
		switch(item.getItemId()){
			case MENU_ONE: 
				selectedMenu = "menu_one";
			case MENU_TWO:
				selectedMenu = "menu_two";
			case MENU_Three:
				selectedMenu = "menu_three";
		}
		
		//Toast.makeText(MainActivity.this, selectedMenu, Toast LENGTH_SHORT);
		return super.onContextItemSelected(item);
	}


	@Override
	public void onContextMenuClosed(Menu menu) {
		// TODO Auto-generated method stub
		
		super.onContextMenuClosed(menu);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	
		final String text= data.getStringExtra("text");
		mTextview2.setText(text);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		
		   switch (item.getItemId()) {  
           case R.id.item1:  
             Toast.makeText(getApplicationContext(),"Item 1 Selected",Toast.LENGTH_LONG).show();  
           return true;     
 
           case R.id.item2:  
               Toast.makeText(getApplicationContext(),"Item 2 Selected",Toast.LENGTH_LONG).show();  
             return true;     
             
           case R.id.item3:  
               Toast.makeText(getApplicationContext(),"Item 3 Selected",Toast.LENGTH_LONG).show();  
             return true;     
 
             default:  
               return super.onOptionsItemSelected(item);  
       }  
		
		
	}

	}
