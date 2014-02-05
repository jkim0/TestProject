package com.example.service_y;

import com.example.service_y.ISimpleListener;
interface ISimpleAIDL{
	int getvalue(int value);
	void startCounting();
	void stopCounting();
	boolean Change_Mode();
	void registerListener(ISimpleListener callback);
	void unregisterListener(ISimpleListener callback);
	void reset();
	void setMode(int mode);
	int getMode();
	void selectIPC(boolean use);
	
} 

