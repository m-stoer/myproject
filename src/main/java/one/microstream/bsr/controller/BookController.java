package one.microstream.bsr.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import one.microstream.bsr.domain.Book;
import one.microstream.bsr.dto.BookDto;
import one.microstream.bsr.dto.BookReferenceDto;
import one.microstream.bsr.repository.AuthorRepository;
import one.microstream.bsr.repository.BookRepository;
import one.microstream.bsr.repository.PublisherRepository;

@RestController
@RequestMapping("/book")
public class BookController
{
	private final BookRepository books;
	private final AuthorRepository authors;
	private final PublisherRepository publishers;

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

	@GetMapping("/count")
	public long getCount()
	{
		return this.books.countBooks();
	}

	@GetMapping("/search")
	public List<BookDto> searchBook(
		@RequestParam("title") @NonNull final String title,
		@RequestParam("page") @Nullable final Integer page,
		@RequestParam("pageSize") @Nullable final Integer pageSize
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

	@GetMapping("/{isbn}")
	public BookDto getBookByIsbn(@NonNull @PathVariable final String isbn)
	{
		return Optional.ofNullable(this.books.getBookByISBN(isbn)).map(BookDto::new).orElse(null);
	}

	@GetMapping("/id/{id}")
	public BookDto getBookById(@NonNull @PathVariable final Long id)
	{
		return Optional.ofNullable(this.books.getBookById(id)).map(BookDto::new).orElse(null);
	}

	@PutMapping
	public void putBook(@RequestBody @NonNull final BookReferenceDto dto)
	{
		this.books.insert(this.convertDtoToBook(dto));
	}

	@PutMapping("/batch")
	public void putBookBatch(@RequestBody @NonNull final List<BookReferenceDto> dto)
	{
		try
		{
			this.books.insertAll(dto.stream().map(this::convertDtoToBook).toList());
		}
		catch (final IllegalArgumentException e)
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	private Book convertDtoToBook(final BookReferenceDto dto) throws ResponseStatusException
	{
		final var book = new Book(dto);
		final var author = this.authors.getAuthorById(dto.author());
		if (author == null)
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find author with id " + dto.author());
		}
		final var publisher = this.publishers.getPublisherById(dto.publisher());
		if (publisher == null)
		{
			throw new ResponseStatusException(
				HttpStatus.NOT_FOUND,
				"Could not find publisher with id " + dto.publisher()
			);
		}
		book.setAuthor(author);
		book.setPublisher(publisher);
		return book;
	}

	@PostMapping("/clear")
	public void clearBooks()
	{
		this.books.clearBooks();
	}
}
