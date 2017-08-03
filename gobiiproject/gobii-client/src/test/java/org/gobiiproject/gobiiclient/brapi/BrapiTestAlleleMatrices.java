package org.gobiiproject.gobiiclient.brapi;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.http.HttpStatus;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiibrapi.calls.markerprofiles.allelematrices.BrapiResponseAlleleMatrices;
import org.gobiiproject.gobiibrapi.core.common.BrapiStatus;
import org.gobiiproject.gobiibrapi.core.responsemodel.BrapiResponseDataList;
import org.gobiiproject.gobiibrapi.core.responsemodel.BrapiResponseEnvelope;
import org.gobiiproject.gobiibrapi.core.responsemodel.BrapiResponseEnvelopeMasterDetail;
import org.gobiiproject.gobiiclient.core.brapi.BrapiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.core.common.HttpMethodResult;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.gobiiproject.gobiiclient.core.gobii.GobiiTestConfiguration;
import org.gobiiproject.gobiiclient.gobii.Helpers.GlobalPkColl;
import org.gobiiproject.gobiiclient.gobii.dbops.crud.DtoCrudRequestDataSetTest;
import org.gobiiproject.gobiimodel.config.TestExecConfig;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Phil on 12/15/2016.
 */
public class BrapiTestAlleleMatrices {

    private static TestExecConfig testExecConfig = null;



    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.authenticate());
        testExecConfig = new GobiiTestConfiguration().getConfigSettings().getTestExecConfig();


    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
    }


    @Test
    public void getAlleleMatrices() throws Exception {

        List<Integer> dataSetIds = (new GlobalPkColl<DtoCrudRequestDataSetTest>()).getPkVals(DtoCrudRequestDataSetTest.class, GobiiEntityNameType.DATASETS, 10);

        RestUri alleleMatrices = GobiiClientContext.getInstance(null, false)
                .getUriFactory(GobiiControllerType.BRAPI)
                .resourceColl(GobiiServiceRequestId.URL_ALLELE_MATRICES);


        BrapiEnvelopeRestResource<ObjectUtils.Null, ObjectUtils.Null, BrapiResponseAlleleMatrices> brapiEnvelopeRestResource =
                new BrapiEnvelopeRestResource<>(alleleMatrices,
                        ObjectUtils.Null.class,
                        ObjectUtils.Null.class,
                        BrapiResponseAlleleMatrices.class);

        BrapiResponseEnvelopeMasterDetail<BrapiResponseAlleleMatrices> matricesResult = brapiEnvelopeRestResource.getFromListResource();
        BrapiTestResponseStructure.validatateBrapiResponseStructure(matricesResult.getBrapiMetaData());


        Assert.assertTrue("No matrices were returned",
                matricesResult.getResult().getData().size() > 0);

        for (Integer currentDatasetId : dataSetIds) {

            Assert.assertTrue("A dataset was not retrieved " + currentDatasetId.toString(),
                    matricesResult
                            .getResult()
                            .getData()
                            .stream()
                            .filter(i -> i.getMatrixDbId().equals(currentDatasetId.toString()))
                            .collect(Collectors.toList())
                            .size() == 1
            );
        }

    }

}
