package one.microstream.bsr.dto;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import one.microstream.bsr.domain.Author;
import one.microstream.bsr.domain.blob.BlobAuthor;

public record AuthorDto(
	@Nullable Long id,
	@NonNull String email,
	@NonNull String firstname,
	@NonNull String lastname,
	@NonNull List<AddressDto> addresses
)
{
	public AuthorDto(@NonNull final Author author)
	{
		this(
			author.getId(),
			author.getEmail(),
			author.getFirstname(),
			author.getLastname(),
			author.getAddresses().stream().map(AddressDto::new).toList()
		);
	}

	public AuthorDto(@NonNull final BlobAuthor author)
	{
		this(
			null,
			author.getEmail(),
			author.getFirstname(),
			author.getLastname(),
			author.getAddresses().stream().map(AddressDto::new).toList()
		);
	}
}
