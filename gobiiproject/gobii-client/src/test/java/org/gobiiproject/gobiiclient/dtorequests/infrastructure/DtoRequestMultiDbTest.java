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
import org.gobiiproject.gobiiclient.dtorequests.DtoRequestCv;
import org.gobiiproject.gobiiclient.dtorequests.DtoRequestPing;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestConfiguration;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.CropConfig;
import org.gobiiproject.gobiimodel.config.ServerConfig;
import org.gobiiproject.gobiimodel.dto.container.CvDTO;
import org.gobiiproject.gobiimodel.dto.container.PingDTO;

import org.gobiiproject.gobiimodel.headerlesscontainer.ConfigSettingsDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

public class DtoRequestMultiDbTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }


    @Test
    public void testGetPingDatabaseConfig() throws Exception {

//        ConfigSettings configSettings = new TestConfiguration().getConfigSettings(); // we're deliberately going to the source instead of using ClientContext


//        List<String> activeCropTypes = configSettings
//                .getActiveCropConfigs()
//                .stream()
//                .map(CropConfig::getGobiiCropType)
//                .collect(Collectors.toList());


        RestUri confgSettingsUri = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(ServiceRequestId.URL_CONFIGSETTINGS);

        GobiiEnvelopeRestResource<ConfigSettingsDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(confgSettingsUri);
        PayloadEnvelope<ConfigSettingsDTO> resultEnvelope = gobiiEnvelopeRestResource
                .get(ConfigSettingsDTO.class);

        TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader());
        Assert.assertTrue("Config settings were not retrieved",
                resultEnvelope.getPayload().getData().size() > 0);
        ConfigSettingsDTO configSettingsDTOResponse = resultEnvelope.getPayload().getData().get(0);


        PingDTO pingDTORequest = TestDtoFactory.makePingDTO();
        for (ServerConfig currentServerConfig : configSettingsDTOResponse.getServerConfigs().values()) {

            // should cause server to assign the correct datasource
            String currentCropType = currentServerConfig.getGobiiCropType();
            Assert.assertTrue(Authenticator.authenticate(currentCropType));

            DtoRequestPing currentDtoRequestPing = new DtoRequestPing();
            PingDTO currentPingDTOResponse = currentDtoRequestPing.process(pingDTORequest);
            Assert.assertFalse("Ping failed for crop " + currentCropType.toString(),
                    TestUtils.checkAndPrintHeaderMessages(currentPingDTOResponse)
            );

            Assert.assertNotNull("The ping response does not contain the db url for crop "
                    + currentPingDTOResponse.getDbMetaData());

            Assert.assertTrue("The ping response contains null data ",
                    + currentPingDTOResponse
                    .getDbMetaData()
                    .stream()
                    .filter(item -> LineUtils.isNullOrEmpty(item))
                    .count() == 0);

        }

    }


    @Test
    public void testCreateCvOnMultipleDb() throws Exception {

        ConfigSettings configSettings = new TestConfiguration().getConfigSettings(); // we're deliberately going to the source instead of using ClientContext


        List<String> activeCropTypes = configSettings
                .getActiveCropConfigs()
                .stream()
                .map(CropConfig::getGobiiCropType)
                .collect(Collectors.toList());


        for (String currentCropType : activeCropTypes) {

            // should cause server to assign the correct datasource
            Assert.assertTrue(Authenticator.authenticate(currentCropType));

            CvDTO currentCvDtoRequest = TestDtoFactory
                    .makePopulatedCvDTO(GobiiProcessType.CREATE, 1);
            currentCvDtoRequest.setDefinition("Destination DB should be: " + currentCropType.toString());

            DtoRequestCv dtoRequestCv = new DtoRequestCv();
            // set the plain properties


            CvDTO cvDTOResponse = dtoRequestCv.process(currentCvDtoRequest);

            Assert.assertNotEquals(null, cvDTOResponse);
            Assert.assertFalse("CVTERMS Create failed for crop " + currentCropType.toString(),
                    TestUtils.checkAndPrintHeaderMessages(cvDTOResponse));
            Assert.assertTrue(cvDTOResponse.getCvId() > 0);

        }
    }


}
