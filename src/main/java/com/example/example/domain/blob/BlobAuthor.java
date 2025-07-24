package com.example.example.domain.blob;

import java.util.List;
import java.util.Objects;

import com.example.example.domain.Address;
import com.example.example.dto.AuthorDto;

public class BlobAuthor
{
	private String email;
	private String firstname;
	private String lastname;
	private List<Address> addresses;

	public BlobAuthor(final String email, final String firstname, final String lastname, final List<Address> addresses)
	{
		this.email = email;
		this.firstname = firstname;
		this.lastname = lastname;
		this.addresses = addresses;
	}

	public BlobAuthor(final AuthorDto author)
	{
		this(
			author.email(),
			author.firstname(),
			author.lastname(),
			author.addresses().stream().map(Address::new).toList()
		);
	}

	public String getEmail()
	{
		return this.email;
	}

	public void setEmail(final String email)
	{
		this.email = email;
	}

	public String getFirstname()
	{
		return this.firstname;
	}

	public void setFirstname(final String firstname)
	{
		this.firstname = firstname;
	}

	public String getLastname()
	{
		return this.lastname;
	}

	public void setLastname(final String lastname)
	{
		this.lastname = lastname;
	}

	public List<Address> getAddresses()
	{
		return this.addresses;
	}

	public void setAddresses(final List<Address> addresses)
	{
		this.addresses = addresses;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(this.addresses, this.email, this.firstname, this.lastname);
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (this.getClass() != obj.getClass())
		{
			return false;
		}
		final BlobAuthor other = (BlobAuthor)obj;
		return Objects.equals(this.addresses, other.addresses) && Objects.equals(this.email, other.email)
			&& Objects.equals(this.firstname, other.firstname)
			&& Objects.equals(this.lastname, other.lastname);
	}
}
