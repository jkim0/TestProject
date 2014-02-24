package com.example.emulator;


public class Information{
	public boolean wifi;
	public boolean bluetooth;
	public boolean screen;
	
	public Information()
	{
		wifi = false;
		bluetooth = false;
		screen = false;
	}
	
	public void setValue_wifi(boolean setwifi, boolean setbluetooth, boolean setscreen)
	{
		wifi = setwifi;
		bluetooth = setbluetooth;
		screen = setscreen;
	}
	
	public boolean getValue_wifi()
	{
		return wifi;
	}
	
	public boolean getValue_bluetooth()
	{
		return bluetooth;
	}
	
	public boolean getValue_screen()
	{
		return screen;
	}
}

