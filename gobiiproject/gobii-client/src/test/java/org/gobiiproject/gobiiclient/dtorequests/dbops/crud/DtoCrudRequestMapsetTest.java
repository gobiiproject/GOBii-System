package org.gobiiproject.gobiiclient.dtorequests.dbops.crud;

import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiiapimodel.hateos.Link;
import org.gobiiproject.gobiiapimodel.hateos.LinkCollection;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.common.Authenticator;
import org.gobiiproject.gobiiclient.core.common.ClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.*;
import org.gobiiproject.gobiimodel.headerlesscontainer.MapsetDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.NameIdDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiFilterType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Phil on 4/27/2016.
 * Modified by AVB on 10/01/2016.
 */
public class DtoCrudRequestMapsetTest implements DtoCrudRequestTest {


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

        RestUri restUriMapset = ClientContext.getInstance(null,false)
                .getUriFactory()
                .resourceColl(ServiceRequestId.URL_MAPSET);
        GobiiEnvelopeRestResource<MapsetDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriMapset);
        PayloadEnvelope<MapsetDTO> resultEnvelope = gobiiEnvelopeRestResource.get(MapsetDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<MapsetDTO> mapsetDTOList = resultEnvelope.getPayload().getData();
        Assert.assertNotNull(mapsetDTOList);
        Assert.assertTrue(mapsetDTOList.size() > 0);
        Assert.assertNotNull(mapsetDTOList.get(0).getName());

        // use an arbitrary mapset id
        Integer mapsetId = mapsetDTOList.get(0).getMapsetId();
        RestUri restUriMapsetForGetById = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(ServiceRequestId.URL_MAPSET);
        restUriMapsetForGetById.setParamValue("id", mapsetId.toString());
        GobiiEnvelopeRestResource<MapsetDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriMapsetForGetById);
        PayloadEnvelope<MapsetDTO> resultEnvelopeForGetById = gobiiEnvelopeRestResourceForGetById
                .get(MapsetDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        MapsetDTO mapsetDTO = resultEnvelopeForGetById.getPayload().getData().get(0);
        Assert.assertTrue(mapsetDTO.getMapsetId() > 0);
        Assert.assertNotNull(mapsetDTO.getName());
    }

    @Test
    public void testEmptyResult() throws Exception {

        DtoRestRequestUtils<MapsetDTO> dtoDtoRestRequestUtils = new DtoRestRequestUtils<>(MapsetDTO.class, ServiceRequestId.URL_MAPSET);
        Integer maxId = dtoDtoRestRequestUtils.getMaxPkVal();
        Integer nonExistentID = maxId + 1;

        PayloadEnvelope<MapsetDTO> resultEnvelope = dtoDtoRestRequestUtils.getResponseEnvelopeForEntityId(nonExistentID.toString());

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        Assert.assertNotNull(resultEnvelope.getPayload());
        Assert.assertNotNull(resultEnvelope.getPayload().getData());
        Assert.assertTrue(resultEnvelope.getPayload().getData().size() == 0);

    }


    //waiting for properties stored procedure change to test this
    @Test
    public void create() throws Exception {

        RestUri namesUri = ClientContext.getInstance(null, false).getUriFactory().nameIdListByQueryParams();
        namesUri.setParamValue("entity", GobiiEntityNameType.CVTERMS.toString().toLowerCase());
        namesUri.setParamValue("filterType", StringUtils.capitalize(GobiiFilterType.BYTYPENAME.toString()));
        namesUri.setParamValue("filterValue", "mapset_type");

        GobiiEnvelopeRestResource<NameIdDTO> gobiiEnvelopeRestResourceForMapsetTerms = new GobiiEnvelopeRestResource<>(namesUri);
        PayloadEnvelope<NameIdDTO> resultEnvelope = gobiiEnvelopeRestResourceForMapsetTerms
                .get(NameIdDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<NameIdDTO> mapsetTypes = resultEnvelope.getPayload().getData();

        List<NameIdDTO> mapsetProperTerms = new ArrayList<>(mapsetTypes);
        EntityParamValues entityParamValues = TestDtoFactory
                .makeConstrainedEntityParams(mapsetProperTerms, 1);

        MapsetDTO newMapsetDto = TestDtoFactory
                .makePopulatedMapsetDTO(GobiiProcessType.CREATE, 1, entityParamValues);

        PayloadEnvelope<MapsetDTO> payloadEnvelope = new PayloadEnvelope<>(newMapsetDto, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<MapsetDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(ServiceRequestId.URL_MAPSET));
        PayloadEnvelope<MapsetDTO> mapsetDTOResponseEnvelope = gobiiEnvelopeRestResource.post(MapsetDTO.class,
                payloadEnvelope);
        MapsetDTO mapsetDTOResponse = mapsetDTOResponseEnvelope.getPayload().getData().get(0);

        Assert.assertNotEquals(null, mapsetDTOResponse);
        Assert.assertTrue(mapsetDTOResponse.getMapsetId() > 0);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(mapsetDTOResponseEnvelope.getHeader()));

        GlobalPkValues.getInstance().addPkVal(GobiiEntityNameType.MAPSETS, mapsetDTOResponse.getMapsetId());

        RestUri restUriMapsetForGetById = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(ServiceRequestId.URL_MAPSET);
        restUriMapsetForGetById.setParamValue("id", mapsetDTOResponse.getMapsetId().toString());
        GobiiEnvelopeRestResource<MapsetDTO> gobiiEnvelopeRestResouceForGetById = new GobiiEnvelopeRestResource<>(restUriMapsetForGetById);
        PayloadEnvelope<MapsetDTO> resultEnvelopeForGetById = gobiiEnvelopeRestResouceForGetById
                .get(MapsetDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetById.getHeader()));
        MapsetDTO mapsetDTOResponseForParams = resultEnvelopeForGetById.getPayload().getData().get(0);

        GlobalPkValues.getInstance().addPkVal(GobiiEntityNameType.MAPSETS, mapsetDTOResponse.getMapsetId());
    }


    //waiting for properties stored procedure change to test this
    @Test
    @Override
    public void update() throws Exception {

        RestUri namesUri = ClientContext.getInstance(null, false).getUriFactory().nameIdListByQueryParams();
        namesUri.setParamValue("entity", GobiiEntityNameType.CVTERMS.toString().toLowerCase());
        namesUri.setParamValue("filterType", StringUtils.capitalize(GobiiFilterType.BYTYPENAME.toString()));
        namesUri.setParamValue("filterValue", "mapset_type");

        GobiiEnvelopeRestResource<NameIdDTO> gobiiEnvelopeRestResourceForMapsetTerms = new GobiiEnvelopeRestResource<>(namesUri);
        PayloadEnvelope<NameIdDTO> resultEnvelope = gobiiEnvelopeRestResourceForMapsetTerms
                .get(NameIdDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<NameIdDTO> mapsetTypes = resultEnvelope.getPayload().getData();
        List<NameIdDTO> mapsetProperTerms = new ArrayList<>(mapsetTypes);

        EntityParamValues entityParamValues = TestDtoFactory
                .makeConstrainedEntityParams(mapsetProperTerms, 1);

        // create a new mapset for our test
        MapsetDTO newMapsetDto = TestDtoFactory
                .makePopulatedMapsetDTO(GobiiProcessType.CREATE, 1, entityParamValues);

        PayloadEnvelope<MapsetDTO> payloadEnvelope = new PayloadEnvelope<>(newMapsetDto, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<MapsetDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(ServiceRequestId.URL_MAPSET));
        PayloadEnvelope<MapsetDTO> mapsetDTOResponseEnvelope = gobiiEnvelopeRestResource.post(MapsetDTO.class,
                payloadEnvelope);
        MapsetDTO newMapsetDTOResponse = mapsetDTOResponseEnvelope.getPayload().getData().get(0);

        // re-retrieve the mapset we just created so we start with a fresh READ mode dto

        RestUri restUriMapsetForGetById = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(ServiceRequestId.URL_MAPSET);
        restUriMapsetForGetById.setParamValue("id", newMapsetDTOResponse.getMapsetId().toString());
        GobiiEnvelopeRestResource<MapsetDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriMapsetForGetById);
        PayloadEnvelope<MapsetDTO> resultEnvelopeForGetByID = gobiiEnvelopeRestResourceForGetById
                .get(MapsetDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));
        MapsetDTO mapsetDTOReceived = resultEnvelopeForGetByID.getPayload().getData().get(0);

        String newName = UUID.randomUUID().toString();
        mapsetDTOReceived.setName(newName);
        gobiiEnvelopeRestResourceForGetById.setParamValue("id", mapsetDTOReceived.getMapsetId().toString());
        PayloadEnvelope<MapsetDTO> mapsetDTOResponseEnvelopeUpdate = gobiiEnvelopeRestResourceForGetById.put(MapsetDTO.class,
                new PayloadEnvelope<>(mapsetDTOReceived, GobiiProcessType.UPDATE));

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(mapsetDTOResponseEnvelopeUpdate.getHeader()));

        MapsetDTO mapsetDTORequest = mapsetDTOResponseEnvelopeUpdate.getPayload().getData().get(0);


        restUriMapsetForGetById.setParamValue("id", mapsetDTORequest.getMapsetId().toString());
        resultEnvelopeForGetByID = gobiiEnvelopeRestResourceForGetById
                .get(MapsetDTO.class);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));


        MapsetDTO dtoRequestMapsetReRetrieved = resultEnvelopeForGetByID.getPayload().getData().get(0);


        Assert.assertTrue(dtoRequestMapsetReRetrieved.getName().equals(newName));

    }

    @Test
    @Override
    public void getList() throws Exception {

        RestUri restUriMapset = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(ServiceRequestId.URL_MAPSET);
        GobiiEnvelopeRestResource<MapsetDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriMapset);
        PayloadEnvelope<MapsetDTO> resultEnvelope = gobiiEnvelopeRestResource
                .get(MapsetDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<MapsetDTO> mapsetDTOList = resultEnvelope.getPayload().getData();
        Assert.assertNotNull(mapsetDTOList);
        Assert.assertTrue(mapsetDTOList.size() > 0);
        Assert.assertNotNull(mapsetDTOList.get(0).getName());

        LinkCollection linkCollection = resultEnvelope.getPayload().getLinkCollection();
        Assert.assertTrue(linkCollection.getLinksPerDataItem().size() == mapsetDTOList.size());

        List<Integer> itemsToTest = new ArrayList<>();
        if (mapsetDTOList.size() > 50) {
            itemsToTest = TestUtils.makeListOfIntegersInRange(10, mapsetDTOList.size());
        } else {
            for (int idx = 0; idx < mapsetDTOList.size(); idx++) {
                itemsToTest.add(idx);
            }
        }

        for (Integer currentIdx : itemsToTest) {
            MapsetDTO currentMapsetDto = mapsetDTOList.get(currentIdx);

            Link currentLink = linkCollection.getLinksPerDataItem().get(currentIdx);

            RestUri restUriMapsetForGetById = ClientContext.getInstance(null, false)
                    .getUriFactory()
                    .RestUriFromUri(currentLink.getHref());
            GobiiEnvelopeRestResource<MapsetDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriMapsetForGetById);
            PayloadEnvelope<MapsetDTO> resultEnvelopeForGetById = gobiiEnvelopeRestResourceForGetById
                    .get(MapsetDTO.class);
            Assert.assertNotNull(resultEnvelopeForGetById);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetById.getHeader()));
            MapsetDTO mapsetDTOFromLink = resultEnvelopeForGetById.getPayload().getData().get(0);
            Assert.assertTrue(currentMapsetDto.getName().equals(mapsetDTOFromLink.getName()));
            Assert.assertTrue(currentMapsetDto.getMapsetId().equals(mapsetDTOFromLink.getMapsetId()));
        }

    }
}
