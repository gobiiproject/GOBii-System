// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.ProjectDTO;
import org.gobiiproject.gobiimodel.dto.container.EntityPropertyDTO;
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

public class DtoRequestProjectTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }


    @Test
    public void testGetProjectDetails() throws Exception {


        DtoRequestProject dtoRequestProject = new DtoRequestProject();
        ProjectDTO projectDTORequest = new ProjectDTO();
        projectDTORequest.setProjectId(1);
        ProjectDTO projectDTO = dtoRequestProject.process(projectDTORequest);

        Assert.assertNotEquals(null, projectDTO);
        Assert.assertNotEquals(null, projectDTO.getProjectName());
        Assert.assertTrue(projectDTO.getProperties().size() > 0);

    } // testGetMarkers()

    @Test
    public void testCreateProject() throws Exception {


        DtoRequestProject dtoRequestProject = new DtoRequestProject();
        ProjectDTO projectDTORequest = new ProjectDTO(DtoMetaData.ProcessType.CREATE);
        projectDTORequest.setCreatedBy(1);
        projectDTORequest.setProjectName(UUID.randomUUID().toString());
        projectDTORequest.setProjectDescription("foo description");
        projectDTORequest.setProjectCode("foo codez");
        projectDTORequest.setProjectStatus(1);
        projectDTORequest.setModifiedBy(1);
        projectDTORequest.setPiContact(1);

        projectDTORequest.getProperties().add(new EntityPropertyDTO(null, null, "division", "foo division"));
        projectDTORequest.getProperties().add(new EntityPropertyDTO(null, null, "study_name", "foo study name"));
        projectDTORequest.getProperties().add(new EntityPropertyDTO(null, null, "genotyping_purpose", "foo purpose"));

        ProjectDTO projectDTOResponse = dtoRequestProject.process(projectDTORequest);

        Assert.assertNotEquals(null, projectDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(projectDTOResponse));
        Assert.assertNotEquals(null, projectDTOResponse.getProjectId());
        Assert.assertTrue(projectDTOResponse.getProjectId() > 0);

        Assert.assertNotEquals(null, projectDTOResponse.getProperties());
        Assert.assertTrue(projectDTOResponse.getProperties().size() > 0);

        EntityPropertyDTO divisionProperty = projectDTOResponse
                .getProperties()
                .stream()
                .filter(p -> p.getPropertyName().equals("division"))
                .collect(Collectors.toList())
                .get(0);

        Assert.assertTrue(divisionProperty.getEntityIdId().equals(projectDTOResponse.getProjectId()));
        Assert.assertTrue(divisionProperty.getPropertyId() > 0);

    } // testGetMarkers()

    @Test
    public void testCreateExistingProject() throws Exception {

        DtoRequestProject dtoRequestProject = new DtoRequestProject();
        ProjectDTO projectDTORequest = new ProjectDTO();
        projectDTORequest.setProjectId(1);
        ProjectDTO projectDTOExisting = dtoRequestProject.process(projectDTORequest);
        projectDTOExisting.setProcessType(DtoMetaData.ProcessType.CREATE);


        ProjectDTO projectDTOResponse = dtoRequestProject.process(projectDTOExisting);


        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(projectDTOResponse));


        List<HeaderStatusMessage> headerStatusMessages = projectDTOResponse
                .getDtoHeaderResponse()
                .getStatusMessages()
                .stream()
                .filter(m -> m.getValidationStatusType().equals(DtoHeaderResponse.ValidationStatusType.VALIDATION_COMPOUND_UNIQUE))
                .collect(Collectors.toList());


        Assert.assertNotNull(headerStatusMessages);
        Assert.assertTrue(headerStatusMessages.size() > 0);
        HeaderStatusMessage headerStatusMessageValidation = headerStatusMessages.get(0);
        Assert.assertTrue(headerStatusMessageValidation.getMessage().toLowerCase().contains("contact"));
        Assert.assertTrue(headerStatusMessageValidation.getMessage().toLowerCase().contains("name"));


    } // testCreateProject()

    @Test
    public void testUpdateProject() throws Exception {

        DtoRequestProject dtoRequestProject = new DtoRequestProject();
        ProjectDTO projectDTORequest = new ProjectDTO();
        projectDTORequest.setProjectId(1);
        ProjectDTO projectDTORequestReceived = dtoRequestProject.process(projectDTORequest);

        projectDTORequestReceived.setProcessType(DtoMetaData.ProcessType.UPDATE);

        String newDescription = UUID.randomUUID().toString();

        projectDTORequestReceived.setProjectDescription(newDescription);

        String divisionPropertyNewValue = UUID.randomUUID().toString();
        EntityPropertyDTO divisionProperty = projectDTORequestReceived
                .getProperties()
                .stream()
                .filter(p -> p.getPropertyName().equals("division"))
                .collect(Collectors.toList())
                .get(0);

        divisionProperty.setPropertyValue(divisionPropertyNewValue);


        ProjectDTO projectDTOResponse = dtoRequestProject.process(projectDTORequestReceived);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(projectDTOResponse));


        ProjectDTO projectDTOReRequest = new ProjectDTO();
        projectDTOReRequest.setProjectId(1);
        ProjectDTO dtoRequestProjectProjectReRetrieved = dtoRequestProject.process(projectDTOReRequest);
        Assert.assertTrue(dtoRequestProjectProjectReRetrieved.getProjectDescription().equals(newDescription));

        EntityPropertyDTO divisionPropertyReceived = dtoRequestProjectProjectReRetrieved
                .getProperties()
                .stream()
                .filter(p -> p.getPropertyName().equals("division"))
                .collect(Collectors.toList())
                .get(0);

        Assert.assertTrue(divisionPropertyReceived.getPropertyValue().equals(divisionPropertyNewValue));


    }


}
