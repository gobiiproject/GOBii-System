// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;


import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.EntityParamValues;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.container.EntityPropertyDTO;
import org.gobiiproject.gobiimodel.dto.container.ExperimentDTO;
import org.gobiiproject.gobiimodel.types.SystemUserDetail;
import org.gobiiproject.gobiimodel.types.SystemUserNames;
import org.gobiiproject.gobiimodel.types.SystemUsers;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DtoRequestAnalysisTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }


    @Test
    public void testAnalysisSet() throws Exception {


        DtoRequestAnalysis dtoRequestAnalysis = new DtoRequestAnalysis();
        AnalysisDTO analysisDTORequest = new AnalysisDTO();
        analysisDTORequest.setAnalysisId(1);
        AnalysisDTO analysisDTOResponse = dtoRequestAnalysis.process(analysisDTORequest);

        Assert.assertNotEquals(null, analysisDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(analysisDTOResponse));
        Assert.assertNotEquals(null, analysisDTOResponse.getProgram());


    }

    @Test
    public void testAnalysisCreate() throws Exception {

        DtoRequestAnalysis dtoRequestAnalysis = new DtoRequestAnalysis();
        EntityParamValues entityParamValues = TestDtoFactory.makeArbitraryEntityParams();
        AnalysisDTO analysisDTORequest = TestDtoFactory
                .makePopulatedAnalysisDTO(DtoMetaData.ProcessType.CREATE, 1, entityParamValues);

        AnalysisDTO analysisDTOResponse = dtoRequestAnalysis.process(analysisDTORequest);

        Assert.assertNotEquals(null, analysisDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(analysisDTOResponse));
        Assert.assertTrue(analysisDTOResponse.getAnalysisId() > 1);

        AnalysisDTO analysisDTORequestForParams = new AnalysisDTO();
        analysisDTORequestForParams.setAnalysisId(analysisDTOResponse.getAnalysisId());
        AnalysisDTO analysisDTOResponseForParams = dtoRequestAnalysis.process(analysisDTORequestForParams);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(analysisDTOResponseForParams));

        Assert.assertNotEquals("Parameter collection is null",null, analysisDTOResponseForParams.getParameters());
        Assert.assertTrue( "No parameters were returned",
                analysisDTOResponseForParams.getParameters().size() > 0);
        Assert.assertTrue("Parameter values do not match",
                entityParamValues.compare(analysisDTOResponseForParams.getParameters()));

    } // testAnalysisCreate

    @Test
    public void testUpdateAnalysis() throws Exception {

        DtoRequestAnalysis dtoRequestAnalysis = new DtoRequestAnalysis();

        // create a new analysis for our test
        EntityParamValues entityParamValues = TestDtoFactory.makeArbitraryEntityParams();
        AnalysisDTO newAnalysisDto = TestDtoFactory
                .makePopulatedAnalysisDTO(DtoMetaData.ProcessType.CREATE, 1, entityParamValues);
        AnalysisDTO newAnalysisDTOResponse = dtoRequestAnalysis.process(newAnalysisDto);


        // re-retrieve the analysis we just created so we start with a fresh READ mode dto
        AnalysisDTO AnalysisDTORequest = new AnalysisDTO();
        AnalysisDTORequest.setAnalysisId(newAnalysisDTOResponse.getAnalysisId());
        AnalysisDTO analysisDTOReceived = dtoRequestAnalysis.process(AnalysisDTORequest);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(analysisDTOReceived));


        // so this would be the typical workflow for the client app
        analysisDTOReceived.setProcessType(DtoMetaData.ProcessType.UPDATE);
        String newDataFile = UUID.randomUUID().toString();
        analysisDTOReceived.setSourceName(newDataFile);

        EntityPropertyDTO propertyToUpdate = analysisDTOReceived.getParameters().get(0);
        String updatedPropertyName = propertyToUpdate.getPropertyName();
        String updatedPropertyValue = UUID.randomUUID().toString();
        propertyToUpdate.setPropertyValue(updatedPropertyValue);

        AnalysisDTO AnalysisDTOResponse = dtoRequestAnalysis.process(analysisDTOReceived);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(AnalysisDTOResponse));


        AnalysisDTO dtoRequestAnalysisReRetrieved =
                dtoRequestAnalysis.process(AnalysisDTORequest);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dtoRequestAnalysisReRetrieved));

        Assert.assertTrue(dtoRequestAnalysisReRetrieved.getSourceName().equals(newDataFile));

        EntityPropertyDTO matchedProperty = dtoRequestAnalysisReRetrieved
                .getParameters()
                .stream()
                .filter(m -> m.getPropertyName().equals(updatedPropertyName) )
                .collect(Collectors.toList())
                .get(0);

        Assert.assertTrue(matchedProperty.getPropertyValue().equals(updatedPropertyValue));

    }

}
