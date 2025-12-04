package com.example.example.storage;

import org.eclipse.datagrid.cluster.nodelibrary.springboot.RootProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExampleRootProvider
{
	@Bean
	public RootProvider<DataRoot> rootProvider()
	{
		return () -> new DataRoot();
	}
}
