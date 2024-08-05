package com.example.example;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import one.microstream.enterprise.cluster.nodelibrary.helidon.RootProvider;

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
