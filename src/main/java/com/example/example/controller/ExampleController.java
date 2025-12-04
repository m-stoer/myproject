package com.example.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.example.storage.DataRoot;

import one.microstream.enterprise.cluster.nodelibrary.common.ClusterStorageManager;

@RestController("/")
public class ExampleController
{
	private final ClusterStorageManager<DataRoot> storage;
	private final DataRoot root;

	public ExampleController(final ClusterStorageManager<DataRoot> storageManager)
	{
		this.storage = storageManager;
		this.root = this.storage.root().get();
	}
	
	@GetMapping
	public String root()
	{
		return "Data root, created @ " + root;
	}
}
