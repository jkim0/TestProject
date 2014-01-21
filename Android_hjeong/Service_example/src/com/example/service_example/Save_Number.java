package com.example.service_example;

public class Save_Number {
	static int save_number;
	
	
	
	public int send()
	{
		return save_number;
	}
	
	public int reset()
	{
		save_number=0;
		return save_number;
	}

	public void save(int num) {
		// TODO Auto-generated method stub
		save_number=num;
		
	}

}
