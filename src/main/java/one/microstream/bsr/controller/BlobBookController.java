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

import one.microstream.bsr.domain.blob.BlobBook;
import one.microstream.bsr.dto.BookDto;
import one.microstream.bsr.repository.BlobBookRepository;

@RestController
@RequestMapping("/blobbook")
public class BlobBookController
{
	private final BlobBookRepository repo;

	public BlobBookController(final BlobBookRepository repo)
	{
		this.repo = repo;
	}

	@GetMapping("/count")
	public long getCount()
	{
		return this.repo.countBooks();
	}

	@GetMapping("/search")
	public List<BookDto> searchBook(
		@RequestParam("title") @NonNull final String title,
		@RequestParam("page") @Nullable final Integer page,
		@RequestParam("pageSize") @Nullable final Integer pageSize
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

	@GetMapping("/{isbn}")
	public BookDto getBookByIsbn(@NonNull @PathVariable final String isbn)
	{
		return Optional.ofNullable(this.repo.getBookByISBN(isbn)).map(BookDto::new).orElse(null);
	}

	@GetMapping("/id/{id}")
	public BookDto getBookById(@NonNull @PathVariable final Long id)
	{
		return Optional.ofNullable(this.repo.getBookById(id)).map(BookDto::new).orElse(null);
	}

	@PutMapping
	public void putBook(@RequestBody @NonNull final BookDto dto)
	{
		this.repo.insert(new BlobBook(dto));
	}

	@PutMapping("/batch")
	public void putBookBatch(@RequestBody @NonNull final List<BookDto> dto)
	{
		try
		{
			this.repo.insertAll(dto.stream().map(BlobBook::new).toList());
		}
		catch (final IllegalArgumentException e)
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@PostMapping("/clear")
	public void clearBooks()
	{
		this.repo.clearBooks();
	}
}
