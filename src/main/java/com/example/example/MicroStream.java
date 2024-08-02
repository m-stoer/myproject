package com.example.example;

import org.eclipse.serializer.reference.Lazy;

import one.microstream.enterprise.cluster.nodelibrary.common.ClusterEnv;
import one.microstream.enterprise.cluster.nodelibrary.common.ClusterStorageManager;
import one.microstream.enterprise.cluster.nodelibrary.common.impl._default.BackupDefaultClusterStorageManager;
import one.microstream.enterprise.cluster.nodelibrary.common.impl._default.NodeDefaultClusterStorageManager;
import one.microstream.enterprise.cluster.nodelibrary.common.impl.dev.DevClusterStorageManager;
import one.microstream.enterprise.cluster.nodelibrary.common.impl.micro.MicroClusterStorageManager;
import one.microstream.enterprise.cluster.nodelibrary.common.spi.ClusterStorageManagerProvider;

public class MicroStream implements ClusterStorageManagerProvider
{
	private static Lazy<DataRoot> root;
	private final static ClusterStorageManager<DataRoot> storage;

	static
	{
		final var root = new DataRoot();
		if (!ClusterEnv.isProdMode())
		{
			storage = new DevClusterStorageManager<>(root);
		}
		else if (ClusterEnv.isMicro())
		{
			storage = new MicroClusterStorageManager<>(root);
		}
		else
		{
			if (ClusterEnv.isBackupNode())
			{
				storage = new BackupDefaultClusterStorageManager<>(root);
			}
			else
			{
				storage = new NodeDefaultClusterStorageManager<>(root);
			}
		}

		MicroStream.root = storage.root();
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
