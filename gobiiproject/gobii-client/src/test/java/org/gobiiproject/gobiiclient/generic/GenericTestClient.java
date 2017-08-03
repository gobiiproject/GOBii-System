package org.gobiiproject.gobiiclient.generic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpStatus;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiclient.core.common.GenericClientContext;
import org.gobiiproject.gobiiclient.core.common.HttpMethodResult;
import org.gobiiproject.gobiiclient.core.gobii.GobiiTestConfiguration;
import org.gobiiproject.gobiiclient.generic.model.GenericTestValues;
import org.gobiiproject.gobiiclient.generic.model.Person;
import org.gobiiproject.gobiimodel.config.ServerBase;
import org.gobiiproject.gobiimodel.config.TestExecConfig;
import org.gobiiproject.gobiimodel.types.GobiiHttpHeaderNames;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import javax.ws.rs.core.MediaType;
import java.io.File;
import java.util.UUID;

/**
 * Created by Phil on 5/19/2017.
 */
public class GenericTestClient {

    private static Logger LOGGER = LoggerFactory.getLogger(GenericTestClient.class);

    private static Server server = null;
    private static ServerBase serverBase = null;
    private static GenericClientContext genericClientContext = null;
    private static TestExecConfig testExecConfig = null;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeClass
    public static void serverSetup() throws Exception {

        testExecConfig = new GobiiTestConfiguration().getConfigSettings().getTestExecConfig();


        // We are using a simple Jetty server with Jersey annotations
        // By setting the packages() to one of the service implementation classes,
        // we tell the resource to scan that package for all classes having the Jersey
        // annotations. So we don't need to to manually create the context classes here.
        serverBase = new ServerBase("localhost",
                GenericTestPaths.GENERIC_CONTEXT_ONE,
                8099,
                true);


        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.packages(GenericServerMethodsContextOne.class.getPackage().getName());
        resourceConfig.register(JacksonFeature.class);
        resourceConfig.register(SerializationFeature.INDENT_OUTPUT);
        ServletContainer servletContainer = new ServletContainer(resourceConfig);
        ServletHolder sh = new ServletHolder(servletContainer);
        server = new Server(serverBase.getPort());

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addServlet(sh, "/*");
        server.setHandler(context);


        genericClientContext = new GenericClientContext(serverBase);

        //System.out.print(server.dump());
        server.start();
        //Uncomment server.join() to make server persist so you can curl the server
        //server.join();
    }

    @AfterClass
    public static void serverTearDown() throws Exception {

        server.stop();

        String testFileDirectory = testExecConfig.getTestFileDownloadDirectory();
        File file = new File(testFileDirectory);
        if ( file.exists() ) {
            FileUtils.deleteDirectory(file);
        }
    }



    public static boolean didHttpMethodSucceed(HttpMethodResult httpMethodResult) {

        boolean returnVal = true;

        if (httpMethodResult.getResponseCode() != HttpStatus.SC_OK) {
            String message = "http method failed: "
                    + httpMethodResult.getUri().toString()
                    + "; failure mode: "
                    + Integer.toString(httpMethodResult.getResponseCode())
                    + " ("
                    + httpMethodResult.getReasonPhrase()
                    + ")";

            LOGGER.error(message);

            returnVal = false;
        }

        return returnVal;
    }

    /***
     * get against a simple a resource: we verify that the Person object created
     * on the server has what we know to be the accurate test values
     * Note that this example uses the raw json to look at the response.
     * Later we will deserialize to a person object.
     * @throws Exception
     */
    @Test
    public void testGetJson() throws Exception {

        RestUri restUriGetPerson = new RestUri(GenericTestPaths.GENERIC_TEST_ROOT,
                GenericTestPaths.GENERIC_CONTEXT_ONE,
                GenericTestPaths.RESOURCE_PERSON);

        HttpMethodResult httpMethodResult = genericClientContext
                .get(restUriGetPerson);

        Assert.assertTrue(didHttpMethodSucceed(httpMethodResult));

        Assert.assertNotNull(httpMethodResult.getJsonPayload());

        Assert.assertEquals(httpMethodResult
                        .getJsonPayload()
                        .get("nameLast")
                        .getAsString(),
                GenericTestValues.NAME_LAST);

        Assert.assertEquals(httpMethodResult
                        .getJsonPayload()
                        .get("nameFirst")
                        .getAsString(),
                GenericTestValues.NAME_FIRST);

    }

    /***
     * Here we use a uri parameter (/person/{personId}). The test sevice
     * will set the Person object's ID to the id we used as the parameter,
     * which is arbitrary. So we verify that the person object has our
     * uri parameter value
     * Note that this example uses the raw json to look at the response.
     * Later we will deserialize to a person object.
     * @throws Exception
     */
    @Test
    public void testGetPersonByIdJson() throws Exception {

        String personIdVal = "101";
        RestUri restUriGetPerson = new RestUri(GenericTestPaths.GENERIC_TEST_ROOT,
                GenericTestPaths.GENERIC_CONTEXT_ONE,
                GenericTestPaths.RESOURCE_PERSON)
                .addUriParam("personId")
                .setParamValue("personId", personIdVal);

        HttpMethodResult httpMethodResult = genericClientContext
                .get(restUriGetPerson);

        Assert.assertTrue(didHttpMethodSucceed(httpMethodResult));

        Assert.assertNotNull(httpMethodResult.getJsonPayload());

        Assert.assertEquals(httpMethodResult
                        .getJsonPayload()
                        .get("personId")
                        .getAsString(),
                personIdVal);
    }

    /***
     * Here we use query parameters (/person?nameLast="Smith",nameFirst="John"). The test sevice
     * will set the Person object's ID to an arbitrary id and gives an object whose
     * first and last name matches
     * uri parameter value
     * Here for the first time we are deserializing to a person object rather than using
     * raw json.
     * @throws Exception
     */
    @Test
    public void testGetPersonBySearchJson() throws Exception {

        String nameFirst = "nuFirstName";
        String nameLast = "nuLasetName";

        RestUri restUriGetPerson = new RestUri(GenericTestPaths.GENERIC_TEST_ROOT,
                GenericTestPaths.GENERIC_CONTEXT_ONE,
                GenericTestPaths.RESOURCE_PERSON_SEARCH)
                .addQueryParam("nameLast")
                .setParamValue("nameLast", nameLast)
                .addQueryParam("nameFirst")
                .setParamValue("nameFirst", nameFirst);

        HttpMethodResult httpMethodResult = genericClientContext
                .get(restUriGetPerson);

        Assert.assertTrue(didHttpMethodSucceed(httpMethodResult));

        Assert.assertNotNull(httpMethodResult.getJsonPayload());

        Person personRetrieved = objectMapper.readValue(httpMethodResult.getJsonPayload().toString(),
                Person.class);

        // we didn't send an ID, and the server should have specified
        Assert.assertNotNull(personRetrieved.getPersonId());

        //these should equal what we searched for
        Assert.assertEquals(personRetrieved.getNameLast(), nameLast);
        Assert.assertEquals(personRetrieved.getNameFirst(), nameFirst);


    }

    /***
     * here we use a POST to create a new person
     * @throws Exception
     */
    @Test
    public void postNewPerson() throws Exception {

        String nameFirst = "nuFirstName";
        String nameLast = "nuLasetName";
        String desc = UUID.randomUUID().toString();
        Person person = new Person(null, nameFirst, nameLast, desc);

        RestUri restUriPostPerson = new RestUri(GenericTestPaths.GENERIC_TEST_ROOT,
                GenericTestPaths.GENERIC_CONTEXT_ONE,
                GenericTestPaths.RESOURCE_PERSON);

        String postBody = this.objectMapper.writeValueAsString(person);

        HttpMethodResult httpMethodResult = genericClientContext
                .post(restUriPostPerson, postBody);

        Assert.assertTrue(didHttpMethodSucceed(httpMethodResult));

        Assert.assertNotNull(httpMethodResult.getJsonPayload());

        Person personRetrieved = objectMapper.readValue(httpMethodResult.getJsonPayload().toString(),
                Person.class);

        // we didn't send an ID, and the server should have created one
        Assert.assertNotNull(personRetrieved.getPersonId());

        //these should equal what we sent
        Assert.assertEquals(personRetrieved.getNameLast(), nameLast);
        Assert.assertEquals(personRetrieved.getNameFirst(), nameFirst);
        Assert.assertEquals(personRetrieved.getDescription(), desc);
    }

    /***
     * here we use a PUT to update aperson
     * @throws Exception
     */
    @Test
    public void puttNewPerson() throws Exception {

        String nameFirst = "nuFirstName";
        String nameLast = "nuLasetName";
        String desc = UUID.randomUUID().toString();
        String personId = "1500";
        Person person = new Person(personId, nameFirst, nameLast, desc);

        RestUri restUriPostPerson = new RestUri(GenericTestPaths.GENERIC_TEST_ROOT,
                GenericTestPaths.GENERIC_CONTEXT_ONE,
                GenericTestPaths.RESOURCE_PERSON);

        String putBody = this.objectMapper.writeValueAsString(person);

        HttpMethodResult httpMethodResult = genericClientContext
                .put(restUriPostPerson, putBody);

        Assert.assertTrue(didHttpMethodSucceed(httpMethodResult));

        Assert.assertNotNull(httpMethodResult.getJsonPayload());

        Person personRetrieved = objectMapper.readValue(httpMethodResult.getJsonPayload().toString(),
                Person.class);

        //This time we supplied an ID and since it's an update that should not have changed
        Assert.assertEquals(personRetrieved.getPersonId(), personId);

        //But the last name should be set to the one the server updates it  to
        Assert.assertEquals(personRetrieved.getNameLast(), GenericTestValues.NAME_LAST_UPDATED);
        Assert.assertEquals(personRetrieved.getNameFirst(), nameFirst);
        Assert.assertEquals(personRetrieved.getDescription(), desc);
    }


    /***
     * Here we switch to context two, which is Media type plain text. Therte is only
     * one of these because we are just verifying that the header setting infrastructure
     * works correctly. Note that the HttpCore::submitHttpMethod() method has to have
     * a condition to handle the specified media type. So it for sure handles json and
     * plain text, but other media types might require more tinkering
     *
     * @throws Exception
     */
    @Test
    public void testGetPlain() throws Exception {

        RestUri restUriGetPerson = new RestUri(GenericTestPaths.GENERIC_TEST_ROOT,
                GenericTestPaths.GENERIC_CONTEXT_TWO,
                GenericTestPaths.RESOURCE_PERSON)
                .withHttpHeader(GobiiHttpHeaderNames.HEADER_NAME_CONTENT_TYPE,
                        MediaType.TEXT_PLAIN)
                .withHttpHeader(GobiiHttpHeaderNames.HEADER_NAME_ACCEPT,
                        MediaType.TEXT_PLAIN);

        HttpMethodResult httpMethodResult = genericClientContext
                .get(restUriGetPerson);

        Assert.assertTrue(didHttpMethodSucceed(httpMethodResult));

        String plainTextResult = httpMethodResult.getPlainPayload();
        Assert.assertNotNull(plainTextResult);
        Assert.assertTrue(plainTextResult.contains(GenericTestValues.NAME_FIRST));
        Assert.assertTrue(plainTextResult.contains(GenericTestValues.NAME_LAST));

    }


    /***
     * Test download of a file as OctetStream.
     * The file will be removed at the end of the test -- if you want to physically
     * inspect that download succeeded, put a break point in tearDown() before th e
     * deletion
     * @throws Exception
     */
    @Test
    public void testGetFileOctet() throws Exception {

        String destinationPath = testExecConfig
                .getTestFileDownloadDirectory();

        Assert.assertNotNull("The test configuration does not define a temp download directory",
                destinationPath);

        File destinationFolder = new File(destinationPath);
        if (!destinationFolder.exists()) {
            destinationFolder.mkdir();
        }

        String fileFqpn = destinationPath + "/" + GenericTestValues.FILE_MARKERS;

        RestUri restUriGetFileDownload = new RestUri(GenericTestPaths.GENERIC_TEST_ROOT,
                GenericTestPaths.GENERIC_CONTEXT_THREE,
                GenericTestPaths.FILES_MARKERS)
                .withHttpHeader(GobiiHttpHeaderNames.HEADER_NAME_CONTENT_TYPE,
                        MediaType.APPLICATION_OCTET_STREAM)
                .withHttpHeader(GobiiHttpHeaderNames.HEADER_NAME_ACCEPT,
                        MediaType.APPLICATION_OCTET_STREAM)
                .withDestinationFqpn(fileFqpn);

        HttpMethodResult httpMethodResult = genericClientContext
                .get(restUriGetFileDownload);

        Assert.assertTrue(didHttpMethodSucceed(httpMethodResult));

        File downloadedFile = new File(httpMethodResult.getFileName());
        Assert.assertTrue("File download web method succeeded, but the file does not exist on the specified path"
                        + httpMethodResult.getFileName(),
                downloadedFile.exists());
    }


    /***
     * Test download of a file as Multipart.
     * @throws Exception
     */
    @Test
    public void testGetFileMultiPart() throws Exception {

        String destinationPath = testExecConfig
                .getTestFileDownloadDirectory();

        Assert.assertNotNull("The test configuration does not define a temp download directory",
                destinationPath);

        File destinationFolder = new File(destinationPath);
        if (!destinationFolder.exists()) {
            destinationFolder.mkdir();
        }

        String fileFqpn = destinationPath + "/" + GenericTestValues.FILE_MARKERS;

        RestUri restUriGetFileDownload = new RestUri(GenericTestPaths.GENERIC_TEST_ROOT,
                GenericTestPaths.GENERIC_CONTEXT_THREE,
                GenericTestPaths.FILES_MARKERS)
                .withHttpHeader(GobiiHttpHeaderNames.HEADER_NAME_CONTENT_TYPE,
                        MediaType.MULTIPART_FORM_DATA)
                .withHttpHeader(GobiiHttpHeaderNames.HEADER_NAME_ACCEPT,
                        MediaType.MULTIPART_FORM_DATA)
                .withDestinationFqpn(fileFqpn);

        HttpMethodResult httpMethodResult = genericClientContext
                .get(restUriGetFileDownload);

        Assert.assertTrue(didHttpMethodSucceed(httpMethodResult));

        File downloadedFile = new File(httpMethodResult.getFileName());
        Assert.assertTrue("File download web method succeeded, but the file does not exist on the specified path"
                        + httpMethodResult.getFileName(),
                downloadedFile.exists());
    }

    /***
     * Test download of a file as Multipart.
     * @throws Exception
     */
    @Test
    public void testGetFilePlainText() throws Exception {

        String destinationPath = testExecConfig
                .getTestFileDownloadDirectory();

        Assert.assertNotNull("The test configuration does not define a temp download directory",
                destinationPath);

        File destinationFolder = new File(destinationPath);
        if (!destinationFolder.exists()) {
            destinationFolder.mkdir();
        }

        String fileFqpn = destinationPath + "/" + GenericTestValues.FILE_MARKERS;

        RestUri restUriGetFileDownload = new RestUri(GenericTestPaths.GENERIC_TEST_ROOT,
                GenericTestPaths.GENERIC_CONTEXT_THREE,
                GenericTestPaths.FILES_PLAIN_TEXT)
                .withHttpHeader(GobiiHttpHeaderNames.HEADER_NAME_ACCEPT,
                        MediaType.TEXT_PLAIN)
                .withDestinationFqpn(fileFqpn);

        HttpMethodResult httpMethodResult = genericClientContext
                .get(restUriGetFileDownload);

        Assert.assertTrue(didHttpMethodSucceed(httpMethodResult));

        File downloadedFile = new File(httpMethodResult.getFileName());
        Assert.assertTrue("File download web method succeeded, but the file does not exist on the specified path"
                        + httpMethodResult.getFileName(),
                downloadedFile.exists());
    }

}