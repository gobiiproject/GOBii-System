package org.gobiiproject.gobiiclient.core.common;

import org.apache.http.HttpStatus;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiimodel.config.ServerBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Use Jersey annotations for server methods. For more info,
 * see this doc: https://www.javatpoint.com/jax-rs-annotations-example
 */
public class GenericClientContext {

    Logger LOGGER = LoggerFactory.getLogger(GenericClientContext.class);

    private HttpCore httpCore;

    public GenericClientContext(ServerBase serverBase) {

        this.httpCore = new HttpCore(serverBase.getHost(), serverBase.getPort());
    }

    private void validateResult(HttpMethodResult httpMethodResult, RestUri restUri) throws Exception {

        if (httpMethodResult.getResponseCode() == HttpStatus.SC_UNAUTHORIZED) {

            String message = "The http method requires authentication -- changes must be made to the core client infrastructure:"
                    + restUri.getResourcePath();

            LOGGER.error(message);
            throw new Exception(message);
        }
    }


    public HttpMethodResult get(RestUri restUri) throws Exception {

        HttpMethodResult returnVal = this
                .httpCore
                .get(restUri);

        this.validateResult(returnVal, restUri);

        return returnVal;

    }

    public HttpMethodResult post(RestUri restUri,
                                 String body) throws Exception {

        HttpMethodResult returnVal =
                this.httpCore
                        .post(restUri, body);

        this.validateResult(returnVal, restUri);

        return returnVal;
    }

    public HttpMethodResult put(RestUri restUri,
                                String body) throws Exception {

        HttpMethodResult returnVal =
                this.httpCore.put(restUri, body);

        this.validateResult(returnVal, restUri);

        return returnVal;
    }

    public HttpMethodResult patch(RestUri restUri,
                                  String body) throws Exception {

        HttpMethodResult returnVal =
                this.httpCore
                        .patch(restUri, body);

        this.validateResult(returnVal, restUri);

        return returnVal;
    }


    public HttpMethodResult delete(RestUri restUri) throws Exception {

        HttpMethodResult returnVal =
                this.httpCore
                        .delete(restUri);

        this.validateResult(returnVal, restUri);

        return returnVal;
    }

}
