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
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.core.common.Authenticator;
import org.gobiiproject.gobiiclient.core.common.TestConfiguration;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.config.CropConfig;
import org.gobiiproject.gobiimodel.headerlesscontainer.AnalysisDTO;
import org.gobiiproject.gobiiapimodel.types.ControllerType;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiimodel.headerlesscontainer.ContactDTO;
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


        String url = ServiceRequestId.URL_ANALYSIS.getRequestUrl(ClientContext.getInstance(null, false).getCurrentCropContextRoot(),
                ControllerType.GOBII);

//        DtoRequestProcessor<AnalysisDTO> dtoDtoRequestProcessor = new DtoRequestProcessor<>();


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

            Assert.assertNotNull("Could not get testexecconfig", Authenticator.getTestExecConfig());

            String testUser = Authenticator.getTestExecConfig().getLdapUserForUnitTest();
            String testPassword = Authenticator.getTestExecConfig().getLdapPasswordForUnitTest();


            ClientContext.getInstance(null, false).login(testUser, testPassword);

            // ****************** SECOND LOGIN
            CropConfig cropConfigTwo = activeCropConfigs.get(1);
            String serviceUrlTwo = makeUrl(cropConfigTwo);


            ClientContext.resetConfiguration();
//            String cropIdTwo = ClientContext.getInstance(serviceUrlTwo,true).getDefaultCropType();
            ClientContext.getInstance(serviceUrlTwo, true);
            String cropIdTwo = cropConfigTwo.getGobiiCropType();

            ClientContext.getInstance(null, false)
                    .setCurrentClientCrop(cropIdTwo);

            ClientContext.getInstance(null, false).login(testUser, testPassword);

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
            ClientContext.getInstance(null, false).login(testUser, testPassword);
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

    @Test
    public void testSwitchToSecondCrop () throws Exception {


        // these steps require physical access to a config file. Other clients do not have
        // this access. So we need to make sure that, aside from retrieving te config URL nad the
        // username/password, the remainder of the test does not consume the testConfiguration.
        TestConfiguration testConfiguration = new TestConfiguration();
        String initialConfigUrl = testConfiguration.getConfigSettings().getTestExecConfig().getInitialConfigUrl();
        String testUser = testConfiguration.getConfigSettings().getTestExecConfig().getLdapUserForUnitTest();
        String testPassword = testConfiguration.getConfigSettings().getTestExecConfig().getLdapPasswordForUnitTest();

        ClientContext.getInstance(initialConfigUrl, true);
        List<String> activeCrops = ClientContext.getInstance(null, false).getCropTypeTypes();
        if (activeCrops.size() > 1) {

            String cropIdOne = activeCrops.get(0);
            String cropIdTwo = activeCrops.get(1);

            // ****************** FIRST LOGIN
            ClientContext.getInstance(null, false)
                    .setCurrentClientCrop(cropIdOne);
            ClientContext.getInstance(null, false).login(testUser, testPassword);
            Assert.assertNotNull("Authentication with first crop failed: " + cropIdOne,
                    ClientContext.getInstance(null,true).getUserToken());

            String cropOneToken = ClientContext.getInstance(null,true).getUserToken();

            // ****************** SECOND LOGIN

            ClientContext.getInstance(null, false)
                    .setCurrentClientCrop(cropIdTwo);
            ClientContext.getInstance(null, false).login(testUser, testPassword);
            Assert.assertNotNull("Authentication with second crop failed: " + cropIdTwo,
                    ClientContext.getInstance(null,true).getUserToken());

            String cropTwoToken = ClientContext.getInstance(null,true).getUserToken();

            Assert.assertFalse("The tokens for the two authentications should be different: " + cropOneToken + "," + cropTwoToken,
                    cropOneToken.equals(cropTwoToken));

        }
    }

}
