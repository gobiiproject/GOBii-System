package org.gobiiproject.gobiiclient.core.common;

import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;

import java.net.URI;


/**
 * Created by Phil on 9/21/2016.
 */
public class HttpMethodResult {


    HttpMethodResult(HttpResponse httpResponse) {
        this.responseCode = httpResponse.getStatusLine().getStatusCode();
        this.reasonPhrase = httpResponse.getStatusLine().getReasonPhrase();
    }

    int responseCode;
    String reasonPhrase;
    JsonObject jsonPayload;
    String plainPayload = null;
    URI uri;
    String fileName = null;
    String message;

    public int getResponseCode() {
        return responseCode;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public JsonObject getJsonPayload() {
        return jsonPayload;
    }

    public void setJsonPayload(JsonObject jsonPayload) {
        this.jsonPayload = jsonPayload;
    }

    public String getPlainPayload() {
        return plainPayload;
    }

    public void setPlainPayload(String plainPayload) {
        this.plainPayload = plainPayload;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
