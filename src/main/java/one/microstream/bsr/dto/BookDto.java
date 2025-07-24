package one.microstream.bsr.dto;

import java.time.LocalDate;

import org.springframework.lang.NonNull;

import one.microstream.bsr.domain.Book;
import one.microstream.bsr.domain.blob.BlobBook;

public record BookDto(
	long id,
	@NonNull String isbn,
	@NonNull String title,
	@NonNull LocalDate publicationDate,
	int edition,
	int availableQuantity,
	int priceEuroCent,
	@NonNull AuthorDto author,
	@NonNull PublisherDto publisher
)
{
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
