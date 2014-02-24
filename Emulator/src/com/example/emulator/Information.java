package com.example.emulator;


public class Information{
	public String wifi;
	public String bluetooth;
	public String screen;
	
	public Information()
	{
		wifi = "off";
		bluetooth = "off";
		screen = "off";
	}
	
	public void setValue_wifi(String setwifi, String setbluetooth, String setscreen)
	{
		wifi = setwifi;
		bluetooth = setbluetooth;
		screen = setscreen;
	}
}

