package com.example.click_context;

import android.os.Bundle;
import android.app.Activity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Click_context extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_click_context);
		LinearLayout linearLayoutMain = (LinearLayout)findViewById(R.id.linearLayoutMain);
        registerForContextMenu(linearLayoutMain);
    }
   
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        menu.add("새 게임");
        menu.add("이어하기");
        menu.add("설정");
    } 



    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if ("새 게임" == item.getTitle())
            Toast.makeText(this, "새 게임 시작은 곧 구현할 예정입니다.", Toast.LENGTH_SHORT).show();
        else if ("이어하기" == item.getTitle())
            Toast.makeText(this, "이어하기는 곧 구현할 예정입니다.", Toast.LENGTH_SHORT).show();
        else if ("설정" == item.getTitle())
            Toast.makeText(this, "설정은 곧 구현할 예정입니다.", Toast.LENGTH_SHORT).show(); 

        return true;
    }    

}
