
package io.helidon.examples.quickstart.mp;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.junit.jupiter.api.Test;

import io.helidon.microprofile.tests.junit5.HelidonTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@HelidonTest
class MainTest
{
	@Inject
	private MetricRegistry registry;
	@Inject
	private WebTarget target;

	@Test
	void testMicroprofileMetrics()
	{
		String message = this.target.path("simple-greet/Joe").request().get(String.class);

		assertThat(message, is("Hello Joe"));
		final Counter counter = this.registry.counter("personalizedGets");
		final double before = counter.getCount();

		message = this.target.path("simple-greet/Eric").request().get(String.class);

		assertThat(message, is("Hello Eric"));
		final double after = counter.getCount();
		assertEquals(1d, after - before, "Difference in personalized greeting counter between successive calls");
	}

	@Test
	void testMetrics()
	{
		final Response response = this.target.path("metrics").request().get();
		assertThat(response.getStatus(), is(200));
	}

	@Test
	void testHealth()
	{
		final Response response = this.target.path("health").request().get();
		assertThat(response.getStatus(), is(200));
	}

	@Test
	void testGreet()
	{
		final Message message = this.target.path("simple-greet").request().get(Message.class);
		assertThat(message.getMessage(), is("Hello World!"));
	}

	@Test
	void testGreetings()
	{
		Message jsonMessage = this.target.path("greet/Joe").request().get(Message.class);
		assertThat(jsonMessage.getMessage(), is("Hello Joe!"));

		try (
			Response r = this.target.path("greet/greeting")
				.request()
				.put(Entity.entity("{\"greeting\" : \"Hola\"}", MediaType.APPLICATION_JSON))
		)
		{
			assertThat(r.getStatus(), is(204));
		}

		jsonMessage = this.target.path("greet/Jose").request().get(Message.class);
		assertThat(jsonMessage.getMessage(), is("Hola Jose!"));
	}
}
