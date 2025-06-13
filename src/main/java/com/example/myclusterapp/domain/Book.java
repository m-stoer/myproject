package com.example.myclusterapp.domain;

import java.time.LocalDate;

import org.apache.kafka.common.Uuid;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Serdeable
@Introspected
public record Book(
	@NonNull @NotBlank String isbn,
	@NonNull @NotBlank String title,
	@NonNull LocalDate publicationDate,
	@Valid @NonNull Author author
)
{
	public Book(
		@NonNull @NotBlank final String title,
		@NonNull final LocalDate publicationDate,
		@Valid @NonNull final Author author
	)
	{
		this(Uuid.randomUuid().toString(), title, publicationDate, author);
	}
}
