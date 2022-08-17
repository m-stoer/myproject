package io.helidon.examples.quickstart.mp;

import one.microstream.cluster.nodelibrary.common.ClusterStorageManager;
import one.microstream.reference.Lazy;

public class DB
{
	private static final DB INSTANCE = new DB();
	
	public static DB get()
	{
		return INSTANCE;
	}
	
	private final Lazy<DataRoot> root;
	private final ClusterStorageManager<DataRoot> storage = new ClusterStorageManager<>(new DataRoot());

	public DB()
	{
		this.root = this.storage.getRoot();
	}
	
	public ClusterStorageManager<DataRoot> storage()
	{
		return this.storage;
	}

	public DataRoot root()
	{
		return this.root.get();
	}
}
