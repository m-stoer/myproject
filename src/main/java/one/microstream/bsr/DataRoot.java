
package one.microstream.bsr;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.store.gigamap.types.GigaMap;

import one.microstream.bsr.domain.Author;
import one.microstream.bsr.domain.Book;
import one.microstream.bsr.domain.Publisher;
import one.microstream.bsr.domain.blob.BlobBook;
import one.microstream.bsr.domain.indices.BlobBookIndices;

public class DataRoot
{
	private final GigaMap<BlobBook> blobBooks = GigaMap.New();
	private final List<Book> books = new ArrayList<>();
	private final List<Author> authors = new ArrayList<>();
	private final List<Publisher> publishers = new ArrayList<>();

	public DataRoot()
	{
		this.initBooksMap(this.blobBooks);
	}

	@SuppressWarnings("unchecked")
	private void initBooksMap(final GigaMap<BlobBook> map)
	{
		final var indices = map.index().bitmap();
		indices.add(BlobBookIndices.ID_INDEX);
		indices.add(BlobBookIndices.TITLE_INDEX);
		indices.add(BlobBookIndices.ISBN_INDEX);
		indices.add(BlobBookIndices.PUBLICATION_DATE_INDEX);
		indices.add(BlobBookIndices.AUTHOR_FIRSTNAME_INDEX);
		indices.add(BlobBookIndices.AUTHOR_LASTNAME_INDEX);
		indices.add(BlobBookIndices.AUTHOR_EMAIL_INDEX);
		indices.setIdentityIndices(BlobBookIndices.ISBN_INDEX, BlobBookIndices.ID_INDEX);
	}

	public GigaMap<BlobBook> getBlobBooks()
	{
		return this.blobBooks;
	}

	public List<Book> getBooks()
	{
		return this.books;
	}

	public List<Author> getAuthors()
	{
		return this.authors;
	}

	public List<Publisher> getPublishers()
	{
		return this.publishers;
	}
}
