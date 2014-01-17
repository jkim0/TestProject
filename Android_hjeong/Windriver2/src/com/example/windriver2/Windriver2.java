package com.example.windriver2;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Windriver2 extends Activity {

	static final int DIALOG= 1;
	LayoutInflater m_edit_inflater=null;
	View m_edit_layout = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_windriver2);
		Button Dialog_button= (Button)findViewById(R.id.Dialog);
		
		m_edit_inflater = getLayoutInflater();
		m_edit_layout = m_edit_inflater.inflate(R.layout.edit_text2, null);
		
		Dialog_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Toast.makeText(Windriver2.this, "Button", Toast.LENGTH_SHORT).show();
				showDialog(DIALOG);
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
				.setMessage("This will end the activity")
				.setView(m_edit_layout)
				.setPositiveButton("I agree", new OkOnClickListener())
			    .setNegativeButton("No, no", new CancelOnClickListener())
			    //AlertDialog dialog = builder.create();
			    .show();
			    //showDialog(id);
		}
		//return super.onCreateDialog(id);
		return dialog;
	}
	
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
	/*
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
	}*/
	
}