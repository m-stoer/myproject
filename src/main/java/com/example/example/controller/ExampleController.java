package com.example.example.controller;

import com.example.example.storage.DataRoot;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import one.microstream.enterprise.cluster.nodelibrary.common.ClusterStorageManager;

@Controller("/")
public class ExampleController
{
	private final ClusterStorageManager<DataRoot> storage;
	
	public ExampleController(final ClusterStorageManager<DataRoot> storage)
	{
		this.storage = storage;
	}
	
	@Get
	public String root()
	{
		return "Data root, created @ " + this.storage.root().get().getData();
	}
}
