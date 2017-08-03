
package org.gobiiproject.gobiiclient.generic;

import org.gobiiproject.gobiiclient.generic.model.GenericTestValues;
import org.gobiiproject.gobiiclient.generic.model.Person;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(GenericTestPaths.GENERIC_TEST_ROOT + "/" + GenericTestPaths.GENERIC_CONTEXT_TWO)
public class GenericServerMethodsContextTwo {

    @GET
    @Path(GenericTestPaths.RESOURCE_PERSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String helloWorld() {


        return "nameFirst:\t"
                + GenericTestValues.NAME_FIRST + "\n"
                + "nameLast:\t"
                + GenericTestValues.NAME_LAST + "\n";

    }
}
