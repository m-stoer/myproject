package com.example.myclusterapp.controller;

import java.util.List;

import org.eclipse.store.storage.types.StorageManager;

import com.example.myclusterapp.domain.Author;
import com.example.myclusterapp.dto.PutAuthorDto;
import com.example.myclusterapp.storage.Authors;
import com.example.myclusterapp.storage.DataRoot;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Put;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import one.microstream.enterprise.cluster.nodelibrary.common.ClusterStorageManager;

@Controller("/author")
public class AuthorController
{
	private final Authors authors;
	private final StorageManager storage;

	public AuthorController(final ClusterStorageManager<DataRoot> storage)
	{
		this.storage = storage;
		this.authors = storage.root().get().authors();
	}

	@Get("/{uid}")
	public Author getAuthor(@PathVariable @NonNull @NotBlank final String uid)
	{
		return this.authors.find(uid);
	}

	@Get
	public List<Author> getAllAuthors()
	{
		return this.authors.findAll();
	}

	@Put
	public Author putAuthor(@Body @NonNull @NotNull @Valid final PutAuthorDto dto)
	{
		final Author author;

		if (dto.uid() == null || this.authors.remove(dto.uid()) == null)
		{
			author = new Author(dto.firstname(), dto.lastname());
		}
		else
		{
			author = new Author(dto.uid(), dto.firstname(), dto.lastname());
		}

		this.authors.put(author);
		this.authors.store(this.storage);
		return author;
	}

	@Delete("/{uid}")
	public Author deleteAuthor(@PathVariable @NonNull @NotBlank final String uid)
	{
		final var author = this.authors.remove(uid);
		if (author != null)
		{
			this.authors.store(this.storage);
		}
		return author;
	}
}
