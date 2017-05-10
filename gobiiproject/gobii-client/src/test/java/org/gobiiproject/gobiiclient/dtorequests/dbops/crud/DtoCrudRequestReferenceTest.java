// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests.dbops.crud;


import org.gobiiproject.gobiiapimodel.hateos.Link;
import org.gobiiproject.gobiiapimodel.hateos.LinkCollection;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.common.Authenticator;
import org.gobiiproject.gobiiclient.core.common.ClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.*;
import org.gobiiproject.gobiimodel.headerlesscontainer.ReferenceDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
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

        RestUri restUriMapset = ClientContext.getInstance(null,false)
                .getUriFactory()
                .resourceColl(ServiceRequestId.URL_REFERENCE);
        GobiiEnvelopeRestResource<ReferenceDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriMapset);
        PayloadEnvelope<ReferenceDTO> resultEnvelope = gobiiEnvelopeRestResource.get(ReferenceDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<ReferenceDTO> referenceDTOList = resultEnvelope.getPayload().getData();
        Assert.assertNotNull(referenceDTOList);
        Assert.assertTrue(referenceDTOList.size() > 0);
        Assert.assertNotNull(referenceDTOList.get(0).getName());

        // use an arbitrary reference id
        Integer referenceId = referenceDTOList.get(0).getReferenceId();
        RestUri restUriReferenceForGetById = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(ServiceRequestId.URL_REFERENCE);
        restUriReferenceForGetById.setParamValue("id", referenceId.toString());
        GobiiEnvelopeRestResource<ReferenceDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriReferenceForGetById);
        PayloadEnvelope<ReferenceDTO> resultEnvelopeForGetById = gobiiEnvelopeRestResourceForGetById
                .get(ReferenceDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        ReferenceDTO referenceDTO = resultEnvelopeForGetById.getPayload().getData().get(0);
        Assert.assertTrue(referenceDTO.getReferenceId() > 0);
        Assert.assertNotNull(referenceDTO.getName());

    }


    @Test
    @Override
    public void create() throws Exception {

        ReferenceDTO newReferenceDto = TestDtoFactory
                .makePopulatedReferenceDTO(GobiiProcessType.CREATE, 1);

        PayloadEnvelope<ReferenceDTO> payloadEnvelope = new PayloadEnvelope<>(newReferenceDto, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<ReferenceDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(ServiceRequestId.URL_REFERENCE));
        PayloadEnvelope<ReferenceDTO> referenceDTOResponseEnvelope = gobiiEnvelopeRestResource.post(ReferenceDTO.class,
                payloadEnvelope);
        ReferenceDTO referenceDTOResponse = referenceDTOResponseEnvelope.getPayload().getData().get(0);

        Assert.assertNotEquals(null, referenceDTOResponse);
        Assert.assertTrue(referenceDTOResponse.getReferenceId() > 0);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(referenceDTOResponseEnvelope.getHeader()));

        GlobalPkValues.getInstance().addPkVal(GobiiEntityNameType.REFERENCES, referenceDTOResponse.getReferenceId());

        RestUri restUriReferenceForGetById = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(ServiceRequestId.URL_REFERENCE);
        restUriReferenceForGetById.setParamValue("id", referenceDTOResponse.getReferenceId().toString());
        GobiiEnvelopeRestResource<ReferenceDTO> gobiiEnvelopeRestResouceForGetById = new GobiiEnvelopeRestResource<>(restUriReferenceForGetById);
        PayloadEnvelope<ReferenceDTO> resultEnvelopeForGetById = gobiiEnvelopeRestResouceForGetById
                .get(ReferenceDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetById.getHeader()));
        ReferenceDTO referenceDTOResponseForParams = resultEnvelopeForGetById.getPayload().getData().get(0);

        GlobalPkValues.getInstance().addPkVal(GobiiEntityNameType.REFERENCES, referenceDTOResponse.getReferenceId());


    }

    @Test
    @Override
    public void testEmptyResult() throws Exception {

        DtoRestRequestUtils<ReferenceDTO> dtoDtoRestRequestUtils =
                new DtoRestRequestUtils<>(ReferenceDTO.class,ServiceRequestId.URL_REFERENCE);
        Integer maxId = dtoDtoRestRequestUtils.getMaxPkVal();
        Integer nonExistentId = maxId + 1;


        PayloadEnvelope<ReferenceDTO> resultEnvelope =
                dtoDtoRestRequestUtils.getResponseEnvelopeForEntityId(nonExistentId.toString());

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        Assert.assertNotNull(resultEnvelope.getPayload());
        Assert.assertNotNull(resultEnvelope.getPayload().getData());
        Assert.assertTrue(resultEnvelope.getPayload().getData().size() == 0 );

    }

    @Test
    @Override
    public void update() throws Exception {

        // create a new reference for our test
        ReferenceDTO newReferenceDto = TestDtoFactory
                .makePopulatedReferenceDTO(GobiiProcessType.CREATE, 1);

        PayloadEnvelope<ReferenceDTO> payloadEnvelope = new PayloadEnvelope<>(newReferenceDto, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<ReferenceDTO> restResource = new GobiiEnvelopeRestResource<>(ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(ServiceRequestId.URL_REFERENCE));
        PayloadEnvelope<ReferenceDTO> referenceDTOResponseEnvelope = restResource.post(ReferenceDTO.class,
                payloadEnvelope);
        ReferenceDTO newReferenceDTOResponse = referenceDTOResponseEnvelope.getPayload().getData().get(0);

        // re-retrieve the reference we just created so we start with a fresh READ mode dto

        RestUri restUriReferenceForGetById = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(ServiceRequestId.URL_REFERENCE);
        restUriReferenceForGetById.setParamValue("id", newReferenceDTOResponse.getReferenceId().toString());
        GobiiEnvelopeRestResource<ReferenceDTO> restResourceForGetById = new GobiiEnvelopeRestResource<>(restUriReferenceForGetById);
        PayloadEnvelope<ReferenceDTO> resultEnvelopeForGetByID = restResourceForGetById
                .get(ReferenceDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));
        ReferenceDTO referenceDTOReceived = resultEnvelopeForGetByID.getPayload().getData().get(0);


        String newName = UUID.randomUUID().toString();
        referenceDTOReceived.setName(newName);
        restResourceForGetById.setParamValue("id", referenceDTOReceived.getReferenceId().toString());
        PayloadEnvelope<ReferenceDTO> referenceDTOResponseEnvelopeUpdate = restResourceForGetById.put(ReferenceDTO.class,
                new PayloadEnvelope<>(referenceDTOReceived, GobiiProcessType.UPDATE));

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(referenceDTOResponseEnvelopeUpdate.getHeader()));

        ReferenceDTO ReferenceDTORequest = referenceDTOResponseEnvelopeUpdate.getPayload().getData().get(0);


        restUriReferenceForGetById.setParamValue("id", ReferenceDTORequest.getReferenceId().toString());
        resultEnvelopeForGetByID = restResourceForGetById
                .get(ReferenceDTO.class);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));


        ReferenceDTO dtoRequestReferenceReRetrieved = resultEnvelopeForGetByID.getPayload().getData().get(0);


        Assert.assertTrue(dtoRequestReferenceReRetrieved.getName().equals(newName));


    }

    @Override
    public void getList() throws Exception {

        RestUri restUriReference = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(ServiceRequestId.URL_REFERENCE);
        GobiiEnvelopeRestResource<ReferenceDTO> restResource = new GobiiEnvelopeRestResource<>(restUriReference);
        PayloadEnvelope<ReferenceDTO> resultEnvelope = restResource
                .get(ReferenceDTO.class);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<ReferenceDTO> referenceDTOList = resultEnvelope.getPayload().getData();
        Assert.assertNotNull(referenceDTOList);
        Assert.assertTrue(referenceDTOList.size() > 0);
        Assert.assertNotNull(referenceDTOList.get(0).getName());


        LinkCollection linkCollection = resultEnvelope.getPayload().getLinkCollection();
        Assert.assertTrue(linkCollection.getLinksPerDataItem().size() == referenceDTOList.size());

        List<Integer> itemsToTest = new ArrayList<>();
        if (referenceDTOList.size() > 50) {
            itemsToTest = TestUtils.makeListOfIntegersInRange(10, referenceDTOList.size());

        } else {
            for (int idx = 0; idx < referenceDTOList.size(); idx++) {
                itemsToTest.add(idx);
            }
        }

        for (Integer currentItemIdx : itemsToTest) {
            ReferenceDTO currentReferenceDto = referenceDTOList.get(currentItemIdx);

            Link currentLink = linkCollection.getLinksPerDataItem().get(currentItemIdx);

            RestUri restUriReferenceForGetById = ClientContext.getInstance(null, false)
                    .getUriFactory()
                    .RestUriFromUri(currentLink.getHref());
            GobiiEnvelopeRestResource<ReferenceDTO> restResourceForGetById = new GobiiEnvelopeRestResource<>(restUriReferenceForGetById);
            PayloadEnvelope<ReferenceDTO> resultEnvelopeForGetByID = restResourceForGetById
                    .get(ReferenceDTO.class);
            Assert.assertNotNull(resultEnvelopeForGetByID);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));
            ReferenceDTO referenceDTOFromLink = resultEnvelopeForGetByID.getPayload().getData().get(0);
            Assert.assertTrue(currentReferenceDto.getName().equals(referenceDTOFromLink.getName()));
            Assert.assertTrue(currentReferenceDto.getReferenceId().equals(referenceDTOFromLink.getReferenceId()));
        }

    }
}
