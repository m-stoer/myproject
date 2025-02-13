package com.example.myclusterapp.controller;

import java.util.List;
import java.util.Optional;

import org.eclipse.store.storage.types.StorageManager;

import com.example.myclusterapp.domain.Author;
import com.example.myclusterapp.storage.DataRoot;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import one.microstream.enterprise.cluster.nodelibrary.common.ClusterStorageManager;

@Controller("/author")
public class AuthorController
{
	private final List<Author> authors;
	private final StorageManager storage;

	public AuthorController(ClusterStorageManager<DataRoot> storage)
	{
		this.storage = storage;
		this.authors = storage.root().get().authors;
	}
	
	@Post("/explode")
	public void explode()
	{
		final var storer = storage.createEagerStorer();
		storer.store(new String("Hello :)"));
		storer.commit();
	}

	@Get("/{uid}")
	public Optional<Author> getAuthor(@PathVariable String uid)
	{
		return authors.stream().filter(a -> a.uid().equals(uid)).findFirst();
	}

	@Get
	public List<Author> getAllAuthors()
	{
		return authors;
	}

	@Put
	public HttpResponse<Object> putAuthor(@Body Author author)
	{
		if (author.uid() == null)
		{
			author = new Author(author);
		}
		else
		{
			final var a = author;
			authors.stream().filter(b -> b.uid().equals(a.uid())).forEach(authors::remove);
		}

		authors.add(author);
		storage.store(authors);
		return HttpResponse.ok(author);
	}

	@Delete("/{uid}")
	public Optional<Author> deleteAuthor(@PathVariable String uid)
	{
		final var findAuthor = authors.stream().filter(a -> a.uid().equals(uid)).findFirst();
		if (findAuthor.isEmpty())
		{
			return Optional.empty();
		}
		final var old = findAuthor.get();
		authors.remove(old);
		return Optional.of(old);
	}
}
