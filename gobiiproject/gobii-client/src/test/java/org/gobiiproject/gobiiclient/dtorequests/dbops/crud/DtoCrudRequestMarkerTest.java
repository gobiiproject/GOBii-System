// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests.dbops.crud;


import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.common.ClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.core.common.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.DtoRestRequestUtils;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.GlobalPkColl;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.GlobalPkValues;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.headerlesscontainer.MarkerDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DtoCrudRequestMarkerTest implements DtoCrudRequestTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }


    private MarkerDTO getArbitraryMarkerDTO() throws Exception {

        MarkerDTO returnVal;

        Integer markerId = (new GlobalPkColl<DtoCrudRequestMarkerTest>().getAPkVal(DtoCrudRequestMarkerTest.class,
                GobiiEntityNameType.MARKERS));

        RestUri projectsUri = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(ServiceRequestId.URL_MARKERS);
        projectsUri.setParamValue("id", markerId.toString());
        GobiiEnvelopeRestResource<MarkerDTO> gobiiEnvelopeRestResourceForProjects = new GobiiEnvelopeRestResource<>(projectsUri);
        PayloadEnvelope<MarkerDTO> resultEnvelope = gobiiEnvelopeRestResourceForProjects
                .get(MarkerDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        returnVal = resultEnvelope.getPayload().getData().get(0);

        return returnVal;

    }

    @Test
    @Override
    public void get() throws Exception {


        MarkerDTO arbitraryMarkerDTO = this.getArbitraryMarkerDTO();

        Assert.assertNotEquals(null, arbitraryMarkerDTO);
        Assert.assertNotNull(arbitraryMarkerDTO.getMarkerName());
        Assert.assertNotNull(arbitraryMarkerDTO.getPlatformId());
        Assert.assertTrue(arbitraryMarkerDTO.getPlatformId() > 0);
        Assert.assertTrue(arbitraryMarkerDTO.getMarkerId() > 0);

    } //

    @Test
    public void testEmptyResult() throws Exception {

        DtoRestRequestUtils<MarkerDTO> dtoDtoRestRequestUtils =
                new DtoRestRequestUtils<>(MarkerDTO.class,ServiceRequestId.URL_MARKERS);
        Integer maxId = dtoDtoRestRequestUtils.getMaxPkVal();
        Integer nonExistentId = maxId + 1;


        PayloadEnvelope<MarkerDTO> resultEnvelope =
                dtoDtoRestRequestUtils.getResponseEnvelopeForEntityId(nonExistentId.toString());

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        Assert.assertNotNull(resultEnvelope.getPayload());
        Assert.assertNotNull(resultEnvelope.getPayload().getData());
        Assert.assertTrue(resultEnvelope.getPayload().getData().size() == 0 );
    }


    @Test
    @Override
    public void create() throws Exception {


        MarkerDTO markerDTORequest = TestDtoFactory
                .makeMarkerDTO("testmarker");

        RestUri markerCollUri = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(ServiceRequestId.URL_MARKERS);
        GobiiEnvelopeRestResource<MarkerDTO> gobiiEnvelopeRestResourceForMarkerPost = new GobiiEnvelopeRestResource<>(markerCollUri);
        PayloadEnvelope<MarkerDTO> resultEnvelope = gobiiEnvelopeRestResourceForMarkerPost
                .post(MarkerDTO.class, new PayloadEnvelope<>(markerDTORequest, GobiiProcessType.CREATE));

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        MarkerDTO markerDTOResponse = resultEnvelope.getPayload().getData().get(0);

        Assert.assertNotEquals(null, markerDTOResponse);
        Assert.assertNotNull(markerDTOResponse.getMarkerName());
        Assert.assertNotNull(markerDTOResponse.getPlatformId());
        Assert.assertTrue(markerDTOResponse.getPlatformId() > 0);

        Assert.assertTrue(markerDTOResponse.getMarkerId() > 0);
        GlobalPkValues.getInstance().addPkVal(GobiiEntityNameType.MARKERS,markerDTOResponse.getMarkerId());

    }

    @Test
    public void getMarkerByName() throws Exception {

        MarkerDTO arbitraryMarkerDTO = this.getArbitraryMarkerDTO();
        String arbitaryMarkerName = arbitraryMarkerDTO.getMarkerName();
        RestUri restUriContact = ClientContext.getInstance(null,false)
                .getUriFactory()
                .markerssByQueryParams();
        restUriContact.setParamValue("name", arbitaryMarkerName);

        GobiiEnvelopeRestResource<MarkerDTO> gobiiEnvelopeRestResourceForProjects = new GobiiEnvelopeRestResource<>(restUriContact);
        PayloadEnvelope<MarkerDTO> resultEnvelope = gobiiEnvelopeRestResourceForProjects
                .get(MarkerDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        MarkerDTO markerDTOResponse = resultEnvelope.getPayload().getData().get(0);

        Assert.assertNotEquals(null, arbitraryMarkerDTO);
        Assert.assertNotNull(markerDTOResponse.getMarkerName());
        Assert.assertNotNull(markerDTOResponse.getPlatformId());
        Assert.assertTrue(markerDTOResponse.getPlatformId() > 0);

        Assert.assertTrue(markerDTOResponse.getMarkerId() > 0);


    }

    @Test
    @Override
    public void update() throws Exception {


    }


    @Test
    @Override
    public void getList() throws Exception {



    }
}
