package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.eclipse.store.storage.types.StorageManager;
import org.slf4j.LoggerFactory;

import io.micronaut.eclipsestore.RootProvider;
import jakarta.inject.Singleton;

@Singleton
public class MyDao
{
	private final StorageManager storage;
	private final DataRoot root;

	public MyDao(final StorageManager storage, final RootProvider<DataRoot> root)
	{
		this.storage = storage;
		this.root = root.root();
	}

	public void addMyString(final String string)
	{
		synchronized (this.storage)
		{
			final List<String> strings = this.root.strings;
			strings.add(string);
			this.storage.store(strings);
		}
	}

	public List<String> getMyStrings()
	{
		return new ArrayList<>(this.root.strings);
	}

	public String getMyString(final String myString)
	{
		return this.getMyStrings().stream().filter(myString::equals).findFirst().orElse(null);
	}

	public void deleteMyString(final String myString)
	{
		synchronized (this.storage)
		{
			final List<String> myStrings = this.root.strings;
			myStrings.remove(myString);
			this.storage.store(myStrings);
		}
	}

	public void testOmegaStore()
	{
		final var log = LoggerFactory.getLogger("OmegaTest");
		log.info("Generating data");
		root.omega.clear();
		final var random = new Random();
		for (int i = 0; i < 1_000_000; i++)
		{
			final var sb = new StringBuilder();
			for (int j = 0; j < 8; j++)
			{
				sb.append((char)random.nextInt('a', 'z' + 1));
			}
			root.omega.add(sb.toString());
		}
		log.info("Storing data");
		storage.store(root.omega);
		log.info("Done");
	}
}
