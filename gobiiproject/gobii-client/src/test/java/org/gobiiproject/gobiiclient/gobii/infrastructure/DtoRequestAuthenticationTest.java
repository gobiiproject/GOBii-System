// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.gobii.infrastructure;


import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.gobii.GobiiUriFactory;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.core.gobii.GobiiTestConfiguration;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiCropConfig;
import org.gobiiproject.gobiimodel.headerlesscontainer.AnalysisDTO;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiimodel.headerlesscontainer.ContactDTO;
import org.gobiiproject.gobiimodel.types.GobiiAutoLoginType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URL;
import java.util.List;

public class DtoRequestAuthenticationTest {


    @BeforeClass
    public static void setUpClass() throws Exception {
//        Assert.assertTrue(GobiiClientContextAuth.authenticate());
    }

    //
    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.deAuthenticate());
    }


    @Test
    public void testSucceedWithOneAuthentication() throws Exception {

        Assert.assertTrue(GobiiClientContextAuth.authenticate());
    }

    @Test
    public void testFailOnSecondAuthentication() throws Exception {

        Assert.assertTrue(GobiiClientContextAuth.authenticate());
        String oldToken = GobiiClientContext.getInstance(null, false).getUserToken();

        Assert.assertTrue(GobiiClientContextAuth.authenticate());
        String newToken = GobiiClientContext.getInstance(null, false).getUserToken();

        Assert.assertNotEquals(oldToken, newToken);

        AnalysisDTO analysisDTORequest = new AnalysisDTO();
        analysisDTORequest.setAnalysisId(1);


        String url = GobiiServiceRequestId.URL_ANALYSIS.getRequestUrl(GobiiClientContext.getInstance(null, false).getCurrentCropContextRoot(),
                GobiiControllerType.GOBII.getControllerPath());

//        DtoRequestProcessor<AnalysisDTO> dtoDtoRequestProcessor = new DtoRequestProcessor<>();


    }

    private String makeUrl(GobiiCropConfig gobiiCropConfig) throws Exception {

        String returnVal;

        URL url = new URL("http",
                gobiiCropConfig.getHost(),
                gobiiCropConfig.getPort(),
                gobiiCropConfig.getContextPath());

        returnVal = url.toString();
        return returnVal;
    }

    @Test
    public void testSwitchToSecondServer() throws Exception {

        GobiiTestConfiguration gobiiTestConfiguration = new GobiiTestConfiguration();
        List<GobiiCropConfig> activeGobiiCropConfigs = gobiiTestConfiguration.getConfigSettings().getActiveCropConfigs();
        if (activeGobiiCropConfigs.size() > 1) {
            // http://localhost:8282/gobii-dev/


            // **************** stuff we need for both tests
            GobiiCropConfig gobiiCropConfigOne = activeGobiiCropConfigs.get(0);
            String serviceUrlOne = makeUrl(gobiiCropConfigOne);
            String cropIdOne = gobiiCropConfigOne.getGobiiCropType();
            String cropContextRootOne = gobiiCropConfigOne.getContextPath();
            Integer cropPortOne = gobiiCropConfigOne.getPort();
            GobiiUriFactory gobiiUriFactoryServerOne = new GobiiUriFactory(cropContextRootOne);
            RestUri restUriContactServerOne = gobiiUriFactoryServerOne
                    .resourceByUriIdParam(GobiiServiceRequestId.URL_CONTACTS);

            GobiiCropConfig gobiiCropConfigTwo = activeGobiiCropConfigs.get(1);
            String serviceUrlTwo = makeUrl(gobiiCropConfigTwo);
            String cropIdTwo = gobiiCropConfigTwo.getGobiiCropType();
            String cropContextRootTwo = gobiiCropConfigTwo.getContextPath();
            Integer cropPortTwo = gobiiCropConfigTwo.getPort();
            GobiiUriFactory gobiiUriFactoryServeTwo = new GobiiUriFactory(cropContextRootTwo);
            RestUri restUriContactServerTwo = gobiiUriFactoryServeTwo
                    .resourceByUriIdParam(GobiiServiceRequestId.URL_CONTACTS);

            Assert.assertNotNull("Could not get testexecconfig", GobiiClientContextAuth.getTestExecConfig());
            String testUser = GobiiClientContextAuth.getTestExecConfig().getLdapUserForUnitTest();
            String testPassword = GobiiClientContextAuth.getTestExecConfig().getLdapPasswordForUnitTest();

            // ****************** FIRST LOGIN

            GobiiClientContext.resetConfiguration();
            GobiiClientContext.getInstance(serviceUrlTwo, true);
            Assert.assertTrue("Unable to authenticate with test user "
                            + testUser
                            + " to server of "
                            + serviceUrlOne,
                    GobiiClientContext.getInstance(null, false).login(cropIdOne, testUser, testPassword));

            Assert.assertTrue("The context root retrieved from the context "
                            + GobiiClientContext.getInstance(null, false)
                            .getCurrentCropContextRoot()
                            + " does not match the one retrieved from the configuration file "
                            + cropContextRootOne,
                    GobiiClientContext.getInstance(null, false)
                            .getCurrentCropContextRoot().equals(cropContextRootOne));

            Assert.assertTrue("The port retrieved from the context "
                            + GobiiClientContext.getInstance(null, false)
                            .getCurrentCropPort()
                            + " does not match the one retrieved from the configuration file "
                            + cropPortOne,
                    GobiiClientContext.getInstance(null, false)
                            .getCurrentCropPort().equals(cropPortOne));
            // ****************** SECOND LOGIN


            GobiiClientContext.resetConfiguration();
            GobiiClientContext.getInstance(serviceUrlTwo, true);
            Assert.assertTrue("Unable to authenticate with test user "
                            + testUser
                            + " to server of "
                            + serviceUrlTwo,
                    GobiiClientContext.getInstance(null, false).login(cropIdTwo, testUser, testPassword));

            Assert.assertTrue("The context root retrieved from the context "
                            + GobiiClientContext.getInstance(null, false)
                            .getCurrentCropContextRoot()
                            + " does not match the one retrieved from the configuration file "
                            + cropContextRootTwo,
                    GobiiClientContext.getInstance(null, false)
                            .getCurrentCropContextRoot().equals(cropContextRootTwo));

            Assert.assertTrue("The port retrieved from the context "
                            + GobiiClientContext.getInstance(null, false)
                            .getCurrentCropPort()
                            + " does not match the one retrieved from the configuration file "
                            + cropPortTwo,
                    GobiiClientContext.getInstance(null, false)
                            .getCurrentCropPort().equals(cropPortTwo));


            // do contacts request with crop two context root
            restUriContactServerTwo.setParamValue("id", "6");
            GobiiEnvelopeRestResource<ContactDTO> gobiiEnvelopeRestResourceServerTwo = new GobiiEnvelopeRestResource<>(restUriContactServerTwo);
            PayloadEnvelope<ContactDTO> resultEnvelope = gobiiEnvelopeRestResourceServerTwo
                    .get(ContactDTO.class);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));


            // now set current root to crop onej -- this will cause an error because the
            // gobiiUriFactory still has crop two's context root.
            restUriContactServerOne.setParamValue("id", "6");
            GobiiEnvelopeRestResource<ContactDTO> gobiiEnvelopeRestResourceServerOne = new GobiiEnvelopeRestResource<>(restUriContactServerOne);
            resultEnvelope = gobiiEnvelopeRestResourceServerOne
                    .get(ContactDTO.class);

            Assert.assertTrue("Method with incorrectly configured gobiiUriFactory should have failed with 404",
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
            GobiiClientContext.getInstance(null, false).login(cropIdTwo, testUser, testPassword);
            restUriContactServerTwo = gobiiUriFactoryServeTwo
                    .resourceByUriIdParam(GobiiServiceRequestId.URL_CONTACTS);
            restUriContactServerTwo.setParamValue("id", "6");
            gobiiEnvelopeRestResourceServerTwo = new GobiiEnvelopeRestResource<>(restUriContactServerTwo);
            resultEnvelope = gobiiEnvelopeRestResourceServerTwo
                    .get(ContactDTO.class);


            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));


        }

    }

    @Test
    public void testSwitchToSecondCrop() throws Exception {


        // these steps require physical access to a config file. Other clients do not have
        // this access. So we need to make sure that, aside from retrieving te config URL nad the
        // username/password, the remainder of the test does not consume the gobiiTestConfiguration.
        GobiiTestConfiguration gobiiTestConfiguration = new GobiiTestConfiguration();
        String initialConfigUrl = gobiiTestConfiguration.getConfigSettings().getTestExecConfig().getInitialConfigUrl();
        String testUser = gobiiTestConfiguration.getConfigSettings().getTestExecConfig().getLdapUserForUnitTest();
        String testPassword = gobiiTestConfiguration.getConfigSettings().getTestExecConfig().getLdapPasswordForUnitTest();

        GobiiClientContext.getInstance(initialConfigUrl, true);
        List<String> activeCrops = GobiiClientContext.getInstance(null, false).getCropTypeTypes();

        if (activeCrops.size() > 1) {

            String cropIdOne = activeCrops.get(0);
            String cropIdTwo = activeCrops.get(1);

            // ****************** FIRST LOGIN
            GobiiClientContext.getInstance(null, false).login(cropIdOne, testUser, testPassword);
            Assert.assertNotNull("Authentication with first crop failed: " + cropIdOne,
                    GobiiClientContext.getInstance(null, true).getUserToken());

            String cropOneToken = GobiiClientContext.getInstance(null, true).getUserToken();

            // ****************** SECOND LOGIN

            GobiiClientContext.getInstance(null, false).login(cropIdTwo, testUser, testPassword);
            Assert.assertNotNull("Authentication with second crop failed: " + cropIdTwo,
                    GobiiClientContext.getInstance(null, true).getUserToken());

            String cropTwoToken = GobiiClientContext.getInstance(null, true).getUserToken();

            Assert.assertFalse("The tokens for the two authentications should be different: " + cropOneToken + "," + cropTwoToken,
                    cropOneToken.equals(cropTwoToken));

        }
    }

    @Test
    public void testUnknownContactError() throws Exception {

        GobiiTestConfiguration gobiiTestConfiguration = new GobiiTestConfiguration();
        ConfigSettings configSettings = gobiiTestConfiguration.getConfigSettings();
        List<GobiiCropConfig> activeGobiiCropConfigs = gobiiTestConfiguration.getConfigSettings().getActiveCropConfigs();
        Assert.assertTrue("There is no active crop to test with",
                activeGobiiCropConfigs.size() > 0);

        // this login should succeeed
        GobiiCropConfig GobiiCropConfigOne = activeGobiiCropConfigs.get(0);
        String serviceUrl = makeUrl(GobiiCropConfigOne);
        GobiiClientContext.resetConfiguration();
        String cropId = GobiiCropConfigOne.getGobiiCropType();
        GobiiClientContext gobiiClientContext = GobiiClientContext.getInstance(configSettings, cropId, GobiiAutoLoginType.USER_TEST);
        Assert.assertNotNull("No user token was created for initial login: " + gobiiClientContext.getLoginFailure(),
                gobiiClientContext.getUserToken());
        String validToken = gobiiClientContext.getUserToken();

        // now break the test user login
        String testLoginUser = gobiiTestConfiguration.getConfigSettings().getTestExecConfig().getLdapUserForUnitTest();
        RestUri restUriContactSearch = gobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .contactsByQueryParams();
        restUriContactSearch.setParamValue("userName", testLoginUser);
        GobiiEnvelopeRestResource<ContactDTO> gobiiEnvelopeRestResourceForGet = new GobiiEnvelopeRestResource<>(restUriContactSearch);
        PayloadEnvelope<ContactDTO> resultEnvelope = gobiiEnvelopeRestResourceForGet
                .get(ContactDTO.class);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        Assert.assertTrue("Test user was not retrieved: " + testLoginUser, resultEnvelope.getPayload().getData().size() == 1);
        ContactDTO contactDTOFromGet = resultEnvelope.getPayload().getData().get(0);
        contactDTOFromGet.setUserName("not");

        RestUri restUriContactUpdate = gobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_CONTACTS)
                .addUriParam("id")
                .setParamValue("id", contactDTOFromGet.getContactId().toString());

        GobiiEnvelopeRestResource<ContactDTO> gobiiEnvelopeRestResourceForPut = new GobiiEnvelopeRestResource<>(restUriContactUpdate);
        PayloadEnvelope<ContactDTO> payloadEnvelopeForPut = new PayloadEnvelope<>(contactDTOFromGet, GobiiProcessType.UPDATE);
        PayloadEnvelope<ContactDTO> contactDTOPayloadEnvelope = gobiiEnvelopeRestResourceForPut.put(ContactDTO.class, payloadEnvelopeForPut);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(contactDTOPayloadEnvelope.getHeader()));


        // now test that we get the correct borken response
        GobiiClientContext newGobiiClientContext = gobiiClientContext.getInstance(serviceUrl, true);

        String testUserName = gobiiTestConfiguration.getConfigSettings().getTestExecConfig().getLdapUserForUnitTest();
        String testPassword = gobiiTestConfiguration.getConfigSettings().getTestExecConfig().getLdapPasswordForUnitTest();
        Assert.assertFalse("Login should have failed", newGobiiClientContext.login(cropId,testUserName, testPassword));
        Assert.assertTrue("Message does not contain expected error: " + newGobiiClientContext.getLoginFailure(),
                newGobiiClientContext.getLoginFailure().toLowerCase().contains("missing contact info for user"));

        // restore user name
        // re-set the token to the valid one we had -- otherwise we won't have permission to reset the contact record!
        GobiiClientContext.getInstance(null,false).setUserToken(validToken);
        contactDTOFromGet.setUserName(testUserName);
        PayloadEnvelope<ContactDTO> contactDTOPayloadEnvelopeRestore = gobiiEnvelopeRestResourceForPut.put(ContactDTO.class, payloadEnvelopeForPut);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(contactDTOPayloadEnvelopeRestore.getHeader()));

    }

}
