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
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.gobii.Helpers.*;
import org.gobiiproject.gobiidao.entity.pojos.MarkerGroup;
import org.gobiiproject.gobiimodel.headerlesscontainer.MarkerGroupDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.MarkerGroupMarkerDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.MarkerDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.PlatformDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.junit.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


public class DtoCrudRequestMarkerGroupTest implements DtoCrudRequestTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.authenticate());


        for (String currentMarkerName : validMarkerNames) {

            makeMarker(currentMarkerName);

        }

    }

    private static MarkerDTO makeMarker(String markerName ) throws Exception {


        MarkerDTO returnVal = null;

        RestUri restUriMarkerByName = GobiiClientContext.getInstance(null, false)
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

            RestUri markerCollUri = GobiiClientContext.getInstance(null, false)
                    .getUriFactory()
                    .resourceColl(GobiiServiceRequestId.URL_MARKERS);
            GobiiEnvelopeRestResource<MarkerDTO> gobiiEnvelopeRestResourceForMarkerPost = new GobiiEnvelopeRestResource<>(markerCollUri);
            resultEnvelope = gobiiEnvelopeRestResourceForMarkerPost
                    .post(MarkerDTO.class, new PayloadEnvelope<>(markerDTORequest, GobiiProcessType.CREATE));

            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));

            MarkerDTO markerDTOResponse = resultEnvelope.getPayload().getData().get(0);

            returnVal = markerDTOResponse;
        }

        return returnVal;
    }


    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.deAuthenticate());
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

        // make pre-requisite marker
        String testMarkerName = "40539";
        MarkerDTO newMarkerDto = makeMarker(testMarkerName);
        MarkerGroupMarkerDTO markerGroupMarkerDTOToAdd = new MarkerGroupMarkerDTO(GobiiProcessType.CREATE);
        markerGroupMarkerDTOToAdd.setMarkerName(testMarkerName);
        markerGroupMarkerDTOToAdd.setFavorableAllele("N");

        // get platform name

        RestUri restUriPlatform = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_PLATFORM);
        GobiiEnvelopeRestResource<PlatformDTO> gobiiEnvelopeRestResourcePlatform = new GobiiEnvelopeRestResource<>(restUriPlatform);
        PayloadEnvelope<PlatformDTO> resultEnvelopePlatform = gobiiEnvelopeRestResourcePlatform
                .get(PlatformDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopePlatform.getHeader()));
        List<PlatformDTO> platformDTOList = resultEnvelopePlatform.getPayload().getData();
        Assert.assertNotNull(platformDTOList);
        Assert.assertTrue(platformDTOList.size() > 0);


        markerGroupMarkerDTOToAdd.setPlatformName(platformDTOList.get(0).getPlatformName());


        List<MarkerGroupMarkerDTO> markerGroupMarkerDTOS = new ArrayList<>();

        markerGroupMarkerDTOS.add(markerGroupMarkerDTOToAdd);


        MarkerGroupDTO newMarkerGroupDto = TestDtoFactory
                .makePopulatedMarkerGroupDTO(GobiiProcessType.CREATE, 1, markerGroupMarkerDTOS);

        PayloadEnvelope<MarkerGroupDTO> payloadEnvelope = new PayloadEnvelope<>(newMarkerGroupDto, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<MarkerGroupDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_MARKERGROUP));
        PayloadEnvelope<MarkerGroupDTO> markerGroupDTOResponseEnvelope = gobiiEnvelopeRestResource.post(MarkerGroupDTO.class,
                payloadEnvelope);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(markerGroupDTOResponseEnvelope.getHeader()));
        MarkerGroupDTO markerGroupDTOResponse = markerGroupDTOResponseEnvelope.getPayload().getData().get(0);
        Assert.assertNotEquals(null, markerGroupDTOResponse);
        Assert.assertTrue(markerGroupDTOResponse.getMarkerGroupId() > 0);

        GlobalPkValues.getInstance().addPkVal(GobiiEntityNameType.MARKERGROUPS, markerGroupDTOResponse.getMarkerGroupId());

        RestUri restUriMapsetForGetById = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_MARKERGROUP);
        restUriMapsetForGetById.setParamValue("id", markerGroupDTOResponse.getMarkerGroupId().toString());
        GobiiEnvelopeRestResource<MarkerGroupDTO> gobiiEnvelopeRestResouceForGetById = new GobiiEnvelopeRestResource<>(restUriMapsetForGetById);
        PayloadEnvelope<MarkerGroupDTO> resultEnvelopeForGetById = gobiiEnvelopeRestResouceForGetById
                .get(MarkerGroupDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetById.getHeader()));
        MarkerGroupDTO markerGroupDTOResponseForParams = resultEnvelopeForGetById.getPayload().getData().get(0);

        GlobalPkValues.getInstance().addPkVal(GobiiEntityNameType.MARKERGROUPS, markerGroupDTOResponse.getMarkerGroupId());

    }

    @Test
    @Override
    public void get() throws Exception {

        RestUri restUriMarkerGroup = GobiiClientContext.getInstance(null,false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_MARKERGROUP);
        GobiiEnvelopeRestResource<MarkerGroupDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriMarkerGroup);
        PayloadEnvelope<MarkerGroupDTO> resultEnvelope = gobiiEnvelopeRestResource.get(MarkerGroupDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<MarkerGroupDTO> markerGroupDTOList = resultEnvelope.getPayload().getData();
        Assert.assertNotNull(markerGroupDTOList);
        Assert.assertTrue(markerGroupDTOList.size() > 0);
        Assert.assertNotNull(markerGroupDTOList.get(0).getName());

        // use an arbitrary marker group id
        Integer markerGroupId = markerGroupDTOList.get(0).getMarkerGroupId();
        RestUri restUriMapsetForGetById = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_MARKERGROUP);
        restUriMapsetForGetById.setParamValue("id", markerGroupId.toString());
        GobiiEnvelopeRestResource<MarkerGroupDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriMapsetForGetById);
        PayloadEnvelope<MarkerGroupDTO> resultEnvelopeForGetById = gobiiEnvelopeRestResourceForGetById
                .get(MarkerGroupDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        MarkerGroupDTO markerGroupDTO = resultEnvelopeForGetById.getPayload().getData().get(0);
        Assert.assertTrue(markerGroupDTO.getMarkerGroupId() > 0);
        Assert.assertNotNull(markerGroupDTO.getName());
    }

    @Override
    public void testEmptyResult() throws Exception {

        DtoRestRequestUtils<MarkerGroupDTO> dtoDtoRestRequestUtils = new DtoRestRequestUtils<>(MarkerGroupDTO.class, GobiiServiceRequestId.URL_MARKERGROUP);
        Integer maxId = dtoDtoRestRequestUtils.getMaxPkVal();
        Integer nonExistentID = maxId + 1;

        PayloadEnvelope<MarkerGroupDTO> resultEnvelope = dtoDtoRestRequestUtils.getResponseEnvelopeForEntityId(nonExistentID.toString());

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        Assert.assertNotNull(resultEnvelope.getPayload());
        Assert.assertNotNull(resultEnvelope.getPayload().getData());
        Assert.assertTrue(resultEnvelope.getPayload().getData().size() == 0);

    }


    @Test
    @Override
    public void update() throws Exception {

        // make pre-requisite marker
        String testMarkerName = "40539";
        makeMarker(testMarkerName);
        MarkerGroupMarkerDTO markerGroupMarkerDTOToAdd = new MarkerGroupMarkerDTO(GobiiProcessType.CREATE);
        markerGroupMarkerDTOToAdd.setMarkerName(testMarkerName);
        markerGroupMarkerDTOToAdd.setFavorableAllele("N");

        // get platform name

        RestUri restUriPlatform = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_PLATFORM);
        GobiiEnvelopeRestResource<PlatformDTO> gobiiEnvelopeRestResourcePlatform = new GobiiEnvelopeRestResource<>(restUriPlatform);
        PayloadEnvelope<PlatformDTO> resultEnvelopePlatform = gobiiEnvelopeRestResourcePlatform
                .get(PlatformDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopePlatform.getHeader()));
        List<PlatformDTO> platformDTOList = resultEnvelopePlatform.getPayload().getData();
        Assert.assertNotNull(platformDTOList);
        Assert.assertTrue(platformDTOList.size() > 0);

        markerGroupMarkerDTOToAdd.setPlatformName(platformDTOList.get(0).getPlatformName());

        List<MarkerGroupMarkerDTO> markerGroupMarkerDTOS = new ArrayList<>();

        markerGroupMarkerDTOS.add(markerGroupMarkerDTOToAdd);

        // create a new marker group for our test
        MarkerGroupDTO newMarkerGroupDto = TestDtoFactory
                .makePopulatedMarkerGroupDTO(GobiiProcessType.CREATE, 1, markerGroupMarkerDTOS);

        PayloadEnvelope<MarkerGroupDTO> payloadEnvelope = new PayloadEnvelope<>(newMarkerGroupDto, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<MarkerGroupDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_MARKERGROUP));
        PayloadEnvelope<MarkerGroupDTO> markerGroupDTOResponseEnvelope = gobiiEnvelopeRestResource.post(MarkerGroupDTO.class,
                payloadEnvelope);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(markerGroupDTOResponseEnvelope.getHeader()));

        MarkerGroupDTO newMarkerGroupDTOResponse = markerGroupDTOResponseEnvelope.getPayload().getData().get(0);

        // re-retrieve the marker group we just created so we start with a fresh READ mode dto

        RestUri restUriMapsetForGetById = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_MARKERGROUP);
        restUriMapsetForGetById.setParamValue("id", newMarkerGroupDTOResponse.getMarkerGroupId().toString());
        GobiiEnvelopeRestResource<MarkerGroupDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriMapsetForGetById);
        PayloadEnvelope<MarkerGroupDTO> resultEnvelopeForGetByID = gobiiEnvelopeRestResourceForGetById
                .get(MarkerGroupDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));
        MarkerGroupDTO markerGroupDTOReceived = resultEnvelopeForGetByID.getPayload().getData().get(0);

        String newName = UUID.randomUUID().toString();
        markerGroupDTOReceived.setName(newName);

        MarkerGroupMarkerDTO newMarkerGroupMarkerDTOToAdd = new MarkerGroupMarkerDTO(GobiiProcessType.CREATE);
        newMarkerGroupMarkerDTOToAdd.setMarkerName(testMarkerName);
        newMarkerGroupMarkerDTOToAdd.setFavorableAllele("T");
        newMarkerGroupMarkerDTOToAdd.setPlatformName("1New Platform");

        List<MarkerGroupMarkerDTO> newMarkerGroupMarkerDTOS = new ArrayList<>();

        newMarkerGroupMarkerDTOS.add(newMarkerGroupMarkerDTOToAdd);

        markerGroupDTOReceived.setMarkers(newMarkerGroupMarkerDTOS);

        gobiiEnvelopeRestResourceForGetById.setParamValue("id", markerGroupDTOReceived.getMarkerGroupId().toString());
        PayloadEnvelope<MarkerGroupDTO> markerGroupDTOResponseEnvelopeUpdate = gobiiEnvelopeRestResourceForGetById.put(MarkerGroupDTO.class,
                new PayloadEnvelope<>(markerGroupDTOReceived, GobiiProcessType.UPDATE));

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(markerGroupDTOResponseEnvelopeUpdate.getHeader()));

        MarkerGroupDTO markerGroupDTORequest = markerGroupDTOResponseEnvelopeUpdate.getPayload().getData().get(0);


        restUriMapsetForGetById.setParamValue("id", markerGroupDTORequest.getMarkerGroupId().toString());
        resultEnvelopeForGetByID = gobiiEnvelopeRestResourceForGetById
                .get(MarkerGroupDTO.class);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));


        MarkerGroupDTO dtoRequestMarkerGroupReRetrieved = resultEnvelopeForGetByID.getPayload().getData().get(0);


        Assert.assertTrue(dtoRequestMarkerGroupReRetrieved.getName().equals(newName));

    }

    @Test
    @Override
    public void getList() throws Exception {

        RestUri restUriMarkerGroup = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_MARKERGROUP);
        GobiiEnvelopeRestResource<MarkerGroupDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriMarkerGroup);
        PayloadEnvelope<MarkerGroupDTO> resultEnvelope = gobiiEnvelopeRestResource
                .get(MarkerGroupDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<MarkerGroupDTO> markerGroupDTOList = resultEnvelope.getPayload().getData();
        Assert.assertNotNull(markerGroupDTOList);
        Assert.assertTrue(markerGroupDTOList.size() > 0);
        Assert.assertNotNull(markerGroupDTOList.get(0).getName());

        LinkCollection linkCollection = resultEnvelope.getPayload().getLinkCollection();
        Assert.assertTrue(linkCollection.getLinksPerDataItem().size() == markerGroupDTOList.size());

        List<Integer> itemsToTest = new ArrayList<>();
        if (markerGroupDTOList.size() > 50) {
            itemsToTest = TestUtils.makeListOfIntegersInRange(10, markerGroupDTOList.size());
        } else {
            for (int idx = 0; idx < markerGroupDTOList.size(); idx++) {
                itemsToTest.add(idx);
            }
        }

        for (Integer currentIdx : itemsToTest) {
            MarkerGroupDTO currentMarkerGroupDto = markerGroupDTOList.get(currentIdx);

            Link currentLink = linkCollection.getLinksPerDataItem().get(currentIdx);

            RestUri restUriMarkerGroupForGetById = GobiiClientContext.getInstance(null, false)
                    .getUriFactory()
                    .RestUriFromUri(currentLink.getHref());
            GobiiEnvelopeRestResource<MarkerGroupDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriMarkerGroupForGetById);
            PayloadEnvelope<MarkerGroupDTO> resultEnvelopeForGetById = gobiiEnvelopeRestResourceForGetById
                    .get(MarkerGroupDTO.class);
            Assert.assertNotNull(resultEnvelopeForGetById);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetById.getHeader()));
            MarkerGroupDTO markerGroupDTOFromLink = resultEnvelopeForGetById.getPayload().getData().get(0);
            Assert.assertTrue(currentMarkerGroupDto.getName().equals(markerGroupDTOFromLink.getName()));
            Assert.assertTrue(currentMarkerGroupDto.getMarkerGroupId().equals(markerGroupDTOFromLink.getMarkerGroupId()));
        }

    }

}
