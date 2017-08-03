package org.gobiiproject.gobiiclient.gobii.dbops.crud;

import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiiapimodel.hateos.Link;
import org.gobiiproject.gobiiapimodel.hateos.LinkCollection;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.gobii.Helpers.DtoRestRequestUtils;
import org.gobiiproject.gobiiclient.gobii.Helpers.EntityParamValues;
import org.gobiiproject.gobiiclient.gobii.Helpers.GlobalPkValues;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.headerlesscontainer.*;
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
import java.util.stream.Collectors;

/**
 * Created by Angel on 5/9/2016.
 */
public class DtoCrudRequestPlatformTest implements DtoCrudRequestTest {

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

        // BEGIN:   ****** THIS PART WILL HAVE TO BE REFACTORED LATER *********
//        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
//        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
//        nameIdListDTORequest.setEntityName("cvgroupterms");
//        nameIdListDTORequest.setFilter("platform_type");
//
//        NameIdListDTO nameIdListDTO = dtoRequestNameIdList.process(nameIdListDTORequest);
        RestUri namesUri = GobiiClientContext.getInstance(null, false).getUriFactory().nameIdListByQueryParams();
        namesUri.setParamValue("entity", GobiiEntityNameType.CVTERMS.toString().toLowerCase());
        namesUri.setParamValue("filterType", StringUtils.capitalize(GobiiFilterType.BYTYPENAME.toString()));
        namesUri.setParamValue("filterValue", "platform_type");

        GobiiEnvelopeRestResource<NameIdDTO> gobiiEnvelopeRestResourceForPlatformTerms = new GobiiEnvelopeRestResource<>(namesUri);
        PayloadEnvelope<NameIdDTO> resultEnvelope = gobiiEnvelopeRestResourceForPlatformTerms
                .get(NameIdDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<NameIdDTO> platformTypes = resultEnvelope.getPayload().getData();


        List<NameIdDTO> platformProperTerms = new ArrayList<>(platformTypes);
        EntityParamValues entityParamValues = TestDtoFactory
                .makeConstrainedEntityParams(platformProperTerms, 1);
        // END:   ****** THIS PART WILL HAVE TO BE REFACTORED LATER *********


        PlatformDTO newPlatformDto = TestDtoFactory
                .makePopulatedPlatformDTO(GobiiProcessType.CREATE, 1, entityParamValues);

        PayloadEnvelope<PlatformDTO> payloadEnvelope = new PayloadEnvelope<>(newPlatformDto, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<PlatformDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false).getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_PLATFORM));
        PayloadEnvelope<PlatformDTO> platformDTOResponseEnvelope = gobiiEnvelopeRestResource.post(PlatformDTO.class,
                payloadEnvelope);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(platformDTOResponseEnvelope.getHeader()));
        PlatformDTO platformDTOResponse = platformDTOResponseEnvelope.getPayload().getData().get(0);

        Assert.assertNotEquals(null, platformDTOResponse);
        Assert.assertTrue(platformDTOResponse.getPlatformId() > 0);
        GlobalPkValues.getInstance().addPkVal(GobiiEntityNameType.PLATFORMS, platformDTOResponse.getPlatformId());


        RestUri restUriPlatformForGetById = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_PLATFORM);
        restUriPlatformForGetById.setParamValue("id", platformDTOResponse.getPlatformId().toString());
        GobiiEnvelopeRestResource<PlatformDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriPlatformForGetById);
        PayloadEnvelope<PlatformDTO> resultEnvelopeForGetByID = gobiiEnvelopeRestResourceForGetById
                .get(PlatformDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));
        PlatformDTO platformDTOResponseForParams = resultEnvelopeForGetByID.getPayload().getData().get(0);


        Assert.assertNotEquals("Parameter collection is null", null, platformDTOResponseForParams.getProperties());
        Assert.assertTrue("No properties were returned",
                platformDTOResponseForParams.getProperties().size() > 0);

        List<EntityPropertyDTO> missing = entityParamValues
                .getMissingEntityProperties(platformDTOResponseForParams.getProperties());

        String missingItems = null;

        if (missing.size() > 0) {

            for (EntityPropertyDTO currentEntityPropDTO : missing) {
                missingItems += "Name: " + currentEntityPropDTO.getPropertyName()
                        + "Value: " + currentEntityPropDTO.getPropertyValue()
                        + "\n";
            }
        }

        Assert.assertNull("There are missing entity property items", missingItems);

        Assert.assertTrue("Parameter values do not match",
                entityParamValues.compare(platformDTOResponseForParams.getProperties()));


    }

    @Test
    @Override
    public void update() throws Exception {


        // BEGIN:   ****** THIS PART WILL HAVE TO BE REFACTORED LATER *********
        //get terms for platform properties:
//        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
//        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
//        nameIdListDTORequest.setEntityName("cvgroupterms");
//        nameIdListDTORequest.setFilter("platform_type");
//        NameIdListDTO nameIdListDTO = dtoRequestNameIdList.process(nameIdListDTORequest);
//        List<NameIdDTO> platformProperTerms = new ArrayList<>(nameIdListDTO
//                .getNamesById());

        RestUri namesUri = GobiiClientContext.getInstance(null, false).getUriFactory().nameIdListByQueryParams();
        namesUri.setParamValue("entity", GobiiEntityNameType.CVTERMS.toString().toLowerCase());
        namesUri.setParamValue("filterType", StringUtils.capitalize(GobiiFilterType.BYTYPENAME.toString()));
        namesUri.setParamValue("filterValue", "platform_type");

        GobiiEnvelopeRestResource<NameIdDTO> gobiiEnvelopeRestResourceForPlatformTerms = new GobiiEnvelopeRestResource<>(namesUri);
        PayloadEnvelope<NameIdDTO> resultEnvelope = gobiiEnvelopeRestResourceForPlatformTerms
                .get(NameIdDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<NameIdDTO> platformTypes = resultEnvelope.getPayload().getData();
        List<NameIdDTO> platformProperTerms = new ArrayList<>(platformTypes);

        EntityParamValues entityParamValues = TestDtoFactory
                .makeConstrainedEntityParams(platformProperTerms, 1);
        // END:   ****** THIS PART WILL HAVE TO BE REFACTORED LATER *********

        // create a new platform for our test
        PlatformDTO newPlatformDto = TestDtoFactory
                .makePopulatedPlatformDTO(GobiiProcessType.CREATE, 1, entityParamValues);

        PayloadEnvelope<PlatformDTO> payloadEnvelope = new PayloadEnvelope<>(newPlatformDto, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<PlatformDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_PLATFORM));
        PayloadEnvelope<PlatformDTO> platformDTOResponseEnvelope = gobiiEnvelopeRestResource.post(PlatformDTO.class,
                payloadEnvelope);
        PlatformDTO newPlatformDTOResponse = platformDTOResponseEnvelope.getPayload().getData().get(0);

        // re-retrieve the platform we just created so we start with a fresh READ mode dto

        RestUri restUriPlatformForGetById = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_PLATFORM);
        restUriPlatformForGetById.setParamValue("id", newPlatformDTOResponse.getPlatformId().toString());
        GobiiEnvelopeRestResource<PlatformDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriPlatformForGetById);
        PayloadEnvelope<PlatformDTO> resultEnvelopeForGetByID = gobiiEnvelopeRestResourceForGetById
                .get(PlatformDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));
        PlatformDTO platformDTOReceived = resultEnvelopeForGetByID.getPayload().getData().get(0);


        // so this would be the typical workflow for the client app
        String newName = UUID.randomUUID().toString();
        platformDTOReceived.setPlatformName(newName);

        EntityPropertyDTO propertyToUpdate = platformDTOReceived.getProperties().get(0);
        String updatedPropertyName = propertyToUpdate.getPropertyName();
        String updatedPropertyValue = UUID.randomUUID().toString();
        propertyToUpdate.setPropertyValue(updatedPropertyValue);

        restUriPlatformForGetById.setParamValue("id", platformDTOReceived.getPlatformId().toString());
        PayloadEnvelope<PlatformDTO> platformDTOResponseEnvelopeUpdate = gobiiEnvelopeRestResourceForGetById.put(PlatformDTO.class,
                new PayloadEnvelope<>(platformDTOReceived, GobiiProcessType.UPDATE));

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(platformDTOResponseEnvelopeUpdate.getHeader()));

        PlatformDTO PlatformDTORequest = platformDTOResponseEnvelopeUpdate.getPayload().getData().get(0);

//        PlatformDTO PlatformDTOResponse = dtoRequestPlatform.process(platformDTOReceived);
//        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(PlatformDTOResponse));

        restUriPlatformForGetById.setParamValue("id", PlatformDTORequest.getPlatformId().toString());
        resultEnvelopeForGetByID = gobiiEnvelopeRestResourceForGetById
                .get(PlatformDTO.class);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));


        PlatformDTO dtoRequestPlatformReRetrieved = resultEnvelopeForGetByID.getPayload().getData().get(0);


        Assert.assertTrue(dtoRequestPlatformReRetrieved.getPlatformName().equals(newName));
        EntityPropertyDTO matchedProperty = dtoRequestPlatformReRetrieved
                .getProperties()
                .stream()
                .filter(m -> m.getPropertyName().equals(updatedPropertyName))
                .collect(Collectors.toList())
                .get(0);

        Assert.assertTrue(matchedProperty.getPropertyValue().equals(updatedPropertyValue));
    }


    @Test
    @Override
    public void get() throws Exception {


        // get a list of platforms
        RestUri restUriPlatform = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_PLATFORM);
        GobiiEnvelopeRestResource<PlatformDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriPlatform);
        PayloadEnvelope<PlatformDTO> resultEnvelope = gobiiEnvelopeRestResource
                .get(PlatformDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<PlatformDTO> platformDTOList = resultEnvelope.getPayload().getData();
        Assert.assertNotNull(platformDTOList);
        Assert.assertTrue(platformDTOList.size() > 0);
        Assert.assertNotNull(platformDTOList.get(0).getPlatformName());


        // use an artibrary platform id
        Integer platformId = platformDTOList.get(0).getPlatformId();
        RestUri restUriPlatformForGetById = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_PLATFORM);
        restUriPlatformForGetById.setParamValue("id", platformId.toString());
        GobiiEnvelopeRestResource<PlatformDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriPlatformForGetById);
        PayloadEnvelope<PlatformDTO> resultEnvelopeForGetByID = gobiiEnvelopeRestResourceForGetById
                .get(PlatformDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        PlatformDTO platformDTO = resultEnvelopeForGetByID.getPayload().getData().get(0);
        Assert.assertTrue(platformDTO.getPlatformId() > 0);
        Assert.assertNotNull(platformDTO.getPlatformName());
    }

    @Test
    public void testEmptyResult() throws Exception {

        DtoRestRequestUtils<PlatformDTO> dtoDtoRestRequestUtils =
                new DtoRestRequestUtils<>(PlatformDTO.class, GobiiServiceRequestId.URL_PLATFORM);
        Integer maxId = dtoDtoRestRequestUtils.getMaxPkVal();
        Integer nonExistentId = maxId + 1;


        PayloadEnvelope<PlatformDTO> resultEnvelope
                = dtoDtoRestRequestUtils.getResponseEnvelopeForEntityId(nonExistentId.toString());

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        Assert.assertNotNull(resultEnvelope.getPayload());
        Assert.assertNotNull(resultEnvelope.getPayload().getData());
        Assert.assertTrue(resultEnvelope.getPayload().getData().size() == 0);
    }


    @Test
    @Override
    public void getList() throws Exception {

        RestUri restUriPlatform = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_PLATFORM);
        GobiiEnvelopeRestResource<PlatformDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriPlatform);
        PayloadEnvelope<PlatformDTO> resultEnvelope = gobiiEnvelopeRestResource
                .get(PlatformDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<PlatformDTO> platformDTOList = resultEnvelope.getPayload().getData();
        Assert.assertNotNull(platformDTOList);
        Assert.assertTrue(platformDTOList.size() > 0);
        Assert.assertNotNull(platformDTOList.get(0).getPlatformName());


        LinkCollection linkCollection = resultEnvelope.getPayload().getLinkCollection();
        Assert.assertTrue(linkCollection.getLinksPerDataItem().size() == platformDTOList.size());

        List<Integer> itemsToTest = new ArrayList<>();
        if (platformDTOList.size() > 50) {
            itemsToTest = TestUtils.makeListOfIntegersInRange(10, platformDTOList.size());

        } else {
            for (int idx = 0; idx < platformDTOList.size(); idx++) {
                itemsToTest.add(idx);
            }
        }

        for (Integer currentIdx : itemsToTest) {
            PlatformDTO currentPlatformDto = platformDTOList.get(currentIdx);

            Link currentLink = linkCollection.getLinksPerDataItem().get(currentIdx);

            RestUri restUriPlatformForGetById = GobiiClientContext.getInstance(null, false)
                    .getUriFactory()
                    .RestUriFromUri(currentLink.getHref());
            GobiiEnvelopeRestResource<PlatformDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriPlatformForGetById);
            PayloadEnvelope<PlatformDTO> resultEnvelopeForGetByID = gobiiEnvelopeRestResourceForGetById
                    .get(PlatformDTO.class);
            Assert.assertNotNull(resultEnvelopeForGetByID);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));
            PlatformDTO platformDTOFromLink = resultEnvelopeForGetByID.getPayload().getData().get(0);
            Assert.assertTrue(currentPlatformDto.getPlatformName().equals(platformDTOFromLink.getPlatformName()));
            Assert.assertTrue(currentPlatformDto.getPlatformId().equals(platformDTOFromLink.getPlatformId()));
        }

    }

}
