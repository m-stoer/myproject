package com.example.myclusterapp.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.store.storage.types.StorageManager;

import com.example.myclusterapp.domain.Book;

public class Books
{
	final Map<String, Book> values = new HashMap<>();

	public Book find(final String isbn)
	{
		return this.values.get(isbn);
	}

	public Book put(final Book book)
	{
		return this.values.put(book.isbn(), book);
	}

	public Book remove(final String isbn)
	{
		return this.values.remove(isbn);
	}

	public List<Book> findAll()
	{
		return this.values.values().stream().toList();
	}

	public long store(final StorageManager storage)
	{
		return storage.store(this.values);
	}
}
