
package io.helidon.examples.quickstart.mp;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * A simple JAX-RS resource to greet you. Examples:
 *
 * Get default greeting message:
 * curl -X GET http://localhost:8080/simple-greet
 *
 * The message is returned as a JSON object.
 */
@Path("/simple-greet")
public class SimpleGreetResource {

    private static final String PERSONALIZED_GETS_COUNTER_NAME = "personalizedGets";
    private static final String PERSONALIZED_GETS_COUNTER_DESCRIPTION = "Counts personalized GET operations";
    private static final String GETS_TIMER_NAME = "allGets";
    private static final String GETS_TIMER_DESCRIPTION = "Tracks all GET operations";    private final String message;

    @Inject
    public SimpleGreetResource(@ConfigProperty(name = "app.greeting") final String message) {
        this.message = message;
    }

    /**
     * Return a worldly greeting message.
     *
     * @return {@link Message}
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Message getDefaultMessage() {
        final String msg = String.format("%s %s!", this.message, "World");
        final Message message = new Message();
        message.setMessage(msg);
        return message;
    }


    @Path("/{name}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Counted(name = PERSONALIZED_GETS_COUNTER_NAME,
             absolute = true,
             description = PERSONALIZED_GETS_COUNTER_DESCRIPTION)
    @Timed(name = GETS_TIMER_NAME,
           description = GETS_TIMER_DESCRIPTION,
           unit = MetricUnits.SECONDS,
           absolute = true)
    public String getMessage(@PathParam("name") final String name) {
        return String.format("Hello %s", name);
    }
}
