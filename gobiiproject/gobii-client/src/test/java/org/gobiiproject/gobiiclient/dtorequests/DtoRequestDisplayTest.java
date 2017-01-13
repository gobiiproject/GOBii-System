// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import java.util.Date;
import java.util.UUID;

import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.EntityParamValues;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.DisplayDTO;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DtoRequestDisplayTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }


    @Test
    public void testGetTableDisplayNamesWithColDisplay() throws Exception {

        DtoRequestDisplay dtoRequestDisplay = new DtoRequestDisplay();

        DisplayDTO displayDTORequest = new DisplayDTO();
        displayDTORequest.getTableNamesWithColDisplay();
        displayDTORequest.setIncludeDetailsList(true);


        DisplayDTO displayDTOResponse = dtoRequestDisplay.process(displayDTORequest);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(displayDTOResponse));


        Assert.assertNotEquals(displayDTOResponse, null);
        Assert.assertTrue(displayDTOResponse.getTableNamesWithColDisplay().size() > 0);
//        Assert.assertNotNull(displayDTOResponse.getDisplayRank());
        Assert.assertNotNull(displayDTOResponse.getColumnName());


    } // testGetMarkers()

    @Test
    public void testCreateDisplay() throws Exception {

        DtoRequestDisplay dtoRequestDisplay = new DtoRequestDisplay();

        DisplayDTO displayDTORequest = TestDtoFactory
                .makePopulatedDisplayDTO(DtoMetaData.ProcessType.CREATE, 1);

        // set the plain properties

        String testTableName = displayDTORequest.getTableName();

        DisplayDTO referenceDTOResponse = dtoRequestDisplay.process(displayDTORequest);

        Assert.assertNotEquals(null, referenceDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(referenceDTOResponse));
        Assert.assertTrue(referenceDTOResponse.getDisplayId() > 0);
        Assert.assertNotNull(referenceDTOResponse.getDisplayRank());

        DisplayDTO reRequestDto = new DisplayDTO();
        reRequestDto.setDisplayId(displayDTORequest.getDisplayId());
        DisplayDTO reResponseDto = dtoRequestDisplay.process(reRequestDto);

        Assert.assertNotEquals(null, reResponseDto);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(reResponseDto));
        Assert.assertTrue(reResponseDto.getDisplayId() > 0);
        Assert.assertNotNull(reResponseDto.getDisplayRank());
        Assert.assertNotNull(reResponseDto.getColumnName());
        Assert.assertTrue(testTableName.equals(reResponseDto.getTableName()));
    }


    @Test
    public void testUpdateDisplay() throws Exception {
        DtoRequestDisplay dtoRequestDisplay = new DtoRequestDisplay();

        // create a new reference for our test

        DisplayDTO newDisplayDto = TestDtoFactory
                .makePopulatedDisplayDTO(DtoMetaData.ProcessType.CREATE, 1);
        DisplayDTO newDisplayDTOResponse = dtoRequestDisplay.process(newDisplayDto);


        // re-retrieve the reference we just created so we start with a fresh READ mode dto
        DisplayDTO DisplayDTORequest = new DisplayDTO();
        DisplayDTORequest.setDisplayId(newDisplayDTOResponse.getDisplayId());
        DisplayDTO referenceDTOReceived = dtoRequestDisplay.process(DisplayDTORequest);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(referenceDTOReceived));


        // so this would be the typical workflow for the client app
        referenceDTOReceived.setProcessType(DtoMetaData.ProcessType.UPDATE);
        String newDisplayName = UUID.randomUUID().toString();
        referenceDTOReceived.setDisplayName(newDisplayName);

        DisplayDTO DisplayDTOResponse = dtoRequestDisplay.process(referenceDTOReceived);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(DisplayDTOResponse));

        DisplayDTO dtoRequestDisplayReRetrieved =
                dtoRequestDisplay.process(DisplayDTORequest);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dtoRequestDisplayReRetrieved));

        Assert.assertTrue(dtoRequestDisplayReRetrieved.getDisplayName().equals(newDisplayName));
    }

}
