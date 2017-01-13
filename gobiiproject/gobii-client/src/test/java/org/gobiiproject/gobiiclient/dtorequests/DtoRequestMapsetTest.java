package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.EntityParamValues;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.EntityPropertyDTO;
import org.gobiiproject.gobiimodel.dto.container.MapsetDTO;
import org.gobiiproject.gobiimodel.dto.container.NameIdListDTO;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by Phil on 4/27/2016.
 */
public class DtoRequestMapsetTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
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
    public void testCreateMapset() throws Exception {

        //get terms for mapset properties:
        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
        nameIdListDTORequest.setEntityName("cvgroupterms");
        nameIdListDTORequest.setFilter("mapset_type");
        NameIdListDTO nameIdListDTO = dtoRequestNameIdList.process(nameIdListDTORequest);
        List<String> mapsetProperTerms = new ArrayList<>(nameIdListDTO
                .getNamesById()
                .values());
        DtoRequestMapset dtoRequestMapset = new DtoRequestMapset();
        EntityParamValues entityParamValues = TestDtoFactory
                .makeConstrainedEntityParams(mapsetProperTerms, 1);

        MapsetDTO mapsetDTORequest = TestDtoFactory
                .makePopulatedMapsetDTO(DtoMetaData.ProcessType.CREATE, 1, entityParamValues);

        MapsetDTO mapsetDTOResponse = dtoRequestMapset.process(mapsetDTORequest);

        Assert.assertNotEquals(null, mapsetDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(mapsetDTOResponse));
        Assert.assertTrue(mapsetDTOResponse.getMapsetId() > 1);

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
    public void testUpdateMapset() throws Exception {

        DtoRequestMapset dtoRequestMapset = new DtoRequestMapset();

        //get terms for mapset properties:
        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
        nameIdListDTORequest.setEntityName("cvgroupterms");
        nameIdListDTORequest.setFilter("mapset_type");
        NameIdListDTO nameIdListDTO = dtoRequestNameIdList.process(nameIdListDTORequest);
        List<String> mapsetProperTerms = new ArrayList<>(nameIdListDTO
                .getNamesById()
                .values());
        EntityParamValues entityParamValues = TestDtoFactory
                .makeConstrainedEntityParams(mapsetProperTerms, 1);


        // create a new mapset for our test
        MapsetDTO newMapsetDto = TestDtoFactory
                .makePopulatedMapsetDTO(DtoMetaData.ProcessType.CREATE, 1, entityParamValues);
        MapsetDTO newMapsetDTOResponse = dtoRequestMapset.process(newMapsetDto);


        // re-retrieve the mapset we just created so we start with a fresh READ mode dto
        MapsetDTO MapsetDTORequest = new MapsetDTO();
        MapsetDTORequest.setMapsetId(newMapsetDTOResponse.getMapsetId());
        MapsetDTO mapsetDTOReceived = dtoRequestMapset.process(MapsetDTORequest);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(mapsetDTOReceived));


        // so this would be the typical workflow for the client app
        mapsetDTOReceived.setProcessType(DtoMetaData.ProcessType.UPDATE);
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
}
