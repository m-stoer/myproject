package com.example;

import one.microstream.reference.Lazy;
import one.microstream.storage.embedded.types.EmbeddedStorage;
import one.microstream.storage.embedded.types.EmbeddedStorageManager;

public class DB
{
	private static final DB INSTANCE = new DB();

	public static DB get()
	{
		return INSTANCE;
	}

	private final Lazy<DataRoot> root;
	//private final ClusterStorageManager<DataRoot> storage = new ClusterStorageManager<>(new DataRoot());
	public final EmbeddedStorageManager DEBUG = EmbeddedStorage.start();

	public DB()
	{
		//this.root = this.storage.getRoot();
		if (this.DEBUG.root() == null)
		{
			this.DEBUG.setRoot(Lazy.Reference(new DataRoot()));
			this.DEBUG.storeRoot();
		}
		this.root = (Lazy<DataRoot>)this.DEBUG.root();
	}

//	public ClusterStorageManager<DataRoot> storage()
//	{
//		return this.storage;
//	}

	public DataRoot root()
	{
		return this.root.get();
	}
}
