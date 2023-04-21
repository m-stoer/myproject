package com.example.example;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/")
public class ExampleResource
{
	@GET
	public Response root()
	{
		return Response.ok("Data root, created @ " + MicroStream.root().getData()).build();
	}
}
