package com.example.myclusterapp.storage;

public record DataRoot(Authors authors, Books books)
{
	public DataRoot()
	{
		this(new Authors(), new Books());
	}
}
