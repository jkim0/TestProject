package com.example.windriver;

import android.content.Context;
import android.widget.Toast;



public class String_message {
	
	static String save_text=null;
	
	public void save(String message)
	{
		save_text= message;
		System.out.println(save_text);	
	}
	
	
	public String send()
	{
		return save_text;
	}

	
}
