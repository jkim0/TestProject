package com.example.remoteservice2;

import com.example.remoteservice2.IRemoteServiceCallback;

interface IRemoteService {
	void registerCallback(IRemoteServiceCallback cb);
	void unregisterCallback(IRemoteServiceCallback cb);
	int example(int i);
}