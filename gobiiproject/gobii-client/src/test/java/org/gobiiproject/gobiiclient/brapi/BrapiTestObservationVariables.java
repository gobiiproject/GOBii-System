package org.gobiiproject.gobiiclient.brapi;


import org.apache.commons.lang.ObjectUtils;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiibrapi.calls.studies.observationvariables.BrapiResponseObservationVariablesMaster;
import org.gobiiproject.gobiibrapi.core.responsemodel.BrapiResponseEnvelopeMaster;
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
public class BrapiTestObservationVariables {


    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.authenticate());

    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.deAuthenticate());
    }


    @Test
    public void getObservationVariables() throws Exception {


        RestUri restUriObservationVariables = GobiiClientContext.getInstance(null, false)
                .getUriFactory(GobiiControllerType.BRAPI)
                .childResourceByUriIdParam(GobiiServiceRequestId.URL_STUDIES,
                        GobiiServiceRequestId.URL_OBSERVATION_VARIABLES);
        restUriObservationVariables.setParamValue("id", "1");

        BrapiEnvelopeRestResource<ObjectUtils.Null, BrapiResponseObservationVariablesMaster, BrapiResponseObservationVariablesMaster> brapiEnvelopeRestResource =
                new BrapiEnvelopeRestResource<>(restUriObservationVariables,
                        ObjectUtils.Null.class,
                        BrapiResponseObservationVariablesMaster.class,
                        BrapiResponseObservationVariablesMaster.class);

        BrapiResponseEnvelopeMaster<BrapiResponseObservationVariablesMaster> brapiResponseEnvelopeMaster =
                brapiEnvelopeRestResource.getFromMasterResource();

        BrapiTestResponseStructure.validatateBrapiResponseStructure(brapiResponseEnvelopeMaster.getBrapiMetaData());

        BrapiResponseObservationVariablesMaster brapiResponseObservationVariablesMaster = brapiResponseEnvelopeMaster.getResult();


        Assert.assertNotNull(brapiResponseObservationVariablesMaster.getStudyDbId());
        Assert.assertNotNull(brapiResponseObservationVariablesMaster.getTrialName());
        Assert.assertTrue(brapiResponseObservationVariablesMaster.getData().size() > 0);
        Assert.assertNotNull(brapiResponseObservationVariablesMaster.getData().get(0).getName());

    }
}
