package com.example.windriver2;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Windriver2 extends Activity {

	static final int DIALOG= 1;
	LayoutInflater m_edit_inflater=null;
	View m_edit_layout = null;
	Intent m_intent=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		// 상위 클레스인 Activity 의 onCreate 메소드를 실행시켜 
		// class Windriver2를 동작시킬때 필요한 경우의 수를 초기화 한다.
		setContentView(R.layout.activity_windriver2); 
		//activity_windriver2 layout을 등록, 화면에 activity_windriver2 layout이 보여 진다.
		
		Button Dialog_button= (Button)findViewById(R.id.Dialog); //Dialog_button 버튼 등록
		Button Edit_text = (Button)findViewById(R.id.Edit_text); //Edit_text 버튼 등록
		m_intent = new Intent(Windriver2.this,Write_text.class);
		
		m_edit_inflater = getLayoutInflater(); //LayoutInflater 객체 생성
		m_edit_layout = m_edit_inflater.inflate(R.layout.edit_text2, null);//layout 객체 생성
		 LinearLayout linearLayoutMain = (LinearLayout)findViewById(R.id.Context_View);
		
		 registerForContextMenu(linearLayoutMain);
        
		Dialog_button.setOnClickListener(new View.OnClickListener() { 
			//Dialog_button의 버튼 기능을 설정한다.
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Toast.makeText(Windriver2.this, "Button", Toast.LENGTH_SHORT).show();
				showDialog(DIALOG);
				//showDialog 함수는 source -> override 함수에서 찾을수 없었다.
				//showDialog 를 private 함수라고 하는데 private은 private가 속한 class만 접근이 가능한데
				//어떻게 class Windriver2에서 접근이 가능한지 궁금합니다.
				//함수명에 왜 -----이런 줄이 그어져 있는지 궁금합니다.
				//showDialog 를 실행한뒤 onCreateDialog를 실행합니다.
			}
		});	//버튼 기능 설정 끝
		
		Edit_text.setOnClickListener(new View.OnClickListener() {
			//Edit_text 버튼 기능 설정
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivityForResult(m_intent, 2);
				//Edit_text 버튼을 누르면 startActiviyForResult가 실행되고
				//Write_text activity로 이동합니다.
			}
		});
	}
	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		Toast.makeText(Windriver2.this, "onCreateDialog", Toast.LENGTH_SHORT).show();
		Dialog dialog=null;
		switch (id){
			case DIALOG:
				dialog = new AlertDialog.Builder(this)
				.setMessage(R.string.Dialog_tile)
				.setView(m_edit_layout) // m_edit_layout는 edit_text2-> layout을 가리키고 있다.
				.setPositiveButton(R.string.Dialog_ok, new OkOnClickListener())
			    .setNegativeButton(R.string.Dialog_calnce, new CancelOnClickListener())
			    //.setCancelable(false)-> back_button을 누르면 Dialog 창이 없이지게 하는 기능
			    .show();    
		}
		//return super.onCreateDialog(id);
		return dialog;
	}
	/**/
	private final class CancelOnClickListener implements
	  DialogInterface.OnClickListener {
	  public void onClick(DialogInterface dialog, int which) {
		  Toast.makeText(Windriver2.this, "Cancle", Toast.LENGTH_SHORT).show();
	  }
	}

	private final class OkOnClickListener implements
	  DialogInterface.OnClickListener {
	  public void onClick(DialogInterface dialog, int which) {
		  Toast.makeText(Windriver2.this, "OK", Toast.LENGTH_SHORT).show();
		  final EditText input = (EditText)m_edit_layout.findViewById(R.id.edit_message2);
         final String str = input.getText().toString();
         TextView view = (TextView)findViewById(R.id.text);
         view.setText(str);
	  }
	}
	/**/
	@Override
	@Deprecated
	protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
		// TODO Auto-generated method stub
		super.onPrepareDialog(id, dialog, args);
		
		Toast.makeText(Windriver2.this, "onPrepareDialog", Toast.LENGTH_SHORT).show();
		switch(id){
			case DIALOG: 
				final EditText input = (EditText)m_edit_layout.findViewById(R.id.edit_message2);
				input.setText(null);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		TextView view = (TextView)findViewById(R.id.Editext_show);
		
		if(requestCode == 2)
		{
			String message=data.getStringExtra("MESSAGE");
			view.setText(message);		
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		
		getMenuInflater().inflate(R.menu.windriver2, menu);
        return true;  
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		 switch (item.getItemId()) {  
         case R.id.item1:  
           Toast.makeText(getApplicationContext(),"Item 1 Selected",Toast.LENGTH_LONG).show();
           showDialog(DIALOG);
         return true;     

         case R.id.item2:  
             Toast.makeText(getApplicationContext(),"Item 2 Selected",Toast.LENGTH_LONG).show();
             startActivityForResult(m_intent, 2);
           return true;
          
         default:
        	  return super.onOptionsItemSelected(item); 	   
		 }
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		
		menu.add("Dialog");
        menu.add("Write");
         super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		 if(item.getTitle()=="Dialog"){  
             Toast.makeText(getApplicationContext(),"Context_Dialog",Toast.LENGTH_LONG).show();
             showDialog(DIALOG);
         }    
         else if(item.getTitle()=="Write"){  
             Toast.makeText(getApplicationContext(),"Context_Write",Toast.LENGTH_LONG).show(); 
             startActivityForResult(m_intent, 2);
         }else{  
            return false;  
         }    
       return true;    
   }    
		
}
