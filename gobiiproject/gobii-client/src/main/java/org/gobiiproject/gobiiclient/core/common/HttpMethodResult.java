package org.gobiiproject.gobiiclient.core.common;

import com.google.gson.JsonObject;

import java.net.URI;


/**
 * Created by Phil on 9/21/2016.
 */
public class HttpMethodResult {

    int responseCode;

    String reasonPhrase;
    JsonObject payLoad;
    URI uri;

    public int getResponseCode() {
        return responseCode;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public URI getUri() {
        return uri;
    }

    public void setResponse(int responseCode, String reasonPhrase, URI uri) {
        this.responseCode = responseCode;
        this.reasonPhrase = reasonPhrase;
        this.uri = uri;
    }

    public JsonObject getPayLoad() {
        return payLoad;
    }

    public void setPayLoad(JsonObject payLoad) {
        this.payLoad = payLoad;
    }
}
