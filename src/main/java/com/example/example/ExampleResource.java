package com.example.example;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/")
public class ExampleResource
{
	@GET
	public Response get()
	{
		return Response.ok(MicroStream.root().strings).build();
	}

	@PUT
	public Response put()
	{
		final var s = MicroStream.root().strings;
		s.add("Hello :)");
		MicroStream.storage().store(s);
		return Response.ok().build();
	}
}
