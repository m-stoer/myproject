package com.example.example.controller;

import java.util.List;
import java.util.Optional;

import com.example.example.domain.Author;
import com.example.example.dto.AuthorDto;
import com.example.example.repository.AuthorRepository;

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

@Path("/author")
public class AuthorController
{
	private final AuthorRepository authors;

	@Inject
	public AuthorController(final AuthorRepository authors)
	{
		this.authors = authors;
	}

	@GET
	@Path("/count")
	public long getCount()
	{
		return this.authors.countAuthors();
	}

	@GET
	@Path("/search")
	public List<AuthorDto> searchAuthors(
		@QueryParam("name") @NotNull @NotBlank final String name,
		@QueryParam("page") @Nullable @PositiveOrZero final Integer page,
		@QueryParam("pageSize") @Nullable @PositiveOrZero final Integer pageSize
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

	@GET
	@Path("/{email}")
	public AuthorDto getAuthorByEmail(@NotNull @NotBlank @PathParam("email") final String email)
	{
		return Optional.ofNullable(this.authors.getAuthorByEmail(email)).map(AuthorDto::new).orElse(null);
	}

	@GET
	@Path("/id/{id}")
	public AuthorDto getAuthorById(@NotNull @PathParam("id") @Positive final Long id)
	{
		return Optional.ofNullable(this.authors.getAuthorById(id)).map(AuthorDto::new).orElse(null);
	}

	@PUT
	public void putAuthor(@Valid @NotNull final AuthorDto dto)
	{
		this.authors.insert(new Author(dto));
	}

	@PUT
	@Path("/batch")
	public void putAuthorBatch(@NotEmpty @NotNull final List<@Valid @NotNull AuthorDto> dto)
	{
		try
		{
			this.authors.insertAll(dto.stream().map(Author::new).toList());
		}
		catch (final IllegalArgumentException e)
		{
			throw new HttpException(e.getMessage(), Status.BAD_REQUEST_400);
		}
	}

	@POST
	@Path("/clear")
	public void clearAuthors()
	{
		this.authors.clearAuthors();
	}
}
