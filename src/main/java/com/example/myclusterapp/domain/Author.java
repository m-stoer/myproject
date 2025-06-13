package com.example.myclusterapp.domain;

import java.util.UUID;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Introspected
@Serdeable
public record Author(
	@NonNull @NotBlank String uid,
	@NonNull @NotBlank String firstname,
	@NonNull @NotBlank String lastname
)
{
	public Author(@NonNull @NotBlank final String firstname, @NonNull @NotBlank final String lastname)
	{
		this(UUID.randomUUID().toString(), firstname, lastname);
	}

	/**
	 * Copies all properties but generates a new uid
	 */
	public Author(@NonNull @Valid final Author other)
	{
		this(other.firstname, other.lastname);
	}
}
