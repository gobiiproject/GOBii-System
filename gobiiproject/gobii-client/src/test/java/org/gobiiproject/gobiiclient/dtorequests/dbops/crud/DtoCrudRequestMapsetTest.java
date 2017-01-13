package org.gobiiproject.gobiiclient.dtorequests.dbops.crud;

import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.common.ClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.dtorequests.DtoRequestMapset;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.EntityParamValues;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.GlobalPkValues;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.container.EntityPropertyDTO;
import org.gobiiproject.gobiimodel.dto.container.MapsetDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.NameIdDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiFilterType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        for (int mapsetDTOIndex = 0; mapsetDTOIndex < mapsetDTOList.size(); mapsetDTOIndex++) {
            Assert.assertNotNull(mapsetDTOList.get(mapsetDTOIndex).getName()); }
    }

    @Test
    public void testEmptyResult() throws Exception {

    }


    @Test
    public void testGetMapsetDetails() throws Exception {
        DtoRequestMapset dtoRequestMapset = new DtoRequestMapset();
        MapsetDTO mapsetDTORequest = new MapsetDTO();
        mapsetDTORequest.setMapsetId(2);
        MapsetDTO mapsetDTOResponse = dtoRequestMapset.process(mapsetDTORequest);

        Assert.assertNotEquals(null, mapsetDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(mapsetDTOResponse));
        Assert.assertFalse(mapsetDTOResponse.getName().isEmpty());
        Assert.assertTrue(mapsetDTOResponse.getMapsetId().equals(2));
    }

    //waiting for properties stored procedure change to test this
    @Test
    @Override
    public void create() throws Exception {
        //get terms for mapset properties:
//        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
//        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
//        nameIdListDTORequest.setEntityName("cvgroupterms");
//        nameIdListDTORequest.setFilter("mapset_type");
//        NameIdListDTO nameIdListDTO = dtoRequestNameIdList.process(nameIdListDTORequest);
//        List<NameIdDTO> mapsetProperTerms = new ArrayList<>(nameIdListDTO
//                .getNamesById());

        RestUri namesUri = ClientContext.getInstance(null,false)
                .getUriFactory()
                .nameIdListByQueryParams();
        namesUri.setParamValue("entity", GobiiEntityNameType.CVTERMS.toString().toLowerCase());
        namesUri.setParamValue("filterType", StringUtils.capitalize(GobiiFilterType.BYTYPENAME.toString()));
        namesUri.setParamValue("filterValue", "mapset_type");

        GobiiEnvelopeRestResource<NameIdDTO> gobiiEnvelopeRestResourceForPlatformTerms = new GobiiEnvelopeRestResource<>(namesUri);
        PayloadEnvelope<NameIdDTO> resultEnvelope = gobiiEnvelopeRestResourceForPlatformTerms
                .get(NameIdDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<NameIdDTO> mapsetTypes = resultEnvelope.getPayload().getData();


        DtoRequestMapset dtoRequestMapset = new DtoRequestMapset();
        EntityParamValues entityParamValues = TestDtoFactory
                .makeConstrainedEntityParams(mapsetTypes, 1);

        MapsetDTO mapsetDTORequest = TestDtoFactory
                .makePopulatedMapsetDTO(GobiiProcessType.CREATE, 1, entityParamValues);

        MapsetDTO mapsetDTOResponse = dtoRequestMapset.process(mapsetDTORequest);

        Assert.assertNotEquals(null, mapsetDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(mapsetDTOResponse));
        Assert.assertTrue(mapsetDTOResponse.getMapsetId() > 0);
        GlobalPkValues.getInstance().addPkVal(GobiiEntityNameType.MAPSETS, mapsetDTOResponse.getMapsetId());

        MapsetDTO mapsetDTORequestForParams = new MapsetDTO();
        mapsetDTORequestForParams.setMapsetId(mapsetDTOResponse.getMapsetId());
        MapsetDTO mapsetDTOResponseForParams = dtoRequestMapset.process(mapsetDTORequestForParams);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(mapsetDTOResponseForParams));

        Assert.assertNotEquals("Parameter collection is null", null, mapsetDTOResponseForParams.getProperties());
        Assert.assertTrue("No properties were returned",
                mapsetDTOResponseForParams.getProperties().size() > 0);
        Assert.assertTrue("Parameter values do not match",
               entityParamValues.compare(mapsetDTOResponseForParams.getProperties()));
    }


    //waiting for properties stored procedure change to test this
    @Test
    @Override
    public void update() throws Exception {

        DtoRequestMapset dtoRequestMapset = new DtoRequestMapset();

        //get terms for mapset properties:
//        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
//        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
//        nameIdListDTORequest.setEntityName("cvgroupterms");
//        nameIdListDTORequest.setFilter("mapset_type");
//        NameIdListDTO nameIdListDTO = dtoRequestNameIdList.process(nameIdListDTORequest);
//        List<NameIdDTO> mapsetProperTerms = new ArrayList<>(nameIdListDTO
//                .getNamesById());

        RestUri namesUri = ClientContext.getInstance(null,false)
                .getUriFactory()
                .nameIdListByQueryParams();
        namesUri.setParamValue("entity", GobiiEntityNameType.CVTERMS.toString().toLowerCase());
        namesUri.setParamValue("filterType", StringUtils.capitalize(GobiiFilterType.BYTYPENAME.toString()));
        namesUri.setParamValue("filterValue", "mapset_type");

        GobiiEnvelopeRestResource<NameIdDTO> gobiiEnvelopeRestResourceForPlatformTerms = new GobiiEnvelopeRestResource<>(namesUri);
        PayloadEnvelope<NameIdDTO> resultEnvelope = gobiiEnvelopeRestResourceForPlatformTerms
                .get(NameIdDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<NameIdDTO> mapsetTypes = resultEnvelope.getPayload().getData();
        EntityParamValues entityParamValues = TestDtoFactory
                .makeConstrainedEntityParams(mapsetTypes, 1);


        // create a new mapset for our test
        MapsetDTO newMapsetDto = TestDtoFactory
                .makePopulatedMapsetDTO(GobiiProcessType.CREATE, 1, entityParamValues);
        MapsetDTO newMapsetDTOResponse = dtoRequestMapset.process(newMapsetDto);


        // re-retrieve the mapset we just created so we start with a fresh READ mode dto
        MapsetDTO MapsetDTORequest = new MapsetDTO();
        MapsetDTORequest.setMapsetId(newMapsetDTOResponse.getMapsetId());
        MapsetDTO mapsetDTOReceived = dtoRequestMapset.process(MapsetDTORequest);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(mapsetDTOReceived));


        // so this would be the typical workflow for the client app
        mapsetDTOReceived.setGobiiProcessType(GobiiProcessType.UPDATE);
        String newName = UUID.randomUUID().toString();
        mapsetDTOReceived.setName(newName);

        EntityPropertyDTO propertyToUpdate = mapsetDTOReceived.getProperties().get(0);
        String updatedPropertyName = propertyToUpdate.getPropertyName();
        String updatedPropertyValue = UUID.randomUUID().toString();
        propertyToUpdate.setPropertyValue(updatedPropertyValue);

        MapsetDTO MapsetDTOResponse = dtoRequestMapset.process(mapsetDTOReceived);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(MapsetDTOResponse));


        MapsetDTO dtoRequestMapsetReRetrieved =
                dtoRequestMapset.process(MapsetDTORequest);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dtoRequestMapsetReRetrieved));

        Assert.assertTrue(dtoRequestMapsetReRetrieved.getName().equals(newName));

        EntityPropertyDTO matchedProperty = dtoRequestMapsetReRetrieved
                .getProperties()
                .stream()
                .filter(m -> m.getPropertyName().equals(updatedPropertyName))
                .collect(Collectors.toList())
                .get(0);

        Assert.assertTrue(matchedProperty.getPropertyValue().equals(updatedPropertyValue));

    }

    @Override
    public void getList() throws Exception {

    }
}
