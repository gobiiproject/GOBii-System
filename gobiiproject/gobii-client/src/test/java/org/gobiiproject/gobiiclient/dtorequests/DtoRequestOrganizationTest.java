// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.OrganizationDTO;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.UUID;

public class DtoRequestOrganizationTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }

    @Test
    public void testGetOrganization() throws Exception {
        DtoRequestOrganization dtoRequestOrganization = new DtoRequestOrganization();
        OrganizationDTO OrganizationDTORequest = new OrganizationDTO();
        OrganizationDTORequest.setOrganizationId(1);
        OrganizationDTO OrganizationDTOResponse = dtoRequestOrganization.process(OrganizationDTORequest);

        Assert.assertNotEquals(null, OrganizationDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(OrganizationDTOResponse));
    }


    @Test
    public void testCreateOrganization() throws Exception {

        DtoRequestOrganization dtoRequestOrganization = new DtoRequestOrganization();

        // set the plain properties

        OrganizationDTO organizationDTORequest = TestDtoFactory
                .makePopulatedOrganizationDTO(DtoMetaData.ProcessType.CREATE, 1);
        OrganizationDTO organizationDTOResponse = dtoRequestOrganization.process(organizationDTORequest);

        Assert.assertNotEquals(null, organizationDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(organizationDTOResponse));
        Assert.assertTrue(organizationDTOResponse.getOrganizationId() > 0);

    }


    @Test
    public void testUpdateOrganization() throws Exception {
        DtoRequestOrganization dtoRequestOrganization = new DtoRequestOrganization();

        // create a new organization for our test
        OrganizationDTO newOrganizationDto = TestDtoFactory
                .makePopulatedOrganizationDTO(DtoMetaData.ProcessType.CREATE, 1);
        OrganizationDTO newOrganizationDTOResponse = dtoRequestOrganization.process(newOrganizationDto);


        // re-retrieve the organization we just created so we start with a fresh READ mode dto
        OrganizationDTO OrganizationDTORequest = new OrganizationDTO();
        OrganizationDTORequest.setOrganizationId(newOrganizationDTOResponse.getOrganizationId());
        OrganizationDTO organizationDTOReceived = dtoRequestOrganization.process(OrganizationDTORequest);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(organizationDTOReceived));


        // so this would be the typical workflow for the client app
        organizationDTOReceived.setProcessType(DtoMetaData.ProcessType.UPDATE);
        String newAddress = UUID.randomUUID().toString();
        organizationDTOReceived.setAddress(newAddress);
        //organizationDTOReceived.setFilePath(newDataFile);

        OrganizationDTO OrganizationDTOResponse = dtoRequestOrganization.process(organizationDTOReceived);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(OrganizationDTOResponse));

        OrganizationDTO dtoRequestOrganizationReRetrieved =
                dtoRequestOrganization.process(OrganizationDTORequest);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dtoRequestOrganizationReRetrieved));

        Assert.assertTrue(dtoRequestOrganizationReRetrieved.getAddress().equals(newAddress));

    }
}
