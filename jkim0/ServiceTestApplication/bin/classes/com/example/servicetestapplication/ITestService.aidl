package com.example.servicetestapplication;

import com.example.servicetestapplication.ICountingListener;

interface ITestService {
	void setMode(int mode);
	int getMode();
	void setUseListener(boolean use);
	void startCounting();
	void stopCounting();
	void reset();
	void registerCountingListener(ICountingListener listener);
	void unregisterCountingListener(ICountingListener listener);
}