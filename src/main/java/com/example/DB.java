package com.example;

import org.eclipse.store.storage.types.StorageManager;

import io.micronaut.eclipsestore.RootProvider;
import jakarta.inject.Singleton;

@Singleton
public class DB
{
	private final StorageManager storage;
	private final RootProvider<DataRoot> rootProvider;

	public DB(final RootProvider<DataRoot> rootProvider, final StorageManager storage)
	{
		this.storage = storage;
		this.rootProvider = rootProvider;
	}

	public StorageManager storage()
	{
		return this.storage;
	}

	public DataRoot root()
	{
		return this.rootProvider.root();
	}
}
