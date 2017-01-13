// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests.infrastructure;


import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.restresources.UriFactory;
import org.gobiiproject.gobiiclient.core.common.ClientContext;
import org.gobiiproject.gobiiapimodel.restresources.ResourceBuilder;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.core.gobii.dtopost.DtoRequestProcessor;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestConfiguration;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.config.CropConfig;
import org.gobiiproject.gobiimodel.dto.container.AnalysisDTO;
import org.gobiiproject.gobiiapimodel.types.ControllerType;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiimodel.headerlesscontainer.ContactDTO;
import org.gobiiproject.gobiimodel.types.SystemUserDetail;
import org.gobiiproject.gobiimodel.types.SystemUserNames;
import org.gobiiproject.gobiimodel.types.SystemUsers;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URL;
import java.util.List;

public class DtoRequestAuthenticationTest {


    @BeforeClass
    public static void setUpClass() throws Exception {
//        Assert.assertTrue(Authenticator.authenticate());
    }

    //
    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }


    @Test
    public void testSucceedWithOneAuthentication() throws Exception {

        Assert.assertTrue(Authenticator.authenticate());
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


        String url = ResourceBuilder.getRequestUrl(ControllerType.LOADER,
                ClientContext.getInstance(null, false).getCurrentCropContextRoot(),
                ServiceRequestId.URL_ANALYSIS);

        DtoRequestProcessor<AnalysisDTO> dtoDtoRequestProcessor = new DtoRequestProcessor<>();



    }

    private String makeUrl(CropConfig cropConfig) throws Exception {

//        String returnVal = "http://"
//                + cropConfig.getServiceDomain()
//                + ":"
//                + cropConfig.getServicePort().toString()
//                + "/"
//

        String returnVal;

        URL url = new URL("http",
                cropConfig.getServiceDomain(),
                cropConfig.getServicePort(),
                cropConfig.getServiceAppRoot());

        returnVal = url.toString();
        return returnVal;
    }

    @Test
    public void testSwitchToSecondServer() throws Exception {

        TestConfiguration testConfiguration = new TestConfiguration();
        List<CropConfig> activeCropConfigs = testConfiguration.getConfigSettings().getActiveCropConfigs();
        if (activeCropConfigs.size() > 1) {
            // http://localhost:8282/gobii-dev/


            // ****************** FIRST LOGIN
            CropConfig cropConfigOne = activeCropConfigs.get(0);
            String serviceUrlOne = makeUrl(cropConfigOne);
            String cropIdOne = ClientContext.getInstance(serviceUrlOne, true).getDefaultCropType();

            ClientContext.getInstance(null, false)
                    .setCurrentClientCrop(cropIdOne);

            SystemUsers systemUsers = new SystemUsers();
            SystemUserDetail userDetail = systemUsers.getDetail(SystemUserNames.USER_READER.toString());
            ClientContext.getInstance(null, false).login(userDetail.getUserName(), userDetail.getPassword());

            // ****************** SECOND LOGIN
            CropConfig cropConfigTwo = activeCropConfigs.get(1);
            String serviceUrlTwo = makeUrl(cropConfigTwo);


            ClientContext.resetConfiguration();
//            String cropIdTwo = ClientContext.getInstance(serviceUrlTwo,true).getDefaultCropType();
            ClientContext.getInstance(serviceUrlTwo, true);
            String cropIdTwo = cropConfigTwo.getGobiiCropType();

            ClientContext.getInstance(null, false)
                    .setCurrentClientCrop(cropIdTwo);

            ClientContext.getInstance(null, false).login(userDetail.getUserName(), userDetail.getPassword());

            ClientContext.getInstance(null, false).setCurrentClientCrop(cropIdTwo);


            // do contacts request with crop two context root
            String currentCropContextRoot = ClientContext.getInstance(null, false).getCurrentCropContextRoot();
            UriFactory uriFactory = new UriFactory(currentCropContextRoot);
            RestUri restUriContact = uriFactory
                    .resourceByUriIdParam(ServiceRequestId.URL_CONTACTS);
            restUriContact.setParamValue("id", "6");
            GobiiEnvelopeRestResource<ContactDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriContact);
            PayloadEnvelope<ContactDTO> resultEnvelope = gobiiEnvelopeRestResource
                    .get(ContactDTO.class);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));


            // now set current root to crop onej -- this will cause an error because the
            // uriFactory still has crop two's context root.
            ClientContext.getInstance(null, false).setCurrentClientCrop(cropIdOne);
            restUriContact = uriFactory
                    .resourceByUriIdParam(ServiceRequestId.URL_CONTACTS);
            restUriContact.setParamValue("id", "6");
            gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriContact);
            resultEnvelope = gobiiEnvelopeRestResource
                    .get(ContactDTO.class);

            Assert.assertTrue("Method with incorrectly configured uriFactory should have failed with 404",
                    resultEnvelope
                            .getHeader()
                            .getStatus()
                            .getStatusMessages()
                            .stream()
                            .filter(m -> m.getMessage().contains("failed with status code 404"))
                            .count() == 1
            );



            // create a new factory with correct context root and re-do the request
            // this should now work
            String currentCropContextRootForCropOne = ClientContext.getInstance(null, false).getCropContextRoot(cropIdOne);
            uriFactory = new UriFactory(currentCropContextRootForCropOne);
            restUriContact = uriFactory
                    .resourceByUriIdParam(ServiceRequestId.URL_CONTACTS);
            restUriContact.setParamValue("id", "6");
            gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriContact);
            resultEnvelope = gobiiEnvelopeRestResource
                    .get(ContactDTO.class);


            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));


        }

    }


}
