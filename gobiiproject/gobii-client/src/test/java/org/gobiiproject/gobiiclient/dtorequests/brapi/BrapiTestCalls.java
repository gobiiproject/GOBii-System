package org.gobiiproject.gobiiclient.dtorequests.brapi;

import org.apache.commons.lang.ObjectUtils;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.types.ControllerType;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiibrapi.calls.calls.BrapiResponseCallsItem;
import org.gobiiproject.gobiibrapi.core.derived.BrapiResponseEnvelopeList;
import org.gobiiproject.gobiiclient.core.common.ClientContext;
import org.gobiiproject.gobiiclient.core.brapi.BrapiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.core.common.Authenticator;
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
        Assert.assertTrue(Authenticator.authenticate());

    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }


    @Test
    public void getCalls() throws Exception {


        RestUri restUriCalls = ClientContext.getInstance(null, false)
                .getUriFactory(ControllerType.BRAPI)
                .resourceColl(ServiceRequestId.URL_CALLS);

        BrapiEnvelopeRestResource<ObjectUtils.Null,ObjectUtils.Null,BrapiResponseCallsItem> brapiEnvelopeRestResource =
                new BrapiEnvelopeRestResource<>(restUriCalls,
                        ObjectUtils.Null.class,
                        ObjectUtils.Null.class,
                        BrapiResponseCallsItem.class);

        BrapiResponseEnvelopeList<ObjectUtils.Null,BrapiResponseCallsItem> callsResult = brapiEnvelopeRestResource.getFromListResource();

        BrapiTestResponseStructure.validatateBrapiResponseStructure(callsResult.getBrapiMetaData());

        Assert.assertTrue(callsResult.getData().getData().size() > 0 );

    }
}
