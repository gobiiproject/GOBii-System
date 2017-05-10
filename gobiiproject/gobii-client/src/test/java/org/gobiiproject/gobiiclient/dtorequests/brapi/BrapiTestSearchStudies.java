package org.gobiiproject.gobiiclient.dtorequests.brapi;

import org.apache.commons.lang.ObjectUtils;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.types.ControllerType;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiibrapi.calls.studies.search.BrapiRequestStudiesSearch;
import org.gobiiproject.gobiibrapi.calls.studies.search.BrapiResponseStudiesSearch;
import org.gobiiproject.gobiibrapi.core.responsemodel.BrapiResponseEnvelopeMasterDetail;
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
public class BrapiTestSearchStudies {


    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());

    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }


    @Test
    public void getStudies() throws Exception {


        RestUri restUriStudiesSearch = ClientContext.getInstance(null, false)
                .getUriFactory(ControllerType.BRAPI)
                .resourceColl(ServiceRequestId.URL_STUDIES_SEARCH);

        BrapiRequestStudiesSearch brapiRequestStudiesSearch = new BrapiRequestStudiesSearch();
        brapiRequestStudiesSearch.setStudyType("genotype");

        BrapiEnvelopeRestResource<BrapiRequestStudiesSearch,ObjectUtils.Null,BrapiResponseStudiesSearch> brapiEnvelopeRestResource =
                new BrapiEnvelopeRestResource<BrapiRequestStudiesSearch,ObjectUtils.Null,BrapiResponseStudiesSearch>(restUriStudiesSearch,
                        BrapiRequestStudiesSearch.class,
                        ObjectUtils.Null.class,
                        BrapiResponseStudiesSearch.class);

        BrapiResponseEnvelopeMasterDetail<BrapiResponseStudiesSearch> studiesResult = brapiEnvelopeRestResource.postToListResource(brapiRequestStudiesSearch);

        BrapiTestResponseStructure.validatateBrapiResponseStructure(studiesResult.getBrapiMetaData());

        Assert.assertTrue(studiesResult.getResult().getData().size() > 0);

    }
}
