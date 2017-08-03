// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.gobii.dbops.crud;

import org.gobiiproject.gobiiapimodel.hateos.Link;
import org.gobiiproject.gobiiapimodel.hateos.LinkCollection;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.gobii.Helpers.DtoRestRequestUtils;
import org.gobiiproject.gobiiclient.gobii.Helpers.GlobalPkValues;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.headerlesscontainer.ProjectDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.EntityPropertyDTO;
import org.gobiiproject.gobiiapimodel.payload.HeaderStatusMessage;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DtoCrudRequestProjectTest implements DtoCrudRequestTest {


    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.deAuthenticate());
    }


    @Test
    @Override
    public void create() throws Exception {

        ProjectDTO newProjectDTO = new ProjectDTO();
        newProjectDTO.setCreatedBy(1);
        newProjectDTO.setProjectName(UUID.randomUUID().toString());
        newProjectDTO.setProjectDescription("foo description");
        newProjectDTO.setProjectCode("foo codez");
        newProjectDTO.setProjectStatus(1);
        newProjectDTO.setModifiedBy(1);
        newProjectDTO.setPiContact(1);

        newProjectDTO.getProperties().add(new EntityPropertyDTO(null, null, "division", "foo division"));
        newProjectDTO.getProperties().add(new EntityPropertyDTO(null, null, "study_name", "foo study name"));
        newProjectDTO.getProperties().add(new EntityPropertyDTO(null, null, "genotyping_purpose", "foo purpose"));


//        DtoRequestProject dtoRequestProject = new DtoRequestProject();
//        ProjectDTO projectDTOResponse = dtoRequestProject.process(projectDTORequest);

        RestUri projectsUri = GobiiClientContext.getInstance(null, false).getUriFactory().resourceColl(GobiiServiceRequestId.URL_PROJECTS);
        GobiiEnvelopeRestResource<ProjectDTO> gobiiEnvelopeRestResourceForProjects = new GobiiEnvelopeRestResource<>(projectsUri);
        PayloadEnvelope<ProjectDTO> payloadEnvelope = new PayloadEnvelope<>(newProjectDTO, GobiiProcessType.CREATE);
        PayloadEnvelope<ProjectDTO> resultEnvelope = gobiiEnvelopeRestResourceForProjects
                .post(ProjectDTO.class, payloadEnvelope);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));


        Assert.assertTrue("At least one project should have been retrieved",
                resultEnvelope.getPayload().getData().size() > 0);

        ProjectDTO projectDTOResponse = resultEnvelope.getPayload().getData().get(0);


        Assert.assertNotEquals(null, projectDTOResponse);
        Assert.assertNotEquals(null, projectDTOResponse.getProjectId());
        Assert.assertTrue(projectDTOResponse.getProjectId() > 0);
        GlobalPkValues.getInstance().addPkVal(GobiiEntityNameType.PROJECTS, projectDTOResponse.getProjectId());

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

    }

    @Test
    @Override
    public void get() throws Exception {

        RestUri projectsUri = GobiiClientContext.getInstance(null, false).getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_PROJECTS);
        projectsUri.setParamValue("id", "1");
        GobiiEnvelopeRestResource<ProjectDTO> gobiiEnvelopeRestResourceForProjects = new GobiiEnvelopeRestResource<>(projectsUri);
        PayloadEnvelope<ProjectDTO> resultEnvelope = gobiiEnvelopeRestResourceForProjects
                .get(ProjectDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        Assert.assertTrue("At least one project should have been retrieved ",
                resultEnvelope.getPayload().getData().size() > 0 );
        ProjectDTO projectDTO = resultEnvelope.getPayload().getData().get(0);

//        DtoRequestProject dtoRequestProject = new DtoRequestProject();
//        ProjectDTO projectDTORequest = new ProjectDTO();
//        projectDTORequest.setProjectId(1);
//        ProjectDTO projectDTO = dtoRequestProject.process(projectDTORequest);

        Assert.assertNotEquals(null, projectDTO);
        Assert.assertNotEquals(null, projectDTO.getProjectName());
        Assert.assertTrue(projectDTO.getProperties().size() > 0);

    } // testGetMarkers()

    @Test
    public void testEmptyResult() throws Exception {

        DtoRestRequestUtils<ProjectDTO> dtoDtoRestRequestUtils =
                new DtoRestRequestUtils<>(ProjectDTO.class, GobiiServiceRequestId.URL_PROJECTS);
        Integer maxId = dtoDtoRestRequestUtils.getMaxPkVal();
        Integer nonExistentId = maxId + 1;


        PayloadEnvelope<ProjectDTO> resultEnvelope =
                dtoDtoRestRequestUtils.getResponseEnvelopeForEntityId(nonExistentId.toString());

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        Assert.assertNotNull(resultEnvelope.getPayload());
        Assert.assertNotNull(resultEnvelope.getPayload().getData());
        Assert.assertTrue(resultEnvelope.getPayload().getData().size() == 0);
    }


    @Test
    public void testCreateExistingProject() throws Exception {

        RestUri projectsUri = GobiiClientContext.getInstance(null, false).getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_PROJECTS);
        projectsUri.setParamValue("id", "1");
        GobiiEnvelopeRestResource<ProjectDTO> gobiiEnvelopeRestResourceForProjectGet = new GobiiEnvelopeRestResource<>(projectsUri);
        PayloadEnvelope<ProjectDTO> resultEnvelope = gobiiEnvelopeRestResourceForProjectGet
                .get(ProjectDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));

        ProjectDTO projectDTOExisting = resultEnvelope.getPayload().getData().get(0);

        PayloadEnvelope<ProjectDTO> payloadEnvelope = new PayloadEnvelope<>(projectDTOExisting,
                GobiiProcessType.CREATE);

        GobiiEnvelopeRestResource<ProjectDTO> gobiiEnvelopeRestResourceForProjectPost =
                new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false).getUriFactory().resourceColl(GobiiServiceRequestId.URL_PROJECTS));

        resultEnvelope = gobiiEnvelopeRestResourceForProjectPost
                .post(ProjectDTO.class, payloadEnvelope);


        //ProjectDTO projectDTOResponse = dtoRequestProject.process(projectDTOExisting);


        List<HeaderStatusMessage> headerStatusMessages = resultEnvelope.getHeader()
                .getStatus()
                .getStatusMessages()
                .stream()
                .filter(m -> m.getGobiiValidationStatusType().equals(GobiiValidationStatusType.ENTITY_ALREADY_EXISTS))
                .collect(Collectors.toList());


        Assert.assertNotNull(headerStatusMessages);
        Assert.assertTrue(headerStatusMessages.size() > 0);

    } //


    @Test
    public void testViolateUniqueConstraintProject() throws Exception {

        RestUri projectsUri = GobiiClientContext.getInstance(null, false).getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_PROJECTS);
        projectsUri.setParamValue("id", "1");
        GobiiEnvelopeRestResource<ProjectDTO> gobiiEnvelopeRestResourceForProjectGet = new GobiiEnvelopeRestResource<>(projectsUri);
        PayloadEnvelope<ProjectDTO> resultEnvelope = gobiiEnvelopeRestResourceForProjectGet
                .get(ProjectDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));

        ProjectDTO projectDTOExisting = resultEnvelope.getPayload().getData().get(0);

        projectDTOExisting.setProjectId(0);
        PayloadEnvelope<ProjectDTO> payloadEnvelope = new PayloadEnvelope<>(projectDTOExisting,
                GobiiProcessType.CREATE);

        GobiiEnvelopeRestResource<ProjectDTO> gobiiEnvelopeRestResourceForProjectPost =
                new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false).getUriFactory().resourceColl(GobiiServiceRequestId.URL_PROJECTS));

        resultEnvelope = gobiiEnvelopeRestResourceForProjectPost
                .post(ProjectDTO.class, payloadEnvelope);


        //ProjectDTO projectDTOResponse = dtoRequestProject.process(projectDTOExisting);


        List<HeaderStatusMessage> headerStatusMessages = resultEnvelope.getHeader()
                .getStatus()
                .getStatusMessages()
                .stream()
                .filter(m -> m.getGobiiValidationStatusType().equals(GobiiValidationStatusType.VALIDATION_COMPOUND_UNIQUE))
                .collect(Collectors.toList());


        Assert.assertNotNull(headerStatusMessages);
        Assert.assertTrue(headerStatusMessages.size() > 0);
        Assert.assertTrue(headerStatusMessages.get(0).getMessage().toLowerCase().contains("name"));
        Assert.assertTrue(headerStatusMessages.get(0).getMessage().toLowerCase().contains("contact id"));

    } // testCreateProject()

    @Test
    @Override
    public void update() throws Exception {

//        DtoRequestProject dtoRequestProject = new DtoRequestProject();
//        ProjectDTO projectDTORequest = new ProjectDTO();
//        projectDTORequest.setProjectId(1);

        RestUri projectsUri = GobiiClientContext.getInstance(null, false).getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_PROJECTS);
        projectsUri.setParamValue("id", "1");
        GobiiEnvelopeRestResource<ProjectDTO> gobiiEnvelopeRestResourceForProjectGet = new GobiiEnvelopeRestResource<>(projectsUri);
        PayloadEnvelope<ProjectDTO> resultEnvelope = gobiiEnvelopeRestResourceForProjectGet
                .get(ProjectDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));

        ProjectDTO projectDTORequestReceived = resultEnvelope.getPayload().getData().get(0);

        //ProjectDTO projectDTORequestReceived = dtoRequestProject.process(projectDTORequest);

        String newDescription = UUID.randomUUID().toString();

        projectDTORequestReceived.setProjectDescription(newDescription);

        String divisionPropertyNewValue = UUID.randomUUID().toString();

        Assert.assertTrue("At least one property should have been retrieved",
                projectDTORequestReceived.getProperties().size() > 0);


        EntityPropertyDTO divisionProperty = projectDTORequestReceived
                .getProperties()
                .stream()
                .filter(p -> p.getPropertyName().equals("division"))
                .collect(Collectors.toList())
                .get(0);

        divisionProperty.setPropertyValue(divisionPropertyNewValue);

        PayloadEnvelope<ProjectDTO> requestEnvelope = new PayloadEnvelope<>(projectDTORequestReceived, GobiiProcessType.UPDATE);
        resultEnvelope = gobiiEnvelopeRestResourceForProjectGet
                .put(ProjectDTO.class, requestEnvelope);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));

        // ProjectDTO projectDTOResponse = resultEnvelope.getPayload().getData().get(0);
        /// ProjectDTO projectDTOResponse = dtoRequestProject.process(projectDTORequestReceived);


//        ProjectDTO projectDTOReRequest = new ProjectDTO();
//        projectDTOReRequest.setProjectId(1);
//        ProjectDTO dtoRequestProjectProjectReRetrieved = dtoRequestProject.process(projectDTOReRequest);


        resultEnvelope = gobiiEnvelopeRestResourceForProjectGet
                .get(ProjectDTO.class);

        ProjectDTO dtoRequestProjectProjectReRetrieved = resultEnvelope.getPayload().getData().get(0);

        Assert.assertTrue(dtoRequestProjectProjectReRetrieved.getProjectDescription().equals(newDescription));

        EntityPropertyDTO divisionPropertyReceived = dtoRequestProjectProjectReRetrieved
                .getProperties()
                .stream()
                .filter(p -> p.getPropertyName().equals("division"))
                .collect(Collectors.toList())
                .get(0);

        Assert.assertTrue(divisionPropertyReceived.getPropertyValue().equals(divisionPropertyNewValue));


    }


    @Test
    @Override
    public void getList() throws Exception {

        RestUri restUriProject = GobiiClientContext.getInstance(null, false).getUriFactory().resourceColl(GobiiServiceRequestId.URL_PROJECTS);
        GobiiEnvelopeRestResource<ProjectDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriProject);
        PayloadEnvelope<ProjectDTO> resultEnvelope = gobiiEnvelopeRestResource
                .get(ProjectDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<ProjectDTO> projectDTOList = resultEnvelope.getPayload().getData();
        Assert.assertNotNull(projectDTOList);
        Assert.assertTrue(projectDTOList.size() > 0);
        Assert.assertNotNull(projectDTOList.get(0).getProjectName());


        LinkCollection linkCollection = resultEnvelope.getPayload().getLinkCollection();
        Assert.assertTrue(linkCollection.getLinksPerDataItem().size() == projectDTOList.size());

        List<Integer> itemsToTest = new ArrayList<>();
        if (projectDTOList.size() > 50) {
            itemsToTest = TestUtils.makeListOfIntegersInRange(10, projectDTOList.size());

        } else {
            for (int idx = 0; idx < projectDTOList.size(); idx++) {
                itemsToTest.add(idx);
            }
        }

        for (Integer currentIdx : itemsToTest) {
            ProjectDTO currentProjectDto = projectDTOList.get(currentIdx);

            Link currentLink = linkCollection.getLinksPerDataItem().get(currentIdx);

            RestUri restUriProjectForGetById = GobiiClientContext.getInstance(null, false)
                    .getUriFactory()
                    .RestUriFromUri(currentLink.getHref());
            GobiiEnvelopeRestResource<ProjectDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriProjectForGetById);
            PayloadEnvelope<ProjectDTO> resultEnvelopeForGetByID = gobiiEnvelopeRestResourceForGetById
                    .get(ProjectDTO.class);
            Assert.assertNotNull(resultEnvelopeForGetByID);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));
            ProjectDTO projectDTOFromLink = resultEnvelopeForGetByID.getPayload().getData().get(0);
            Assert.assertTrue(currentProjectDto.getProjectName().equals(projectDTOFromLink.getProjectName()));
            Assert.assertTrue(currentProjectDto.getProjectId().equals(projectDTOFromLink.getProjectId()));
        }

    }

}
