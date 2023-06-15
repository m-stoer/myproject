package com.example.example;

import java.util.HashSet;
import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import one.microstream.enterprise.cluster.nodelibrary.helidon.NotADistributorMapper;

@ApplicationScoped
@ApplicationPath("/")
public class JaxRsActivator extends Application
{
	@Override
	public Set<Class<?>> getClasses()
	{
		final var set = new HashSet<Class<?>>();
		set.add(NotADistributorMapper.class);
		set.add(MicrostreamResource.class);
		set.add(ExampleResource.class);
		return set;
	}
}
