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

import one.microstream.bsr.domain.Publisher;
import one.microstream.bsr.dto.PublisherDto;
import one.microstream.bsr.repository.PublisherRepository;

@RestController
@RequestMapping("/publisher")
public class PublisherController
{
	private final PublisherRepository publishers;

	public PublisherController(final PublisherRepository publishers)
	{
		this.publishers = publishers;
	}

	@GetMapping("/count")
	public long getCount()
	{
		return this.publishers.countPublishers();
	}

	@GetMapping("/search")
	public List<PublisherDto> searchPublishers(
		@RequestParam("company") @NonNull final String company,
		@RequestParam("page") @Nullable final Integer page,
		@RequestParam("pageSize") @Nullable final Integer pageSize
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

	@GetMapping("/{email}")
	public PublisherDto getPublisherByEmail(@NonNull @PathVariable final String email)
	{
		return Optional.ofNullable(this.publishers.getPublisherByEmail(email)).map(PublisherDto::new).orElse(null);
	}

	@GetMapping("/id/{id}")
	public PublisherDto getPublisherById(@NonNull @PathVariable final Long id)
	{
		return Optional.ofNullable(this.publishers.getPublisherById(id)).map(PublisherDto::new).orElse(null);
	}

	@PutMapping
	public void putPublisher(@RequestBody @NonNull final PublisherDto dto)
	{
		this.publishers.insert(new Publisher(dto));
	}

	@PutMapping("/batch")
	public void putPublisherBatch(@RequestBody @NonNull final List<PublisherDto> dto)
	{
		try
		{
			this.publishers.insertAll(dto.stream().map(Publisher::new).toList());
		}
		catch (final IllegalArgumentException e)
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@PostMapping("/clear")
	public void clearPublishers()
	{
		this.publishers.clearPublishers();
	}
}
