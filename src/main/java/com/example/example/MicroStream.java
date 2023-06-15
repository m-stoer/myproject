package com.example.example;

import one.microstream.enterprise.cluster.nodelibrary.common.ClusterStorageManager;
import one.microstream.enterprise.cluster.nodelibrary.common.spi.ClusterStorageManagerProvider;
import one.microstream.reference.Lazy;

public class MicroStream implements ClusterStorageManagerProvider
{
	private final static Lazy<DataRoot> root;
	private final static ClusterStorageManager<DataRoot> storage;

	static
	{
		storage = new ClusterStorageManager<>(new DataRoot());
		root = storage.getRoot();
	}

	public static ClusterStorageManager<DataRoot> storage()
	{
		return storage;
	}

	public static DataRoot root()
	{
		return root.get();
	}

	@Override
	public ClusterStorageManager<DataRoot> provideClusterStorageManager()
	{
		return MicroStream.storage();
	}
}
