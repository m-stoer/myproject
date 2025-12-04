package com.example.example.storage;

import java.time.LocalDateTime;

public class DataRoot
{
	private String data;
	
	public DataRoot()
	{
		this.data = LocalDateTime.now().toString();
	}
	
	public String getData()
	{
		return this.data;
	}
	
	public void setData(final String data)
	{
		this.data = data;
	}
}
