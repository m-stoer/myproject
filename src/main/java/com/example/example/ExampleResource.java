package com.example.example;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import one.microstream.enterprise.cluster.nodelibrary.common.ClusterStorageManager;

@Path("/")
@ApplicationScoped
public class ExampleResource
{
	private final DataRoot root;
	private final ClusterStorageManager<DataRoot> storage;

	@Inject
	public ExampleResource(final ClusterStorageManager<DataRoot> storage)
	{
		this.storage = storage;
		this.root = storage.root().get();
	}

	@GET
	public Response get()
	{
		return Response.ok(this.root.strings).build();
	}

	@PUT
	public Response put()
	{
		final var s = this.root.strings;
		s.add("Hello :)");
		this.storage.store(s);
		return Response.ok().build();
	}
}
