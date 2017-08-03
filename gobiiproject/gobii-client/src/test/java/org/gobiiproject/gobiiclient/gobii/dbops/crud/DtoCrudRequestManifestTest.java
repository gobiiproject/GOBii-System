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
import org.gobiiproject.gobiimodel.headerlesscontainer.ManifestDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DtoCrudRequestManifestTest implements DtoCrudRequestTest {

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

        RestUri restUriManifest = GobiiClientContext.getInstance(null,false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_MANIFEST);
        GobiiEnvelopeRestResource<ManifestDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriManifest);
        PayloadEnvelope<ManifestDTO> resultEnvelope = gobiiEnvelopeRestResource.get(ManifestDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<ManifestDTO> manifestDTOList = resultEnvelope.getPayload().getData();
        Assert.assertNotNull(manifestDTOList);
        Assert.assertTrue(manifestDTOList.size() > 0);
        Assert.assertNotNull(manifestDTOList.get(0).getName());

        // use an arbitrary manifest id
        Integer manifestId = manifestDTOList.get(0).getManifestId();
        RestUri restUriManifestForGetById = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_MANIFEST);
        restUriManifestForGetById.setParamValue("id", manifestId.toString());
        GobiiEnvelopeRestResource<ManifestDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriManifestForGetById);
        PayloadEnvelope<ManifestDTO> resultEnvelopeForGetById = gobiiEnvelopeRestResourceForGetById
                .get(ManifestDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        ManifestDTO manifestDTO = resultEnvelopeForGetById.getPayload().getData().get(0);
        Assert.assertTrue(manifestDTO.getManifestId() > 0);
        Assert.assertNotNull(manifestDTO.getName());
    }

    @Test
    @Override
    public void testEmptyResult() throws Exception {

        DtoRestRequestUtils<ManifestDTO> dtoDtoRestRequestUtils = new DtoRestRequestUtils<>(ManifestDTO.class, GobiiServiceRequestId.URL_MANIFEST);
        Integer maxId = dtoDtoRestRequestUtils.getMaxPkVal();
        Integer nonExistentID = maxId + 1;

        PayloadEnvelope<ManifestDTO> resultEnvelope = dtoDtoRestRequestUtils.getResponseEnvelopeForEntityId(nonExistentID.toString());

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        Assert.assertNotNull(resultEnvelope.getPayload());
        Assert.assertNotNull(resultEnvelope.getPayload().getData());
        Assert.assertTrue(resultEnvelope.getPayload().getData().size() == 0);

    }


    @Test
    @Override
    public void create() throws Exception {

        ManifestDTO newManifestDto = TestDtoFactory
                .makePopulatedManifestDTO(GobiiProcessType.CREATE, 1);

        PayloadEnvelope<ManifestDTO> payloadEnvelope = new PayloadEnvelope<>(newManifestDto, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<ManifestDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_MANIFEST));
        PayloadEnvelope<ManifestDTO> manifestDTOResponseEnvelope = gobiiEnvelopeRestResource.post(ManifestDTO.class,
                payloadEnvelope);
        ManifestDTO manifestDTOResponse = manifestDTOResponseEnvelope.getPayload().getData().get(0);

        Assert.assertNotEquals(null, manifestDTOResponse);
        Assert.assertTrue(manifestDTOResponse.getManifestId() > 0);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(manifestDTOResponseEnvelope.getHeader()));

        GlobalPkValues.getInstance().addPkVal(GobiiEntityNameType.MANIFESTS, manifestDTOResponse.getManifestId());

        RestUri restUriManifestForGetById = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_MANIFEST);
        restUriManifestForGetById.setParamValue("id", manifestDTOResponse.getManifestId().toString());
        GobiiEnvelopeRestResource<ManifestDTO> gobiiEnvelopeRestResouceForGetById = new GobiiEnvelopeRestResource<>(restUriManifestForGetById);
        PayloadEnvelope<ManifestDTO> resultEnvelopeForGetById = gobiiEnvelopeRestResouceForGetById
                .get(ManifestDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetById.getHeader()));
        ManifestDTO manifestDTOResponseForParams = resultEnvelopeForGetById.getPayload().getData().get(0);

        GlobalPkValues.getInstance().addPkVal(GobiiEntityNameType.MANIFESTS, manifestDTOResponse.getManifestId());


    }


    @Test
    @Override
    public void update() throws Exception {

        // create a new manifest for our test
        ManifestDTO newManifestDto = TestDtoFactory
                .makePopulatedManifestDTO(GobiiProcessType.CREATE, 1);

        PayloadEnvelope<ManifestDTO> payloadEnvelope = new PayloadEnvelope<>(newManifestDto, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<ManifestDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_MANIFEST));
        PayloadEnvelope<ManifestDTO> manifestDTOResponseEnvelope = gobiiEnvelopeRestResource.post(ManifestDTO.class,
                payloadEnvelope);
        ManifestDTO newManifestDTOResponse = manifestDTOResponseEnvelope.getPayload().getData().get(0);

        // re-retrieve the manifest we just created so we start with a fresh READ mode dto

        RestUri restUriManifestForGetById = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_MANIFEST);
        restUriManifestForGetById.setParamValue("id", newManifestDTOResponse.getManifestId().toString());
        GobiiEnvelopeRestResource<ManifestDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriManifestForGetById);
        PayloadEnvelope<ManifestDTO> resultEnvelopeForGetByID = gobiiEnvelopeRestResourceForGetById
                .get(ManifestDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));
        ManifestDTO manifestDTOReceived = resultEnvelopeForGetByID.getPayload().getData().get(0);

        String newName = UUID.randomUUID().toString();
        manifestDTOReceived.setName(newName);
        gobiiEnvelopeRestResourceForGetById.setParamValue("id", manifestDTOReceived.getManifestId().toString());
        PayloadEnvelope<ManifestDTO> manifestDTOResponseEnvelopeUpdate = gobiiEnvelopeRestResourceForGetById.put(ManifestDTO.class,
                new PayloadEnvelope<>(manifestDTOReceived, GobiiProcessType.UPDATE));

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(manifestDTOResponseEnvelopeUpdate.getHeader()));

        ManifestDTO manifestDTORequest = manifestDTOResponseEnvelopeUpdate.getPayload().getData().get(0);


        restUriManifestForGetById.setParamValue("id", manifestDTORequest.getManifestId().toString());
        resultEnvelopeForGetByID = gobiiEnvelopeRestResourceForGetById
                .get(ManifestDTO.class);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));


        ManifestDTO dtoRequestManifestReRetrieved = resultEnvelopeForGetByID.getPayload().getData().get(0);


        Assert.assertTrue(dtoRequestManifestReRetrieved.getName().equals(newName));

    }

    @Override
    public void getList() throws Exception {

        RestUri restUriManifest = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_MANIFEST);
        GobiiEnvelopeRestResource<ManifestDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriManifest);
        PayloadEnvelope<ManifestDTO> resultEnvelope = gobiiEnvelopeRestResource
                .get(ManifestDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<ManifestDTO> manifestDTOList = resultEnvelope.getPayload().getData();
        Assert.assertNotNull(manifestDTOList);
        Assert.assertTrue(manifestDTOList.size() > 0);
        Assert.assertNotNull(manifestDTOList.get(0).getName());

        LinkCollection linkCollection = resultEnvelope.getPayload().getLinkCollection();
        Assert.assertTrue(linkCollection.getLinksPerDataItem().size() == manifestDTOList.size());

        List<Integer> itemsToTest = new ArrayList<>();
        if (manifestDTOList.size() > 50) {
            itemsToTest = TestUtils.makeListOfIntegersInRange(10, manifestDTOList.size());
        } else {
            for (int idx = 0; idx < manifestDTOList.size(); idx++) {
                itemsToTest.add(idx);
            }
        }

        for (Integer currentIdx : itemsToTest) {
            ManifestDTO currentManifestDto = manifestDTOList.get(currentIdx);

            Link currentLink = linkCollection.getLinksPerDataItem().get(currentIdx);

            RestUri restUriMapsetForGetById = GobiiClientContext.getInstance(null, false)
                    .getUriFactory()
                    .RestUriFromUri(currentLink.getHref());
            GobiiEnvelopeRestResource<ManifestDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriMapsetForGetById);
            PayloadEnvelope<ManifestDTO> resultEnvelopeForGetById = gobiiEnvelopeRestResourceForGetById
                    .get(ManifestDTO.class);
            Assert.assertNotNull(resultEnvelopeForGetById);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetById.getHeader()));
            ManifestDTO manifestDTOFromLink = resultEnvelopeForGetById.getPayload().getData().get(0);
            Assert.assertTrue(currentManifestDto.getName().equals(manifestDTOFromLink.getName()));
            Assert.assertTrue(currentManifestDto.getManifestId().equals(manifestDTOFromLink.getManifestId()));
        }


    }
}
