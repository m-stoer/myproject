package com.example.example.storage;

import org.eclipse.datagrid.cluster.nodelibrary.helidon.RootProvider;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class ExampleRootProvider
{
	@ApplicationScoped
	@Produces
	public RootProvider rootProvider()
	{
		return DataRoot::new;
	}
}
