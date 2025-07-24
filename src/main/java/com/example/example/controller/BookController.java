package com.example.example.controller;

import java.util.List;
import java.util.Optional;

import com.example.example.domain.Book;
import com.example.example.dto.BookDto;
import com.example.example.dto.BookReferenceDto;
import com.example.example.repository.AuthorRepository;
import com.example.example.repository.BookRepository;
import com.example.example.repository.PublisherRepository;

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

@Path("/book")
public class BookController
{
	private final BookRepository books;
	private final AuthorRepository authors;
	private final PublisherRepository publishers;

	@Inject
	public BookController(
		final BookRepository books,
		final AuthorRepository authors,
		final PublisherRepository publishers
	)
	{
		this.books = books;
		this.authors = authors;
		this.publishers = publishers;
	}

	@GET
	@Path("/count")
	public long getCount()
	{
		return this.books.countBooks();
	}

	@GET
	@Path("/search")
	public List<BookDto> searchBook(
		@QueryParam("title") @NotNull @NotBlank final String title,
		@QueryParam("page") @Nullable @PositiveOrZero final Integer page,
		@QueryParam("pageSize") @Nullable @PositiveOrZero final Integer pageSize
	)
	{
		final List<Book> searchedBooks;
		if (pageSize == null && page == null)
		{
			searchedBooks = this.books.searchBooksByTitle(title);
		}
		else if (pageSize == null)
		{
			searchedBooks = this.books.searchBooksByTitle(title, page);
		}
		else
		{
			searchedBooks = this.books.searchBooksByTitle(title, page, pageSize);
		}
		return searchedBooks.stream().map(BookDto::new).toList();
	}

	@GET
	@Path("/{isbn}")
	public BookDto getBookByIsbn(@NotNull @NotBlank @PathParam("isbn") final String isbn)
	{
		return Optional.ofNullable(this.books.getBookByISBN(isbn)).map(BookDto::new).orElse(null);
	}

	@GET
	@Path("/id/{id}")
	public BookDto getBookById(@NotNull @PathParam("id") @Positive final Long id)
	{
		return Optional.ofNullable(this.books.getBookById(id)).map(BookDto::new).orElse(null);
	}

	@PUT
	public void putBook( @Valid @NotNull final BookReferenceDto dto)
	{
		this.books.insert(this.convertDtoToBook(dto));
	}

	@PUT
	@Path("/batch")
	public void putBookBatch(@NotEmpty  @NotNull final List<@Valid @NotNull BookReferenceDto> dto)
	{
		try
		{
			this.books.insertAll(dto.stream().map(this::convertDtoToBook).toList());
		}
		catch (final IllegalArgumentException e)
		{
			throw new HttpException(e.getMessage(), Status.BAD_REQUEST_400);
		}
	}

	private Book convertDtoToBook(final BookReferenceDto dto) throws HttpException
	{
		final var book = new Book(dto);
		final var author = this.authors.getAuthorById(dto.author());
		if (author == null)
		{
			throw new HttpException("Could not find author with id " + dto.author(), Status.NOT_FOUND_404);
		}
		final var publisher = this.publishers.getPublisherById(dto.publisher());
		if (publisher == null)
		{
			throw new HttpException("Could not find publisher with id " + dto.publisher(), Status.NOT_FOUND_404);
		}
		book.setAuthor(author);
		book.setPublisher(publisher);
		return book;
	}

	@POST
	@Path("/clear")
	public void clearBooks()
	{
		this.books.clearBooks();
	}
}
