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
import org.gobiiproject.gobiiclient.gobii.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.headerlesscontainer.OrganizationDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Angel on 5/9/2016.
 */
public class DtoCrudRequestOrganizationTest implements DtoCrudRequestTest {


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

        OrganizationDTO newOrganizationDto = TestDtoFactory
                .makePopulatedOrganizationDTO(GobiiProcessType.CREATE, 1);

        PayloadEnvelope<OrganizationDTO> payloadEnvelope = new PayloadEnvelope<>(newOrganizationDto, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<OrganizationDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_ORGANIZATION));
        PayloadEnvelope<OrganizationDTO> organizationDTOResponseEnvelope = gobiiEnvelopeRestResource.post(OrganizationDTO.class,
                payloadEnvelope);
        OrganizationDTO organizationDTOResponse = organizationDTOResponseEnvelope.getPayload().getData().get(0);

        Assert.assertNotEquals(null, organizationDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(organizationDTOResponseEnvelope.getHeader()));
        Assert.assertTrue(organizationDTOResponse.getOrganizationId() > 0);

        GlobalPkValues.getInstance().addPkVal(GobiiEntityNameType.ORGANIZATIONS,
                organizationDTOResponse.getOrganizationId());


        RestUri restUriOrganizationForGetById = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_ORGANIZATION);
        restUriOrganizationForGetById.setParamValue("id", organizationDTOResponse.getOrganizationId().toString());
        GobiiEnvelopeRestResource<OrganizationDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriOrganizationForGetById);
        PayloadEnvelope<OrganizationDTO> resultEnvelopeForGetByID = gobiiEnvelopeRestResourceForGetById
                .get(OrganizationDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));
        OrganizationDTO organizationDTOResponseForParams = resultEnvelopeForGetByID.getPayload().getData().get(0);

        GlobalPkValues.getInstance().addPkVal(GobiiEntityNameType.ORGANIZATIONS, organizationDTOResponse.getOrganizationId());
    }

    @Test
    @Override
    public void update() throws Exception {

        // create a new organization for our test
        OrganizationDTO newOrganizationDto = TestDtoFactory
                .makePopulatedOrganizationDTO(GobiiProcessType.CREATE, 1);

        PayloadEnvelope<OrganizationDTO> payloadEnvelope = new PayloadEnvelope<>(newOrganizationDto, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<OrganizationDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_ORGANIZATION));
        PayloadEnvelope<OrganizationDTO> organizationDTOResponseEnvelope = gobiiEnvelopeRestResource.post(OrganizationDTO.class,
                payloadEnvelope);
        OrganizationDTO newOrganizationDTOResponse = organizationDTOResponseEnvelope.getPayload().getData().get(0);

        // re-retrieve the organization we just created so we start with a fresh READ mode dto

        RestUri restUriOrganizationForGetById = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_ORGANIZATION);
        restUriOrganizationForGetById.setParamValue("id", newOrganizationDTOResponse.getOrganizationId().toString());
        GobiiEnvelopeRestResource<OrganizationDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriOrganizationForGetById);
        PayloadEnvelope<OrganizationDTO> resultEnvelopeForGetByID = gobiiEnvelopeRestResourceForGetById
                .get(OrganizationDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));
        OrganizationDTO organizationDTOReceived = resultEnvelopeForGetByID.getPayload().getData().get(0);


        // so this would be the typical workflow for the client app
        String newName = UUID.randomUUID().toString();
        organizationDTOReceived.setName(newName);
        gobiiEnvelopeRestResourceForGetById.setParamValue("id", organizationDTOReceived.getOrganizationId().toString());
        PayloadEnvelope<OrganizationDTO> organizationDTOResponseEnvelopeUpdate = gobiiEnvelopeRestResourceForGetById.put(OrganizationDTO.class,
                new PayloadEnvelope<>(organizationDTOReceived, GobiiProcessType.UPDATE));

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(organizationDTOResponseEnvelopeUpdate.getHeader()));

        OrganizationDTO OrganizationDTORequest = organizationDTOResponseEnvelopeUpdate.getPayload().getData().get(0);

//        OrganizationDTO OrganizationDTOResponse = dtoRequestOrganization.process(organizationDTOReceived);
//        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(OrganizationDTOResponse));

        restUriOrganizationForGetById.setParamValue("id", OrganizationDTORequest.getOrganizationId().toString());
        resultEnvelopeForGetByID = gobiiEnvelopeRestResourceForGetById
                .get(OrganizationDTO.class);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));


        OrganizationDTO dtoRequestOrganizationReRetrieved = resultEnvelopeForGetByID.getPayload().getData().get(0);


        Assert.assertTrue(dtoRequestOrganizationReRetrieved.getName().equals(newName));
    }


    @Test
    @Override
    public void get() throws Exception {


        // get a list of organizations
        RestUri restUriOrganization = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_ORGANIZATION);
        GobiiEnvelopeRestResource<OrganizationDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriOrganization);
        PayloadEnvelope<OrganizationDTO> resultEnvelope = gobiiEnvelopeRestResource
                .get(OrganizationDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<OrganizationDTO> organizationDTOList = resultEnvelope.getPayload().getData();
        Assert.assertNotNull(organizationDTOList);
        Assert.assertTrue(organizationDTOList.size() > 0);
        Assert.assertNotNull(organizationDTOList.get(0).getName());


        // use an artibrary organization id
        Integer organizationId = organizationDTOList.get(0).getOrganizationId();
        RestUri restUriOrganizationForGetById = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_ORGANIZATION);
        restUriOrganizationForGetById.setParamValue("id", organizationId.toString());
        GobiiEnvelopeRestResource<OrganizationDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriOrganizationForGetById);
        PayloadEnvelope<OrganizationDTO> resultEnvelopeForGetByID = gobiiEnvelopeRestResourceForGetById
                .get(OrganizationDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));
        OrganizationDTO organizationDTO = resultEnvelopeForGetByID.getPayload().getData().get(0);
        Assert.assertTrue(organizationDTO.getOrganizationId() > 0);
        Assert.assertNotNull(organizationDTO.getName());
    }


    @Test
    public void testEmptyResult() throws Exception {

        DtoRestRequestUtils<OrganizationDTO> dtoDtoRestRequestUtils =
                new DtoRestRequestUtils<>(OrganizationDTO.class, GobiiServiceRequestId.URL_ORGANIZATION);
        Integer maxId = dtoDtoRestRequestUtils.getMaxPkVal();
        Integer nonExistentId = maxId + 1;


        PayloadEnvelope<OrganizationDTO> resultEnvelope =
                dtoDtoRestRequestUtils.getResponseEnvelopeForEntityId(nonExistentId.toString());

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        Assert.assertNotNull(resultEnvelope.getPayload());
        Assert.assertNotNull(resultEnvelope.getPayload().getData());
        Assert.assertTrue(resultEnvelope.getPayload().getData().size() == 0 );
    }



    @Test
    @Override
    public void getList() throws Exception {

        RestUri restUriOrganization = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_ORGANIZATION);
        GobiiEnvelopeRestResource<OrganizationDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriOrganization);
        PayloadEnvelope<OrganizationDTO> resultEnvelope = gobiiEnvelopeRestResource
                .get(OrganizationDTO.class);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<OrganizationDTO> organizationDTOList = resultEnvelope.getPayload().getData();
        Assert.assertNotNull(organizationDTOList);
        Assert.assertTrue(organizationDTOList.size() > 0);
        Assert.assertNotNull(organizationDTOList.get(0).getName());


        LinkCollection linkCollection = resultEnvelope.getPayload().getLinkCollection();
        Assert.assertTrue(linkCollection.getLinksPerDataItem().size() == organizationDTOList.size());

        List<Integer> itemsToTest = new ArrayList<>();
        if (organizationDTOList.size() > 50) {
            itemsToTest = TestUtils.makeListOfIntegersInRange(10, organizationDTOList.size());

        } else {
            for (int idx = 0; idx < organizationDTOList.size(); idx++) {
                itemsToTest.add(idx);
            }
        }

        for (Integer currentItemIdx : itemsToTest) {
            OrganizationDTO currentOrganizationDto = organizationDTOList.get(currentItemIdx);

            Link currentLink = linkCollection.getLinksPerDataItem().get(currentItemIdx);

            RestUri restUriOrganizationForGetById = GobiiClientContext.getInstance(null, false)
                    .getUriFactory()
                    .RestUriFromUri(currentLink.getHref());
            GobiiEnvelopeRestResource<OrganizationDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriOrganizationForGetById);
            PayloadEnvelope<OrganizationDTO> resultEnvelopeForGetByID = gobiiEnvelopeRestResourceForGetById
                    .get(OrganizationDTO.class);
            Assert.assertNotNull(resultEnvelopeForGetByID);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));
            OrganizationDTO organizationDTOFromLink = resultEnvelopeForGetByID.getPayload().getData().get(0);
            Assert.assertTrue(currentOrganizationDto.getName().equals(organizationDTOFromLink.getName()));
            Assert.assertTrue(currentOrganizationDto.getOrganizationId().equals(organizationDTOFromLink.getOrganizationId()));
        }

    }

}
