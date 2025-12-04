package com.example.example.controller;

import org.eclipse.datagrid.cluster.nodelibrary.types.ClusterStorageManager;

import com.example.example.storage.DataRoot;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

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
