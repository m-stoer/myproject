package one.microstream.bsr.domain.blob;

import java.util.List;
import java.util.Objects;

import one.microstream.bsr.domain.Address;
import one.microstream.bsr.dto.PublisherDto;

public class BlobPublisher
{
	private String email;
	private String company;
	private List<Address> addresses;

	public BlobPublisher(final String email, final String company, final List<Address> addresses)
	{
		this.email = email;
		this.company = company;
		this.addresses = addresses;
	}

	public BlobPublisher(final PublisherDto publisher)
	{
		this(publisher.email(), publisher.company(), publisher.addresses().stream().map(Address::new).toList());
	}

	public String getEmail()
	{
		return this.email;
	}

	public void setEmail(final String email)
	{
		this.email = email;
	}

	public String getCompany()
	{
		return this.company;
	}

	public void setCompany(final String company)
	{
		this.company = company;
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
		return Objects.hash(this.addresses, this.company, this.email);
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
		final BlobPublisher other = (BlobPublisher)obj;
		return Objects.equals(this.addresses, other.addresses) && Objects.equals(this.company, other.company)
			&& Objects.equals(this.email, other.email);
	}
}
