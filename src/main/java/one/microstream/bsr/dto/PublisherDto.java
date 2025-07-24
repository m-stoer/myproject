package one.microstream.bsr.dto;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import one.microstream.bsr.domain.Publisher;
import one.microstream.bsr.domain.blob.BlobPublisher;

public record PublisherDto(
	@Nullable Long id,
	@NonNull String email,
	@NonNull String company,
	@NonNull List<AddressDto> addresses
)
{
	public PublisherDto(@NonNull final Publisher publisher)
	{
		this(
			publisher.getId(),
			publisher.getEmail(),
			publisher.getCompany(),
			publisher.getAddresses().stream().map(AddressDto::new).toList()
		);
	}

	public PublisherDto(@NonNull final BlobPublisher publisher)
	{
		this(
			null,
			publisher.getEmail(),
			publisher.getCompany(),
			publisher.getAddresses().stream().map(AddressDto::new).toList()
		);
	}
}
