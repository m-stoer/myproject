
package io.helidon.examples.quickstart.mp;

import java.util.Collections;

import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class PersonResource
{
	private static final JsonBuilderFactory JSON = Json.createBuilderFactory(Collections.emptyMap());

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public JsonArray getPersons()
	{
		final JsonArrayBuilder retval = JSON.createArrayBuilder();
		DB.get().root().persons.stream().map(this::personToJson).forEach(retval::add);
		return retval.build();
	}

	@GET
	@Path("/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public JsonObject getPerson(@PathParam("name") final String name)
	{
		return this.personToJson(
			DB.get().root().persons.stream().filter(p -> p.getName().equals(name)).findFirst().orElse(null)
		);
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@RequestBody(name = "person", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON))
	public void putPerson(final JsonObject person)
	{
		final DB db = DB.get();

		synchronized (db)
		{
			db.root().persons.add(this.jsonToPerson(person));
			db.storage().store(db.root().persons);
		}
	}

	private JsonObject personToJson(final Person person)
	{
		if (person == null)
		{
			return JSON.createObjectBuilder().build();
		}

		return JSON.createObjectBuilder().add("name", person.getName()).add("age", person.getAge()).build();
	}

	private Person jsonToPerson(final JsonObject person)
	{
		if (person.isEmpty())
		{
			return null;
		}

		return new Person(person.getString("name"), person.getInt("age"));
	}
}
