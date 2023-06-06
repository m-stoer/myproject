package com.example;

import java.util.List;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.context.ServerRequestContext;

@Controller("/")
public class MyStringController
{
	private final MyStringDAO myStringDAO = new MyStringDAO();

	@Get
	public List<String> getAllMyString()
	{
		System.out.println("Received get all");
		return this.myStringDAO.getMyStrings();
	}

	@Get("/{myString}")
	public String getMyString(@QueryValue final String myString)
	{
		System.out.println("Received get my string");
		return this.myStringDAO.getMyString(myString);
	}

	@Put(consumes = MediaType.TEXT_PLAIN)
	public void postMyString(@Body final String myString)
	{
		System.out.println("Received post my string");
		final var writos = ServerRequestContext.currentRequest().get().getHeaders().get("Writos");
		if (writos != null)
		{
			System.out.println("Request has writos with value: " + writos);
			if(writos == "Pogos")
			{
				System.out.println("Correct value, therefore we are savin!");
				this.myStringDAO.addMyString(myString);
			}
		}
	}

	@Delete("/{myString}")
	public void deleteMyString(@QueryValue final String myString)
	{
		this.myStringDAO.deleteMyString(myString);
	}
}
