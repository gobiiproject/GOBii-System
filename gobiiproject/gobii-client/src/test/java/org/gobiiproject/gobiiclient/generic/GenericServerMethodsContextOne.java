
package org.gobiiproject.gobiiclient.generic;

import org.gobiiproject.gobiiclient.generic.model.GenericTestValues;
import org.gobiiproject.gobiiclient.generic.model.Person;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

// configuraiton examples with Jersey annotations:
// https://www.javatpoint.com/jax-rs-annotations-example
@Path(GenericTestPaths.GENERIC_TEST_ROOT + "/" + GenericTestPaths.GENERIC_CONTEXT_ONE)
public class GenericServerMethodsContextOne {

    @GET
    @Path(GenericTestPaths.RESOURCE_PERSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Person getAPerson() {

        return new Person(null, GenericTestValues.NAME_FIRST,
                GenericTestValues.NAME_LAST, "Nice guy");
    }

    @GET
    @Path(GenericTestPaths.RESOURCE_PERSON + "/{personId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Person getAPersonById(@PathParam("personId") String personId) {

        return new Person(personId,
                GenericTestValues.NAME_FIRST,
                GenericTestValues.NAME_LAST, "Nice guy");
    }

    @GET
    @Path(GenericTestPaths.RESOURCE_PERSON_SEARCH)
    @Produces(MediaType.APPLICATION_JSON)
    public Person getAPersonBySearch(@QueryParam("nameLast") String nameLast,
                                     @QueryParam("nameFirst") String nameFirst) {

        return new Person("2100",
                nameFirst,
                nameLast,
                "a matching guy");
    }

    @POST
    @Path(GenericTestPaths.RESOURCE_PERSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Person postAPerson(Person person) {

        return new Person("1500",
                person.getNameFirst(),
                person.getNameLast(),
                person.getDescription());
    }

    @PUT
    @Path(GenericTestPaths.RESOURCE_PERSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Person putAPerson(Person person) {

        return new Person(person.getPersonId(),
                person.getNameFirst(),
                GenericTestValues.NAME_LAST_UPDATED,
                person.getDescription());
    }


}
