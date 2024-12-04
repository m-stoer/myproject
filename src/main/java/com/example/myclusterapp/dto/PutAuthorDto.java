package com.example.myclusterapp.dto;

import com.example.myclusterapp.domain.Author;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record PutAuthorDto(String firstname, String lastname)
{
	public Author toAuthor()
	{
		return new Author(firstname, lastname);
	}
}
