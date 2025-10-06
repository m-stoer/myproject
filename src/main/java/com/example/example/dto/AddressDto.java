package com.example.example.dto;

import com.example.example.domain.Address;

import jakarta.annotation.Nullable;
import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddressDto(
	@NotNull @NotBlank String address,
	@Nullable String address2,
	@Nullable String zip,
	@NotNull @NotBlank String city,
	@NotNull @NotBlank String country
)
{
	@JsonbCreator
	public AddressDto
	{
	}

	public AddressDto(@NotNull final Address address)
	{
		this(address.getAddress(), address.getAddress2(), address.getZip(), address.getCity(), address.getCountry());
	}
}
