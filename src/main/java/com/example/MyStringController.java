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

	@Put(value = "/write", consumes = MediaType.TEXT_PLAIN)
	public void postMyString(@Body final String myString)
	{
		this.logger.info("Received post my string");
		this.myStringDAO.addMyString(myString);
	}

	@Delete("/{myString}")
	public void deleteMyString(@QueryValue final String myString)
	{
		this.myStringDAO.deleteMyString(myString);
	}
}