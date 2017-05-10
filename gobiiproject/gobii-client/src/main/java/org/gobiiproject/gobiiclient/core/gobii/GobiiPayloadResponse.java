package org.gobiiproject.gobiiclient.core.gobii;

import org.apache.http.HttpStatus;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiimodel.types.RestMethodTypes;
import org.gobiiproject.gobiiclient.core.common.HttpMethodResult;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.utils.LineUtils;

/**
 * Created by Phil on 10/19/2016.
 */
public class GobiiPayloadResponse<T> {

    private RestUri restUri;
    public GobiiPayloadResponse(RestUri restUri) {
        this.restUri = restUri;
    }

    private String makeMessageFromHttpResult(String method,
                                             HttpMethodResult httpMethodResult) throws Exception {

        return (makeMessageFromHttpResult(method, httpMethodResult, null));
    }


    private String makeMessageFromHttpResult(String method,
                                             HttpMethodResult httpMethodResult,
                                             String additionalReason) throws Exception {
        return method.toUpperCase()
                + " method on "
                + httpMethodResult.getUri().toString()
                + " failed with status code "
                + Integer.toString(httpMethodResult.getResponseCode())
                + ": "
                + httpMethodResult.getReasonPhrase()
                + (!LineUtils.isNullOrEmpty(additionalReason) ? (": " + additionalReason) : "");
    }

    public PayloadEnvelope<T> getPayloadFromResponse(Class<T> dtoType,
                                                     RestMethodTypes restMethodType,
                                                     int httpSuccessCode,
                                                     HttpMethodResult httpMethodResult) throws Exception {

        PayloadEnvelope<T> returnVal = new PayloadEnvelope<>();

        if (HttpStatus.SC_NOT_FOUND != httpMethodResult.getResponseCode()) {

            if (HttpStatus.SC_BAD_REQUEST != httpMethodResult.getResponseCode()) {

                if(HttpStatus.SC_METHOD_NOT_ALLOWED != httpMethodResult.getResponseCode()) {

                    if( HttpStatus.SC_UNAUTHORIZED != httpMethodResult.getResponseCode() ) {

                        try {

                            returnVal = new PayloadEnvelope<T>()
                                    .fromJson(httpMethodResult.getPayLoad(),
                                            dtoType);

                            // it's possible that you can have codes other than success, and still have valid response
                            // body
                            if (httpMethodResult.getResponseCode() != httpSuccessCode) {

                                String message = makeMessageFromHttpResult(restMethodType.toString(), httpMethodResult);

                                GobiiStatusLevel gobiiStatusLevel = returnVal.getHeader().getStatus().isSucceeded() ?
                                        GobiiStatusLevel.WARNING :
                                        GobiiStatusLevel.ERROR;

                                returnVal.getHeader()
                                        .getStatus()
                                        .addStatusMessage(gobiiStatusLevel,
                                                message);
                            }


                        } catch (Exception e) {
                            returnVal.getHeader().getStatus().addException(e);
                        }

                    } else {
                        returnVal.getHeader()
                                .getStatus()
                                .addStatusMessage(GobiiStatusLevel.ERROR,
                                        makeMessageFromHttpResult(restMethodType.toString(),
                                                httpMethodResult,
                                                "Access is not authorized: " + restMethodType.toString()));
                    }
                } else {
                    returnVal.getHeader()
                            .getStatus()
                            .addStatusMessage(GobiiStatusLevel.ERROR,
                                    makeMessageFromHttpResult(restMethodType.toString(),
                                            httpMethodResult,
                                            "Unauthorized: the client may needs to re-authenticate"));
                }

            } else {

                returnVal.getHeader()
                        .getStatus()
                        .addStatusMessage(GobiiStatusLevel.ERROR,
                                makeMessageFromHttpResult(restMethodType.toString(),
                                        httpMethodResult,
                                        "Request format error or one or more client DTOs may be out of date with those of the server"));
            }

        } else {
            returnVal.getHeader()
                    .getStatus()
                    .addStatusMessage(GobiiStatusLevel.ERROR,
                            makeMessageFromHttpResult(restMethodType.toString(),
                                    httpMethodResult,
                                    "The specified URI may be unknown to the server"));
        }

        return returnVal;
    }

}
