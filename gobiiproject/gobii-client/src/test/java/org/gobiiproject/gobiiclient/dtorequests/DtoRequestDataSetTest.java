// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;


import com.fasterxml.jackson.annotation.JsonIgnoreType;
import jdk.nashorn.internal.runtime.ECMAException;
import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.EntityParamValues;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.container.DataSetDTO;
import org.gobiiproject.gobiimodel.dto.container.EntityPropertyDTO;
import org.gobiiproject.gobiimodel.dto.header.HeaderStatusMessage;
import org.gobiiproject.gobiimodel.types.GobiiCropType;
import org.gobiiproject.gobiimodel.types.SystemUserDetail;
import org.gobiiproject.gobiimodel.types.SystemUserNames;
import org.gobiiproject.gobiimodel.types.SystemUsers;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DtoRequestDataSetTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }


    @Test
    public void testGetDataSet() throws Exception {


        DtoRequestDataSet dtoRequestDataSet = new DtoRequestDataSet();
        DataSetDTO dataSetDTORequest = new DataSetDTO();
        dataSetDTORequest.setDataSetId(2);
        DataSetDTO dataSetDTOResponse = dtoRequestDataSet.process(dataSetDTORequest);

        Assert.assertNotEquals(null, dataSetDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dataSetDTOResponse));
        Assert.assertNotEquals(null, dataSetDTOResponse.getDataFile());
        Assert.assertNotNull(dataSetDTOResponse.getCallingAnalysisId());
        Assert.assertTrue(dataSetDTOResponse.getCallingAnalysisId() > 0);
        Assert.assertTrue(dataSetDTOResponse
                .getAnalysesIds()
                .stream()
                .filter(a -> a.equals(null))
                .toArray().length == 0);

//        if (dataSetDTOResponse.getAnalyses() != null && dataSetDTOResponse.getAnalyses().size() > 0) {
//            Assert.assertNotEquals(null, dataSetDTOResponse.getAnalyses().get(0).getAnalysisId());
//            Assert.assertTrue(dataSetDTOResponse.getAnalyses().get(0).getAnalysisId() > 0);
//        }
//        Assert.assertTrue(dataSetDTOResponse.getProperties().size() > 0);

    } //


    @Test
    public void testCreateDataSet() throws Exception {


        EntityParamValues entityParamValues = TestDtoFactory.makeArbitraryEntityParams();

        // ******** make analyses we'll need for the new data set
        DtoRequestAnalysis dtoRequestAnalysis = new DtoRequestAnalysis();
        AnalysisDTO analysisDTORequest = TestDtoFactory
                .makePopulatedAnalysisDTO(DtoMetaData.ProcessType.CREATE, 1, entityParamValues);

        AnalysisDTO callingAnalysisDTO = dtoRequestAnalysis.process(analysisDTORequest);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(callingAnalysisDTO));

        List<AnalysisDTO> analyses = new ArrayList<>();
        analyses.add(TestDtoFactory
                .makePopulatedAnalysisDTO(DtoMetaData.ProcessType.CREATE,
                        2,
                        entityParamValues));
        analyses.add(TestDtoFactory
                .makePopulatedAnalysisDTO(DtoMetaData.ProcessType.CREATE,
                        3,
                        entityParamValues));
        analyses.add(TestDtoFactory
                .makePopulatedAnalysisDTO(DtoMetaData.ProcessType.CREATE,
                        4,
                        entityParamValues));

        for (AnalysisDTO currentAnalysis : analyses) {
            dtoRequestAnalysis.process(currentAnalysis);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(currentAnalysis));
        }

        List<Integer> analysisIds = analyses
                .stream()
                .map(a -> a.getAnalysisId())
                .collect(Collectors.toList());


        // ********** make raw data set dto and add anlyses
        DtoRequestDataSet dtoRequestDataSet = new DtoRequestDataSet();
        DataSetDTO dataSetDTORequest = TestDtoFactory
                .makePopulatedDataSetDTO(DtoMetaData.ProcessType.CREATE,
                        1,
                        callingAnalysisDTO.getAnalysisId(),
                        analysisIds);


        DataSetDTO dataSetDTOResponse = dtoRequestDataSet.process(dataSetDTORequest);

        Assert.assertNotEquals(null, dataSetDTOResponse);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dataSetDTOResponse));
        Assert.assertTrue(dataSetDTOResponse.getDataSetId() > 0);
        Assert.assertTrue(dataSetDTOResponse.getCallingAnalysisId() > 0);
        Assert.assertNotNull(dataSetDTOResponse.getAnalysesIds());
        Assert.assertTrue(dataSetDTOResponse.getAnalysesIds().size() > 0);
        Assert.assertTrue(dataSetDTOResponse.getTypeId() > 0);


        DataSetDTO dataSetDTOReRequest = new DataSetDTO();
        dataSetDTOReRequest.setDataSetId(dataSetDTOResponse.getDataSetId());
        DataSetDTO dataSetDTOReResponse = dtoRequestDataSet.process(dataSetDTOReRequest);

        Assert.assertNotEquals(null, dataSetDTOReResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dataSetDTOReResponse));
        Assert.assertTrue(dataSetDTOReResponse.getDataSetId() > 0);
        Assert.assertTrue(dataSetDTOReResponse.getCallingAnalysisId() > 0);
        Assert.assertNotNull(dataSetDTOReResponse.getAnalysesIds());
        Assert.assertTrue(dataSetDTOReResponse.getAnalysesIds().size() > 0);

    }

    @Test
    public void UpdateDataSet() throws Exception {

        // ******** make analyses we'll need for the new data set
        EntityParamValues entityParamValues = TestDtoFactory.makeArbitraryEntityParams();
        DtoRequestAnalysis dtoRequestAnalysis = new DtoRequestAnalysis();
        AnalysisDTO analysisDTORequest = TestDtoFactory
                .makePopulatedAnalysisDTO(DtoMetaData.ProcessType.CREATE, 1, entityParamValues);

        AnalysisDTO newCallingAnalysisDTO = dtoRequestAnalysis.process(analysisDTORequest);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(newCallingAnalysisDTO));

        List<AnalysisDTO> analysesToCreate = new ArrayList<>();
        List<AnalysisDTO> analysesNew = new ArrayList<>();
        analysesToCreate.add(TestDtoFactory
                .makePopulatedAnalysisDTO(DtoMetaData.ProcessType.CREATE,
                        2,
                        entityParamValues));
        analysesToCreate.add(TestDtoFactory
                .makePopulatedAnalysisDTO(DtoMetaData.ProcessType.CREATE,
                        3,
                        entityParamValues));
        analysesToCreate.add(TestDtoFactory
                .makePopulatedAnalysisDTO(DtoMetaData.ProcessType.CREATE,
                        4,
                        entityParamValues));

        for (AnalysisDTO currentAnalysis : analysesToCreate) {
            AnalysisDTO newAnalysis = dtoRequestAnalysis.process(currentAnalysis);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(newAnalysis));
            analysesNew.add(newAnalysis);
        }

        List<Integer> analysisIds = analysesNew
                .stream()
                .map(a -> a.getAnalysisId())
                .collect(Collectors.toList());


        DtoRequestDataSet dtoRequestDataSet = new DtoRequestDataSet();

        // create a new aataSet for our test
        DataSetDTO newDataSetDto = TestDtoFactory
                .makePopulatedDataSetDTO(DtoMetaData.ProcessType.CREATE,
                        1,
                        newCallingAnalysisDTO.getAnalysisId(),
                        analysisIds);

        DataSetDTO newDataSetDTOResponse = dtoRequestDataSet.process(newDataSetDto);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(newDataSetDto));

        // re-retrieve the aataSet we just created so we start with a fresh READ mode dto
        DataSetDTO dataSetDTORequest = new DataSetDTO();
        dataSetDTORequest.setDataSetId(newDataSetDTOResponse.getDataSetId());
        DataSetDTO dataSetDTOReceived = dtoRequestDataSet.process(dataSetDTORequest);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dataSetDTOReceived));


        // so this would be the typical workflow for the client app
        dataSetDTOReceived.setProcessType(DtoMetaData.ProcessType.UPDATE);
        String newDataFile = UUID.randomUUID().toString();
        dataSetDTOReceived.setDataFile(newDataFile);
        Integer anlysisIdRemovedFromList = dataSetDTOReceived.getAnalysesIds().remove(0);
        Integer newCallingAnalysisId = anlysisIdRemovedFromList;
        dataSetDTOReceived.setCallingAnalysisId(newCallingAnalysisId);


        DataSetDTO dataSetDTOResponse = dtoRequestDataSet.process(dataSetDTOReceived);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dataSetDTOResponse));

        dataSetDTORequest.setProcessType(DtoMetaData.ProcessType.READ);
        dataSetDTORequest.setDataSetId(dataSetDTOResponse.getDataSetId());
        DataSetDTO dtoRequestDataSetReRetrieved =
                dtoRequestDataSet.process(dataSetDTORequest);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dtoRequestDataSetReRetrieved));

        Assert.assertTrue(dtoRequestDataSetReRetrieved.getDataSetId().equals(dataSetDTORequest.getDataSetId()));
        Assert.assertTrue(dtoRequestDataSetReRetrieved.getDataFile().equals(newDataFile));
        Assert.assertTrue(dtoRequestDataSetReRetrieved.getCallingAnalysisId().equals(newCallingAnalysisId));
        Assert.assertTrue(dtoRequestDataSetReRetrieved
                .getAnalysesIds()
                .stream()
                .filter(a -> a.equals(anlysisIdRemovedFromList))
                .toArray().length == 0);
    }

    public void testNewColumnValues() throws Exception {


        EntityParamValues entityParamValues = TestDtoFactory.makeArbitraryEntityParams();

        // ******** make analyses we'll need for the new data set
        DtoRequestAnalysis dtoRequestAnalysis = new DtoRequestAnalysis();
        AnalysisDTO analysisDTORequest = TestDtoFactory
                .makePopulatedAnalysisDTO(DtoMetaData.ProcessType.CREATE, 1, entityParamValues);

        AnalysisDTO callingAnalysisDTO = dtoRequestAnalysis.process(analysisDTORequest);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(callingAnalysisDTO));

        List<AnalysisDTO> analyses = new ArrayList<>();
        analyses.add(TestDtoFactory
                .makePopulatedAnalysisDTO(DtoMetaData.ProcessType.CREATE,
                        2,
                        entityParamValues));
        analyses.add(TestDtoFactory
                .makePopulatedAnalysisDTO(DtoMetaData.ProcessType.CREATE,
                        3,
                        entityParamValues));
        analyses.add(TestDtoFactory
                .makePopulatedAnalysisDTO(DtoMetaData.ProcessType.CREATE,
                        4,
                        entityParamValues));

        for (AnalysisDTO currentAnalysis : analyses) {
            dtoRequestAnalysis.process(currentAnalysis);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(currentAnalysis));
        }

        List<Integer> analysisIds = analyses
                .stream()
                .map(a -> a.getAnalysisId())
                .collect(Collectors.toList());


        // ********** make raw data set dto and add anlyses
        DtoRequestDataSet dtoRequestDataSet = new DtoRequestDataSet();
        DataSetDTO dataSetDTORequest = TestDtoFactory
                .makePopulatedDataSetDTO(DtoMetaData.ProcessType.CREATE,
                        1,
                        callingAnalysisDTO.getAnalysisId(),
                        analysisIds);


        //These are nullable now:
        dataSetDTORequest.setDataTable(null);
        dataSetDTORequest.setDataFile(null);

        //And we added a name
        String name = UUID.randomUUID().toString();
        dataSetDTORequest.setName(name);

        DataSetDTO dataSetDTOResponse = dtoRequestDataSet.process(dataSetDTORequest);

        Assert.assertNotEquals(null, dataSetDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dataSetDTOResponse));

        DataSetDTO dataSetDTOReRequest = new DataSetDTO();
        dataSetDTOReRequest.setDataSetId(dataSetDTOResponse.getDataSetId());
        DataSetDTO dataSetDTOReReuestResponse = dtoRequestDataSet.process(dataSetDTOReRequest);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dataSetDTOReReuestResponse));
        dataSetDTOReReuestResponse.getName().equals(name);

    }

    @Test
    public void testRawUpdate() throws Exception {

        // set up authentication and so forth
        // you'll need to get the current from the instruction file
        ClientContext.getInstance(null, false).setCurrentClientCrop(GobiiCropType.DEV);
        SystemUsers systemUsers = new SystemUsers();
        SystemUserDetail userDetail = systemUsers.getDetail(SystemUserNames.USER_READER.toString());
        // you'll do an if-then for succesfull login
        Assert.assertTrue(ClientContext.getInstance(null, false).login(userDetail.getUserName(), userDetail.getPassword()));


        Integer dataSetIdYouGotFromFile = 2;
        DataSetDTO dataSetRequest = new DataSetDTO(DtoMetaData.ProcessType.UPDATE);
        dataSetRequest.setDataSetId(dataSetIdYouGotFromFile);
        dataSetRequest.setDataTable("your table name");
        dataSetRequest.setDataFile("your file name");

        DtoRequestDataSet dtoRequestDataSet = new DtoRequestDataSet();
        DataSetDTO dataSetResponse = dtoRequestDataSet.process(dataSetRequest);

        // if you didn't succeed, do not pass go, but do log errors to your log file
        if (!dataSetResponse.getDtoHeaderResponse().isSucceeded()) {
            System.out.println();
            System.out.println("*** Header errors: ");
            for (HeaderStatusMessage currentStatusMesage : dataSetResponse.getDtoHeaderResponse().getStatusMessages()) {
                System.out.println(currentStatusMesage.getMessage());
            }
        }
    }

    //This doesn't work yet -- the server doesn't handle updates without the required values in the DTO
    @Ignore
    public void testUpdateDataSetWithDataSetIdOnly() throws Exception {

        // ******** make analyses we'll need for the new data set
        EntityParamValues entityParamValues = TestDtoFactory.makeArbitraryEntityParams();
        DtoRequestAnalysis dtoRequestAnalysis = new DtoRequestAnalysis();
        AnalysisDTO analysisDTORequest = TestDtoFactory
                .makePopulatedAnalysisDTO(DtoMetaData.ProcessType.CREATE, 1, entityParamValues);

        AnalysisDTO newCallingAnalysisDTO = dtoRequestAnalysis.process(analysisDTORequest);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(newCallingAnalysisDTO));

        List<AnalysisDTO> analysesToCreate = new ArrayList<>();
        List<AnalysisDTO> analysesNew = new ArrayList<>();
        analysesToCreate.add(TestDtoFactory
                .makePopulatedAnalysisDTO(DtoMetaData.ProcessType.CREATE,
                        2,
                        entityParamValues));
        analysesToCreate.add(TestDtoFactory
                .makePopulatedAnalysisDTO(DtoMetaData.ProcessType.CREATE,
                        3,
                        entityParamValues));
        analysesToCreate.add(TestDtoFactory
                .makePopulatedAnalysisDTO(DtoMetaData.ProcessType.CREATE,
                        4,
                        entityParamValues));

        for (AnalysisDTO currentAnalysis : analysesToCreate) {
            AnalysisDTO newAnalysis = dtoRequestAnalysis.process(currentAnalysis);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(newAnalysis));
            analysesNew.add(newAnalysis);
        }

        List<Integer> analysisIds = analysesNew
                .stream()
                .map(a -> a.getAnalysisId())
                .collect(Collectors.toList());


        DtoRequestDataSet dtoRequestDataSet = new DtoRequestDataSet();

        // create a new aataSet for our test
        DataSetDTO newDataSetDto = TestDtoFactory
                .makePopulatedDataSetDTO(DtoMetaData.ProcessType.CREATE,
                        1,
                        newCallingAnalysisDTO.getAnalysisId(),
                        analysisIds);

        DataSetDTO newDataSetDTOResponse = dtoRequestDataSet.process(newDataSetDto);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(newDataSetDto));

        // re-retrieve the aataSet we just created so we start with a fresh READ mode dto
        DataSetDTO dataSetDTORequest = new DataSetDTO();
        dataSetDTORequest.setDataSetId(newDataSetDTOResponse.getDataSetId());
        DataSetDTO dataSetDTOReceived = dtoRequestDataSet.process(dataSetDTORequest);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dataSetDTOReceived));

        Integer dataSetId = dataSetDTOReceived.getDataSetId();
        String oldDataFileName = dataSetDTOReceived.getDataFile();
        String newDataFilename = UUID.randomUUID().toString();
        DataSetDTO simpleDataSetUpdate = new DataSetDTO(DtoMetaData.ProcessType.UPDATE);
        simpleDataSetUpdate.setDataSetId(dataSetId);
        simpleDataSetUpdate.setDataFile(newDataFilename);
        DataSetDTO dataSEtDtoUpdated = dtoRequestDataSet.process(simpleDataSetUpdate);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dataSEtDtoUpdated ));
        Assert.assertTrue(!oldDataFileName.equals(newDataFilename));

    }
}
