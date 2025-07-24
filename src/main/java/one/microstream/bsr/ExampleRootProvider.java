package one.microstream.bsr;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import one.microstream.enterprise.cluster.nodelibrary.springboot.RootProvider;

@Configuration
public class ExampleRootProvider
{
	@Bean
	public RootProvider<DataRoot> rootProvider()
	{
		return DataRoot::new;
	}
}
