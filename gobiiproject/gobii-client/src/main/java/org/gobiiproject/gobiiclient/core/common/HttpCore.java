package org.gobiiproject.gobiiclient.core.common;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
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
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.gobiiproject.gobiiapimodel.restresources.common.ResourceParam;
import org.gobiiproject.gobiimodel.types.RestMethodTypes;
import org.gobiiproject.gobiimodel.types.GobiiHttpHeaderNames;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class provides generic HTTP rest-oriented client functionality.
 * For example, it takes a plain string for a post and converts it to
 * an HTTP entity as the POST body. It is vital that this class not contain
 * any GOBII specific functionality. For example, serialization and deserialization
 * of GOBII POJOs is handled by another class that consumes this class. We
 * want this class to be generic so that it can serve as the workhorse for
 * all client operations performed by GOBII clients with arbitrary web services,
 * not just GOBII ones.
 */
public class HttpCore {

    private String host = null;
    private Integer port = null;
    private boolean logJson = false;


    public HttpCore(String host,
                    Integer port) {

        this.host = host;
        this.port = port;
    }


    Logger LOGGER = LoggerFactory.getLogger(HttpCore.class);


    String token = null;

    public String getToken() {
        return token;
    }

    public String setToken(String token) {
        return this.token = token;
    }

    URIBuilder getBaseBuilder() throws Exception {
        return (new URIBuilder().setScheme("http")
                .setHost(host)
                .setPort(port));
    }

    private URI makeUri(RestUri restUri) throws Exception {

        URI returnVal;

        URIBuilder baseBuilder = getBaseBuilder()
                .setPath(restUri.makeUrlPath());

        for (ResourceParam currentParam : restUri.getRequestParams()) {
            baseBuilder.addParameter(currentParam.getName(), currentParam.getValue());
        }

        returnVal = baseBuilder.build();

        return (returnVal);

    }

    private void setAuthenticationHeaders(HttpUriRequest httpUriRequest,
                                          String userName,
                                          String password) {

        httpUriRequest.addHeader(GobiiHttpHeaderNames.HEADER_NAME_CONTENT_TYPE,
                MediaType.APPLICATION_JSON);

        httpUriRequest.addHeader(GobiiHttpHeaderNames.HEADER_NAME_ACCEPT,
                MediaType.APPLICATION_JSON);

        httpUriRequest.addHeader(GobiiHttpHeaderNames.HEADER_NAME_USERNAME, userName);

        httpUriRequest.addHeader(GobiiHttpHeaderNames.HEADER_NAME_PASSWORD, password);
    }

    private void setTokenHeader(HttpUriRequest httpUriRequest) {

        httpUriRequest.addHeader(GobiiHttpHeaderNames.HEADER_NAME_TOKEN, this.token);

    }

    private HttpResponse submitUriRequest(HttpUriRequest httpUriRequest, Map<String, String> headers) throws Exception {


        if (headers != null) {
            Iterator it = headers.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry currentPair = (Map.Entry) it.next();
                httpUriRequest
                        .addHeader(currentPair.getKey().toString(),
                                currentPair.getValue().toString());
            }
        }

        HttpResponse returnVal = (HttpClientBuilder.create().build().execute(httpUriRequest));

        return returnVal;

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


    public HttpMethodResult authenticateWithUser(RestUri restUri, String userName, String password) throws Exception {

        URI uri = makeUri(restUri);
        HttpPost postRequest = new HttpPost(uri);
        this.setHttpBody(postRequest, "empty");
        this.setAuthenticationHeaders(postRequest, userName, password);

        // content headers are already set in setAuthenticationHeaders()
        HttpResponse httpResponse = this.submitUriRequest(postRequest, new HashMap<>());

        HttpMethodResult returnVal = new HttpMethodResult(httpResponse);

        if (HttpStatus.SC_OK == returnVal.getResponseCode()) {
            Header tokenHeader = getHeader(httpResponse.getAllHeaders(), GobiiHttpHeaderNames.HEADER_NAME_TOKEN);
            if (tokenHeader != null) {
                this.token = tokenHeader.getValue();
            }
        } else {
            // if we got SC_FORBIDDEN, we're expecting a meaingful response body from the server
            if (HttpStatus.SC_FORBIDDEN == returnVal.getResponseCode()) {
                returnVal.setMessage(EntityUtils.toString(httpResponse.getEntity()));
            }
        }

        return (returnVal);

    }//authenticateWithUser()

    /***
     * The strategy of extracting the entire contents of streamed file to a string and
     * then writing that string to a file is probably not going to work well when we are
     * dealing with large files, because you basically have to copy the content twice.
     * For now, this approach simplifies the matter and enables us to use pre-existing
     * library utilities in order to either stream a file or write the plain-text body
     * of a response to a file.
     *
     * @param content The String content that will be written to the file
     * @param httpMethodResult The method result to which the file name will be written
     * @param restUri The RestUri that should contain the destination file name
     * @throws Exception
     */
    private void makeDownloadedFile(String content,
                                    HttpMethodResult httpMethodResult,
                                    RestUri restUri) throws Exception {


        String destinationFqpn = restUri.getDestinationFqpn();
        File outputFile = new File(destinationFqpn);
        FileUtils.writeStringToFile(outputFile, content);
        httpMethodResult.setFileName(destinationFqpn);

    }

    private HttpMethodResult submitHttpMethod(HttpRequestBase httpRequestBase,
                                              RestUri restUri) throws Exception {

        HttpMethodResult returnVal = null;


        HttpResponse httpResponse;

        URI uri = makeUri(restUri);
        httpRequestBase.setURI(uri);


        this.setTokenHeader(httpRequestBase);
        httpResponse = submitUriRequest(httpRequestBase, restUri.getHttpHeaders());
        returnVal = new HttpMethodResult(httpResponse);
        returnVal.setUri(uri);


        // this really needs to be changed so that it allows only the
        // known-good succeess codes. The problem is that it is not so simple
        // There can actually be many ways to succeed. For example,
        // SC_INTERNAL_SERVER_ERROR has to be allowed to go through because
        // we actually want server exceptions to buble up to the client so he
        // knows something bad happened. indeed, a significant number of our
        // unit tests verify the _presence_ of such exceptions as a success
        // criterion.
        // What we can do universally in this condition is is to verify that
        // a content type was set. The absence of a content type is a sure sign that the server
        // barfed in an unpredicted way. So in this case we report the
        // condition as an exception with the reason code and
        // so forth. I would also observe that there are several different classes that are formulating
        // error based on these response codes. That really needs to be enapsulated.
        if (HttpStatus.SC_NOT_FOUND != returnVal.getResponseCode() &&
                HttpStatus.SC_BAD_REQUEST != returnVal.getResponseCode() &&
                HttpStatus.SC_METHOD_NOT_ALLOWED != returnVal.getResponseCode() &&
                HttpStatus.SC_UNAUTHORIZED != returnVal.getResponseCode() &&
                HttpStatus.SC_NOT_ACCEPTABLE != returnVal.getResponseCode()) {


            String contentType = null; // default
            Header headers[] = httpResponse.getHeaders(GobiiHttpHeaderNames.HEADER_NAME_CONTENT_TYPE);
            if (headers.length > 0) {
                contentType = headers[0].getValue();
            }

            String resultAsString = EntityUtils.toString(httpResponse.getEntity());

            if (contentType != null) {

                if (contentType.contains(MediaType.APPLICATION_JSON)) {

                    JsonParser parser = new JsonParser();
                    JsonObject jsonObject = parser.parse(resultAsString).getAsJsonObject();
                    returnVal.setJsonPayload(jsonObject);

                } else if (contentType.contains(MediaType.TEXT_PLAIN)) {

                    returnVal.setPlainPayload(resultAsString);
                    if (!LineUtils.isNullOrEmpty(restUri.getDestinationFqpn())) {
                        this.makeDownloadedFile(resultAsString, returnVal, restUri);
                    }

                } else if (contentType.contains(MediaType.APPLICATION_OCTET_STREAM)
                        || contentType.contains(MediaType.MULTIPART_FORM_DATA)) {

                    if (!LineUtils.isNullOrEmpty(restUri.getDestinationFqpn())) {
                        this.makeDownloadedFile(resultAsString, returnVal, restUri);
                    }
                }

            } else {
                String message = "Unable to process response because no content type was set: "
                        + httpRequestBase.getMethod()
                        + ": " + uri.toString() + " "
                        + returnVal.getResponseCode()
                        + " (" + returnVal.getReasonPhrase() + ")";
                LOGGER.error(message);
                throw new Exception(message);
            }

        } else {
            returnVal.setPlainPayload(EntityUtils.toString(httpResponse.getEntity()));
        }

        ///returnVal.setJsonPayload(getJsonFromInputStream(httpResponse.getEntity().getContent()));

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

            System.out.println("=========method: " + restMethodType.toString() + " on resource: " + restUri.makeUrlPath());

            if (!LineUtils.isNullOrEmpty(body)) {

                System.out.println("body:");
                System.out.println(body);
            }

            System.out.println("Response: ");

            if (httpMethodResult.getJsonPayload() != null) {
                System.out.println(httpMethodResult.getJsonPayload().toString());
            } else {
                System.out.println("Null payload");
            }

            System.out.println();
            System.out.println();
        }

    }

    public HttpMethodResult get(RestUri restUri) throws Exception {

        HttpMethodResult returnVal = this.submitHttpMethod(new HttpGet(), restUri);
        this.logRequest(RestMethodTypes.GET, restUri, null, returnVal);
        return returnVal;

    }

    public HttpMethodResult post(RestUri restUri,
                                 String body) throws Exception {

        HttpMethodResult returnVal;

        HttpPost httpPost = new HttpPost();
        this.setHttpBody(httpPost, body);
        returnVal = this.submitHttpMethod(httpPost, restUri);
        this.logRequest(RestMethodTypes.POST, restUri, body, returnVal);

        return returnVal;
    }

    public HttpMethodResult upload(RestUri restUri,
                                   File file) throws Exception {

        HttpMethodResult returnVal;

        // remove the default headers
        restUri.getHttpHeaders().remove(GobiiHttpHeaderNames.HEADER_NAME_CONTENT_TYPE);
        restUri.getHttpHeaders().remove(GobiiHttpHeaderNames.HEADER_NAME_ACCEPT);

        HttpPost httpPost = new HttpPost();

        // getting this to work required a bit of dabbling in the Dark Arts
        // thank goodness for the kindness of strangers:
        // https://stackoverflow.com/questions/1378920/how-can-i-make-a-multipart-form-data-post-request-using-java
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("field1", "yes", ContentType.TEXT_PLAIN);
        builder.addBinaryBody(
                "file",
                new FileInputStream(file),
                ContentType.APPLICATION_OCTET_STREAM,
                file.getName()
        );
        HttpEntity multipart = builder.build();
        httpPost.setEntity(multipart);

        returnVal = this.submitHttpMethod(httpPost, restUri);
        this.logRequest(RestMethodTypes.POST, restUri, file.getAbsolutePath(), returnVal);

        return returnVal;
    }

    public HttpMethodResult put(RestUri restUri,
                                String body) throws Exception {

        HttpMethodResult returnVal;
        HttpPut httpPut = new HttpPut();
        this.setHttpBody(httpPut, body);
        returnVal = this.submitHttpMethod(httpPut, restUri);
        this.logRequest(RestMethodTypes.PUT, restUri, body, returnVal);
        return returnVal;
    }

    public HttpMethodResult patch(RestUri restUri,
                                  String body) throws Exception {

        HttpPatch httpPatch = new HttpPatch();
        this.setHttpBody(httpPatch, body);
        return this.submitHttpMethod(httpPatch, restUri);
    }


    public HttpMethodResult delete(RestUri restUri) throws Exception {

        return this.submitHttpMethod(new HttpDelete(), restUri);
    }

}
