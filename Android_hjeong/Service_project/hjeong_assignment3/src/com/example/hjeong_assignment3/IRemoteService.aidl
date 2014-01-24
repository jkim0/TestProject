package com.example.hjeong_assignment3;

import com.example.hjeong_assignment3.ISend_Value;

interface IRemoteService {
	void Registercallback(ISend_Value cb);
	void Unregistercallback(ISend_Value cb);
}