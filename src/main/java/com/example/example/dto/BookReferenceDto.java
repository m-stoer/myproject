package com.example.example.dto;

import java.time.LocalDate;

import com.example.example.domain.Book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record BookReferenceDto(
	@PositiveOrZero long id,
	@NotNull @NotBlank String isbn,
	@NotNull @NotBlank String title,
	@NotNull LocalDate publicationDate,
	@PositiveOrZero int edition,
	@PositiveOrZero int availableQuantity,
	@PositiveOrZero int priceEuroCent,
	@Positive long author,
	@Positive long publisher
)
{
	public BookReferenceDto(final Book book)
	{
		this(
			book.getId(),
			book.getIsbn(),
			book.getTitle(),
			book.getPublicationDate(),
			book.getEdition(),
			book.getAvailableQuantity(),
			book.getPriceEuroCent(),
			book.getAuthor().getId(),
			book.getPublisher().getId()
		);
	}
}
