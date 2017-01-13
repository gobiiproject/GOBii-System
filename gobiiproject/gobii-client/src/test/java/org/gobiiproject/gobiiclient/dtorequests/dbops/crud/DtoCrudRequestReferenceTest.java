// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests.dbops.crud;


import org.gobiiproject.gobiiclient.dtorequests.DtoRequestReference;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.EntityParamValues;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.GlobalPkValues;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.container.ReferenceDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;

public class DtoCrudRequestReferenceTest implements DtoCrudRequestTest {

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

        DtoRequestReference dtoRequestReference = new DtoRequestReference();
        ReferenceDTO referenceDTORequest = new ReferenceDTO();
        referenceDTORequest.setReferenceId(1);
        ReferenceDTO referenceDTOResponse = dtoRequestReference.process(referenceDTORequest);

        Assert.assertNotEquals(null, referenceDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(referenceDTOResponse));

    } //


    @Test
    @Override
    public void create() throws Exception {

        DtoRequestReference dtoRequestReference = new DtoRequestReference();
        ReferenceDTO referenceDTORequest = new ReferenceDTO(GobiiProcessType.CREATE);

        // set the plain properties
        referenceDTORequest.setName("dummy reference name");
        referenceDTORequest.setFilePath("C://pathy/dummy/path");
        referenceDTORequest.setLink("dummylink.com");
        referenceDTORequest.setVersion("version1");
        referenceDTORequest.setModifiedDate(new Date());
        referenceDTORequest.setModifiedBy(1);
        referenceDTORequest.setCreatedDate(new Date());
        referenceDTORequest.setCreatedBy(1);
        ReferenceDTO referenceDTOResponse = dtoRequestReference.process(referenceDTORequest);

        Assert.assertNotEquals(null, referenceDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(referenceDTOResponse));
        Assert.assertTrue(referenceDTOResponse.getReferenceId() > 0);
        GlobalPkValues.getInstance().addPkVal(GobiiEntityNameType.REFERENCES,
                referenceDTOResponse.getReferenceId());

    }

    @Test
    @Override
    public void testEmptyResult() throws Exception {
    }

    @Test
    @Override
    public void update() throws Exception {
        DtoRequestReference dtoRequestReference = new DtoRequestReference();

        // create a new reference for our test
        EntityParamValues entityParamValues = TestDtoFactory.makeArbitraryEntityParams();
        ReferenceDTO newReferenceDto = TestDtoFactory
                .makePopulatedReferenceDTO(GobiiProcessType.CREATE, 1);
        ReferenceDTO newReferenceDTOResponse = dtoRequestReference.process(newReferenceDto);


        // re-retrieve the reference we just created so we start with a fresh READ mode dto
        ReferenceDTO ReferenceDTORequest = new ReferenceDTO();
        ReferenceDTORequest.setReferenceId(newReferenceDTOResponse.getReferenceId());
        ReferenceDTO referenceDTOReceived = dtoRequestReference.process(ReferenceDTORequest);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(referenceDTOReceived));


        // so this would be the typical workflow for the client app
        referenceDTOReceived.setGobiiProcessType(GobiiProcessType.UPDATE);
        String newDataFile = UUID.randomUUID().toString();
        referenceDTOReceived.setFilePath(newDataFile);

        ReferenceDTO ReferenceDTOResponse = dtoRequestReference.process(referenceDTOReceived);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(ReferenceDTOResponse));

        ReferenceDTO dtoRequestReferenceReRetrieved =
                dtoRequestReference.process(ReferenceDTORequest);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dtoRequestReferenceReRetrieved));

        Assert.assertTrue(dtoRequestReferenceReRetrieved.getFilePath().equals(newDataFile));

    }

    @Override
    public void getList() throws Exception {

    }
}
