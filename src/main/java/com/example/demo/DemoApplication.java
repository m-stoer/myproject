package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import one.microstream.enterprise.cluster.nodelibrary.springboot.MicroStreamCluster;

@SpringBootApplication
@Import(MicroStreamCluster.class)
public class DemoApplication
{
	public static void main(final String[] args)
	{
		SpringApplication.run(DemoApplication.class, args);
	}
}
