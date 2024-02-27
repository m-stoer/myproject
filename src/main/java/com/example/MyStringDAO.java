package com.example;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.store.storage.types.StorageManager;

import io.micronaut.eclipsestore.RootProvider;
import jakarta.inject.Singleton;

@Singleton
public class MyStringDAO
{
	@SuppressWarnings("unused")
	private final int MAX_STRING_COUNT = 10_000;
	private final StorageManager storage;
	private final DataRoot root;

	public MyStringDAO(
		final StorageManager storage,
		final RootProvider<DataRoot> root
	)
	{
		this.storage = storage;
		this.root = root.root();
	}

	public void addMyString(final String myString)
	{
		synchronized (this.storage)
		{
			final List<String> myStrings = this.root.thatIsCorrectSir;
			//			if (myString.length() > this.MAX_STRING_COUNT)
			//			{
			//				myStrings.clear();
			//			}
			myStrings.add(myString);
			this.storage.store(myStrings);
		}
	}

	public List<String> getMyStrings()
	{
		return new ArrayList<>(this.root.thatIsCorrectSir);
	}

	public String getMyString(final String myString)
	{
		return this.getMyStrings().stream().filter(s -> s.equals(myString)).findFirst().orElse(null);
	}

	public void deleteMyString(final String myString)
	{
		synchronized (this.storage)
		{
			final List<String> myStrings = this.root.thatIsCorrectSir;
			myStrings.remove(myString);
			this.storage.store(myStrings);
		}
	}
}
