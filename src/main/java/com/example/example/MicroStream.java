package com.example.example;

import one.microstream.enterprise.cluster.nodelibrary.common.ClusterStorageManager;
import one.microstream.reference.Lazy;

public class MicroStream
{
	private final static Lazy<DataRoot>					 root   ;
	private final static ClusterStorageManager<DataRoot> storage;
	
	static
	{
		storage	= new ClusterStorageManager<>(new DataRoot());
		root    = storage.getRoot();
	}
	
	public static ClusterStorageManager<DataRoot> storage()
	{
		return storage;
	}
	
	public static DataRoot root()
	{
		return root.get();
	}
	
	private MicroStream()
	{
		// static only
	}
}
