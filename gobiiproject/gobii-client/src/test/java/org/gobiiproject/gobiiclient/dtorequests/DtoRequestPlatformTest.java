package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.EntityParamValues;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.*;
import org.gobiiproject.gobiimodel.dto.container.PlatformDTO;
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
public class DtoRequestPlatformTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }


    @Test
    public void testGetPlatformDetails() throws Exception {
        DtoRequestPlatform dtoRequestPlatform = new DtoRequestPlatform();
        PlatformDTO PlatformDTORequest = new PlatformDTO();
        PlatformDTORequest.setPlatformId(1);
        PlatformDTO PlatformDTOResponse = dtoRequestPlatform.process(PlatformDTORequest);

        Assert.assertNotEquals(null, PlatformDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(PlatformDTOResponse));
        Assert.assertFalse(PlatformDTOResponse.getPlatformName().isEmpty());
        Assert.assertTrue(PlatformDTOResponse.getPlatformId() == 1);
    }


    @Test
    public void testCreatePlatform() throws Exception {
        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
        nameIdListDTORequest.setEntityName("cvgroupterms");
        nameIdListDTORequest.setFilter("platform_type");

        NameIdListDTO nameIdListDTO = dtoRequestNameIdList.process(nameIdListDTORequest);
        List<String> platformProperTerms = new ArrayList<>(nameIdListDTO
                .getNamesById()
                .values());
        EntityParamValues entityParamValues = TestDtoFactory
                .makeConstrainedEntityParams(platformProperTerms, 1);

        DtoRequestPlatform dtoRequestPlatform = new DtoRequestPlatform();

        PlatformDTO newPlatformDto = TestDtoFactory
                .makePopulatedPlatformDTO(DtoMetaData.ProcessType.CREATE, 1, entityParamValues);

        PlatformDTO platformDTOResponse = dtoRequestPlatform.process(newPlatformDto);
        Assert.assertNotEquals(null, platformDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(platformDTOResponse));
        Assert.assertTrue(platformDTOResponse.getPlatformId() > 0);


        PlatformDTO platformDTORequestForParams = new PlatformDTO();
        platformDTORequestForParams.setPlatformId(platformDTOResponse.getPlatformId());
        PlatformDTO platformDTOResponseForParams = dtoRequestPlatform.process(platformDTORequestForParams);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(platformDTOResponseForParams));

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

        Assert.assertNull("There are missing entity property items",missingItems);

        Assert.assertTrue("Parameter values do not match",
                entityParamValues.compare(platformDTOResponseForParams.getProperties()));


    }

    @Test
    public void testUpdatePlatform() throws Exception {
        DtoRequestPlatform dtoRequestPlatform = new DtoRequestPlatform();

        //get terms for platform properties:
        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
        nameIdListDTORequest.setEntityName("cvgroupterms");
        nameIdListDTORequest.setFilter("platform_type");
        NameIdListDTO nameIdListDTO = dtoRequestNameIdList.process(nameIdListDTORequest);
        List<String> platformProperTerms = new ArrayList<>(nameIdListDTO
                .getNamesById()
                .values());
        EntityParamValues entityParamValues = TestDtoFactory
                .makeConstrainedEntityParams(platformProperTerms, 1);

        // create a new platform for our test
        PlatformDTO newPlatformDto = TestDtoFactory
                .makePopulatedPlatformDTO(DtoMetaData.ProcessType.CREATE, 1, entityParamValues);
        PlatformDTO newPlatformDTOResponse = dtoRequestPlatform.process(newPlatformDto);


        // re-retrieve the platform we just created so we start with a fresh READ mode dto
        PlatformDTO PlatformDTORequest = new PlatformDTO();
        PlatformDTORequest.setPlatformId(newPlatformDTOResponse.getPlatformId());
        PlatformDTO platformDTOReceived = dtoRequestPlatform.process(PlatformDTORequest);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(platformDTOReceived));

        // so this would be the typical workflow for the client app
        platformDTOReceived.setProcessType(DtoMetaData.ProcessType.UPDATE);
        String newName = UUID.randomUUID().toString();
        platformDTOReceived.setPlatformName(newName);

        EntityPropertyDTO propertyToUpdate = platformDTOReceived.getProperties().get(0);
        String updatedPropertyName = propertyToUpdate.getPropertyName();
        String updatedPropertyValue = UUID.randomUUID().toString();
        propertyToUpdate.setPropertyValue(updatedPropertyValue);

        PlatformDTO PlatformDTOResponse = dtoRequestPlatform.process(platformDTOReceived);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(PlatformDTOResponse));

        PlatformDTO dtoRequestPlatformReRetrieved =
                dtoRequestPlatform.process(PlatformDTORequest);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dtoRequestPlatformReRetrieved));

        Assert.assertTrue(dtoRequestPlatformReRetrieved.getPlatformName().equals(newName));
        EntityPropertyDTO matchedProperty = dtoRequestPlatformReRetrieved
                .getProperties()
                .stream()
                .filter(m -> m.getPropertyName().equals(updatedPropertyName))
                .collect(Collectors.toList())
                .get(0);

        Assert.assertTrue(matchedProperty.getPropertyValue().equals(updatedPropertyValue));
    }
}
