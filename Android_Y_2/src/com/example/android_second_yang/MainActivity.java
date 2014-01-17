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
	//메뉴First는 1로 계속고정, 한마디로 첫번째 메뉴에는 2를 주는거야
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
	//Context Menu를 위한 리니어 레이아웃
		LinearLayout center =(LinearLayout)findViewById(R.id.center);
		//final Button button3= (Button)findViewById(R.id.button3_name);
		mTextview1 = (TextView)findViewById(R.id.textview1);
		mTextview2 = (TextView)findViewById(R.id.textview2);
		//Button_Listener
	//contextmenu를 위한 버튼 설정
		//registerForContextMenu(button3);
//레이아웃-인플레이터 객체를 받
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
			//사실여기에는, (스트링) + (인터페이스타입이 들어간다)
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				final EditText edit= (EditText)mLayoutforView.findViewById(R.id.editText1);
				final String text = edit.getText().toString();
				mTextview1.setText(text);
			}
		})
		.setNegativeButton(R.string.button_quit, null)
		.setCancelable(false) //뒤로가기안되		//
		.create();
//////////
//사실 저위의 구문들은 다이얼로그. 그리고 다디얼로그. 그리고 마지막에 다디얼로그.create();로 생성
		return dialog;
	}
	


	@Override
	@Deprecated
	protected void onPrepareDialog(int id, Dialog dialog) {
		// TODO Auto-generated method stub
		//다이얼로그 뷰타입의 객체에서 아이디 뽑아내고 
		final EditText edit = (EditText)mLayoutforView.findViewById(R.id.editText1);
		edit.setText(null); //그 임시의 타입에 보여주는 에디트텍스트부분을 셋 텍스트로 지
		super.onPrepareDialog(id, dialog);
		//아무리봐도, 수퍼타입으로 넘겨주면 알아서 찍어주는 화면과, 관리를 다해주는거같
		Log.d("tag","onCreateDialog"+id);
		
	}
//컨텍스메뉴 만들어줌 , 참고로 xml에서 리니어레이아웃타입은 안에 넣어주거나, 렐러티브를 밖에 설정해보자
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		String str=null;
		Log.i("MainAvtivity","createContext_ll");
		//로그아이 앞에 TAG로 MainActivity final로 해놓으면 편하대?
		//그룹아이디, 아이템아이디, 순서, tilte(보일내용), 메뉴타입아래.none이라는게 아이디이자, 순서까지 감
		menu.add(Menu.NONE, MENU_ONE, Menu.NONE, "First Choice");
		menu.add(Menu.NONE, MENU_TWO, Menu.NONE, "Second Choice");
		menu.add(Menu.NONE, MENU_Three, Menu.NONE, "Third Choice");

		super.onCreateContextMenu(menu, v, menuInfo);
	}

	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
//메뉴아이템 타입이 들어오면, 거기서 선택메뉴 아이디를 뽑아서, 어떤메뉴인지 판단하기(
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

//안닫아도되네???
	@Override
	public void onContextMenuClosed(Menu menu) {
		// TODO Auto-generated method stub
		
		super.onContextMenuClosed(menu);
	}

//startActivity for result를 쓸경우 알아서, 온 크리에이트 후에 다시 온크리에이트로 감(온리줌으에로 돌아오지않는다)
// 그디음, set_result로 액티비티가 닫히기 전에 (putStringExtra)인텐트로 마지막 스트링넘겨
// onActivityResult로, getStringExta로 받아서 텍스트에 저장해서 
// TextView타입속성중에 setText로 그걸 찍어줌 
	
	//cf) putstringExtra("text",value); value변수에들어가있는걸 나는 멤버(텍스트)에저장할
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	//나는 membet Text를 열어볼래.. 이뜻이
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
