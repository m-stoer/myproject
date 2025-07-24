package com.example.example.repository;

import java.util.List;

import org.eclipse.serializer.concurrency.LockedExecutor;

import com.example.example.DataRoot;
import com.example.example.domain.blob.BlobBook;
import com.example.example.domain.indices.BlobBookIndices;
import com.example.example.exception.IndexAlreadyExistsException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import one.microstream.enterprise.cluster.nodelibrary.common.ClusterLockScope;
import one.microstream.enterprise.cluster.nodelibrary.common.ClusterStorageManager;
import one.microstream.gigamap.GigaMap;

/**
 * The difference between this repository and the normal book repository is that
 * any references to authors and publishers here are not shared with each other.
 * Meaning that each book has it's own author and publisher object attached even
 * if it is identical to already existing author objects. The normal book
 * repository queries the author and publisher repositories so that equal author
 * objects are also the same object im memory.
 * 
 * Also another difference for testing purposes is that the books are stored in
 * a giga map.
 */
@ApplicationScoped
public class BlobBookRepository extends ClusterLockScope
{
	private static final int PAGE_SIZE_LIMIT = 250;

	private final GigaMap<BlobBook> books;

	@SuppressWarnings({
		"unchecked", "rawtypes"
	})
	@Inject
	public BlobBookRepository(final ClusterStorageManager storageManager, final LockedExecutor executor)
	{
		super(executor);
		this.books = ((ClusterStorageManager<DataRoot>)storageManager).root().get().getBlobBooks();
	}

	public BlobBook getBookByISBN(final String isbn)
	{
		return this.read(() -> this.books.query(BlobBookIndices.ISBN_INDEX.is(isbn)).findFirst().orElse(null));
	}

	public BlobBook getBookById(final long id)
	{
		return this.read(() -> this.books.query(BlobBookIndices.ID_INDEX.is(id)).findFirst().orElse(null));
	}

	public List<BlobBook> searchBooksByTitle(final String title)
	{
		return this.searchBooksByTitle(title, 1, PAGE_SIZE_LIMIT);
	}

	public List<BlobBook> searchBooksByTitle(final String title, final int page)
	{
		return this.searchBooksByTitle(title, page, PAGE_SIZE_LIMIT);
	}

	public List<BlobBook> searchBooksByTitle(final String title, final int page, final int pageSize)
	{
		final int offset = (page - 1) * pageSize;
		final int limit = Math.max(pageSize, PAGE_SIZE_LIMIT);
		return this.read(
			() -> this.books.query(BlobBookIndices.TITLE_INDEX.containsIgnoreCase(title)).toList(offset, limit)
		);
	}

	public void insert(final BlobBook book) throws IndexAlreadyExistsException
	{
		this.write(() ->
		{
			this.ensureUniqueIndex(book);
			book.setId(this.books.size() + 1);
			this.books.add(book);
			this.books.store();
		});
	}

	public void insertAll(final List<BlobBook> moreBooks) throws IndexAlreadyExistsException
	{
		this.write(() ->
		{
			for (final BlobBook book : moreBooks)
			{
				this.ensureUniqueIndex(book);

				if (moreBooks.stream().filter(b -> b.getIsbn().equals(book.getIsbn())).count() > 1)
				{
					throw new IndexAlreadyExistsException("Books with duplicate isbn / id found in batch save.");
				}
			}

			final long nextId = this.books.size() + 1;
			for (int i = 0; i < moreBooks.size(); i++)
			{
				moreBooks.get(i).setId(nextId + i);
			}

			this.books.addAll(moreBooks);
			this.books.store();
		});

	}

	public long countBooks()
	{
		return this.read(this.books::size);
	}

	public void clearBooks()
	{
		this.write(() ->
		{
			this.books.clear();
			this.books.store();
		});
	}

	private void ensureUniqueIndex(final BlobBook book) throws IndexAlreadyExistsException
	{
		if (this.books.query(BlobBookIndices.ISBN_INDEX.is(book.getIsbn())).findFirst().isPresent())
		{
			throw new IndexAlreadyExistsException("BlobBook with isbn %s already exists.".formatted(book.getIsbn()));
		}
	}
}
