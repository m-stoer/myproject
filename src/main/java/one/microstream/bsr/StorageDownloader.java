package one.microstream.bsr;

import static org.apache.kafka.clients.consumer.ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.ISOLATION_LEVEL_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.common.IsolationLevel.READ_COMMITTED;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import one.microstream.enterprise.cluster.nodelibrary.types.ClusterStorageBinaryDistributedKafka;
import one.microstream.enterprise.cluster.nodelibrary.types.KafkaPropertiesProvider;
import one.microstream.enterprise.cluster.nodelibrary.types.NodelibraryPropertiesProvider;
import one.microstream.enterprise.storage.distributed.types.StorageBinaryDataPacket;
import one.microstream.enterprise.storage.distributed.types.StorageBinaryDataPacketAcceptor;

public class StorageDownloader
{
	public static void main(final String[] args) throws IOException
	{
		final String topic = "a01842ed2-2be0-4d86-9e20-d902aaf18138";
		final String groupid = "a01842ed2-2be0-4d86-9e20-d902aaf18138-client";
		final var kafka = new StorageDownloader(topic, groupid, Long.MIN_VALUE + 2L, StorageDownloader::accept);
		kafka.start();
		dataFile.close();
	}

	private static final DataOutputStream dataFile;
	private static int count = 0;

	static
	{
		try
		{
			Files.deleteIfExists(Paths.get("data.bin"));
			dataFile = new DataOutputStream(new FileOutputStream(Files.createFile(Paths.get("data.bin")).toFile()));
		}
		catch (final IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	private static final byte[] bytes = new byte[1000];

	public static void accept(final List<StorageBinaryDataPacket> packets)
	{
		try
		{
			System.out.println("Writing list " + count++);
			dataFile.writeInt(packets.size());

			for (final var packet : packets)
			{
				dataFile.writeInt(packet.messageLength());
				dataFile.writeInt(packet.packetCount());
				dataFile.writeInt(packet.packetIndex());
				dataFile.writeInt(packet.messageType().ordinal());
				dataFile.writeInt(packet.buffer().remaining());
				while (packet.buffer().hasRemaining())
				{
					final int n = Math.min(packet.buffer().remaining(), 1000);
					packet.buffer().get(bytes, 0, n);
					dataFile.write(bytes, 0, n);
				}
			}
		}
		catch (final IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	private final Logger logger = LoggerFactory.getLogger(StorageDownloader.class);
	private long microstreamOffset;
	private final long initialMicrostreamOffset;
	private final StorageBinaryDataPacketAcceptor packetAcceptor;
	private final String topicName;
	private final String groupId;

	public StorageDownloader(
		final String topicName,
		final String groupId,
		final long microstreamOffset,
		final StorageBinaryDataPacketAcceptor packetAcceptor
	)
	{
		this.topicName = topicName;
		this.groupId = groupId;
		this.microstreamOffset = microstreamOffset;
		this.initialMicrostreamOffset = microstreamOffset;
		this.packetAcceptor = packetAcceptor;
	}

	public void start()
	{
		this.run();
	}

	private void run()
	{
		this.logger.info("Starting reading with microstream offset {}", this.microstreamOffset);

		final Properties properties = KafkaPropertiesProvider.New(NodelibraryPropertiesProvider.Env()).provide();
		properties.setProperty(GROUP_ID_CONFIG, this.groupId);
		properties.setProperty(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
		properties.setProperty(ENABLE_AUTO_COMMIT_CONFIG, "false");
		properties.setProperty(AUTO_OFFSET_RESET_CONFIG, "earliest");
		properties.setProperty(ALLOW_AUTO_CREATE_TOPICS_CONFIG, "false");
		properties.setProperty(ISOLATION_LEVEL_CONFIG, READ_COMMITTED.toString().toLowerCase(Locale.ROOT));

		try (final KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<>(properties))
		{
			consumer.subscribe(Arrays.asList(this.topicName));
			while (true)
			{
				var records = consumer.poll(Duration.ofSeconds(3));
				if (records.isEmpty())
				{
					for (int i = 0; i < 10; i++)
					{
						System.out.println("Retrying kafka records");
						records = consumer.poll(Duration.ofSeconds(10));
						if (!records.isEmpty())
						{
							System.out.println("It worked!!");
							break;
						}
					}
				}
				this.consume(records);
				if (records.isEmpty())
				{
					System.out.println("Done at offset " + this.microstreamOffset);
					return;
				}
			}
		}
	}

	private void consume(final ConsumerRecords<String, byte[]> records)
	{
		final List<StorageBinaryDataPacket> packets = new ArrayList<>();
		final Iterator<ConsumerRecord<String, byte[]>> iterator = records.iterator();
		while (iterator.hasNext())
		{
			final ConsumerRecord<String, byte[]> record = iterator.next();
			if (record.serializedValueSize() >= 0)
			{
				final Headers headers = record.headers();
				final long offset = ClusterStorageBinaryDistributedKafka.deserializeLong(
					headers.lastHeader(ClusterStorageBinaryDistributedKafka.keyMicrostreamOffset()).value()
				);
				if (offset % 1000 == 0)
				{
					System.out.println("Processing offset " + offset);
				}
				if (offset <= this.microstreamOffset && offset > this.initialMicrostreamOffset)
				{
					this.logger.warn(
						"WARNING: Encountered microstream offset {}, but own offset is at {}, there might have been multiple writers!",
						offset,
						this.microstreamOffset
					);
				}
				if (offset > this.microstreamOffset)
				{
					this.microstreamOffset = offset;
					packets.add(this.createDataPacket(record, headers));
				}
			}
		}

		if (!packets.isEmpty())
		{
			this.packetAcceptor.accept(packets);
		}
	}

	private StorageBinaryDataPacket createDataPacket(final ConsumerRecord<String, byte[]> record, final Headers headers)
	{
		return StorageBinaryDataPacket.New(
			ClusterStorageBinaryDistributedKafka.messageType(headers),
			ClusterStorageBinaryDistributedKafka.messageLength(headers),
			ClusterStorageBinaryDistributedKafka.packetIndex(headers),
			ClusterStorageBinaryDistributedKafka.packetCount(headers),
			ByteBuffer.wrap(record.value())
		);
	}
}
