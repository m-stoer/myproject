package com.example.myclusterapp.controller;

import java.util.List;

import org.eclipse.store.storage.types.StorageManager;

import com.example.myclusterapp.domain.Book;
import com.example.myclusterapp.dto.PutBookDto;
import com.example.myclusterapp.storage.Books;
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

@Controller("/book")
public class BookController
{
	private final Books books;
	private final StorageManager storage;

	public BookController(final ClusterStorageManager<DataRoot> storage)
	{
		this.storage = storage;
		this.books = storage.root().get().books();
	}

	@Get("/{isbn}")
	public Book getBook(@PathVariable @NonNull @NotBlank final String isbn)
	{
		return this.books.find(isbn);
	}

	@Get
	public List<Book> getAllBooks()
	{
		return this.books.findAll();
	}

	@Put
	public Book putBook(@Body @Valid @NonNull @NotNull final PutBookDto dto)
	{
		final Book book;

		if (dto.isbn() == null || this.books.remove(dto.isbn()) == null)
		{
			book = new Book(dto.title(), dto.publicationDate(), dto.author());
		}
		else
		{
			book = new Book(dto.isbn(), dto.title(), dto.publicationDate(), dto.author());
		}

		this.books.put(book);
		this.books.store(this.storage);
		return book;
	}

	@Delete("/{isbn}")
	public Book deleteBook(@PathVariable @NonNull @NotBlank final String isbn)
	{
		final var book = this.books.remove(isbn);
		if (book != null)
		{
			this.books.store(this.storage);
		}
		return book;
	}
}
