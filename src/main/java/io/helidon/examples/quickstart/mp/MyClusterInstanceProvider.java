package io.helidon.examples.quickstart.mp;

import one.microstream.cluster.nodelibrary.common.ClusterStorageManager;
import one.microstream.cluster.nodelibrary.common.spi.ClusterStorageManagerProvider;

public class MyClusterInstanceProvider implements ClusterStorageManagerProvider
{
	@Override
	public ClusterStorageManager<DataRoot> provideClusterStorageManager()
	{
		return DB.get().storage();
	}
}
