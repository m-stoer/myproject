package com.example.example.controller;

import java.util.List;
import java.util.Optional;

import com.example.example.domain.blob.BlobBook;
import com.example.example.dto.BookDto;
import com.example.example.repository.BlobBookRepository;

import io.helidon.http.HttpException;
import io.helidon.http.Status;
import jakarta.annotation.Nullable;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;

@Path("/blobbook")
public class BlobBookController
{
	private final BlobBookRepository repo;

	@Inject
	public BlobBookController(final BlobBookRepository repo)
	{
		this.repo = repo;
	}

	@GET
	@Path("/count")
	public long getCount()
	{
		return this.repo.countBooks();
	}

	@GET
	@Path("/search")
	public List<BookDto> searchBook(
		@QueryParam("title") @NotNull @NotBlank final String title,
		@QueryParam("page") @Nullable @PositiveOrZero final Integer page,
		@QueryParam("pageSize") @Nullable @PositiveOrZero final Integer pageSize
	)
	{
		final List<BlobBook> books;
		if (pageSize == null && page == null)
		{
			books = this.repo.searchBooksByTitle(title);
		}
		else if (pageSize == null)
		{
			books = this.repo.searchBooksByTitle(title, page);
		}
		else
		{
			books = this.repo.searchBooksByTitle(title, page, pageSize);
		}
		return books.stream().map(BookDto::new).toList();
	}

	@GET
	@Path("/{isbn}")
	public BookDto getBookByIsbn(@NotNull @NotBlank @PathParam("isbn") final String isbn)
	{
		return Optional.ofNullable(this.repo.getBookByISBN(isbn)).map(BookDto::new).orElse(null);
	}

	@GET
	@Path("/id/{id}")
	public BookDto getBookById(@NotNull @PathParam("id") @Positive final Long id)
	{
		return Optional.ofNullable(this.repo.getBookById(id)).map(BookDto::new).orElse(null);
	}

	@PUT
	public void putBook(@Valid @NotNull final BookDto dto)
	{
		this.repo.insert(new BlobBook(dto));
	}

	@PUT
	@Path("/batch")
	public void putBookBatch(@NotEmpty @NotNull final List<@Valid @NotNull BookDto> dto)
	{
		try
		{
			this.repo.insertAll(dto.stream().map(BlobBook::new).toList());
		}
		catch (final IllegalArgumentException e)
		{
			throw new HttpException(e.getMessage(), Status.BAD_REQUEST_400);
		}
	}

	@POST
	@Path("/clear")
	public void clearBooks()
	{
		this.repo.clearBooks();
	}
}
