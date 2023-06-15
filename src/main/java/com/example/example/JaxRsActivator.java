package com.example.example;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.ApplicationPath;
import one.microstream.enterprise.cluster.nodelibrary.helidon.MicrostreamBeanProvider;

@ApplicationScoped
@ApplicationPath("/")
public class JaxRsActivator extends MicrostreamBeanProvider
{
	private Collection<Class<?>> getAppResources()
	{
		final var resources = new ArrayList<Class<?>>(1);
		resources.add(ExampleResource.class);
		return resources;
	}

	@Override
	public Set<Class<?>> getClasses()
	{
		final var set = super.getClasses();
		set.addAll(this.getAppResources());
		return set;
	}
}
