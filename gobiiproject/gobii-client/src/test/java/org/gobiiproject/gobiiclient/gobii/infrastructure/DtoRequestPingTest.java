// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.gobii.infrastructure;

import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.headerlesscontainer.PingDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DtoRequestPingTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.deAuthenticate());
    }


    @Test
    public void testGetPingFromLoadController() throws Exception {

        PingDTO pingDTORequest = TestDtoFactory.makePingDTO();

        //DtoRequestPing dtoRequestPing = new DtoRequestPing();
        GobiiEnvelopeRestResource<PingDTO> gobiiEnvelopeRestResourcePingDTO = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_PING));

        PayloadEnvelope<PingDTO> resultEnvelopePing = gobiiEnvelopeRestResourcePingDTO.post(PingDTO.class,
                new PayloadEnvelope<>(pingDTORequest, GobiiProcessType.CREATE));
        //PayloadEnvelope<ContactDTO> resultEnvelopeNewContact = dtoRequestContact.process(new PayloadEnvelope<>(newContactDto, GobiiProcessType.CREATE));

        Assert.assertNotNull(resultEnvelopePing);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopePing.getHeader()));
        Assert.assertTrue(resultEnvelopePing.getPayload().getData().size() > 0);
        PingDTO pingDTOResponse = resultEnvelopePing.getPayload().getData().get(0);


        Assert.assertNotEquals(null, pingDTOResponse);
        Assert.assertNotEquals(null, pingDTOResponse.getDbMetaData());
        Assert.assertNotEquals(null, pingDTOResponse.getPingResponses());
        Assert.assertTrue(pingDTOResponse.getPingResponses().size()
                >= pingDTORequest.getDbMetaData().size());

    } // testGetMarkers()


}
