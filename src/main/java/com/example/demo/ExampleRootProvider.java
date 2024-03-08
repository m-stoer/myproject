package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import one.microstream.enterprise.cluster.nodelibrary.springboot.RootProvider;

@Configuration
public class ExampleRootProvider
{
	@Bean
	public RootProvider<DataRoot> rootProvider()
	{
		return () -> new DataRoot();
	}
}
