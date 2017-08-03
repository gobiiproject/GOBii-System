// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.gobii.dbops.crud;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.gobiiproject.gobiiapimodel.hateos.Link;
import org.gobiiproject.gobiiapimodel.hateos.LinkCollection;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.gobii.Helpers.*;
import org.gobiiproject.gobiimodel.headerlesscontainer.DisplayDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DtoCrudRequestDisplayTest implements DtoCrudRequestTest {

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
    public void get() throws Exception {

        RestUri restUriDisplay = GobiiClientContext.getInstance(null,false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_DISPLAY);
        GobiiEnvelopeRestResource<DisplayDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriDisplay);
        PayloadEnvelope<DisplayDTO> resultEnvelope = gobiiEnvelopeRestResource.get(DisplayDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<DisplayDTO> displayDTOList = resultEnvelope.getPayload().getData();
        Assert.assertNotNull(displayDTOList);
        Assert.assertTrue(displayDTOList.size() > 0);
        Assert.assertNotNull(displayDTOList.get(0).getTableName());

        // use an arbitrary display id
        Integer displayId = displayDTOList.get(0).getDisplayId();
        RestUri restUriDisplayForGetById = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_DISPLAY);
        restUriDisplayForGetById.setParamValue("id", displayId.toString());
        GobiiEnvelopeRestResource<DisplayDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriDisplayForGetById);
        PayloadEnvelope<DisplayDTO> resultEnvelopeForGetById = gobiiEnvelopeRestResourceForGetById
                .get(DisplayDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        DisplayDTO displayDTO = resultEnvelopeForGetById.getPayload().getData().get(0);
        Assert.assertTrue(displayDTO.getDisplayId() > 0);
        Assert.assertNotNull(displayDTO.getTableName());


    } // testGetDisplays()

    @Test
    @Override
    public void testEmptyResult() throws Exception {

        DtoRestRequestUtils<DisplayDTO> dtoDtoRestRequestUtils = new DtoRestRequestUtils<>(DisplayDTO.class,
                GobiiServiceRequestId.URL_DISPLAY);
        Integer maxId = dtoDtoRestRequestUtils.getMaxPkVal();
        Integer nonExistentID = maxId + 1;

        PayloadEnvelope<DisplayDTO> resultEnvelope = dtoDtoRestRequestUtils.getResponseEnvelopeForEntityId(nonExistentID.toString());

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        Assert.assertNotNull(resultEnvelope.getPayload());
        Assert.assertNotNull(resultEnvelope.getPayload().getData());
        Assert.assertTrue(resultEnvelope.getPayload().getData().size() == 0);

    }


    @Test
    @Override
    public void create() throws Exception {

        DisplayDTO newDisplayDto = TestDtoFactory
                .makePopulatedDisplayDTO(GobiiProcessType.CREATE, 1);

        PayloadEnvelope<DisplayDTO> payloadEnvelope = new PayloadEnvelope<>(newDisplayDto, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<DisplayDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_DISPLAY));
        PayloadEnvelope<DisplayDTO> displayDTOResponseEnvelope = gobiiEnvelopeRestResource.post(DisplayDTO.class,
                payloadEnvelope);
        DisplayDTO displayDTOResponse = displayDTOResponseEnvelope.getPayload().getData().get(0);

        Assert.assertNotEquals(null, displayDTOResponse);
        Assert.assertTrue(displayDTOResponse.getDisplayId() > 0);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(displayDTOResponseEnvelope.getHeader()));

        GlobalPkValues.getInstance().addPkVal(GobiiEntityNameType.DISPLAYNAMES, displayDTOResponse.getDisplayId());

        RestUri restUriDisplayForGetById = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_DISPLAY);
        restUriDisplayForGetById.setParamValue("id", displayDTOResponse.getDisplayId().toString());
        GobiiEnvelopeRestResource<DisplayDTO> gobiiEnvelopeRestResouceForGetById = new GobiiEnvelopeRestResource<>(restUriDisplayForGetById);
        PayloadEnvelope<DisplayDTO> resultEnvelopeForGetById = gobiiEnvelopeRestResouceForGetById
                .get(DisplayDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetById.getHeader()));
        DisplayDTO displaytDTOResponseForParams = resultEnvelopeForGetById.getPayload().getData().get(0);

        GlobalPkValues.getInstance().addPkVal(GobiiEntityNameType.DISPLAYNAMES, displayDTOResponse.getDisplayId());

    }


    @Test
    @Override
    public void update() throws Exception {

        // create a new display for our test
        DisplayDTO newDisplayDto = TestDtoFactory
                .makePopulatedDisplayDTO(GobiiProcessType.CREATE, 1);

        PayloadEnvelope<DisplayDTO> payloadEnvelope = new PayloadEnvelope<>(newDisplayDto, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<DisplayDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_DISPLAY));
        PayloadEnvelope<DisplayDTO> displayDTOResponseEnvelope = gobiiEnvelopeRestResource.post(DisplayDTO.class,
                payloadEnvelope);
        DisplayDTO newDisplayDTOResponse = displayDTOResponseEnvelope.getPayload().getData().get(0);

        // re-retrieve the display we just created so we start with a fresh READ mode dto

        RestUri restUriDisplayForGetById = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_DISPLAY);
        restUriDisplayForGetById.setParamValue("id", newDisplayDTOResponse.getDisplayId().toString());
        GobiiEnvelopeRestResource<DisplayDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriDisplayForGetById);
        PayloadEnvelope<DisplayDTO> resultEnvelopeForGetByID = gobiiEnvelopeRestResourceForGetById
                .get(DisplayDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));
        DisplayDTO displayDTOReceived = resultEnvelopeForGetByID.getPayload().getData().get(0);

        String newName = UUID.randomUUID().toString();
        displayDTOReceived.setTableName(newName);
        gobiiEnvelopeRestResourceForGetById.setParamValue("id", displayDTOReceived.getDisplayId().toString());
        PayloadEnvelope<DisplayDTO> displayDTOResponseEnvelopeUpdate = gobiiEnvelopeRestResourceForGetById.put(DisplayDTO.class,
                new PayloadEnvelope<>(displayDTOReceived, GobiiProcessType.UPDATE));

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(displayDTOResponseEnvelopeUpdate.getHeader()));

        DisplayDTO displayDTORequest = displayDTOResponseEnvelopeUpdate.getPayload().getData().get(0);


        restUriDisplayForGetById.setParamValue("id", displayDTORequest.getDisplayId().toString());
        resultEnvelopeForGetByID = gobiiEnvelopeRestResourceForGetById
                .get(DisplayDTO.class);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));


        DisplayDTO dtoRequestDisplayReRetrieved = resultEnvelopeForGetByID.getPayload().getData().get(0);


        Assert.assertTrue(dtoRequestDisplayReRetrieved.getTableName().equals(newName));
    }

    @Test
    @Override
    public void getList() throws Exception {

        RestUri restUriDisplay = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_DISPLAY);
        GobiiEnvelopeRestResource<DisplayDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriDisplay);
        PayloadEnvelope<DisplayDTO> resultEnvelope = gobiiEnvelopeRestResource
                .get(DisplayDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<DisplayDTO> displayDTOList = resultEnvelope.getPayload().getData();
        Assert.assertNotNull(displayDTOList);
        Assert.assertTrue(displayDTOList.size() > 0);
        Assert.assertNotNull(displayDTOList.get(0).getTableName());

        LinkCollection linkCollection = resultEnvelope.getPayload().getLinkCollection();
        Assert.assertTrue(linkCollection.getLinksPerDataItem().size() == displayDTOList.size());

        List<Integer> itemsToTest = new ArrayList<>();
        if (displayDTOList.size() > 50) {
            itemsToTest = TestUtils.makeListOfIntegersInRange(10, displayDTOList.size());
        } else {
            for (int idx = 0; idx < displayDTOList.size(); idx++) {
                itemsToTest.add(idx);
            }
        }

        for (Integer currentIdx : itemsToTest) {
            DisplayDTO currentDisplayDto = displayDTOList.get(currentIdx);

            Link currentLink = linkCollection.getLinksPerDataItem().get(currentIdx);

            RestUri restUriDisplayForGetById = GobiiClientContext.getInstance(null, false)
                    .getUriFactory()
                    .RestUriFromUri(currentLink.getHref());
            GobiiEnvelopeRestResource<DisplayDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriDisplayForGetById);
            PayloadEnvelope<DisplayDTO> resultEnvelopeForGetById = gobiiEnvelopeRestResourceForGetById
                    .get(DisplayDTO.class);
            Assert.assertNotNull(resultEnvelopeForGetById);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetById.getHeader()));
            DisplayDTO displayDTOFromLink = resultEnvelopeForGetById.getPayload().getData().get(0);
            Assert.assertTrue(currentDisplayDto.getTableName().equals(displayDTOFromLink.getTableName()));
            Assert.assertTrue(currentDisplayDto.getDisplayId().equals(displayDTOFromLink.getDisplayId()));
        }


    }

}
