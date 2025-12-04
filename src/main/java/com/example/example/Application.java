package com.example.example;

import org.eclipse.datagrid.cluster.nodelibrary.springboot.EclipseDataGridCluster;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(EclipseDataGridCluster.class)
public class Application
{
	public static void main(final String[] args)
	{
		SpringApplication.run(Application.class, args);
	}
}
