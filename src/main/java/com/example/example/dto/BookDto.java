package com.example.example.dto;

import java.time.LocalDate;

import com.example.example.domain.Book;
import com.example.example.domain.blob.BlobBook;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record BookDto(
	@PositiveOrZero long id,
	@NotNull @NotBlank String isbn,
	@NotNull @NotBlank String title,
	@NotNull LocalDate publicationDate,
	@PositiveOrZero int edition,
	@PositiveOrZero int availableQuantity,
	@PositiveOrZero int priceEuroCent,
	@NotNull @Valid AuthorDto author,
	@NotNull @Valid PublisherDto publisher
)
{
	@JsonbCreator
	public BookDto
	{
	}

	public BookDto(final Book book)
	{
		this(
			book.getId(),
			book.getIsbn(),
			book.getTitle(),
			book.getPublicationDate(),
			book.getEdition(),
			book.getAvailableQuantity(),
			book.getPriceEuroCent(),
			new AuthorDto(book.getAuthor()),
			new PublisherDto(book.getPublisher())
		);
	}

	public BookDto(final BlobBook book)
	{
		this(
			book.getId(),
			book.getIsbn(),
			book.getTitle(),
			book.getPublicationDate(),
			book.getEdition(),
			book.getAvailableQuantity(),
			book.getPriceEuroCent(),
			new AuthorDto(book.getAuthor()),
			new PublisherDto(book.getPublisher())
		);
	}
}
