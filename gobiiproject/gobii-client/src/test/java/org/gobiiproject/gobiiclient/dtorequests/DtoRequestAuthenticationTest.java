// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;


import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.core.DtoRequestProcessor;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.EntityParamValues;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.container.EntityPropertyDTO;
import org.gobiiproject.gobiimodel.dto.types.ControllerType;
import org.gobiiproject.gobiimodel.dto.types.ServiceRequestId;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.UUID;
import java.util.stream.Collectors;

public class DtoRequestAuthenticationTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }


    @Test
    public void testSucceedWithOneAuthentication() throws Exception {

        Assert.assertTrue(Authenticator.authenticate());
        String token = ClientContext.getInstance(null, false).getUserToken();

        AnalysisDTO analysisDTORequest = new AnalysisDTO();
        analysisDTORequest.setAnalysisId(1);

        AnalysisDTO analysisDTOResponse =  new DtoRequestProcessor<AnalysisDTO>().process(analysisDTORequest,
                AnalysisDTO.class,
                ControllerType.LOADER,
                ServiceRequestId.URL_ANALYSIS,
                ClientContext.getInstance(null, false).getCurrentCropDomain(),
                ClientContext.getInstance(null, false).getCurrentCropPort(),
                token);

        Assert.assertNotEquals(null, analysisDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(analysisDTOResponse));
        Assert.assertNotEquals(null, analysisDTOResponse.getProgram());

    }

    @Test
    public void testFailOnSecondAuthentication() throws Exception {

        Assert.assertTrue(Authenticator.authenticate());
        String oldToken = ClientContext.getInstance(null, false).getUserToken();

        Assert.assertTrue(Authenticator.authenticate());
        String newToken = ClientContext.getInstance(null, false).getUserToken();

        Assert.assertNotEquals(oldToken, newToken);

        AnalysisDTO analysisDTORequest = new AnalysisDTO();
        analysisDTORequest.setAnalysisId(1);


        AnalysisDTO analysisDTOResponse = new DtoRequestProcessor<AnalysisDTO>().process(analysisDTORequest,
                AnalysisDTO.class,
                ControllerType.LOADER,
                ServiceRequestId.URL_ANALYSIS,
                ClientContext.getInstance(null, false).getCurrentCropDomain(),
                ClientContext.getInstance(null, false).getCurrentCropPort(),
                oldToken);

        Assert.assertNotEquals(null, analysisDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(analysisDTOResponse));
        Assert.assertNotEquals(null, analysisDTOResponse.getProgram());

    }


}
