package one.microstream.bsr.dto;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import one.microstream.bsr.domain.Address;

public record AddressDto(
	@NonNull String address,
	@Nullable String address2,
	@Nullable String zip,
	@NonNull String city,
	@NonNull String country
)
{
	public AddressDto(@NonNull final Address address)
	{
		this(address.getAddress(), address.getAddress2(), address.getZip(), address.getCity(), address.getCountry());
	}
}
