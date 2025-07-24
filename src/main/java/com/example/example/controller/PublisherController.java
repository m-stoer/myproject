package com.example.example.controller;

import java.util.List;
import java.util.Optional;

import com.example.example.domain.Publisher;
import com.example.example.dto.PublisherDto;
import com.example.example.repository.PublisherRepository;

import io.helidon.http.HttpException;
import io.helidon.http.Status;
import jakarta.annotation.Nullable;
import jakarta.enterprise.context.ApplicationScoped;
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

@ApplicationScoped
@Path("/publisher")
public class PublisherController
{
	private final PublisherRepository publishers;

	public PublisherController(final PublisherRepository publishers)
	{
		this.publishers = publishers;
	}

	@GET
	@Path("/count")
	public long getCount()
	{
		return this.publishers.countPublishers();
	}

	@GET
	@Path("/search")
	public List<PublisherDto> searchPublishers(
		@QueryParam("company") @NotNull @NotBlank final String company,
		@QueryParam("page") @Nullable @PositiveOrZero final Integer page,
		@QueryParam("pageSize") @Nullable @PositiveOrZero final Integer pageSize
	)
	{
		final List<Publisher> searchedPublishers;
		if (pageSize == null && page == null)
		{
			searchedPublishers = this.publishers.searchPublishersByCompany(company);
		}
		else if (pageSize == null)
		{
			searchedPublishers = this.publishers.searchPublishersByCompany(company, page);
		}
		else
		{
			searchedPublishers = this.publishers.searchPublishersByCompany(company, page, pageSize);
		}
		return searchedPublishers.stream().map(PublisherDto::new).toList();
	}

	@GET
	@Path("/{email}")
	public PublisherDto getPublisherByEmail(@NotNull @NotBlank @PathParam("email") final String email)
	{
		return Optional.ofNullable(this.publishers.getPublisherByEmail(email)).map(PublisherDto::new).orElse(null);
	}

	@GET
	@Path("/id/{id}")
	public PublisherDto getPublisherById(@NotNull @PathParam("id") @Positive final Long id)
	{
		return Optional.ofNullable(this.publishers.getPublisherById(id)).map(PublisherDto::new).orElse(null);
	}

	@PUT
	public void putPublisher(@Valid @NotNull final PublisherDto dto)
	{
		this.publishers.insert(new Publisher(dto));
	}

	@PUT
	@Path("/batch")
	public void putPublisherBatch(@NotEmpty @NotNull final List<@Valid @NotNull PublisherDto> dto)
	{
		try
		{
			this.publishers.insertAll(dto.stream().map(Publisher::new).toList());
		}
		catch (final IllegalArgumentException e)
		{
			throw new HttpException(e.getMessage(), Status.BAD_REQUEST_400);
		}
	}

	@POST
	@Path("/clear")
	public void clearPublishers()
	{
		this.publishers.clearPublishers();
	}
}
