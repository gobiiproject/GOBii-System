package org.gobiiproject.gobiiclient.gobii.infrastructure;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;

import org.gobiiproject.gobiimodel.types.GobiiHttpHeaderNames;
import org.junit.Assert;
import org.junit.Test;

import java.net.URI;

/**
 * Created by Phil on 5/31/2016.
 */
public class DtoRequestSansAuthHeadersTest {

    @Test
    public void testNoAuthFails() throws Exception {

        // Aside from crop domain, port, and type, we don't want the tests relying
        // on GobiiClientContext. Authentication will fill the clientContext with config data,
        // and de-authentication will nuke it.
        Assert.assertTrue(GobiiClientContextAuth.authenticate());
        String currentCropDomain = GobiiClientContext.getInstance(null, false).getCurrentCropDomain();
        Integer currentCropPort = GobiiClientContext.getInstance(null, false).getCurrentCropPort();
        String currentCropContextRoot = GobiiClientContext.getInstance(null, false).getCurrentCropContextRoot();
        String currentGobiiCropType = GobiiClientContext.getInstance(null, false).getCurrentClientCropType();
        String url = GobiiServiceRequestId.URL_AUTH.getRequestUrl(currentCropContextRoot, GobiiControllerType.GOBII.getControllerPath());
        Assert.assertTrue(GobiiClientContextAuth.deAuthenticate());


        URI uri = new URIBuilder().setScheme("http")
                .setHost(currentCropDomain)
                .setPort(currentCropPort)
                .setPath(url)
                .build();

        HttpPost postRequest = new HttpPost(uri);

        postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Accept", "application/json");
        // WE ARE _NOT_ ADDING ANY OF THE AUTHENTICATION TOKENS

        postRequest.addHeader(GobiiHttpHeaderNames.HEADER_NAME_GOBII_CROP,
                currentGobiiCropType.toString());


        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(postRequest);

        Integer httpStatusCode = httpResponse.getStatusLine().getStatusCode();
        Assert.assertTrue("Request without authentication headers should have failed; "
                        + "status code received was "
                        + httpStatusCode
                        + "(" + httpResponse.getStatusLine().getReasonPhrase() + ")",
                httpStatusCode.equals(401));
    }
}
