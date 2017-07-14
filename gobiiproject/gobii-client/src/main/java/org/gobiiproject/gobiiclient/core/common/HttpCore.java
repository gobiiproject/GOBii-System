package org.gobiiproject.gobiiclient.core.common;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.gobiiproject.gobiiapimodel.restresources.ResourceParam;
import org.gobiiproject.gobiimodel.types.RestMethodTypes;
import org.gobiiproject.gobiimodel.types.GobiiHttpHeaderNames;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * Created by Phil on 5/13/2016.
 */
public class HttpCore {

    private String host = null;
    private Integer port = null;
    private String cropId;
    private boolean logJson = false;


    public HttpCore(String host,
                    Integer port,
                    String cropId) {

        this.host = host;
        this.port = port;
        this.cropId = cropId;
    }


    Logger LOGGER = LoggerFactory.getLogger(HttpCore.class);


    URIBuilder getBaseBuilder() throws Exception {
        return (new URIBuilder().setScheme("http")
                .setHost(host)
                .setPort(port));
    }

    private URI makeUri(RestUri restUri) throws Exception {

        URI returnVal;

        URIBuilder baseBuilder = getBaseBuilder()
                .setPath(restUri.makeUrl());

        for (ResourceParam currentParam : restUri.getRequestParams()) {
            baseBuilder.addParameter(currentParam.getName(), currentParam.getValue());
        }

        returnVal = baseBuilder.build();

        return (returnVal);

    }

    private HttpResponse submitUriRequest(HttpUriRequest httpUriRequest,
                                          String userName,
                                          String password,
                                          String token) throws Exception {

        httpUriRequest.addHeader("Content-Type", "application/json");
        httpUriRequest.addHeader("Accept", "application/json");

        if ((null != token) && (false == token.isEmpty())) {
            httpUriRequest.addHeader(GobiiHttpHeaderNames.HEADER_TOKEN, token);
        } else {
            httpUriRequest.addHeader(GobiiHttpHeaderNames.HEADER_USERNAME, userName);
            httpUriRequest.addHeader(GobiiHttpHeaderNames.HEADER_PASSWORD, password);
        }


        if (!LineUtils.isNullOrEmpty(this.cropId)) {
            httpUriRequest.addHeader(GobiiHttpHeaderNames.HEADER_GOBII_CROP, this.cropId);
        }

        return (HttpClientBuilder.create().build().execute(httpUriRequest));

    }// submitUriRequest()


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

    }// getHeader()

    private HttpResponse authenticateWithUser(URI uri, String userName, String password) throws Exception {

        HttpResponse returnVal = null;

        HttpPost postRequest = new HttpPost(uri);
        this.setHttpBody(postRequest, "empty");
        returnVal = submitUriRequest(postRequest, userName, password, null);

        if (HttpStatus.SC_OK != returnVal.getStatusLine().getStatusCode()) {
            throw new Exception("Request did not succeed with http status code "
                    + returnVal.getStatusLine().getStatusCode()
                    + "; the url is: "
                    + uri.toString());
        }


        return (returnVal);

    }//authenticateWithUser()

    public String getTokenForUser(RestUri restUri, String userName, String password) throws Exception {

        String returnVal = null;

        URI uri = makeUri(restUri);
        HttpResponse response = authenticateWithUser(uri, userName, password);
        Header tokenHeader = getHeader(response.getAllHeaders(), GobiiHttpHeaderNames.HEADER_TOKEN);
        returnVal = tokenHeader.getValue();

        if (null == returnVal) {
            LOGGER.error("Unable to get authentication token for user " + userName);
        }

        return returnVal;

    } // getTokenForUser()


    private HttpMethodResult submitHttpMethod(HttpRequestBase httpRequestBase,
                                              RestUri restUri,
                                              String token) throws Exception {

        HttpMethodResult returnVal = new HttpMethodResult();

        HttpResponse httpResponse;

        URI uri = makeUri(restUri);
        httpRequestBase.setURI(uri);


        httpResponse = submitUriRequest(httpRequestBase, "", "", token);

        int responseCode = httpResponse.getStatusLine().getStatusCode();
        String reasonPhrase = httpResponse.getStatusLine().getReasonPhrase();
        returnVal.setResponse(responseCode, reasonPhrase, uri);


        if (HttpStatus.SC_NOT_FOUND != responseCode &&
                HttpStatus.SC_BAD_REQUEST != responseCode &&
                HttpStatus.SC_METHOD_NOT_ALLOWED != responseCode &&
                HttpStatus.SC_UNAUTHORIZED != responseCode) {

            InputStream inputStream = httpResponse.getEntity().getContent();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream));


            StringBuilder stringBuilder = new StringBuilder();
            String currentLine = null;
            while ((currentLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(currentLine);
            }


            JsonParser parser = new JsonParser();

            String jsonAsString = stringBuilder.toString();

            JsonObject jsonObject = parser.parse(jsonAsString).getAsJsonObject();

            returnVal.setPayLoad(jsonObject);
        }


        ///returnVal.setPayLoad(getJsonFromInputStream(httpResponse.getEntity().getContent()));

        return returnVal;
    }

    private void setHttpBody(HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase,
                             String body) throws Exception {

        if (!LineUtils.isNullOrEmpty(body)) {
            StringEntity input = new StringEntity(body);
            httpEntityEnclosingRequestBase.setEntity(input);
        }

    }

    private void logRequest(RestMethodTypes restMethodType,
                            RestUri restUri,
                            String body,
                            HttpMethodResult httpMethodResult) throws Exception {

        if (logJson) {

            System.out.println("=========method: " + restMethodType.toString() + " on resource: " + restUri.makeUrl());

            if (!LineUtils.isNullOrEmpty(body)) {

                System.out.println("body:");
                System.out.println(body);
            }

            System.out.println("Response: ");

            if (httpMethodResult.getPayLoad() != null) {
                System.out.println(httpMethodResult.getPayLoad().toString());
            } else {
                System.out.println("Null payload");
            }

            System.out.println();
            System.out.println();
        }

    }

    public HttpMethodResult get(RestUri restUri,
                                String token) throws Exception {

        HttpMethodResult returnVal = this.submitHttpMethod(new HttpGet(), restUri, token);
        this.logRequest(RestMethodTypes.GET, restUri, null, returnVal);
        return returnVal;

    }

    public HttpMethodResult post(RestUri restUri,
                                 String body,
                                 String token) throws Exception {

        HttpMethodResult returnVal;

        HttpPost httpPost = new HttpPost();
        this.setHttpBody(httpPost, body);
        returnVal = this.submitHttpMethod(httpPost, restUri, token);
        this.logRequest(RestMethodTypes.POST, restUri, body, returnVal);

        return returnVal;
    }

    public HttpMethodResult put(RestUri restUri,
                                String body,
                                String token) throws Exception {

        HttpMethodResult returnVal;
        HttpPut httpPut = new HttpPut();
        this.setHttpBody(httpPut, body);
        returnVal = this.submitHttpMethod(httpPut, restUri, token);
        this.logRequest(RestMethodTypes.PUT, restUri, body, returnVal);
        return returnVal;
    }

    public HttpMethodResult patch(RestUri restUri,
                                  String body,
                                  String token) throws Exception {

        HttpPatch httpPatch = new HttpPatch();
        this.setHttpBody(httpPatch, body);
        return this.submitHttpMethod(httpPatch, restUri, token);
    }


    public HttpMethodResult delete(RestUri restUri,
                                   String token) throws Exception {

        return this.submitHttpMethod(new HttpDelete(), restUri, token);
    }

}
