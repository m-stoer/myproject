package one.microstream.bsr.controller;

import java.util.List;
import java.util.Optional;

import io.micrometer.observation.annotation.Observed;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.exceptions.HttpStatusException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import one.microstream.bsr.domain.blob.BlobBook;
import one.microstream.bsr.dto.BookDto;
import one.microstream.bsr.exception.IndexAlreadyExistsException;
import one.microstream.bsr.repository.BlobBookRepository;

@Observed
@Controller("/blobbook")
public class BlobBookController
{
	private final BlobBookRepository repo;

	public BlobBookController(final BlobBookRepository repo)
	{
		this.repo = repo;
	}

	@Error(exception = IndexAlreadyExistsException.class, status = HttpStatus.BAD_REQUEST)
	public String handleIndexAlreadyExistsException(final IndexAlreadyExistsException e)
	{
		return e.getMessage();
	}

	@Get("/count")
	public long getCount()
	{
		return this.repo.countBooks();
	}

	@Get("/search")
	public List<BookDto> searchBook(
		@QueryValue("title") @NonNull @NotBlank final String title,
		@QueryValue("page") @Nullable @PositiveOrZero final Integer page,
		@QueryValue("pageSize") @Nullable @PositiveOrZero final Integer pageSize
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

	@Get("/{isbn}")
	public BookDto getBookByIsbn(@NonNull @NotBlank @PathVariable final String isbn)
	{
		return Optional.ofNullable(this.repo.getBookByISBN(isbn)).map(BookDto::new).orElse(null);
	}

	@Get("/id/{id}")
	public BookDto getBookById(@NonNull @PathVariable @Positive final Long id)
	{
		return Optional.ofNullable(this.repo.getBookById(id)).map(BookDto::new).orElse(null);
	}

	@Put
	public void putBook(@Body @Valid @NonNull final BookDto dto)
	{
		this.repo.insert(new BlobBook(dto));
	}

	@Put("/batch")
	public void putBookBatch(@NotEmpty @Body @NonNull final List<@Valid @NonNull BookDto> dto)
	{
		try
		{
			this.repo.insertAll(dto.stream().map(BlobBook::new).toList());
		}
		catch (final IllegalArgumentException e)
		{
			throw new HttpStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@Post("/clear")
	public void clearBooks()
	{
		this.repo.clearBooks();
	}
}
