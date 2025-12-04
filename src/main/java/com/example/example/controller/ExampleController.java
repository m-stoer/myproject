package com.example.example.controller;

import com.example.example.storage.DataRoot;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import one.microstream.enterprise.cluster.nodelibrary.common.ClusterStorageManager;

@Path("/")
public class ExampleController
{
	private final ClusterStorageManager<DataRoot> storage;
	private final DataRoot root;

	@SuppressWarnings({ "unchecked", "rawtypes"	})
	@Inject
	public ExampleController(final ClusterStorageManager storage)
	{
		this.storage = storage;
		this.root = this.storage.root().get();
	}
	
	@GET
	public String root()
	{
		return "Data root, created @ " + root.getData();
	}
}
