package com.example.example.dto;

import java.util.List;

import com.example.example.domain.Publisher;
import com.example.example.domain.blob.BlobPublisher;

import jakarta.annotation.Nullable;
import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record PublisherDto(
	@JsonbNillable @Nullable @PositiveOrZero Long id,
	@NotNull @NotBlank String email,
	@NotNull @NotBlank String company,
	@NotNull @NotEmpty List<AddressDto> addresses
)
{
	@JsonbCreator
	public PublisherDto
	{
	}

	public PublisherDto(@NotNull final Publisher publisher)
	{
		this(
			publisher.getId(),
			publisher.getEmail(),
			publisher.getCompany(),
			publisher.getAddresses().stream().map(AddressDto::new).toList()
		);
	}

	public PublisherDto(@NotNull final BlobPublisher publisher)
	{
		this(
			null,
			publisher.getEmail(),
			publisher.getCompany(),
			publisher.getAddresses().stream().map(AddressDto::new).toList()
		);
	}
}
