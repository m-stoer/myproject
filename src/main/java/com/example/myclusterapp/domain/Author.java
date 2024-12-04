package com.example.myclusterapp.domain;

import java.util.UUID;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Introspected
@Serdeable
public record Author(String uid, String firstname, String lastname)
{
	public Author(String firstname, String lastname)
	{
		this(UUID.randomUUID().toString(), firstname, lastname);
	}

	/**
	 * Copies all properties but generates a new uid
	 */
	public Author(Author other)
	{
		this(other.firstname, other.lastname);
	}
}
