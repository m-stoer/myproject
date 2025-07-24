package one.microstream.bsr.dto;

import java.time.LocalDate;

import org.springframework.lang.NonNull;

import one.microstream.bsr.domain.Book;

public record BookReferenceDto(
	long id,
	@NonNull String isbn,
	@NonNull String title,
	@NonNull LocalDate publicationDate,
	int edition,
	int availableQuantity,
	int priceEuroCent,
	long author,
	long publisher
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
