package com.example.example.dto;

import java.util.List;

import com.example.example.domain.Author;
import com.example.example.domain.blob.BlobAuthor;

import jakarta.annotation.Nullable;
import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record AuthorDto(
	@JsonbNillable @Nullable @PositiveOrZero Long id,
	@NotNull @NotBlank String email,
	@NotNull @NotBlank String firstname,
	@NotNull @NotBlank String lastname,
	@NotNull @NotEmpty List<AddressDto> addresses
)
{
	@JsonbCreator
	public AuthorDto
	{
	}

	public AuthorDto(@NotNull final Author author)
	{
		this(
			author.getId(),
			author.getEmail(),
			author.getFirstname(),
			author.getLastname(),
			author.getAddresses().stream().map(AddressDto::new).toList()
		);
	}

	public AuthorDto(@NotNull final BlobAuthor author)
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
