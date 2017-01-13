// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;


import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestValues;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.CropConfig;
import org.gobiiproject.gobiimodel.config.ServerConfig;
import org.gobiiproject.gobiimodel.dto.container.ConfigSettingsDTO;
import org.gobiiproject.gobiimodel.dto.container.PingDTO;
import org.gobiiproject.gobiimodel.dto.types.ControllerType;
import org.gobiiproject.gobiimodel.types.GobiiCropType;
import org.gobiiproject.gobiimodel.types.GobiiFileLocationType;
import org.gobiiproject.gobiimodel.types.SystemUserDetail;
import org.gobiiproject.gobiimodel.types.SystemUserNames;
import org.gobiiproject.gobiimodel.types.SystemUsers;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DtoRequestConfigSettingsTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }

    @Test
    public void testGetConfigSettings() throws Exception {
        DtoRequestConfigSettings dtoRequestConfigSettings = new DtoRequestConfigSettings();
        ConfigSettingsDTO configSettingsDTORequest = new ConfigSettingsDTO();
        ConfigSettingsDTO configSettingsDTOResponse = dtoRequestConfigSettings.process(configSettingsDTORequest);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(configSettingsDTOResponse));
        Assert.assertTrue(configSettingsDTOResponse.getServerConfigs().size() > 0);


        // this works because in our test environment we know that our gobii.config
        // here on the client is the same as on the server
        ConfigSettings configSettings = new ConfigSettings(TestValues.PROP_FILE_FQPN);
        Assert.assertTrue(configSettings
                .getActiveCropConfigs()
                .size() == configSettingsDTOResponse.getServerConfigs().size());

        CropConfig cropConfigArbitrary = configSettings.getActiveCropConfigs().get(0);



        List<ServerConfig> matches = configSettingsDTOResponse
                .getServerConfigs()
                .entrySet()
                .stream()
                .filter(e ->
                        (e.getValue().getDomain().equals(cropConfigArbitrary.getServiceDomain())) &&
                                (e.getValue().getPort().equals(cropConfigArbitrary.getServicePort())) &&
                                (e.getValue().getFileLocations().get(GobiiFileLocationType.EXTRACTORINSTRUCTION_FILES)
                                        .equals(cropConfigArbitrary.getExtractorInstructionFilesDirectory())) &&
                                (e.getKey().equals(cropConfigArbitrary.getGobiiCropType())))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        Assert.assertTrue(1 == matches.size());
        Assert.assertNotNull(matches.get(0).getContextRoot());
    }

    @Test
    public void testInitContextFromConfigSettings() throws  Exception {

        ClientContext.resetConfiguration();
        ConfigSettings configSettings = new ConfigSettings(TestValues.PROP_FILE_FQPN);

        SystemUsers systemUsers = new SystemUsers();
        SystemUserDetail userDetail = systemUsers.getDetail(SystemUserNames.USER_READER.toString());

       Assert.assertTrue("Unable to log in with locally instantiated config settings",
               ClientContext.getInstance(configSettings).login(userDetail.getUserName(), userDetail.getPassword()));

        PingDTO pingDTORequest = TestDtoFactory.makePingDTO();
        pingDTORequest.setControllerType(ControllerType.LOADER);

        DtoRequestPing dtoRequestPing = new DtoRequestPing();
        PingDTO pingDTOResponse = dtoRequestPing.process(pingDTORequest);

        Assert.assertNotEquals(null, pingDTOResponse);
        Assert.assertNotEquals(null, pingDTOResponse.getDbMetaData());
        Assert.assertNotEquals(null, pingDTOResponse.getPingResponses());
        Assert.assertTrue(pingDTOResponse.getPingResponses().size()
                >= pingDTORequest.getDbMetaData().size());

    } // testInitContextFromConfigSettings()

}
