// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.EntityPropertyDTO;
import org.gobiiproject.gobiimodel.dto.container.ExperimentDTO;
import org.gobiiproject.gobiimodel.dto.container.ProjectDTO;
import org.gobiiproject.gobiimodel.dto.header.DtoHeaderResponse;
import org.gobiiproject.gobiimodel.dto.header.HeaderStatusMessage;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DtoRequestExperimentTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }


    @Test
    public void testGetExperimentDetailsByExperimentId() throws Exception {
        DtoRequestExperiment dtoRequestExperiment = new DtoRequestExperiment();

        ExperimentDTO experimentDTO = new ExperimentDTO();
        experimentDTO.setExperimentId(2);
        dtoRequestExperiment.process(experimentDTO);


        ExperimentDTO experimentDTOResponse = dtoRequestExperiment.process(experimentDTO);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(experimentDTOResponse));
        Assert.assertNotEquals(experimentDTOResponse, null);
        Assert.assertTrue(experimentDTOResponse.getExperimentId() > 0);
        Assert.assertNotNull(experimentDTOResponse.getPlatformName());

    } // testGetMarkers()

    @Test
    public void testCreateExperiment() throws Exception {

        DtoRequestExperiment dtoRequestExperiment = new DtoRequestExperiment();

        ExperimentDTO experimentDTORequest = new ExperimentDTO(DtoMetaData.ProcessType.CREATE);
        experimentDTORequest.setExperimentId(1);
        experimentDTORequest.setManifestId(1);
        experimentDTORequest.setPlatformId(1);
        experimentDTORequest.setProjectId(1);
        experimentDTORequest.setCreatedBy(2);
        experimentDTORequest.setModifiedBy(2);
        experimentDTORequest.setExperimentCode("foocode");
        experimentDTORequest.setExperimentDataFile("foofile");
        experimentDTORequest.setStatus(1);
        experimentDTORequest.setExperimentName(UUID.randomUUID().toString());

        dtoRequestExperiment.process(experimentDTORequest);

        ExperimentDTO experimentDTOResponse = dtoRequestExperiment.process(experimentDTORequest);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(experimentDTOResponse));
        Assert.assertNotEquals(experimentDTOResponse, null);
        Assert.assertTrue(experimentDTOResponse.getExperimentId() > 0);

    } // testGetMarkers()

    @Test
    public void testCreateExistingExperiment() throws Exception {

        DtoRequestExperiment dtoRequestExperiment = new DtoRequestExperiment();
        ExperimentDTO experimentDTORequest = new ExperimentDTO();
        experimentDTORequest.setExperimentId(2);
        ExperimentDTO ExperimentDTOExisting = dtoRequestExperiment.process(experimentDTORequest);
        ExperimentDTOExisting.setProcessType(DtoMetaData.ProcessType.CREATE);


        ExperimentDTO ExperimentDTOResponse = dtoRequestExperiment.process(ExperimentDTOExisting);


        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(ExperimentDTOResponse));


        List<HeaderStatusMessage> headerStatusMessages = ExperimentDTOResponse
                .getDtoHeaderResponse()
                .getStatusMessages()
                .stream()
                .filter(m -> m.getValidationStatusType().equals(DtoHeaderResponse.ValidationStatusType.VALIDATION_COMPOUND_UNIQUE))
                .collect(Collectors.toList());


        Assert.assertNotNull(headerStatusMessages);
        Assert.assertTrue(headerStatusMessages.size() > 0);
        HeaderStatusMessage headerStatusMessageValidation = headerStatusMessages.get(0);
        Assert.assertTrue(headerStatusMessageValidation.getMessage().toLowerCase().contains("name"));
        Assert.assertTrue(headerStatusMessageValidation.getMessage().toLowerCase().contains("project"));
        Assert.assertTrue(headerStatusMessageValidation.getMessage().toLowerCase().contains("platform"));


    } // testCreateExperiment()

    @Test
    public void testUpdateExperiment() throws Exception {

        DtoRequestExperiment dtoRequestExperiment = new DtoRequestExperiment();
        ExperimentDTO experimentDTORequest = new ExperimentDTO();
        experimentDTORequest.setExperimentId(2);
        ExperimentDTO experimentDTOReceived = dtoRequestExperiment.process(experimentDTORequest);



        experimentDTOReceived.setProcessType(DtoMetaData.ProcessType.UPDATE);

        String newDataFile = UUID.randomUUID().toString();

        experimentDTOReceived.setExperimentDataFile(newDataFile);

        ExperimentDTO experimentDTOResponse = dtoRequestExperiment.process(experimentDTOReceived);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(experimentDTOResponse));


        ExperimentDTO dtoRequestExperimentExperimentReRetrieved =
                dtoRequestExperiment.process(experimentDTORequest);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dtoRequestExperimentExperimentReRetrieved));

        Assert.assertTrue(dtoRequestExperimentExperimentReRetrieved.getExperimentDataFile().equals(newDataFile));

    }


}
