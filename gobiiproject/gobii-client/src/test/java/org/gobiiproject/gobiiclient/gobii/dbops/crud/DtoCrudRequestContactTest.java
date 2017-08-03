// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.gobii.dbops.crud;


import org.gobiiproject.gobiiapimodel.hateos.Link;
import org.gobiiproject.gobiiapimodel.hateos.LinkCollection;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiclient.gobii.Helpers.DtoRestRequestUtils;
import org.gobiiproject.gobiiclient.gobii.Helpers.EntityParamValues;
import org.gobiiproject.gobiiclient.gobii.Helpers.GlobalPkColl;
import org.gobiiproject.gobiiclient.gobii.Helpers.GlobalPkValues;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.headerlesscontainer.ContactDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class DtoCrudRequestContactTest implements DtoCrudRequestTest {


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

        RestUri restUriContact = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_CONTACTS);
        restUriContact.setParamValue("id", "1");
        GobiiEnvelopeRestResource<ContactDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriContact);
        PayloadEnvelope<ContactDTO> resultEnvelope = gobiiEnvelopeRestResource
                .get(ContactDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        ContactDTO contactDTO = resultEnvelope.getPayload().getData().get(0);
        Assert.assertTrue(contactDTO.getContactId() > 0);
        Assert.assertNotNull(contactDTO.getEmail());
        Assert.assertTrue(contactDTO.getRoles().size() > 0);
    } //


    @Test
    public void testEmptyResult() throws Exception {

        DtoRestRequestUtils<ContactDTO> dtoDtoRestRequestUtils =
                new DtoRestRequestUtils<>(ContactDTO.class, GobiiServiceRequestId.URL_CONTACTS);

        Integer maxId = dtoDtoRestRequestUtils.getMaxPkVal();
        Integer nonExistentId = maxId + 1;

        PayloadEnvelope<ContactDTO> resultEnvelope =
                dtoDtoRestRequestUtils.getResponseEnvelopeForEntityId(nonExistentId.toString());

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        Assert.assertNotNull(resultEnvelope.getPayload());
        Assert.assertNotNull(resultEnvelope.getPayload().getData());
        Assert.assertTrue("Query for Contact with id " + nonExistentId.toString() + "  should not have gotten a result",
                resultEnvelope.getPayload().getData().size() == 0);
    }

//    @Test
//    public void testCreateContact() throws Exception {
//
//        DtoRequestContact dtoRequestContact = new DtoRequestContact();
//        ContactDTO contactDTORequest = new ContactDTO();
//
//        // set the plain properties
//        contactDTORequest.setFirstName("Angel Manica");
//        contactDTORequest.setLastName("Raquel");
//        contactDTORequest.setEmail("added dummy email");
//        contactDTORequest.setCode("added New Code");
//        contactDTORequest.setCreatedBy(1);
//        contactDTORequest.setCreatedDate(new Date());
//        contactDTORequest.setModifiedBy(1);
//        contactDTORequest.setModifiedDate(new Date());
//        contactDTORequest.setOrganizationId(1);
//        contactDTORequest.getRoles().add(1);
//        contactDTORequest.getRoles().add(2);
//
//        PayloadEnvelope<ContactDTO> contactDTOResponseEnvelope = dtoRequestContact.process(new PayloadEnvelope<>(contactDTORequest, GobiiProcessType.CREATE));
//        Assert.assertNotEquals(null, contactDTOResponseEnvelope);
//        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(contactDTOResponseEnvelope.getHeader()));
//
//        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(contactDTOResponseEnvelope.getHeader()));
//
//        ContactDTO contactDTOResponse = contactDTOResponseEnvelope.getPayload().getData().get(0);
//        Assert.assertNotEquals(null, contactDTOResponse);
//        Assert.assertTrue(contactDTOResponse.getContactId() > 0);
//
//    }

    @Test
    @Override
    public void update() throws Exception {


        // create a new contact for our test
        EntityParamValues entityParamValues = TestDtoFactory.makeArbitraryEntityParams();
        ContactDTO newContactDto = TestDtoFactory
                .makePopulatedContactDTO(GobiiProcessType.CREATE, UUID.randomUUID().toString());

        GobiiEnvelopeRestResource<ContactDTO> gobiiEnvelopeRestResourceContacts = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_CONTACTS));


        PayloadEnvelope<ContactDTO> resultEnvelopeNewContact = gobiiEnvelopeRestResourceContacts.post(ContactDTO.class,
                new PayloadEnvelope<>(newContactDto, GobiiProcessType.CREATE));
        //PayloadEnvelope<ContactDTO> resultEnvelopeNewContact = dtoRequestContact.process(new PayloadEnvelope<>(newContactDto, GobiiProcessType.CREATE));

        Assert.assertNotNull(resultEnvelopeNewContact);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeNewContact.getHeader()));
        Assert.assertTrue(resultEnvelopeNewContact.getPayload().getData().size() > 0);
        ContactDTO newContactDTOResponse = resultEnvelopeNewContact.getPayload().getData().get(0);


        RestUri restUriContact = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_CONTACTS);
        restUriContact.setParamValue("id", newContactDTOResponse.getContactId().toString());
        GobiiEnvelopeRestResource<ContactDTO> gobiiEnvelopeRestResourceContactById = new GobiiEnvelopeRestResource<>(restUriContact);
        PayloadEnvelope<ContactDTO> contactDTOResponseEnvelope = gobiiEnvelopeRestResourceContactById
                .get(ContactDTO.class);

        Assert.assertNotEquals(null, contactDTOResponseEnvelope);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(contactDTOResponseEnvelope.getHeader()));
        ContactDTO contactDTOReceived = contactDTOResponseEnvelope.getPayload().getData().get(0);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(contactDTOResponseEnvelope.getHeader()));


        // so this would be the typical workflow for the client app

        String newName = UUID.randomUUID().toString();
        contactDTOReceived.setLastName(newName);

        contactDTOReceived.setOrganizationId(null);

        //PayloadEnvelope<ContactDTO> contactDTOResponseEnvelopeUpdate = dtoRequestContact.process();

        gobiiEnvelopeRestResourceContactById.setParamValue("id", contactDTOReceived.getContactId().toString());
        PayloadEnvelope<ContactDTO> contactDTOResponseEnvelopeUpdate = gobiiEnvelopeRestResourceContactById.put(ContactDTO.class,
                new PayloadEnvelope<>(contactDTOReceived, GobiiProcessType.UPDATE));

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(contactDTOResponseEnvelopeUpdate.getHeader()));


        RestUri restUriContactReRetrive = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_CONTACTS);
        restUriContactReRetrive.setParamValue("id", contactDTOReceived.getContactId().toString());
        GobiiEnvelopeRestResource<ContactDTO> gobiiEnvelopeRestResourceReRetrieve = new GobiiEnvelopeRestResource<>(restUriContactReRetrive);
        PayloadEnvelope<ContactDTO> contactDTOResponseEnvelopeReRetrieved = gobiiEnvelopeRestResourceReRetrieve
                .get(ContactDTO.class);


        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(contactDTOResponseEnvelopeReRetrieved.getHeader()));
        ContactDTO dtoRequestContactReRetrieved =
                contactDTOResponseEnvelopeReRetrieved.getPayload().getData().get(0);

        Assert.assertTrue(dtoRequestContactReRetrieved.getLastName().equals(newName));
        Assert.assertNull(dtoRequestContactReRetrieved.getOrganizationId());

    }

    @Test
    public void getSingleContactWithHttpGet() throws Exception {

        RestUri restUriContact = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_CONTACTS);
        restUriContact.setParamValue("id", "1");
        GobiiEnvelopeRestResource<ContactDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriContact);
        PayloadEnvelope<ContactDTO> resultEnvelope = gobiiEnvelopeRestResource
                .get(ContactDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        ContactDTO contactDTO = resultEnvelope.getPayload().getData().get(0);
        Assert.assertNotNull(contactDTO.getEmail());

        //restUriContact.setParamValue(Param);
    }

    @Test
    public void getContactsBySearchWithHttpGet() throws Exception {

        RestUri restUriContact = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .contactsByQueryParams();
        restUriContact.setParamValue("email", "user1.gobii@gobii.org");
        GobiiEnvelopeRestResource<ContactDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriContact);
        PayloadEnvelope<ContactDTO> resultEnvelope = gobiiEnvelopeRestResource
                .get(ContactDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        ContactDTO contactDTO = resultEnvelope.getPayload().getData().get(0);
        Assert.assertNotNull(contactDTO);
        Assert.assertNotNull(contactDTO.getEmail());
        Assert.assertTrue(contactDTO.getRoles().size() > 0);

        //restUriContact.setParamValue(Param);
    }

    @Test
    public void getContactsBySearchForUserName() throws Exception {


        ContactDTO newContactDTO = new ContactDTO();

        Integer organizationId = (new GlobalPkColl<DtoCrudRequestOrganizationTest>()).getAPkVal(DtoCrudRequestOrganizationTest.class,
                GobiiEntityNameType.ORGANIZATIONS);

        String userName = UUID.randomUUID().toString();
        String emailAddress = UUID.randomUUID().toString();

        // set the plain properties
        newContactDTO.setFirstName("Authenticated");
        newContactDTO.setLastName("Guy");
        newContactDTO.setEmail(emailAddress);
        newContactDTO.setCode("added New Code");
        newContactDTO.setCreatedBy(1);
        newContactDTO.setCreatedDate(new Date());
        newContactDTO.setModifiedBy(1);
        newContactDTO.setModifiedDate(new Date());
        newContactDTO.setOrganizationId(organizationId);
        newContactDTO.getRoles().add(1);
        newContactDTO.getRoles().add(2);
        newContactDTO.setUserName(userName);

        //Set up the POST request to create the contact
        PayloadEnvelope<ContactDTO> payloadEnvelope = new PayloadEnvelope<>(newContactDTO, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<ContactDTO> gobiiEnvelopeRestResourceForPost = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_CONTACTS));
        PayloadEnvelope<ContactDTO> contactDTOResponseEnvelope = gobiiEnvelopeRestResourceForPost.post(ContactDTO.class,
                payloadEnvelope);
        Assert.assertNotEquals(null, contactDTOResponseEnvelope);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(contactDTOResponseEnvelope.getHeader()));


        RestUri restUriContact = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .contactsByQueryParams();
        restUriContact.setParamValue("userName", userName);
        GobiiEnvelopeRestResource<ContactDTO> gobiiEnvelopeRestResourceForGet = new GobiiEnvelopeRestResource<>(restUriContact);
        PayloadEnvelope<ContactDTO> resultEnvelope = gobiiEnvelopeRestResourceForGet
                .get(ContactDTO.class);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        Assert.assertTrue("Test user was not retrieved: " + userName,resultEnvelope.getPayload().getData().size() == 1);
        ContactDTO contactDTOFromGet = resultEnvelope.getPayload().getData().get(0);
        Assert.assertNotNull(contactDTOFromGet);
        Assert.assertNotNull(contactDTOFromGet.getEmail());
        Assert.assertTrue("Retrieved contact for user name does not match",contactDTOFromGet.getEmail().equals(emailAddress));
        Assert.assertNotNull(contactDTOFromGet.getUserName());
        Assert.assertTrue("Retrieved contact for user name does not match", contactDTOFromGet.getUserName().equals(userName));

        //restUriContact.setParamValue(Param);
    }

    @Test
    @Override
    public void create() throws Exception {


        // We are creating a new contact here.
        ContactDTO newContactDTO = new ContactDTO();

        Integer organizationId = (new GlobalPkColl<DtoCrudRequestOrganizationTest>()).getAPkVal(DtoCrudRequestOrganizationTest.class,
                GobiiEntityNameType.ORGANIZATIONS);

        // set the plain properties
        newContactDTO.setFirstName("Angel Manica");
        newContactDTO.setLastName("Raquel");
        newContactDTO.setEmail(UUID.randomUUID().toString());
        newContactDTO.setCode("added New Code");
        newContactDTO.setCreatedBy(1);
        newContactDTO.setCreatedDate(new Date());
        newContactDTO.setModifiedBy(1);
        newContactDTO.setModifiedDate(new Date());
        newContactDTO.setOrganizationId(organizationId);
        newContactDTO.getRoles().add(1);
        newContactDTO.getRoles().add(2);
        newContactDTO.setUserName("araquel " + UUID.randomUUID().toString());

        //Set up the POST request to create the contact
        PayloadEnvelope<ContactDTO> payloadEnvelope = new PayloadEnvelope<>(newContactDTO, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<ContactDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_CONTACTS));
        PayloadEnvelope<ContactDTO> contactDTOResponseEnvelope = gobiiEnvelopeRestResource.post(ContactDTO.class,
                payloadEnvelope);


        Assert.assertNotEquals(null, contactDTOResponseEnvelope);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(contactDTOResponseEnvelope.getHeader()));

        ContactDTO contactDTOResponse = contactDTOResponseEnvelope.getPayload().getData().get(0);
        Assert.assertNotEquals(null, contactDTOResponse);
        Assert.assertTrue(contactDTOResponse.getContactId() > 0);
        GlobalPkValues.getInstance().addPkVal(GobiiEntityNameType.CONTACTS, contactDTOResponse.getContactId());
        Assert.assertNotNull(contactDTOResponse.getUserName());

        //Now re-retrieve with the link we got back
        Assert.assertNotNull(contactDTOResponseEnvelope.getPayload().getLinkCollection());
        Assert.assertNotNull(contactDTOResponseEnvelope.getPayload().getLinkCollection().getLinksPerDataItem());
        Assert.assertNotNull(contactDTOResponseEnvelope.getPayload().getLinkCollection().getLinksPerDataItem().get(0));

        // The name of the game is for the server to be able to create these links
        // with as little code duplication as possible
        Link linkForCreatedItem = contactDTOResponseEnvelope.getPayload().getLinkCollection().getLinksPerDataItem().get(0);


        RestUri restUriContact = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .RestUriFromUri(linkForCreatedItem.getHref());
        GobiiEnvelopeRestResource<ContactDTO> gobiiEnvelopeRestResourceForReRetrieve = new GobiiEnvelopeRestResource<>(restUriContact);
        PayloadEnvelope<ContactDTO> reRetrieveResultEnvelope = gobiiEnvelopeRestResourceForReRetrieve
                .get(ContactDTO.class);
        Assert.assertNotNull(reRetrieveResultEnvelope);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(reRetrieveResultEnvelope.getHeader()));

        ContactDTO contactDTOReRetrieveResponse = reRetrieveResultEnvelope.getPayload().getData().get(0);
        Assert.assertNotNull(contactDTOReRetrieveResponse);
        Assert.assertTrue(contactDTOReRetrieveResponse.getContactId().equals(contactDTOResponse.getContactId()));

    }


    @Test
    @Override
    public void getList() throws Exception {

        RestUri restUriContact = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_CONTACTS);
        GobiiEnvelopeRestResource<ContactDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriContact);
        PayloadEnvelope<ContactDTO> resultEnvelope = gobiiEnvelopeRestResource
                .get(ContactDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<ContactDTO> contactDTOList = resultEnvelope.getPayload().getData();
        Assert.assertNotNull(contactDTOList);
        Assert.assertTrue(contactDTOList.size() > 0);
        Assert.assertNotNull(contactDTOList.get(0).getLastName());


        LinkCollection linkCollection = resultEnvelope.getPayload().getLinkCollection();
        Assert.assertTrue(linkCollection.getLinksPerDataItem().size() == contactDTOList.size());

        List<Integer> itemsToTest = new ArrayList<>();
        if (contactDTOList.size() > 50) {
            itemsToTest = TestUtils.makeListOfIntegersInRange(10, contactDTOList.size());

        } else {
            for (int idx = 0; idx < contactDTOList.size(); idx++) {
                itemsToTest.add(idx);
            }
        }

        for (Integer currentIdx : itemsToTest) {
            ContactDTO currentContactDto = contactDTOList.get(currentIdx);

            Link currentLink = linkCollection.getLinksPerDataItem().get(currentIdx);

            RestUri restUriContactForGetById = GobiiClientContext.getInstance(null, false)
                    .getUriFactory()
                    .RestUriFromUri(currentLink.getHref());
            GobiiEnvelopeRestResource<ContactDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriContactForGetById);
            PayloadEnvelope<ContactDTO> resultEnvelopeForGetByID = gobiiEnvelopeRestResourceForGetById
                    .get(ContactDTO.class);
            Assert.assertNotNull(resultEnvelopeForGetByID);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));
            ContactDTO contactDTOFromLink = resultEnvelopeForGetByID.getPayload().getData().get(0);
            Assert.assertTrue(currentContactDto.getLastName().equals(contactDTOFromLink.getLastName()));
            Assert.assertTrue(currentContactDto.getContactId().equals(contactDTOFromLink.getContactId()));
            Assert.assertTrue(currentContactDto.getRoles().size() > 0);
        }

    }


}
