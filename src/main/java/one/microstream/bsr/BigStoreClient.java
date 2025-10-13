package one.microstream.bsr;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

import org.eclipse.serializer.chars.VarString;

public class BigStoreClient
{
	public static void main(final String[] args) throws IOException, InterruptedException
	{
		final var bodyBuilder = VarString.New().add('[');
		for (int i = 1000; i < 2000; i++)
		{
			final int isbn = i;
			final String title = "I am title for isbn " + isbn;
			final String publisher =
				"{\"id\":0,\"email\":\"random@email.com\",\"company\":\"random company yes\",\"addresses\":[{\"address\":\"I am address\",\"address2\":null,\"zip\":null,\"city\":\"I am city\",\"country\":\"I am country\"}]}";
			final String author =
				"{\"id\":0,\"email\":\"random@email.com\",\"firstname\":\"Hans\",\"lastname\":\"Soler\",\"addresses\":[{\"address\":\"I am address\",\"address2\":null,\"zip\":null,\"city\":\"I am city\",\"country\":\"I am country\"}]}";
			bodyBuilder.add(
				String.format(
					"{\"id\":0,\"isbn\":\"%d\",\"title\":\"%s\",\"publicationDate\":\"1950-03-01\",\"edition\":1,\"availableQuantity\":10,\"priceEuroCent\":10000,\"author\":%s,\"publisher\":%s}",
					isbn,
					title,
					author,
					publisher
				)
			);
			bodyBuilder.add(',');
		}
		// delete last trailing comma
		bodyBuilder.deleteLast().add(']');

		final var rootUri = URI.create(
			"https://0ojpivj51f72qtot.eu-central-1.cluster.microstream.cloud/ab7a0b74-235f-4677-98f2-356d4663c59d/"
		);

		try (final var client = HttpClient.newHttpClient())
		{
			final var request = HttpRequest.newBuilder(rootUri.resolve("blobbook/batch"))
				.PUT(BodyPublishers.ofString(bodyBuilder.toString()))
				.build();
			final var response = client.send(request, BodyHandlers.ofString());
			System.out.println(
				String.format("Received response:\nStatus: %d\nBody: %s", response.statusCode(), response.body())
			);
		}
	}
}
