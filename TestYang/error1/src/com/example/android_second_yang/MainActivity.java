package com.example.android_second_yang;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	
	static final int ZERO=0;	
	
	public static final int MENU_ONE = Menu.FIRST+1;
	public static final int MENU_TWO = Menu.FIRST+2;
	public static final int MENU_Three = Menu.FIRST+3;
	
	View mLayoutforView = null;
	LayoutInflater mInflater =null;
	TextView mTextview1=null;
	TextView mTextview2=null;
	
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//xml 파일을 가지고 뷰 그릴걸 준비
		setContentView(R.layout.activity_main);
		
		Button button1= (Button)findViewById(R.id.button1_name);
		Button button2= (Button)findViewById(R.id.button2_name);
		Button button3= (Button)findViewById(R.id.button3_name);
		//Button_Listener
		registerForContextMenu(button3);
		
		mInflater = getLayoutInflater();
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
				startActivity(mIntent2);
			}
		});
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
				final EditText edit = (EditText)mLayoutforView.findViewById(R.id.editText1);
				final String text = edit.getText().toString();
				mTextview1.setText(text);
			}
		})
		.setNegativeButton(R.string.button_quit, null)
		.setCancelable(false)
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
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
		return super.onContextItemSelected(item);
	}
	

}
