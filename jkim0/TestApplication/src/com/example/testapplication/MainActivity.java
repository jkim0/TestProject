package com.example.testapplication;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

public class MainActivity extends Activity {
	static final String TAG = "MainActivity";
	static final String ACTION = "com.example.testapplication.action.INPUT";
	static final int REQUEST_CODE = 0;
	static final int INPUT_DIALOG = 0;
	LayoutInflater mInflater = null;
	View mCustomLayout = null;
	TextView mValue1 = null;
	TextView mValue2 = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         
        Button button1 = (Button)findViewById(R.id.button1);
        Button button2 = (Button)findViewById(R.id.button2);
        mValue1 = (TextView)findViewById(R.id.value1);
        mValue2 = (TextView)findViewById(R.id.value2);
        
        mInflater = getLayoutInflater();
        mCustomLayout = mInflater.inflate(R.layout.dialog_content, null);
        
        button1.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(INPUT_DIALOG);
			}
		 });
        
        button2.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				launchInputActivity();
			}
		});
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
    	switch(id) {
    	case INPUT_DIALOG:
    		Dialog dialog = new AlertDialog.Builder(MainActivity.this)
    		.setTitle(R.string.dialog_title)
    		.setView(mCustomLayout)
			.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					final EditText edit = (EditText)mCustomLayout.findViewById(R.id.edit_box_dialog);
					final String text = edit.getText().toString();
					Log.i(TAG, "##### text = " + text);
					mValue1.setText(text);
				}
			})
			.setNegativeButton(R.string.cancel, null)
			.setCancelable(false)
			.create();
    		
    		return dialog;
    	}
    	return null;
    }
    
    @Override
    protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
    	super.onPrepareDialog(id, dialog, args);
    	switch(id) {
    	case INPUT_DIALOG:
    		final EditText edit = (EditText)mCustomLayout.findViewById(R.id.edit_box_dialog);
			edit.setText(null);
    		break;
    	}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	switch(requestCode) {
    	case REQUEST_CODE:
    		if (resultCode == RESULT_OK) {
    			//final Bundle data = intent.getExtras();
    			//final String text = data.getString("text");
    			final String text = intent.getStringExtra("text");
    			mValue2.setText(text);
    		}
    		break;
    	}
    	
    }
    
    private void launchInputActivity() {
    	Intent intent = new Intent(this, InputTestActivity.class);
		startActivityForResult(intent, REQUEST_CODE);	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()) {
		case R.id.input_from_dialog:
			showDialog(INPUT_DIALOG);
			return true;
		case R.id.input_from_activity:
			launchInputActivity();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
  
    
}
