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

import one.microstream.bsr.domain.Author;
import one.microstream.bsr.dto.AuthorDto;
import one.microstream.bsr.repository.AuthorRepository;

@RestController
@RequestMapping("/author")
public class AuthorController
{
	private final AuthorRepository authors;

	public AuthorController(final AuthorRepository authors)
	{
		this.authors = authors;
	}

	@GetMapping("/count")
	public long getCount()
	{
		return this.authors.countAuthors();
	}

	@GetMapping("/search")
	public List<AuthorDto> searchAuthors(
		@RequestParam("name") @NonNull final String name,
		@RequestParam("page") @Nullable final Integer page,
		@RequestParam("pageSize") @Nullable final Integer pageSize
	)
	{
		final List<Author> searchedAuthors;
		if (pageSize == null && page == null)
		{
			searchedAuthors = this.authors.searchAuthorsByName(name);
		}
		else if (pageSize == null)
		{
			searchedAuthors = this.authors.searchAuthorsByName(name, page);
		}
		else
		{
			searchedAuthors = this.authors.searchAuthorsByName(name, page, pageSize);
		}
		return searchedAuthors.stream().map(AuthorDto::new).toList();
	}

	@GetMapping("/{email}")
	public AuthorDto getAuthorByEmail(@NonNull @PathVariable final String email)
	{
		return Optional.ofNullable(this.authors.getAuthorByEmail(email)).map(AuthorDto::new).orElse(null);
	}

	@GetMapping("/id/{id}")
	public AuthorDto getAuthorById(@NonNull @PathVariable final Long id)
	{
		return Optional.ofNullable(this.authors.getAuthorById(id)).map(AuthorDto::new).orElse(null);
	}

	@PutMapping
	public void putAuthor(@RequestBody @NonNull final AuthorDto dto)
	{
		this.authors.insert(new Author(dto));
	}

	@PutMapping("/batch")
	public void putAuthorBatch(@RequestBody @NonNull final List<AuthorDto> dto)
	{
		try
		{
			this.authors.insertAll(dto.stream().map(Author::new).toList());
		}
		catch (final IllegalArgumentException e)
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@PostMapping("/clear")
	public void clearAuthors()
	{
		this.authors.clearAuthors();
	}
}
