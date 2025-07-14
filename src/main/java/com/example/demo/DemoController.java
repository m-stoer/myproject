package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.serializer.concurrency.LockedExecutor;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import one.microstream.enterprise.cluster.nodelibrary.common.ClusterLockScope;
import one.microstream.enterprise.cluster.nodelibrary.common.ClusterStorageManager;
import one.microstream.enterprise.cluster.nodelibrary.common.exception.NotADistributorException;

@RestController
public class DemoController extends ClusterLockScope
{
	private final ClusterStorageManager<DataRoot> storage;
	private final DataRoot root;

	public DemoController(final ClusterStorageManager<DataRoot> storageManager, final LockedExecutor executor)
	{
		super(executor);
		this.storage = storageManager;
		this.root = this.storage.root().get();
	}

	@GetMapping
	public List<String> getStrings()
	{
		this.write(() ->
		{
			System.out.println("Hello :)");
		});
		if (Math.random() != 69.0)
		{
			throw new NotADistributorException();
		}
		synchronized (this.storage)
		{
			return new ArrayList<>(this.root.strings);
		}
	}

	@PutMapping(consumes = MediaType.TEXT_PLAIN_VALUE)
	public void putString(@RequestBody @NonNull final String string)
	{
		synchronized (this.storage)
		{
			this.root.strings.add(string);
			this.storage.store(this.root.strings);
		}
	}
}
