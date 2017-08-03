package org.gobiiproject.gobiiclient.brapi;

import org.apache.commons.lang.ObjectUtils;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiibrapi.calls.studies.search.BrapiRequestStudiesSearch;
import org.gobiiproject.gobiibrapi.calls.studies.search.BrapiResponseStudiesSearch;
import org.gobiiproject.gobiibrapi.calls.studies.search.BrapiResponseStudiesSearchItem;
import org.gobiiproject.gobiibrapi.core.responsemodel.BrapiResponseEnvelopeMasterDetail;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.brapi.BrapiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.gobiiproject.gobiiclient.gobii.Helpers.GlobalPkColl;
import org.gobiiproject.gobiiclient.gobii.dbops.crud.DtoCrudRequestProjectTest;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Phil on 12/15/2016.
 */
public class BrapiTestSearchStudies {


    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.authenticate());

    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.deAuthenticate());
    }


    @Test
    public void getStudies() throws Exception {

        List<Integer> projectIds = (new GlobalPkColl<DtoCrudRequestProjectTest>()).getPkVals(DtoCrudRequestProjectTest.class, GobiiEntityNameType.PROJECTS, 10);

        RestUri restUriStudiesSearch = GobiiClientContext.getInstance(null, false)
                .getUriFactory(GobiiControllerType.BRAPI)
                .resourceColl(GobiiServiceRequestId.URL_STUDIES_SEARCH);

        BrapiRequestStudiesSearch brapiRequestStudiesSearch = new BrapiRequestStudiesSearch();
        brapiRequestStudiesSearch.setStudyType("genotype");

        BrapiEnvelopeRestResource<BrapiRequestStudiesSearch, ObjectUtils.Null, BrapiResponseStudiesSearch> brapiEnvelopeRestResource =
                new BrapiEnvelopeRestResource<BrapiRequestStudiesSearch, ObjectUtils.Null, BrapiResponseStudiesSearch>(restUriStudiesSearch,
                        BrapiRequestStudiesSearch.class,
                        ObjectUtils.Null.class,
                        BrapiResponseStudiesSearch.class);

        BrapiResponseEnvelopeMasterDetail<BrapiResponseStudiesSearch> studiesResult = brapiEnvelopeRestResource.postToListResource(brapiRequestStudiesSearch);

        BrapiTestResponseStructure.validatateBrapiResponseStructure(studiesResult.getBrapiMetaData());

        Assert.assertTrue(studiesResult.getResult().getData().size() > 0);

        for (Integer currentProjectId : projectIds) {

            Assert.assertTrue("A project was not retrieved " + currentProjectId.toString(),
                    studiesResult
                            .getResult()
                            .getData()
                            .stream()
                            .filter(i -> i.getStudyDbId().equals(currentProjectId.toString()))
                            .collect(Collectors.toList())
                            .size() == 1
            );
        }



    }
}
