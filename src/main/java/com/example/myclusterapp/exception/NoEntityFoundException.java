package com.example.myclusterapp.exception;

public class NoEntityFoundException extends Exception
{
	public NoEntityFoundException()
	{
	}

	public NoEntityFoundException(String msg)
	{
		super(msg);
	}

	public NoEntityFoundException(Throwable cause)
	{
		super(cause);
	}

	public NoEntityFoundException(String msg, Throwable cause)
	{
		super(msg, cause);
	}
}
