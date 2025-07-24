package com.example.example.domain.indices;

import java.time.LocalDate;

import com.example.example.domain.blob.BlobBook;

import one.microstream.gigamap.IndexerLocalDate;
import one.microstream.gigamap.IndexerLong;
import one.microstream.gigamap.IndexerString;

public final class BlobBookIndices
{
	public static final IndexerLong<BlobBook> ID_INDEX = new IndexerLong.Abstract<BlobBook>()
	{
		@Override
		public String name()
		{
			return "id";
		}

		@Override
		public Long getLong(final BlobBook entity)
		{
			return entity.getId();
		}
	};

	public static final IndexerString<BlobBook> ISBN_INDEX = new IndexerString.Abstract<BlobBook>()
	{
		@Override
		public String name()
		{
			return "isbn";
		}

		@Override
		public String getString(final BlobBook entity)
		{
			return entity.getIsbn();
		}
	};

	public static final IndexerString<BlobBook> TITLE_INDEX = new IndexerString.Abstract<BlobBook>()
	{
		@Override
		public String name()
		{
			return "title";
		}

		@Override
		public String getString(final BlobBook entity)
		{
			return entity.getTitle();
		}
	};

	public static final IndexerLocalDate<BlobBook> PUBLICATION_DATE_INDEX = new IndexerLocalDate.Abstract<BlobBook>()
	{
		@Override
		public String name()
		{
			return "publicationDate";
		}

		@Override
		protected LocalDate getLocalDate(final BlobBook entity)
		{
			return entity.getPublicationDate();
		}
	};

	public static final IndexerString<BlobBook> AUTHOR_EMAIL_INDEX = new IndexerString.Abstract<BlobBook>()
	{
		@Override
		public String name()
		{
			return "authorEmail";
		}

		@Override
		public String getString(final BlobBook entity)
		{
			return entity.getAuthor().getEmail();
		}
	};

	public static final IndexerString<BlobBook> AUTHOR_FIRSTNAME_INDEX = new IndexerString.Abstract<BlobBook>()
	{
		@Override
		public String name()
		{
			return "authorFirstname";
		}

		@Override
		public String getString(final BlobBook entity)
		{
			return entity.getAuthor().getFirstname();
		}
	};

	public static final IndexerString<BlobBook> AUTHOR_LASTNAME_INDEX = new IndexerString.Abstract<BlobBook>()
	{
		@Override
		public String name()
		{
			return "authorLastname";
		}

		@Override
		public String getString(final BlobBook entity)
		{
			return entity.getAuthor().getLastname();
		}
	};

	private BlobBookIndices()
	{
	}
}
