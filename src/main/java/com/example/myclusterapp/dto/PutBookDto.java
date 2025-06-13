package com.example.myclusterapp.dto;

import java.time.LocalDate;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;

@Serdeable
@Introspected
public record PutBookDto(
	@Nullable String isbn,
	@NonNull @NotBlank String title,
	@NonNull LocalDate publicationDate,
	@NonNull @NotBlank String authorUid
)
{
}
