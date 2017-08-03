
package org.gobiiproject.gobiiclient.generic;

import org.gobiiproject.gobiiclient.generic.model.GenericTestValues;
import org.gobiiproject.gobiimodel.types.GobiiHttpHeaderNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Path(GenericTestPaths.GENERIC_TEST_ROOT + "/" + GenericTestPaths.GENERIC_CONTEXT_THREE)
public class GenericServerMethodsContextThree {


    Logger LOGGER = LoggerFactory.getLogger(GenericServerMethodsContextThree.class);

    @GET
    @Path(GenericTestPaths.FILES_MARKERS)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getFileOctet() throws Exception {

        return this.makeTestFileResponse();
    }

    private Response makeTestFileResponse() throws Exception {
        StreamingOutput stream ;

        try {

            stream = new StreamingOutput() {

                public void write(OutputStream output) throws IOException, WebApplicationException {
                    try {

                        File fileToSend = new ClassPathResource(GenericTestValues.FILE_MARKERS).getFile();
                        FileInputStream fileInputStream = new FileInputStream(fileToSend);

                        byte[] buf = new byte[8192];
                        int c;
                        while ((c = fileInputStream.read(buf, 0, buf.length)) > 0) {
                            output.write(buf, 0, c);
                            output.flush();
                        }
                    } catch (Exception e) {
                        throw new WebApplicationException(e);
                    }
                }
            };


        } catch (Exception e) {
            LOGGER.error("errror in " + GenericTestPaths.FILES_MARKERS, e);
            throw new WebApplicationException(e);
        }


        return Response
                .ok(stream)
                .header("content-disposition", "attachment; filename = " + GenericTestValues.FILE_MARKERS)
                .build();
    }

    /***
     * Note that the only difference between this method and the Octo method is the @Produces() value;
     * The exact same resource, even with the same REST method (GET in this case), can be differentiated
     * purely by the content type it produces
     * @return
     * @throws Exception
     */
    @GET
    @Path(GenericTestPaths.FILES_MARKERS)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    public Response getFileMultiPart() throws Exception {

        return this.makeTestFileResponse();
    }

    /***
     * Note that the only difference between this method and the previous ones the @Produces() value;
     * The resource name is different however. We are verifying that we can download plain text with the
     * client framework.
     * @return
     * @throws Exception
     */
    @GET
    @Path(GenericTestPaths.FILES_PLAIN_TEXT)
    @Produces(MediaType.TEXT_PLAIN)
    public String getFilePlainText() throws Exception {

       //return this.makeTestFileResponse();
        return "Plain text";
    }

}
