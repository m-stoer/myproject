package one.microstream.bsr;

import one.microstream.enterprise.cluster.nodelibrary.types.KafkaOffsetProvider;
import one.microstream.enterprise.cluster.nodelibrary.types.KafkaPropertiesProvider;
import one.microstream.enterprise.cluster.nodelibrary.types.NodelibraryPropertiesProvider;

public class CheckKafkaOffset
{
	public static void main(final String[] args)
	{
		final String topic = "a82f5f67e-7464-4271-9bc8-1e37bdcb06cf";
		final String group = "a82f5f67e-7464-4271-9bc8-1e37bdcb06cf-node-2-offset-getter2";
		try (
			final var kafka = KafkaOffsetProvider.New(
				topic,
				group,
				KafkaPropertiesProvider.New(NodelibraryPropertiesProvider.Env())
			)
		)
		{
			kafka.init();
			System.out.println("Offset is: " + kafka.provideLatestOffset());
		}
	}
}
