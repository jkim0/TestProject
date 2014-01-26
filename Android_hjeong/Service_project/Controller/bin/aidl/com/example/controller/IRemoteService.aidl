package com.example.controller;
import com.example.controller.IRemoteServiceCallback;

interface IRemoteService {
	void registerCallback(IRemoteServiceCallback cb);
	void unregisterCallback(IRemoteServiceCallback cb);
}