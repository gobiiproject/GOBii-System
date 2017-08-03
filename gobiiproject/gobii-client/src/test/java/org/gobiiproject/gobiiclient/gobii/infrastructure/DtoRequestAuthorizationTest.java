package org.gobiiproject.gobiiclient.gobii.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
//import org.gobiiproject.gobiimodel.dto.response.HeaderAuth;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;

import org.gobiiproject.gobiiapimodel.payload.HeaderAuth;
import org.gobiiproject.gobiimodel.types.GobiiHttpHeaderNames;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Phil on 5/31/2016.
 */
public class DtoRequestAuthorizationTest {


    @AfterClass
    public static void tearDownUpClass() throws Exception {
        // to be 100% that we cleaned up after ourselves.
        Assert.assertTrue(GobiiClientContextAuth.deAuthenticate());
    }


    private HttpPost makePostRequest() throws Exception {

        // Aside from crop domain, port, and type, we don't want the tests relying
        // on GobiiClientContext. Authentication will fill the clientContext with config data,
        // and de-authentication will nuke it.
        Assert.assertTrue(GobiiClientContextAuth.authenticate());
        String currentCropDomain = GobiiClientContext.getInstance(null, false).getCurrentCropDomain();
        Integer currentCropPort = GobiiClientContext.getInstance(null, false).getCurrentCropPort();
        String currentCropContextRoot = GobiiClientContext.getInstance(null, false).getCurrentCropContextRoot();
        String url = GobiiServiceRequestId.URL_AUTH.getRequestUrl(currentCropContextRoot, GobiiControllerType.GOBII.getControllerPath());
        Assert.assertTrue(GobiiClientContextAuth.deAuthenticate());

        URI uri = new URIBuilder().setScheme("http")
                .setHost(currentCropDomain)
                .setPort(currentCropPort)
                .setPath(url)
                .build();

        HttpPost returnVal = new HttpPost(uri);

        returnVal.setEntity(new StringEntity("empty"));

        returnVal.addHeader("Content-Type", "application/json");
        returnVal.addHeader("Accept", "application/json");

        return returnVal;
    }

    @Test
    public void testNoAuthHeaders() throws Exception {

        HttpPost postRequest = makePostRequest();

        // WE ARE _NOT_ ADDING ANY OF THE AUTHENTICATION TOKENS
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(postRequest);
        Integer httpStatusCode = httpResponse.getStatusLine().getStatusCode();
        Assert.assertTrue("Request without authentication headers should have failed; "
                        + "status code received was "
                        + httpStatusCode
                        + "(" + httpResponse.getStatusLine().getReasonPhrase() + ")",
                httpStatusCode.equals(401));
    }

    @Test
    public void testBadCredentials() throws Exception {

        HttpPost postRequest = makePostRequest();

        // add bogus credentials
        postRequest.addHeader(GobiiHttpHeaderNames.HEADER_NAME_USERNAME, "nobodyspecial");
        postRequest.addHeader(GobiiHttpHeaderNames.HEADER_NAME_PASSWORD, "unimaginative");

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(postRequest);


        Integer httpStatusCode = httpResponse.getStatusLine().getStatusCode();
        Assert.assertTrue("Request with bad user credentials should have failed; "
                        + "status code received was "
                        + httpStatusCode
                        + "(" + httpResponse.getStatusLine().getReasonPhrase() + ")",
                httpStatusCode.equals(401));


    }

    @Test
    public void testBadToken() throws Exception {

        HttpPost postRequest = makePostRequest();

        // add bogus credentials
        postRequest.addHeader(GobiiHttpHeaderNames.HEADER_NAME_TOKEN, "11111111");


        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(postRequest);
        Integer httpStatusCode = httpResponse.getStatusLine().getStatusCode();
        Assert.assertTrue("Request with bad token should have failed; "
                        + "status code received was "
                        + httpStatusCode
                        + "(" + httpResponse.getStatusLine().getReasonPhrase() + ")",
                httpStatusCode.equals(401));
    }

    @Test
    public void testGoodCredentailsWithToken() throws Exception {

        String gobiiCropTypeSent = "TEST";
        HttpPost postRequestForToken = makePostRequest();

        // add good credentials
        String testUser = GobiiClientContextAuth.getTestExecConfig().getLdapUserForUnitTest();
        String testPassword = GobiiClientContextAuth.getTestExecConfig().getLdapPasswordForUnitTest();

        postRequestForToken.addHeader(GobiiHttpHeaderNames.HEADER_NAME_USERNAME, testUser);
        postRequestForToken.addHeader(GobiiHttpHeaderNames.HEADER_NAME_PASSWORD, testPassword);


        postRequestForToken.addHeader(GobiiHttpHeaderNames.HEADER_NAME_GOBII_CROP,
                gobiiCropTypeSent.toString());

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(postRequestForToken);
        Integer httpStatusCode = httpResponse.getStatusLine().getStatusCode();
        Assert.assertTrue("Request with good user credentials should have succeded; "
                        + "status code received was "
                        + httpStatusCode
                        + "(" + httpResponse.getStatusLine().getReasonPhrase() + ")",
                httpStatusCode.equals(200));

        // verify that token was sent back in the response (for clients that use that)
        List<Header> tokenHeaderList = Arrays.asList(httpResponse.getAllHeaders())
                .stream()
                .filter(header -> header.getName().equals(GobiiHttpHeaderNames.HEADER_NAME_TOKEN))
                .collect(Collectors.toList());
        Assert.assertTrue("No authentication token was returned",
                tokenHeaderList.size() == 1);

        // verify that token is also sent as part of the body (for clients that need that because
        // the headers are blocked by the browser
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader((httpResponse.getEntity().getContent())));
        StringBuilder stringBuilder = new StringBuilder();
        String currentLine = null;
        while ((currentLine = bufferedReader.readLine()) != null) {
            stringBuilder.append(currentLine);
        }

        JsonParser parser = new JsonParser();
        String jsonAsString = stringBuilder.toString();
        JsonObject jsonBodyForToken = parser.parse(jsonAsString).getAsJsonObject();
        ObjectMapper objectMapper = new ObjectMapper();

        HeaderAuth dtoHeaderAuth = objectMapper.readValue(jsonBodyForToken.toString(),
                HeaderAuth.class);
        Assert.assertNotNull("No dto response was returned in response body", dtoHeaderAuth);
        String tokenFromBodyResponse = dtoHeaderAuth.getToken();

        Header tokenHeader = tokenHeaderList.get(0);
        String tokenValue = tokenHeader.getValue();
        Assert.assertTrue("Token from response and token from body do not match",
                tokenFromBodyResponse.equals(tokenHeader.getValue()));

        Assert.assertNotNull("Crop type was not returned", dtoHeaderAuth.getGobiiCropType());
        String gobiiCropTypeReceived = dtoHeaderAuth.getGobiiCropType();


        // now test we can do a request with the token we got
        HttpPost postRequestForPing = makePostRequest();
        postRequestForPing.addHeader(GobiiHttpHeaderNames.HEADER_NAME_TOKEN, tokenValue);

        HttpResponse httpResponseForToken = HttpClientBuilder.create().build().execute(postRequestForToken);
        Integer httpStatusCodeForToken = httpResponseForToken.getStatusLine().getStatusCode();
        Assert.assertTrue("Request with good user credentials should have succeded; "
                        + "status code received was "
                        + httpStatusCodeForToken
                        + "(" + httpResponse.getStatusLine().getReasonPhrase() + ")",
                httpStatusCodeForToken.equals(200));

    }

}
