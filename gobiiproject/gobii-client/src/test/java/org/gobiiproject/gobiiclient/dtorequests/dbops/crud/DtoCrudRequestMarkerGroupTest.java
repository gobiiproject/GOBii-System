// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests.dbops.crud;


import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.common.ClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.dtorequests.DtoRequestMarkerGroup;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.container.MarkerGroupDTO;
import org.gobiiproject.gobiimodel.dto.container.MarkerGroupMarkerDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.MarkerDTO;
import org.gobiiproject.gobiimodel.tobemovedtoapimodel.HeaderStatusMessage;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


public class DtoCrudRequestMarkerGroupTest implements DtoCrudRequestTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());


        for (String currentMarkerName : validMarkerNames) {

            makeMarker(currentMarkerName);

        }

    }

    private static void makeMarker(String markerName ) throws Exception {


        RestUri restUriMarkerByName = ClientContext.getInstance(null, false)
                .getUriFactory()
                .markerssByQueryParams();
        restUriMarkerByName.setParamValue("name", markerName);
        GobiiEnvelopeRestResource<MarkerDTO> gobiiEnvelopeRestResourceForProjects = new GobiiEnvelopeRestResource<>(restUriMarkerByName);
        PayloadEnvelope<MarkerDTO> resultEnvelope = gobiiEnvelopeRestResourceForProjects
                .get(MarkerDTO.class);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));

        if ((resultEnvelope.getPayload().getData().size() == 0)
                || (resultEnvelope.getPayload().getData().get(0).getMarkerId() == 0)) {
            MarkerDTO markerDTORequest = TestDtoFactory
                    .makeMarkerDTO(markerName);

            RestUri markerCollUri = ClientContext.getInstance(null, false)
                    .getUriFactory()
                    .resourceColl(ServiceRequestId.URL_MARKERS);
            GobiiEnvelopeRestResource<MarkerDTO> gobiiEnvelopeRestResourceForMarkerPost = new GobiiEnvelopeRestResource<>(markerCollUri);
            resultEnvelope = gobiiEnvelopeRestResourceForMarkerPost
                    .post(MarkerDTO.class, new PayloadEnvelope<>(markerDTORequest, GobiiProcessType.CREATE));

            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));

        }
    }


    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }


    private static List<String> validMarkerNames = Arrays.asList(
            "4806",
            "4824",
            "4831",
            "7925",
            "8144",
            "9614",
            "9673",
            "10710",
            "14005",
            "16297",
            "19846",
            "20215");


    @Test
    @Override
    public void create() throws Exception {

        DtoRequestMarkerGroup dtoRequestMarkerGroup = new DtoRequestMarkerGroup();


        List<MarkerGroupMarkerDTO> markerGroupMarkers = TestDtoFactory.makeMarkerGroupMarkers(validMarkerNames,
                GobiiProcessType.CREATE);

        MarkerGroupDTO markerGroupDTORequest = TestDtoFactory
                .makePopulatedMarkerGroupDTO(GobiiProcessType.CREATE, 1, markerGroupMarkers);

        MarkerGroupDTO markerGroupDTOResponse = dtoRequestMarkerGroup.process(markerGroupDTORequest);

        Assert.assertNotEquals(null, markerGroupDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(markerGroupDTOResponse));
        Assert.assertTrue(markerGroupDTOResponse.getMarkerGroupId() > 0);

        Assert.assertNotNull(markerGroupDTOResponse.getMarkers());

        Integer totalMarkersWithMarkerAndPlatformIds = markerGroupDTOResponse
                .getMarkers()
                .stream()
                .filter(m -> (m.getMarkerId() > 0) && (m.getPlatformId() > 0))
                .collect(Collectors.toList())
                .size();

        Assert.assertTrue(totalMarkersWithMarkerAndPlatformIds == markerGroupDTORequest.getMarkers().size());

        Assert.assertTrue(markerGroupDTOResponse
                .getMarkers()
                .stream()
                .filter(m -> null == m.getFavorableAllele() || m.getFavorableAllele().isEmpty())
                .collect(Collectors.toList())
                .size() == 0);


    }

    @Test
    public void testMarkerGroupCreateFailSomeMarkers() throws Exception {

        DtoRequestMarkerGroup dtoRequestMarkerGroup = new DtoRequestMarkerGroup();

        List<String> someInvalidNames = new ArrayList<>(validMarkerNames);
        someInvalidNames.add("i-do-not-exist!");


        List<MarkerGroupMarkerDTO> markerGroupMarkers = TestDtoFactory.makeMarkerGroupMarkers(someInvalidNames,
                GobiiProcessType.CREATE);

        MarkerGroupDTO markerGroupDTORequest = TestDtoFactory
                .makePopulatedMarkerGroupDTO(GobiiProcessType.CREATE, 1, markerGroupMarkers);

        MarkerGroupDTO markerGroupDTOResponse = dtoRequestMarkerGroup.process(markerGroupDTORequest);

        Assert.assertNotEquals(null, markerGroupDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(markerGroupDTOResponse));

        Assert.assertTrue(markerGroupDTOResponse.getMarkerGroupId() > 0);

        Assert.assertNotNull(markerGroupDTOResponse.getMarkers());

        Integer totalInvalidMarkers = markerGroupDTOResponse
                .getMarkers()
                .stream()
                .filter(m -> !m.isMarkerExists()
                        && (m.getMarkerId() == null)
                        && (m.getPlatformName() == null)
                        && (m.getPlatformId() == null))

                .collect(Collectors.toList())
                .size();

        Assert.assertTrue(totalInvalidMarkers == 1);


    }

    @Test
    public void testMarkerGroupCreateFailAllMarkers() throws Exception {

        DtoRequestMarkerGroup dtoRequestMarkerGroup = new DtoRequestMarkerGroup();

        List<String> allInvalidNames = new ArrayList<>();
        allInvalidNames.add("i-do-not-exist 1");
        allInvalidNames.add("i-do-not-exist 2");
        allInvalidNames.add("i-do-not-exist 3");
        allInvalidNames.add("i-do-not-exist 4");
        allInvalidNames.add("i-do-not-exist 5");


        List<MarkerGroupMarkerDTO> markerGroupMarkers = TestDtoFactory.makeMarkerGroupMarkers(allInvalidNames,
                GobiiProcessType.CREATE);

        MarkerGroupDTO markerGroupDTORequest = TestDtoFactory
                .makePopulatedMarkerGroupDTO(GobiiProcessType.CREATE, 1, markerGroupMarkers);

        MarkerGroupDTO markerGroupDTOResponse = dtoRequestMarkerGroup.process(markerGroupDTORequest);

        Assert.assertNotEquals(null, markerGroupDTOResponse);
        Assert.assertFalse(markerGroupDTOResponse.getStatus().isSucceeded());

        Assert.assertTrue(null == markerGroupDTOResponse.getMarkerGroupId() || markerGroupDTOResponse.getMarkerGroupId() > 0);

        List<HeaderStatusMessage> invalidResponses = markerGroupDTOResponse
                .getStatus()
                .getStatusMessages()
                .stream()
                .filter(m -> m.getGobiiValidationStatusType() == GobiiValidationStatusType.NONEXISTENT_FK_ENTITY)
                .collect(Collectors.toList());

        Assert.assertTrue(invalidResponses.size() == 1);

    }


    @Test
    @Override
    public void get() throws Exception {

        // CREATE A MARKER GROUP
        DtoRequestMarkerGroup dtoRequestMarkerGroup = new DtoRequestMarkerGroup();
        List<MarkerGroupMarkerDTO> markerGroupMarkers = TestDtoFactory.makeMarkerGroupMarkers(validMarkerNames,
                GobiiProcessType.CREATE);
        MarkerGroupDTO markerGroupDTORequest = TestDtoFactory
                .makePopulatedMarkerGroupDTO(GobiiProcessType.CREATE, 1, markerGroupMarkers);
        MarkerGroupDTO markerGroupDTOResponse = dtoRequestMarkerGroup.process(markerGroupDTORequest);
        Integer newMarkerGroupId = markerGroupDTOResponse.getMarkerGroupId();


        // RE-RETREIVE
        MarkerGroupDTO markerGroupDTORequestRefresh = new MarkerGroupDTO();
        markerGroupDTORequestRefresh.setMarkerGroupId(newMarkerGroupId);
        MarkerGroupDTO markerGroupDTOResponseRefresh = dtoRequestMarkerGroup.process(markerGroupDTORequestRefresh);

        Assert.assertNotNull(markerGroupDTOResponseRefresh);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(markerGroupDTOResponseRefresh));
        Assert.assertNotNull(markerGroupDTOResponseRefresh.getName());

        List<String> succededMarkerNames = markerGroupDTOResponseRefresh
                .getMarkers()
                .stream()
                .filter(m -> m.isMarkerExists()
                        && (null != m.getMarkerId())
                        && (m.getMarkerId() > 0)
                )
                .map(m -> m.getMarkerName())
                .collect(Collectors.toList());

        Assert.assertTrue(validMarkerNames.equals(succededMarkerNames));

        Assert.assertTrue(markerGroupDTOResponseRefresh
                .getMarkers()
                .stream()
                .filter(m -> null == m.getFavorableAllele() || m.getFavorableAllele().isEmpty())
                .collect(Collectors.toList())
                .size() == 0);


    }

    @Test
    @Override
    public void testEmptyResult() throws Exception {
    }


    @Test
    @Override
    public void update() throws Exception {

        // CREATE A MARKER GROUP
        DtoRequestMarkerGroup dtoRequestMarkerGroup = new DtoRequestMarkerGroup();
        List<MarkerGroupMarkerDTO> markerGroupMarkers = TestDtoFactory.makeMarkerGroupMarkers(validMarkerNames,
                GobiiProcessType.CREATE);
        MarkerGroupDTO markerGroupDTORequest = TestDtoFactory
                .makePopulatedMarkerGroupDTO(GobiiProcessType.CREATE, 1, markerGroupMarkers);
        MarkerGroupDTO markerGroupDTOResponse = dtoRequestMarkerGroup.process(markerGroupDTORequest);
        Integer newMarkerGroupId = markerGroupDTOResponse.getMarkerGroupId();


        // RE-RETREIVE
        MarkerGroupDTO markerGroupDTORequestRefreshRequest = new MarkerGroupDTO();
        markerGroupDTORequestRefreshRequest.setMarkerGroupId(newMarkerGroupId);
        MarkerGroupDTO markerGroupDTOResponseToUpdate = dtoRequestMarkerGroup.process(markerGroupDTORequestRefreshRequest);
        markerGroupDTOResponseToUpdate.setGobiiProcessType(GobiiProcessType.UPDATE);

        String previousName = markerGroupDTOResponseToUpdate.getName();
        String newName = UUID.randomUUID().toString();
        markerGroupDTOResponseToUpdate.setName(newName);


        // make pre-requisite marker
        String testMarkerName = "40539";
        makeMarker(testMarkerName);

        MarkerGroupMarkerDTO markerGroupMarkerDTOToAdd = new MarkerGroupMarkerDTO(GobiiProcessType.CREATE);
        markerGroupMarkerDTOToAdd.setMarkerName(testMarkerName);
        markerGroupMarkerDTOToAdd.setFavorableAllele("N");
        String newMarkerName = markerGroupMarkerDTOToAdd.getMarkerName();
        markerGroupDTOResponseToUpdate.getMarkers().add(markerGroupMarkerDTOToAdd);

        MarkerGroupMarkerDTO markerGroupMarkerDTOToRemove = markerGroupDTOResponseToUpdate
                .getMarkers()
                .get(0);
        String removedMarkerName = markerGroupMarkerDTOToRemove.getMarkerName();
        markerGroupMarkerDTOToRemove.setGobiiProcessType(GobiiProcessType.DELETE);

        MarkerGroupMarkerDTO markerGroupMarkerDTOToModify = markerGroupDTOResponseToUpdate
                .getMarkers()
                .get(1);
        Integer modifiedAlleleMarkerId = markerGroupMarkerDTOToModify.getMarkerId();
        String modifiedAlelleOldValue = markerGroupMarkerDTOToModify.getFavorableAllele();
        String modifiedAlleleNewValue = "X"; // not a legit value, but will work for our test
        markerGroupMarkerDTOToModify.setFavorableAllele(modifiedAlleleNewValue);
        markerGroupMarkerDTOToModify.setGobiiProcessType(GobiiProcessType.UPDATE);

        MarkerGroupDTO markerGroupDTOUpdated = dtoRequestMarkerGroup.process(markerGroupDTOResponseToUpdate);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(markerGroupDTOUpdated));


        // RE-RETREIVE again
        MarkerGroupDTO markerGroupDTOResponseRefreshFinal = dtoRequestMarkerGroup.process(markerGroupDTORequestRefreshRequest);
        Assert.assertNotNull(markerGroupDTOResponseRefreshFinal);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(markerGroupDTOResponseRefreshFinal));
        Assert.assertTrue(markerGroupDTOResponseRefreshFinal.getName().equals(newName));
        Assert.assertTrue(!markerGroupDTOResponseRefreshFinal.getName().equals(previousName));

        Assert.assertTrue(
                markerGroupDTOResponseRefreshFinal
                        .getMarkers()
                        .stream()
                        .filter(m -> m.getMarkerName().equals(newMarkerName))
                        .collect(Collectors.toList())
                        .size() == 1
        );

        Assert.assertTrue(
                markerGroupDTOResponseRefreshFinal
                        .getMarkers()
                        .stream()
                        .filter(m -> m.getMarkerName().equals(removedMarkerName))
                        .collect(Collectors.toList())
                        .size() == 0
        );


        Assert.assertFalse(modifiedAlleleNewValue.equals(modifiedAlelleOldValue));
        Assert.assertTrue(
                markerGroupDTOResponseRefreshFinal
                        .getMarkers()
                        .stream()
                        .filter(m ->
                                m.getMarkerId().equals(modifiedAlleleMarkerId)
                                        && m.getFavorableAllele().equals(modifiedAlleleNewValue))
                        .collect(Collectors.toList())
                        .size() == 1
        );

    }

    @Override
    public void getList() throws Exception {

    }

}
