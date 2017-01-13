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
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.CvDTO;
import org.gobiiproject.gobiimodel.dto.container.PingDTO;
import org.gobiiproject.gobiimodel.dto.types.ControllerType;
import org.gobiiproject.gobiimodel.entity.CvItem;
import org.gobiiproject.gobiimodel.types.GobiiCropType;
import org.gobiiproject.gobiimodel.types.GobiiDbType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.UUID;
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

        ConfigSettings configSettings = new ConfigSettings(TestValues.PROP_FILE_FQPN); // we're deliberately going to the source instead of using ClientContext


        List<GobiiCropType> activeCropTypes = configSettings
                .getActiveCropConfigs()
                .stream()
                .map(CropConfig::getGobiiCropType)
                .collect(Collectors.toList());

        PingDTO pingDTORequest = TestDtoFactory.makePingDTO();
        for (GobiiCropType currentCropType : activeCropTypes) {

            // should cause server to assign the correct datasource
            Assert.assertTrue(Authenticator.authenticate(currentCropType));

            pingDTORequest.setControllerType(ControllerType.LOADER);
            DtoRequestPing currentDtoRequestPing = new DtoRequestPing();
            PingDTO currentPingDTOResponse = currentDtoRequestPing.process(pingDTORequest);
            Assert.assertFalse("Ping failed for crop " + currentCropType.toString(),
                    TestUtils.checkAndPrintHeaderMessages(currentPingDTOResponse)
            );

            String currentCropDbUrl = configSettings
                    .getCropConfig(currentCropType)
                    .getCropDbConfig(GobiiDbType.POSTGRESQL)
                    .getConnectionString();

            Assert.assertTrue("The ping response does not contain the db url for crop "
                            + currentCropType.toString()
                            + ": "
                            + currentCropDbUrl,
                    1 == currentPingDTOResponse
                            .getPingResponses()
                            .stream()
                            .filter(r -> r.contains(currentCropDbUrl))
                            .count());
        }

    }


    @Test
    public void testCreateCvOnMultipleDb() throws Exception {

        ConfigSettings configSettings = new ConfigSettings(TestValues.PROP_FILE_FQPN); // we're deliberately going to the source instead of using ClientContext


        List<GobiiCropType> activeCropTypes = configSettings
                .getActiveCropConfigs()
                .stream()
                .map(CropConfig::getGobiiCropType)
                .collect(Collectors.toList());


        for (GobiiCropType currentCropType : activeCropTypes) {

            // should cause server to assign the correct datasource
            Assert.assertTrue(Authenticator.authenticate(currentCropType));

            CvDTO currentCvDtoRequest = TestDtoFactory
                    .makePopulatedCvDTO(DtoMetaData.ProcessType.CREATE, 1);
            currentCvDtoRequest.setDefinition("Destination DB should be: " + currentCropType.toString());

            DtoRequestCv dtoRequestCv = new DtoRequestCv();
            // set the plain properties


            CvDTO cvDTOResponse = dtoRequestCv.process(currentCvDtoRequest);

            Assert.assertNotEquals(null, cvDTOResponse);
            Assert.assertFalse("CV Create failed for crop " + currentCropType.toString(),
                    TestUtils.checkAndPrintHeaderMessages(cvDTOResponse));
            Assert.assertTrue(cvDTOResponse.getCvId() > 0);

        }
    }


}
