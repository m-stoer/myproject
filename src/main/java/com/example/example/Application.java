package com.example.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import one.microstream.enterprise.cluster.nodelibrary.springboot.MicroStreamCluster;

@SpringBootApplication
@Import(MicroStreamCluster.class)
public class Application
{
	public static void main(final String[] args)
	{
		SpringApplication.run(Application.class, args);
	}
}
