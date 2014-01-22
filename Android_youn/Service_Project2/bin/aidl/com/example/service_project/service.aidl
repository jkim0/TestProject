package com.example.service_project;

import com.example.service_project.CountingListener;

interface service{
	int getvalue();
	void Change_Mode();
	void register(CountingListener register);
	
}