package one.microstream.bsr.domain.blob;

import java.time.LocalDate;
import java.util.Objects;

import one.microstream.bsr.dto.BookDto;

public class BlobBook
{
	private long id;
	private String isbn;
	private String title;
	private LocalDate publicationDate;
	private int edition;

	private int availableQuantity;
	private int priceEuroCent;

	private BlobAuthor author;
	private BlobPublisher publisher;

	public BlobBook(
		final long id,
		final String isbn,
		final String title,
		final LocalDate publicationDate,
		final int edition,
		final int availableQuantity,
		final int priceEuroCent,
		final BlobAuthor author,
		final BlobPublisher publisher
	)
	{
		this.id = id;
		this.isbn = isbn;
		this.title = title;
		this.publicationDate = publicationDate;
		this.edition = edition;
		this.availableQuantity = availableQuantity;
		this.priceEuroCent = priceEuroCent;
		this.author = author;
		this.publisher = publisher;
	}

	public BlobBook(final BookDto dto)
	{
		this(
			dto.id(),
			dto.isbn(),
			dto.title(),
			dto.publicationDate(),
			dto.edition(),
			dto.availableQuantity(),
			dto.priceEuroCent(),
			new BlobAuthor(dto.author()),
			new BlobPublisher(dto.publisher())
		);

	}

	public String getIsbn()
	{
		return this.isbn;
	}

	public void setIsbn(final String isbn)
	{
		this.isbn = isbn;
	}

	public String getTitle()
	{
		return this.title;
	}

	public void setTitle(final String title)
	{
		this.title = title;
	}

	public LocalDate getPublicationDate()
	{
		return this.publicationDate;
	}

	public void setPublicationDate(final LocalDate publicationDate)
	{
		this.publicationDate = publicationDate;
	}

	public int getEdition()
	{
		return this.edition;
	}

	public void setEdition(final int edition)
	{
		this.edition = edition;
	}

	public int getAvailableQuantity()
	{
		return this.availableQuantity;
	}

	public void setAvailableQuantity(final int availableQuantity)
	{
		this.availableQuantity = availableQuantity;
	}

	public int getPriceEuroCent()
	{
		return this.priceEuroCent;
	}

	public void setPriceEuroCent(final int priceEuroCent)
	{
		this.priceEuroCent = priceEuroCent;
	}

	public BlobAuthor getAuthor()
	{
		return this.author;
	}

	public void setAuthor(final BlobAuthor author)
	{
		this.author = author;
	}

	public BlobPublisher getPublisher()
	{
		return this.publisher;
	}

	public void setPublisher(final BlobPublisher publisher)
	{
		this.publisher = publisher;
	}

	public long getId()
	{
		return this.id;
	}

	public void setId(final long id)
	{
		this.id = id;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(
			this.author,
			this.availableQuantity,
			this.edition,
			this.id,
			this.isbn,
			this.priceEuroCent,
			this.publicationDate,
			this.publisher,
			this.title
		);
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (this.getClass() != obj.getClass())
		{
			return false;
		}
		final BlobBook other = (BlobBook)obj;
		return Objects.equals(this.author, other.author) && this.availableQuantity == other.availableQuantity
			&& this.edition == other.edition
			&& this.id == other.id
			&& Objects.equals(this.isbn, other.isbn)
			&& this.priceEuroCent == other.priceEuroCent
			&& Objects.equals(this.publicationDate, other.publicationDate)
			&& Objects.equals(this.publisher, other.publisher)
			&& Objects.equals(this.title, other.title);
	}
}
