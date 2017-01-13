// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests.dbops.crud;

import org.gobiiproject.gobiiclient.dtorequests.DtoRequestCv;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiimodel.dto.container.CvDTO;
import org.gobiiproject.gobiimodel.entity.CvItem;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.UUID;

public class DtoCrudRequestCvTest implements DtoCrudRequestTest {

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
        DtoRequestCv dtoRequestCv = new DtoRequestCv();

        CvDTO cvDTORequest = new CvDTO();
        cvDTORequest.setCvId(2);
        cvDTORequest.setIncludeDetailsList(true);


        CvDTO cvDTOResponse = dtoRequestCv.process(cvDTORequest);

        Assert.assertNotEquals(cvDTOResponse, null);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(cvDTOResponse));

        Assert.assertNotEquals(null, cvDTOResponse);
        Assert.assertTrue(cvDTOResponse.getCvId() >= 0);
        Assert.assertTrue(cvDTOResponse.getGroupCvItems().size() > 0);
        CvItem arbitraryItem = cvDTOResponse.getGroupCvItems().get("status").get(0);
        Assert.assertNotNull(arbitraryItem.getCvId());
        Assert.assertNotNull(arbitraryItem.getDefinition());
        Assert.assertNotNull(arbitraryItem.getTerm());



    } // testGetMarkers()

    @Test
    @Override
    public void testEmptyResult() throws Exception {
    }


    @Test
    @Override
    public void create() throws Exception {

        DtoRequestCv dtoRequestCv = new DtoRequestCv();
        CvDTO cvDTORequest = TestDtoFactory
                .makePopulatedCvDTO(GobiiProcessType.CREATE, 1);
        // set the plain properties

        CvDTO cvDTOResponse = dtoRequestCv.process(cvDTORequest);

        Assert.assertNotEquals(null, cvDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(cvDTOResponse));
        Assert.assertTrue(cvDTOResponse.getCvId() > 0);
    }


    @Test
    @Override
    public void update() throws Exception {
        DtoRequestCv dtoRequestCv = new DtoRequestCv();

        // create a new cv for our test
        CvDTO newCvDto = TestDtoFactory
                .makePopulatedCvDTO(GobiiProcessType.CREATE, 1);
        CvDTO newCvDTOResponse = dtoRequestCv.process(newCvDto);

        // re-retrieve the cv we just created so we start with a fresh READ mode dto
        CvDTO CvDTORequest = new CvDTO();
        CvDTORequest.setCvId(newCvDTOResponse.getCvId());
        CvDTO cvDTOReceived = dtoRequestCv.process(CvDTORequest);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(cvDTOReceived));


        // so this would be the typical workflow for the client app
        cvDTOReceived.setGobiiProcessType(GobiiProcessType.UPDATE);
        Integer newGrouopId = 2;
        cvDTOReceived.setGroupId(newGrouopId);

        CvDTO CvDTOResponse = dtoRequestCv.process(cvDTOReceived);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(CvDTOResponse));

        CvDTO dtoRequestCvReRetrieved =
                dtoRequestCv.process(CvDTORequest);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dtoRequestCvReRetrieved));

        Assert.assertTrue(dtoRequestCvReRetrieved.getGroupId().equals(newGrouopId));

    }

    @Override
    public void getList() throws Exception {

    }


    @Test
    public void testDeleteCv() throws Exception {
        DtoRequestCv dtoRequestCv = new DtoRequestCv();

        // create a new cv for our test
        CvDTO newCvDto = TestDtoFactory
                .makePopulatedCvDTO(GobiiProcessType.CREATE, 1);
        CvDTO newCvDTOResponse = dtoRequestCv.process(newCvDto);

        // re-retrieve the cv we just created so we start with a fresh READ mode dto
        CvDTO CvDTORequest = new CvDTO();
        CvDTORequest.setCvId(newCvDTOResponse.getCvId());
        CvDTO cvDTOReceived = dtoRequestCv.process(CvDTORequest);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(cvDTOReceived));


        // so this would be the typical workflow for the client app
        cvDTOReceived.setGobiiProcessType(GobiiProcessType.DELETE);


        CvDTO CvDTOResponse = dtoRequestCv.process(cvDTOReceived);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(CvDTOResponse));

        CvDTO dtoRequestCvReRetrieved =
                dtoRequestCv.process(CvDTORequest);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dtoRequestCvReRetrieved));

        //try to retrieve deleted cv

        CvDTO cvDTORequest = new CvDTO();
        cvDTORequest.setCvId(dtoRequestCvReRetrieved.getCvId());
        cvDTORequest.setIncludeDetailsList(true);


        CvDTO cvDTOResponse = dtoRequestCv.process(dtoRequestCvReRetrieved);
        Assert.assertEquals(null, cvDTOResponse.getTerm());

    }
}
