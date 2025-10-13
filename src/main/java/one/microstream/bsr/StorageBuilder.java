package one.microstream.bsr;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.serializer.reference.Lazy;
import org.eclipse.store.storage.embedded.configuration.types.EmbeddedStorageConfiguration;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageFoundation;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageManager;

import one.microstream.enterprise.storage.distributed.types.ObjectGraphUpdateHandler;
import one.microstream.enterprise.storage.distributed.types.StorageBinaryDataMerger;
import one.microstream.enterprise.storage.distributed.types.StorageBinaryDataMessage;
import one.microstream.enterprise.storage.distributed.types.StorageBinaryDataPacket;
import one.microstream.enterprise.storage.distributed.types.StorageBinaryDataPacketAcceptor;

public class StorageBuilder
{
	@SuppressWarnings("unchecked")
	public static void main(final String[] args) throws IOException
	{
		final EmbeddedStorageFoundation<?> foundation = EmbeddedStorageConfiguration.Builder()
			.setStorageDirectory(Paths.get("storage").toString())
			.setChannelCount(1)
			.createEmbeddedStorageFoundation();
		final EmbeddedStorageManager storage = foundation.start();
		if (storage.root() == null)
		{
			throw new RuntimeException("Only use this builder with the 'emptyStorage' storage");
		}

		final StorageBinaryDataMerger merger = StorageBinaryDataMerger.New(
			foundation.getConnectionFoundation(),
			storage,
			ObjectGraphUpdateHandler.Synchronized()
		);
		final var client = StorageBinaryDataPacketAcceptor.New(merger);

		int count = 0;

		try (final var dataFile = new DataInputStream(new FileInputStream(Paths.get("data.bin").toFile())))
		{
			final byte[] bytes = new byte[1000];

			for (long x = 0; x < Long.MAX_VALUE; x++) // linter is going crazy with while true
			{
				final int packetListLength;
				try
				{
					packetListLength = dataFile.readInt();
				}
				catch (final EOFException e)
				{
					// expected with clean data
					break;
				}
				final List<StorageBinaryDataPacket> packets = new ArrayList<>(packetListLength);
				System.out.println("Collecting packet list" + count++);

				for (int i = 0; i < packetListLength; i++)
				{
					final int messageLength, packetCount, packetIndex, bufferLength;
					final StorageBinaryDataMessage.MessageType messageType;

					messageLength = dataFile.readInt();
					packetCount = dataFile.readInt();
					packetIndex = dataFile.readInt();
					messageType = StorageBinaryDataMessage.MessageType.values()[dataFile.readInt()];
					bufferLength = dataFile.readInt();

					final ByteBuffer buffer = ByteBuffer.allocateDirect(bufferLength);
					while (buffer.hasRemaining())
					{
						final int n = dataFile.read(bytes, 0, Math.min(buffer.remaining(), bytes.length));
						buffer.put(bytes, 0, n);
					}
					buffer.flip();

					final var packet = StorageBinaryDataPacket.New(
						messageType,
						messageLength,
						packetIndex,
						packetCount,
						buffer
					);

					packets.add(packet);
				}

				client.accept(packets);
			}
		}

		try
		{
			System.out.println(((Lazy<DataRoot>)storage.root()).get().getAuthors().get(0).getEmail());
		}
		catch (final Exception e)
		{
			System.out.println("Failed to get email: " + e.getMessage());
		}
		storage.shutdown();
	}
}
