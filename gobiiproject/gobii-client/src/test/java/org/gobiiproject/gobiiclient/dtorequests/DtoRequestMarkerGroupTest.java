// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;


import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.EntityParamValues;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.MarkerGroupDTO;
import org.gobiiproject.gobiimodel.dto.container.EntityPropertyDTO;
import org.gobiiproject.gobiimodel.dto.container.MarkerGroupMarkerDTO;
import org.gobiiproject.gobiimodel.dto.header.DtoHeaderResponse;
import org.gobiiproject.gobiimodel.dto.header.HeaderStatusMessage;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DtoRequestMarkerGroupTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }



    private List<String> validMarkerNames = Arrays.asList("4806",
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
    public void testMarkerGroupCreate() throws Exception {

        DtoRequestMarkerGroup dtoRequestMarkerGroup = new DtoRequestMarkerGroup();


        List<MarkerGroupMarkerDTO> markerGroupMarkers = TestDtoFactory.makeMarkerGroupMarkers(validMarkerNames,
                DtoMetaData.ProcessType.CREATE);

        MarkerGroupDTO markerGroupDTORequest = TestDtoFactory
                .makePopulatedMarkerGroupDTO(DtoMetaData.ProcessType.CREATE, 1, markerGroupMarkers);

        MarkerGroupDTO markerGroupDTOResponse = dtoRequestMarkerGroup.process(markerGroupDTORequest);

        Assert.assertNotEquals(null, markerGroupDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(markerGroupDTOResponse));
        Assert.assertTrue(markerGroupDTOResponse.getMarkerGroupId() > 1);

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
                DtoMetaData.ProcessType.CREATE);

        MarkerGroupDTO markerGroupDTORequest = TestDtoFactory
                .makePopulatedMarkerGroupDTO(DtoMetaData.ProcessType.CREATE, 1, markerGroupMarkers);

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
                DtoMetaData.ProcessType.CREATE);

        MarkerGroupDTO markerGroupDTORequest = TestDtoFactory
                .makePopulatedMarkerGroupDTO(DtoMetaData.ProcessType.CREATE, 1, markerGroupMarkers);

        MarkerGroupDTO markerGroupDTOResponse = dtoRequestMarkerGroup.process(markerGroupDTORequest);

        Assert.assertNotEquals(null, markerGroupDTOResponse);
        Assert.assertFalse(markerGroupDTOResponse.getDtoHeaderResponse().isSucceeded());

        Assert.assertTrue(null == markerGroupDTOResponse.getMarkerGroupId() || markerGroupDTOResponse.getMarkerGroupId() > 0);

        List<HeaderStatusMessage> invalidResponses = markerGroupDTOResponse
                .getDtoHeaderResponse()
                .getStatusMessages()
                .stream()
                .filter(m -> m.getValidationStatusType() == DtoHeaderResponse.ValidationStatusType.NONEXISTENT_FK_ENTITY)
                .collect(Collectors.toList());

        Assert.assertTrue(invalidResponses.size() == 1);

    }


    @Test
    public void testMarkerGroupGet() throws Exception {

        // CREATE A MARKER GROUP
        DtoRequestMarkerGroup dtoRequestMarkerGroup = new DtoRequestMarkerGroup();
        List<MarkerGroupMarkerDTO> markerGroupMarkers = TestDtoFactory.makeMarkerGroupMarkers(validMarkerNames,
                DtoMetaData.ProcessType.CREATE);
        MarkerGroupDTO markerGroupDTORequest = TestDtoFactory
                .makePopulatedMarkerGroupDTO(DtoMetaData.ProcessType.CREATE, 1, markerGroupMarkers);
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
    public void testUpdateMarkerGroup() throws Exception {

        // CREATE A MARKER GROUP
        DtoRequestMarkerGroup dtoRequestMarkerGroup = new DtoRequestMarkerGroup();
        List<MarkerGroupMarkerDTO> markerGroupMarkers = TestDtoFactory.makeMarkerGroupMarkers(validMarkerNames,
                DtoMetaData.ProcessType.CREATE);
        MarkerGroupDTO markerGroupDTORequest = TestDtoFactory
                .makePopulatedMarkerGroupDTO(DtoMetaData.ProcessType.CREATE, 1, markerGroupMarkers);
        MarkerGroupDTO markerGroupDTOResponse = dtoRequestMarkerGroup.process(markerGroupDTORequest);
        Integer newMarkerGroupId = markerGroupDTOResponse.getMarkerGroupId();


        // RE-RETREIVE
        MarkerGroupDTO markerGroupDTORequestRefreshRequest = new MarkerGroupDTO();
        markerGroupDTORequestRefreshRequest.setMarkerGroupId(newMarkerGroupId);
        MarkerGroupDTO markerGroupDTOResponseToUpdate = dtoRequestMarkerGroup.process(markerGroupDTORequestRefreshRequest);
        markerGroupDTOResponseToUpdate.setProcessType(DtoMetaData.ProcessType.UPDATE);

        String previousName = markerGroupDTOResponseToUpdate.getName();
        String newName = UUID.randomUUID().toString();
        markerGroupDTOResponseToUpdate.setName(newName);

        MarkerGroupMarkerDTO markerGroupMarkerDTOToAdd = new MarkerGroupMarkerDTO(DtoMetaData.ProcessType.CREATE);
        markerGroupMarkerDTOToAdd.setMarkerName("40539");
        markerGroupMarkerDTOToAdd.setFavorableAllele("N");
        String newMarkerName = markerGroupMarkerDTOToAdd.getMarkerName();
        markerGroupDTOResponseToUpdate.getMarkers().add(markerGroupMarkerDTOToAdd);

        MarkerGroupMarkerDTO markerGroupMarkerDTOToRemove = markerGroupDTOResponseToUpdate
                .getMarkers()
                .get(0);
        String removedMarkerName = markerGroupMarkerDTOToRemove.getMarkerName();
        markerGroupMarkerDTOToRemove.setProcessType(DtoMetaData.ProcessType.DELETE);

        MarkerGroupMarkerDTO markerGroupMarkerDTOToModify = markerGroupDTOResponseToUpdate
                .getMarkers()
                .get(1);
        Integer modifiedAlleleMarkerId = markerGroupMarkerDTOToModify.getMarkerId();
        String modifiedAlelleOldValue = markerGroupMarkerDTOToModify.getFavorableAllele();
        String modifiedAlleleNewValue = "X"; // not a legit value, but will work for our test
        markerGroupMarkerDTOToModify.setFavorableAllele(modifiedAlleleNewValue);
        markerGroupMarkerDTOToModify.setProcessType(DtoMetaData.ProcessType.UPDATE);

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

}
