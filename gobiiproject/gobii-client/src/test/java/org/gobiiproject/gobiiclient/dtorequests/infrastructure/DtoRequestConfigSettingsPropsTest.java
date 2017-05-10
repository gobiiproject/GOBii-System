// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests.infrastructure;


import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.common.ClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.core.common.Authenticator;
import org.gobiiproject.gobiiclient.core.common.TestConfiguration;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.ServerConfig;
import org.gobiiproject.gobiimodel.headerlesscontainer.ConfigSettingsDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.PingDTO;
import org.gobiiproject.gobiimodel.types.GobiiAutoLoginType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URL;

public class DtoRequestConfigSettingsPropsTest {



    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
    }

    private PayloadEnvelope<ConfigSettingsDTO> getConfigSettingsFromServer() throws Exception {

        ClientContext.resetConfiguration();
        Assert.assertTrue(Authenticator.authenticate());

        PayloadEnvelope<ConfigSettingsDTO> returnVal = null;

        RestUri confgSettingsUri = ClientContext.getInstance(null,false)
                .getUriFactory()
                .resourceColl(ServiceRequestId.URL_CONFIGSETTINGS);
        GobiiEnvelopeRestResource<ConfigSettingsDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(confgSettingsUri);
        PayloadEnvelope<ConfigSettingsDTO> resultEnvelope = gobiiEnvelopeRestResource
                .get(ConfigSettingsDTO.class);

        TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader());

        returnVal = resultEnvelope;

        Assert.assertTrue(Authenticator.deAuthenticate());


        return returnVal;

    }

    @Test
    public void testGetConfigSettings() throws Exception {

        ClientContext.resetConfiguration();
        Assert.assertTrue(Authenticator.authenticate());

        PayloadEnvelope<ConfigSettingsDTO> resultEnvelope =  getConfigSettingsFromServer();
        ConfigSettingsDTO configSettingsDTOResponse = resultEnvelope.getPayload().getData().get(0);

        Assert.assertNotNull(configSettingsDTOResponse);
        Assert.assertTrue(configSettingsDTOResponse.getServerConfigs().size() > 0);

        Assert.assertNotNull("The server configuration does not define a default crop",
                configSettingsDTOResponse.getDefaultCrop());

        String defaultCrop = configSettingsDTOResponse.getDefaultCrop();

        ServerConfig serverConfigDefaultCrop = configSettingsDTOResponse
                .getServerConfigs()
                .get(defaultCrop);

        Assert.assertNotNull("The remote server configuration does not define crop type",
                serverConfigDefaultCrop.getGobiiCropType());

        Assert.assertTrue("The default crop's crop ID does not match the settings default crop",
                defaultCrop.equals(serverConfigDefaultCrop.getGobiiCropType()));

        Assert.assertNotNull("The remote server configuration does not define a domain for crop type " + serverConfigDefaultCrop.getGobiiCropType(),
                serverConfigDefaultCrop.getDomain());

        Assert.assertNotNull("The remote server configuration does not define a context root for crop type " + serverConfigDefaultCrop.getGobiiCropType(),
                serverConfigDefaultCrop.getContextRoot());

        Assert.assertNotNull("The remote server configuration does not define a port for crop type " + serverConfigDefaultCrop.getGobiiCropType(),
                serverConfigDefaultCrop.getPort());


        URL url = new URL("http",
                serverConfigDefaultCrop.getDomain(),
                serverConfigDefaultCrop.getPort(),
                serverConfigDefaultCrop.getContextRoot());

        String serviceUrl = url.toString();


        ClientContext.resetConfiguration();

        ClientContext.getInstance(serviceUrl, true);

        ClientContext.getInstance(null, false)
                .setCurrentClientCrop(defaultCrop);

        String testUser = Authenticator.getTestExecConfig().getLdapUserForUnitTest();
        String testPassword = Authenticator.getTestExecConfig().getLdapPasswordForUnitTest();

        Assert.assertTrue("Unable to authenticate to remote server with default drop " + defaultCrop,
                ClientContext.getInstance(null, false).login(testUser, testPassword));

        Assert.assertTrue(Authenticator.deAuthenticate());

    }

    @Test
    public void testWithCaseMisMatchedCropname() throws  Exception {

        PayloadEnvelope<ConfigSettingsDTO> resultEnvelope =  getConfigSettingsFromServer();
        ConfigSettingsDTO configSettingsDTOResponse = resultEnvelope.getPayload().getData().get(0);

        String defaultCrop = configSettingsDTOResponse.getDefaultCrop();

        ServerConfig serverConfigDefaultCrop = configSettingsDTOResponse
                .getServerConfigs()
                .get(defaultCrop);


        URL url = new URL("http",
                serverConfigDefaultCrop.getDomain(),
                serverConfigDefaultCrop.getPort(),
                serverConfigDefaultCrop.getContextRoot());

        String serviceUrl = url.toString();


        ClientContext.resetConfiguration();

        ClientContext.getInstance(serviceUrl, true);


        String defaultCropMismatched = defaultCrop.toUpperCase();



        try {
            ClientContext.getInstance(null, false)
                    .setCurrentClientCrop(defaultCropMismatched);
        } catch(Exception e) {
            Assert.assertTrue("Setting context to a case-mismatched crop type does not throw an exception",
                    e.getMessage().contains("No server configuration is defined for crop: " + defaultCropMismatched));

        }

        Assert.assertTrue(Authenticator.deAuthenticate());

    }

    @Test
    public void testInitContextFromConfigSettings() throws Exception {

        ClientContext.resetConfiguration();
        ConfigSettings configSettings = new TestConfiguration().getConfigSettings();

        TestConfiguration testConfiguration = new TestConfiguration();


                ClientContext.getInstance(configSettings,
                        testConfiguration
                                .getConfigSettings()
                                .getTestExecConfig()
                                .getTestCrop(),
                        GobiiAutoLoginType.USER_TEST);

        Assert.assertNotNull("Unable to log in with locally instantiated config settings",
                ClientContext.getInstance(null,false).getUserToken());


                PingDTO pingDTORequest = TestDtoFactory.makePingDTO();
        //DtoRequestPing dtoRequestPing = new DtoRequestPing();
        GobiiEnvelopeRestResource<PingDTO> gobiiEnvelopeRestResourcePingDTO = new GobiiEnvelopeRestResource<>(ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(ServiceRequestId.URL_PING));

        PayloadEnvelope<PingDTO> resultEnvelopePing = gobiiEnvelopeRestResourcePingDTO.post(PingDTO.class,
                new PayloadEnvelope<>(pingDTORequest, GobiiProcessType.CREATE));
        //PayloadEnvelope<ContactDTO> resultEnvelopeNewContact = dtoRequestContact.process(new PayloadEnvelope<>(newContactDto, GobiiProcessType.CREATE));

        Assert.assertNotNull(resultEnvelopePing);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopePing.getHeader()));
        Assert.assertTrue(resultEnvelopePing.getPayload().getData().size() > 0);
        PingDTO pingDTOResponse = resultEnvelopePing.getPayload().getData().get(0);

        Assert.assertNotEquals(null, pingDTOResponse);
        Assert.assertNotEquals(null, pingDTOResponse.getDbMetaData());
        Assert.assertNotEquals(null, pingDTOResponse.getPingResponses());
        Assert.assertTrue(pingDTOResponse.getPingResponses().size()
                >= pingDTORequest.getDbMetaData().size());

        Assert.assertTrue(Authenticator.deAuthenticate());

    } // testInitContextFromConfigSettings()

}
