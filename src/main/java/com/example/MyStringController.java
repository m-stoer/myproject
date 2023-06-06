package com.example;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Get
	public List<String> getAllMyString()
	{
		this.logger.info("Received get all");
		return this.myStringDAO.getMyStrings();
	}

	@Get("/{myString}")
	public String getMyString(@QueryValue final String myString)
	{
		this.logger.info("Received get my string");
		return this.myStringDAO.getMyString(myString);
	}

	@Put(consumes = MediaType.TEXT_PLAIN)
	public void postMyString(@Body final String myString)
	{
		this.logger.info("Received post my string");
		final var writos = ServerRequestContext.currentRequest().get().getHeaders().get("Writos");
		if (writos != null)
		{
			this.logger.info("Request has writos with value: " + writos);
			if (writos == "Pogos")
			{
				this.logger.info("Correct value, therefore we are savin!");
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
