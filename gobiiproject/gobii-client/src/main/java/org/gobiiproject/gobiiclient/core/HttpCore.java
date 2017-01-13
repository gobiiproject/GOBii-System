package org.gobiiproject.gobiiclient.core;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.gobiiproject.gobiimodel.types.GobiiHttpHeaderNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * Created by Phil on 5/13/2016.
 */
public class HttpCore {

    private String host = null;
    private Integer port = null;

    public HttpCore(String host, Integer port) {

        this.host = host;
        this.port = port;
    }


    Logger LOGGER = LoggerFactory.getLogger(HttpCore.class);



    final private static int HTTP_STATUS_CODE_OK = 200;
    final private static int HTTP_STATUS_CODE_UNAUTHORIZED = 401;  //not authenticated
    final private static int HTTP_STATUS_CODE_FORBIDDEN = 403; //not authorized

    private URIBuilder getBaseBuilder() throws Exception {
        return (new URIBuilder().setScheme("http")
                .setHost(host)
                .setPort(port));
    }

    private URI makeUri(String url) throws Exception {

        URI returnVal = getBaseBuilder()
                .setPath(url)
                .build();

        return (returnVal);

    }//byContentTypeUri()

    private URI logoutURI() throws Exception {

        URI returnVal = getBaseBuilder()
                .setPath("/logout") //configured in security configuration xml
                .build();

        return (returnVal);
    }//byContentTypeUri()

    private HttpResponse submitUriRequest(HttpUriRequest httpUriRequest, String userName, String password, String token) throws Exception {
        httpUriRequest.addHeader("Content-Type", "application/json");
        httpUriRequest.addHeader("Accept", "application/json");

        if ((null != token) && (false == token.isEmpty())) {
            httpUriRequest.addHeader(GobiiHttpHeaderNames.HEADER_TOKEN, token);
        } else {
            httpUriRequest.addHeader(GobiiHttpHeaderNames.HEADER_USERNAME, userName);
            httpUriRequest.addHeader(GobiiHttpHeaderNames.HEADER_PASSWORD, password);
        }


        if( ClientContext.isInitialized() ) {
            httpUriRequest.addHeader(GobiiHttpHeaderNames.HEADER_GOBII_CROP,
                    ClientContext.getInstance(null, false).getCurrentClientCropType().toString());
        }

        return (HttpClientBuilder.create().build().execute(httpUriRequest));

    }// submitUriRequest()


    private void logInfo(String logMessage) {
        LOGGER.info(logMessage);
    } // logInfo()

    private void logHeaders(Header[] headers) {

        for (Header currentHeader : headers) {
            logInfo(currentHeader.getName() + ": " + currentHeader.getValue());
        }

    }//logHeaders()

    private Header getHeader(Header[] headers, String headerName) {
        Header returnVal = null;

        boolean foundHeader = false;
        for (int idx = 0; idx < headers.length && foundHeader == false; idx++) {
            Header currentHeader = headers[idx];

            if (headerName.equals(currentHeader.getName())) {
                returnVal = currentHeader;
                foundHeader = true;
            }
        }//iterate headers

        return (returnVal);

    }//getHeader()

    private void logRequestHeaders(HttpUriRequest httpUriRequest, HttpResponse httpResponse, String testName) throws Exception {


        logInfo("============================================ BEGIN TEST " + testName + "==================================");
        logInfo("");
        logInfo("");
        logInfo("");
        logInfo("****************");
        logInfo("****************");
        logInfo("****************** Request URL");
        logInfo(httpUriRequest.getURI().toString());


        logInfo("");
        logInfo("");
        logInfo("");
        logInfo("****************");
        logInfo("****************");
        logInfo("****************** Request Headers");
        logHeaders(httpUriRequest.getAllHeaders());

        logInfo("");
        logInfo("");
        logInfo("");
        logInfo("****************");
        logInfo("****************");
        logInfo("****************** Response  Headers");
        logHeaders(httpResponse.getAllHeaders());

        logInfo("");
        logInfo("");
        logInfo("");
        logInfo("****************");
        logInfo("****************");
        logInfo("****************** Response  Status");
        logInfo(httpResponse.getStatusLine().toString());

        logInfo("");
        logInfo("");
        logInfo("");
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader((httpResponse.getEntity().getContent())));

        String output;
        logInfo("****************");
        logInfo("****************");
        logInfo("********** *******Response Body content");
        while ((output = bufferedReader.readLine()) != null) {
            logInfo(output);
        }

        logInfo("============================================ END  TEST " + testName + "==================================");


    }//logRequestHeaders()

    private HttpResponse authenticateWithUser(String url, String userName, String password) throws Exception {

        HttpResponse returnVal = null;

        URI uri = makeUri(url);
        HttpPost postRequest = new HttpPost(uri);
        returnVal = submitUriRequest(postRequest, userName, password, null);

        if (HTTP_STATUS_CODE_OK != returnVal.getStatusLine().getStatusCode()) {
            throw new Exception("Request did not succeed: " + returnVal.getStatusLine().getStatusCode());
        }


        // logRequestHeaders(postRequest, returnVal, " Authenticate with user " + userName);

        return (returnVal);

    }//authenticateWithUser()

    public String getTokenForUser(String url, String userName, String password) throws Exception {

        String returnVal = null;

        HttpResponse response = authenticateWithUser(url, userName, password);
        Header tokenHeader = getHeader(response.getAllHeaders(), GobiiHttpHeaderNames.HEADER_TOKEN);
        returnVal = tokenHeader.getValue();

        if (null == returnVal) {
            LOGGER.error("Unable to get authentication token for user " + userName);
        }

        return returnVal;

    } // getTokenForUser()


    public JsonObject getResponseBody(String url,
                                      String jsonString,
                                      String token) throws Exception {

        JsonObject returnVal = null;

        HttpResponse httpResponse = null;

        URI uri = makeUri(url);

        HttpPost postRequest = new HttpPost(uri);
//        String jsonString = jsonObject.toString();
        StringEntity input = new StringEntity(jsonString);
        postRequest.setEntity(input);

        httpResponse = submitUriRequest(postRequest, "", "", token);

        if (HTTP_STATUS_CODE_OK != httpResponse.getStatusLine().getStatusCode()) {
            throw new Exception("Request did not succeed: " + httpResponse.getStatusLine());
        }


        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader((httpResponse.getEntity().getContent())));

        StringBuilder stringBuilder = new StringBuilder();
        String currentLine = null;
        while ((currentLine = bufferedReader.readLine()) != null) {
            stringBuilder.append(currentLine);
        }


        JsonParser parser = new JsonParser();
        String jsonAsString = stringBuilder.toString();
        returnVal = parser.parse(jsonAsString).getAsJsonObject();

        return returnVal;

    }//accessResource_test

}
