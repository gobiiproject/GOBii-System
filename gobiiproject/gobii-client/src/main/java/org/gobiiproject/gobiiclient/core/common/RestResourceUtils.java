package org.gobiiproject.gobiiclient.core.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Phil on 5/13/2016.
 */
public class RestResourceUtils {

    Logger LOGGER = LoggerFactory.getLogger(RestResourceUtils.class);

    private HttpCore httpCore = null;
    private ClientContext clientContext;

    public RestResourceUtils() {
    }


    public ClientContext getClientContext() throws Exception {

        if (!ClientContext.isInitialized()) {
            throw new Exception("Client context is not initialized");
        }

        clientContext = ClientContext.getInstance(null, false);

        return clientContext;

    }

    public HttpCore getHttp() throws Exception {


        if (null == this.httpCore) {
            String host = ClientContext.getInstance(null, false).getCurrentCropDomain();
            Integer port = ClientContext.getInstance(null, false).getCurrentCropPort();
            String clientContextRoot = ClientContext.getInstance(null, false).getCurrentCropContextRoot();
            this.httpCore = new HttpCore(host, port, clientContextRoot);
        }


        return httpCore;
    }



}