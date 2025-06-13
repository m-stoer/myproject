package com.example.myclusterapp.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.store.storage.types.StorageManager;

import com.example.myclusterapp.domain.Author;

public class Authors
{
	final Map<String, Author> values = new HashMap<>();

	public Author find(final String uid)
	{
		return this.values.get(uid);
	}

	public Author put(final Author author)
	{
		return this.values.put(author.uid(), author);
	}

	public Author remove(final String uid)
	{
		return this.values.remove(uid);
	}

	public List<Author> findAll()
	{
		return this.values.values().stream().toList();
	}

	public long store(final StorageManager storage)
	{
		return storage.store(this.values);
	}
}
