/*
package com.example.android_second_yang;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.EditText;

public class DialogControler extends DialogFragment{
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();

	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    builder.setView(inflater.inflate(R.layout.dialoglayout, null))
	    // Add action buttons
	           .setPositiveButton(R.string.button_send, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	                   // sign in the user ...
	            	   final EditText edit = (EditText)mCustomLayout.findViewById(R.id.editText1);
	               }
	           })
	           .setNegativeButton(R.string.button_quit, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                  
	            	   // LoginDialogFragment.this.getDialog().cancel();
	               }
	           });      
	    return builder.create();
	}
	   public Dialog onPrepareDialog(int id, Dialog dialog, Bundle args){
		   super.onPrepareDialog(id,dialog,args);
		   
		   
	   }
	
}	
	/*
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
	    			//getActivity()는 return Context Type;
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setTitle(R.string.dialog_message)
	        		 .setMessage(R.string.dialog_message)
	               .setPositiveButton(R.string.button_send, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       // 
	        
	                	   final EditText edit = (EditText)findViewById(R.id.editText1);
	                   }
	               })
	               .setNegativeButton(R.string.button_quit, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       // User cancelled the dialog
	                   }
	               });
	        // Create the AlertDialog object and return it
	        return builder.create();
	    }
	    */
//}

