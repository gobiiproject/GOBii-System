// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests.dbops.crud;

import java.util.UUID;

import org.gobiiproject.gobiiclient.dtorequests.DtoRequestDisplay;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.GlobalPkColl;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.GlobalPkValues;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.container.DisplayDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DtoCrudRequestDisplayTest implements DtoCrudRequestTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }


    @Test
    @Override
    public void get() throws Exception {

        DtoRequestDisplay dtoRequestDisplay = new DtoRequestDisplay();

        DisplayDTO displayDTORequest = new DisplayDTO();
        Integer displayId = (new GlobalPkColl<DtoCrudRequestDisplayTest>()).getAPkVal(DtoCrudRequestDisplayTest.class,
                GobiiEntityNameType.DISPLAYNAMES);
        displayDTORequest.setDisplayId(displayId);
        displayDTORequest.setIncludeDetailsList(true);

        DisplayDTO displayDTOResponse = dtoRequestDisplay.process(displayDTORequest);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(displayDTOResponse));


        Assert.assertNotEquals(displayDTOResponse, null);
        Assert.assertTrue(displayDTOResponse.getTableNamesWithColDisplay().size() > 0);
//        Assert.assertNotNull(displayDTOResponse.getDisplayRank());
        Assert.assertNotNull(displayDTOResponse.getColumnName());


    } // testGetMarkers()

    @Test
    @Override
    public void testEmptyResult() throws Exception {
    }


    @Test
    @Override
    public void create() throws Exception {

        DtoRequestDisplay dtoRequestDisplay = new DtoRequestDisplay();

        DisplayDTO createDisplayDTO = TestDtoFactory
                .makePopulatedDisplayDTO(GobiiProcessType.CREATE, 1);

        // set the plain properties

        String testTableName = createDisplayDTO.getTableName();

        DisplayDTO createDisplayDTOResponse = dtoRequestDisplay.process(createDisplayDTO);

        Assert.assertNotEquals(null, createDisplayDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(createDisplayDTOResponse));
        Assert.assertTrue(createDisplayDTOResponse.getDisplayId() > 0);
        GlobalPkValues.getInstance().addPkVal(GobiiEntityNameType.DISPLAYNAMES,createDisplayDTOResponse.getDisplayId());
        Assert.assertNotNull(createDisplayDTOResponse.getDisplayRank());

        DisplayDTO reRequestDto = new DisplayDTO();
        reRequestDto.setDisplayId(createDisplayDTOResponse.getDisplayId());
        DisplayDTO reResponseDto = dtoRequestDisplay.process(reRequestDto);

        Assert.assertNotEquals(null, reResponseDto);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(reResponseDto));
        Assert.assertTrue(reResponseDto.getDisplayId() > 0);
        Assert.assertNotNull(reResponseDto.getDisplayRank());
        Assert.assertNotNull(reResponseDto.getColumnName());
        Assert.assertTrue(testTableName.equals(reResponseDto.getTableName()));
    }


    @Test
    @Override
    public void update() throws Exception {

        DtoRequestDisplay dtoRequestDisplay = new DtoRequestDisplay();

        // create a new reference for our test

        DisplayDTO newDisplayDto = TestDtoFactory
                .makePopulatedDisplayDTO(GobiiProcessType.CREATE, 1);
        DisplayDTO newDisplayDTOResponse = dtoRequestDisplay.process(newDisplayDto);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(newDisplayDTOResponse));

        Integer newDisplayId = newDisplayDTOResponse.getDisplayId();
        Assert.assertTrue(newDisplayId > 0);

        // re-retrieve the reference we just created so we start with a fresh READ mode dto
        DisplayDTO displayDTORequest = new DisplayDTO();
        displayDTORequest.setDisplayId(newDisplayId);
        DisplayDTO displayDTOReceived = dtoRequestDisplay.process(displayDTORequest);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(displayDTOReceived));


        // so this would be the typical workflow for the client app
        displayDTOReceived.setGobiiProcessType(GobiiProcessType.UPDATE);
        String oldDisplayName = displayDTOReceived.getDisplayName();
        String newDisplayName = UUID.randomUUID().toString();
        displayDTOReceived.setDisplayName(newDisplayName);

        DisplayDTO displayDTOResponse = dtoRequestDisplay.process(displayDTOReceived);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(displayDTOResponse));

        DisplayDTO displayDTOReRequest = new DisplayDTO();
        displayDTOReRequest.setGobiiProcessType(GobiiProcessType.READ);
        displayDTOReRequest.setDisplayId(newDisplayId);
        DisplayDTO dtoRequestDisplayReRetrieved =
                dtoRequestDisplay.process(displayDTORequest);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dtoRequestDisplayReRetrieved));

        Assert.assertFalse(
                "The retrieved display name ("
                        + dtoRequestDisplayReRetrieved.getDisplayName()
                        + ") matches the previous value("
                        + oldDisplayName
                        + "); it is not set to the new value("
                        + newDisplayName
                        + ")"
                ,
                dtoRequestDisplayReRetrieved.getDisplayName().equals(oldDisplayName));

        Assert.assertTrue(
                "The retrieved display name ("
                        + dtoRequestDisplayReRetrieved.getDisplayName()
                        + ") does not match the updated value("
                        + newDisplayName
                        + "); the display id is "
                        + newDisplayId
                ,
                dtoRequestDisplayReRetrieved.getDisplayName().equals(newDisplayName));
    }

    @Override
    public void getList() throws Exception {

    }

}
