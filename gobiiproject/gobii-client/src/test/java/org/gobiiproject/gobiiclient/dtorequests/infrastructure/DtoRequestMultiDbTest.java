// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests.infrastructure;

import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.common.Authenticator;
import org.gobiiproject.gobiiclient.core.common.ClientContext;
import org.gobiiproject.gobiiclient.core.common.TestConfiguration;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.*;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.CropConfig;
import org.gobiiproject.gobiimodel.config.ServerConfig;
import org.gobiiproject.gobiimodel.headerlesscontainer.CvDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.PingDTO;

import org.gobiiproject.gobiimodel.headerlesscontainer.ConfigSettingsDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
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
            PingDTO currentPingDTOResponse = resultEnvelopePing.getPayload().getData().get(0);


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

            PayloadEnvelope<CvDTO> payloadEnvelope = new PayloadEnvelope<>(currentCvDtoRequest, GobiiProcessType.CREATE);
            GobiiEnvelopeRestResource<CvDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(ClientContext.getInstance(null, false)
                    .getUriFactory()
                    .resourceColl(ServiceRequestId.URL_CV));
            PayloadEnvelope<CvDTO> cvDTOResponseEnvelope = gobiiEnvelopeRestResource.post(CvDTO.class,
                    payloadEnvelope);
            CvDTO cvDTOResponse = cvDTOResponseEnvelope.getPayload().getData().get(0);

            Assert.assertNotEquals(null, cvDTOResponse);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(cvDTOResponseEnvelope.getHeader()));
            Assert.assertTrue(cvDTOResponse.getCvId() > 0);

            GlobalPkValues.getInstance().addPkVal(GobiiEntityNameType.CVTERMS,
                    cvDTOResponse.getCvId());


            RestUri restUriCvForGetById = ClientContext.getInstance(null, false)
                    .getUriFactory()
                    .resourceByUriIdParam(ServiceRequestId.URL_CV);
            restUriCvForGetById.setParamValue("id", cvDTOResponse.getCvId().toString());
            GobiiEnvelopeRestResource<CvDTO> restResourceForGetById = new GobiiEnvelopeRestResource<>(restUriCvForGetById);
            PayloadEnvelope<CvDTO> resultEnvelopeForGetByID = restResourceForGetById
                    .get(CvDTO.class);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));
            CvDTO cvDTOResponseForParams = resultEnvelopeForGetByID.getPayload().getData().get(0);

            GlobalPkValues.getInstance().addPkVal(GobiiEntityNameType.CVTERMS, cvDTOResponse.getCvId());

        }
    }


}
