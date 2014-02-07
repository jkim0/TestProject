package com.example.emulator;

import android.app.Activity;
import android.app.Instrumentation;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class EmulatorSpinner extends Activity {
	private final int COMMAND=1;
	private final int NUMBER=2;
	private final int CHARACTER=3;
	public String show=null;
	int arrayName;
//	private int check_flag=0;
//	private int check_flag2=0;
	public String TAG="SPINNER";
	public Spinner mSpinner1,mSpinner2;

	@Override
	public void onCreate(Bundle savedInstanceState) {

	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_spinner);
	    Spinner mSpinner1= (Spinner)findViewById(R.id.spinner1);
	    ArrayAdapter<CharSequence> select = ArrayAdapter.createFromResource(this, R.array.select, android.R.layout.simple_dropdown_item_1line);
	    Log.d(TAG,"spinner 1, this ="+this);
	    mSpinner1.setAdapter(select);
		 mSpinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
			 
			 		   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {  			   
			 			  if(position!=0){
			 			   Log.d(TAG,"position="+position);
			 			   
			 			   arrayName = choose_spinner(position);
			 				Spinner mSpinner2 = (Spinner) findViewById(R.id.spinner2);
			 				Log.d(TAG,"spinner = "+mSpinner2);
			 				ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(parent.getContext(), arrayName , android.R.layout.simple_spinner_item);
			 				Log.d(TAG,"2) this = "+this);
			 			   adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			 				mSpinner2.setAdapter(adapter);
			 				mSpinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
			 				 
			 						   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			 							   	if(position!=0){
			 							   show = (String) parent.getSelectedItem();
			 							   Log.d(TAG,"9");
			 							   Toast.makeText(EmulatorSpinner.this, show, Toast.LENGTH_SHORT).show();
			 							   Log.d(TAG,"10");
			 							   get_key_number(COMMAND, position);
			 							   Log.d(TAG,"11");
			 							   	}
			 						   }
			 					 
			 					      public void onNothingSelected(AdapterView<?> parent) {
			 					         	  Toast.makeText(EmulatorSpinner.this, "Spinner1: unselected", Toast.LENGTH_SHORT).show();
			 					       }
			 					   });
			 			
			 				
			 			
					}	 
			
			 		   }
			 		   public void onNothingSelected(AdapterView<?> parent) {
				  
				         	  Toast.makeText(EmulatorSpinner.this, "unselected", Toast.LENGTH_SHORT).show();		       
				      }
				     
				   });
		 
		 
		 //  finish();
	}
	
	int  choose_spinner(int position){

	   int arrayName=0;
	   switch(position){
	   case COMMAND:
			arrayName = R.array.command;
			break;
		case NUMBER:
			arrayName=R.array.number;
			break;
		case CHARACTER:
			arrayName= R.array.character;
			break;
	   }	
	   Log.e(TAG,"arrayName = "+arrayName);
	   return arrayName;
	}
		
	
	void get_key_number(int list, int position){
		int set_number = 0;
		
		if(list==NUMBER){
			set_number=6+position;
		}
		else if(list==CHARACTER){
			set_number=28+position;
		}
		else if(list==COMMAND){
			if(position>3){
				//26,27,28이니까 /
				set_number=22+position;
			}
			else{
				set_number = 3+position;
			}
		}
		else{//errorcheck
			Log.e(TAG,"spinner error");
		}
		press_key(set_number);
		
	}
	///아놔...파이널처리 모르겟음...
	void press_key(final int value){
		new Thread(new Runnable(){
			public void run(){
				new Instrumentation().sendKeyDownUpSync(value);
			}
		}).start();
	}
}


