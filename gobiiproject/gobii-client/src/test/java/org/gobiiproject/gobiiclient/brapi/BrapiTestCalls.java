package org.gobiiproject.gobiiclient.brapi;

import org.apache.commons.lang.ObjectUtils;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiibrapi.calls.calls.BrapiResponseCalls;
import org.gobiiproject.gobiibrapi.core.responsemodel.BrapiResponseEnvelopeMasterDetail;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.brapi.BrapiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by Phil on 12/15/2016.
 */
public class BrapiTestCalls {


    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.authenticate());

    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.deAuthenticate());
    }


    @Test
    public void getCalls() throws Exception {


        RestUri restUriCalls = GobiiClientContext.getInstance(null, false)
                .getUriFactory(GobiiControllerType.BRAPI)
                .resourceColl(GobiiServiceRequestId.URL_CALLS);

        BrapiEnvelopeRestResource<ObjectUtils.Null,ObjectUtils.Null,BrapiResponseCalls> brapiEnvelopeRestResource =
                new BrapiEnvelopeRestResource<>(restUriCalls,
                        ObjectUtils.Null.class,
                        ObjectUtils.Null.class,
                        BrapiResponseCalls.class);

        BrapiResponseEnvelopeMasterDetail<BrapiResponseCalls> callsResult = brapiEnvelopeRestResource.getFromListResource();

        BrapiTestResponseStructure.validatateBrapiResponseStructure(callsResult.getBrapiMetaData());

        Assert.assertTrue(callsResult.getResult().getData().size() > 0 );

    }
}
