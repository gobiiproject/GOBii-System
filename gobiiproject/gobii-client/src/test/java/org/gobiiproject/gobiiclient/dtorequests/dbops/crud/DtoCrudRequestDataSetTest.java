// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests.dbops.crud;


import org.gobiiproject.gobiiapimodel.hateos.Link;
import org.gobiiproject.gobiiapimodel.hateos.LinkCollection;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.common.ClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.dtorequests.DtoRequestAnalysis;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.DtoRestRequestUtils;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.EntityParamValues;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.GlobalPkColl;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.GlobalPkValues;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.container.AnalysisDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.DataSetDTO;


import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DtoCrudRequestDataSetTest implements DtoCrudRequestTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }


    @Test
    @Override
    public void get() throws Exception {


//        DtoRequestDataSet dtoRequestDataSet = new DtoRequestDataSet();
//        DataSetDTO dataSetDTORequest = new DataSetDTO();
//        dataSetDTORequest.setDataSetId(2);
//        DataSetDTO dataSetDTOResponse = dtoRequestDataSet.process(dataSetDTORequest);

        Integer dataSetid = (new GlobalPkColl<DtoCrudRequestDataSetTest>().getAPkVal(DtoCrudRequestDataSetTest.class, GobiiEntityNameType.DATASETS));

        RestUri projectsUri = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(ServiceRequestId.URL_DATASETS);
        projectsUri.setParamValue("id", dataSetid.toString());
        GobiiEnvelopeRestResource<DataSetDTO> gobiiEnvelopeRestResourceForProjects = new GobiiEnvelopeRestResource<>(projectsUri);
        PayloadEnvelope<DataSetDTO> resultEnvelope = gobiiEnvelopeRestResourceForProjects
                .get(DataSetDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        DataSetDTO dataSetDTOResponse = resultEnvelope.getPayload().getData().get(0);

        Assert.assertNotEquals(null, dataSetDTOResponse);
        Assert.assertNotEquals(null, dataSetDTOResponse.getDataFile());
        Assert.assertNotNull(dataSetDTOResponse.getCallingAnalysisId());
        Assert.assertTrue(dataSetDTOResponse.getCallingAnalysisId() > 0);
        Assert.assertTrue(dataSetDTOResponse
                .getAnalysesIds()
                .stream()
                .filter(a -> a.equals(null))
                .toArray().length == 0);


    } //

    @Test
    public void testEmptyResult() throws Exception {

        DtoRestRequestUtils<DataSetDTO> dtoDtoRestRequestUtils =
                new DtoRestRequestUtils<>(DataSetDTO.class,ServiceRequestId.URL_DATASETS);
        Integer maxId = dtoDtoRestRequestUtils.getMaxPkVal();
        Integer nonExistentId = maxId + 1;

        PayloadEnvelope<DataSetDTO> resultEnvelope =
                dtoDtoRestRequestUtils.getResponseEnvelopeForEntityId(nonExistentId.toString());

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        Assert.assertNotNull(resultEnvelope.getPayload());
        Assert.assertNotNull(resultEnvelope.getPayload().getData());
        Assert.assertTrue(resultEnvelope.getPayload().getData().size() == 0 );
    }

    @Test
    @Override
    public void create() throws Exception {


        EntityParamValues entityParamValues = TestDtoFactory.makeArbitraryEntityParams();

        // ******** make analyses we'll need for the new data set
        DtoRequestAnalysis dtoRequestAnalysis = new DtoRequestAnalysis();
        AnalysisDTO analysisDTORequest = TestDtoFactory
                .makePopulatedAnalysisDTO(GobiiProcessType.CREATE, 1, entityParamValues);

        AnalysisDTO callingAnalysisDTO = dtoRequestAnalysis.process(analysisDTORequest);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(callingAnalysisDTO));

        List<AnalysisDTO> analyses = new ArrayList<>();
        analyses.add(TestDtoFactory
                .makePopulatedAnalysisDTO(GobiiProcessType.CREATE,
                        2,
                        entityParamValues));
        analyses.add(TestDtoFactory
                .makePopulatedAnalysisDTO(GobiiProcessType.CREATE,
                        3,
                        entityParamValues));
        analyses.add(TestDtoFactory
                .makePopulatedAnalysisDTO(GobiiProcessType.CREATE,
                        4,
                        entityParamValues));

        List<Integer> analysisIds = new ArrayList<>();
        for (AnalysisDTO currentAnalysis : analyses) {
            AnalysisDTO createdAnalysis = dtoRequestAnalysis.process(currentAnalysis);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(createdAnalysis));
            analysisIds.add(createdAnalysis.getAnalysisId());
        }


        // ********** make raw data set dto and add anlyses
        //DtoRequestDataSet dtoRequestDataSet = new DtoRequestDataSet();
        DataSetDTO dataSetDTORequest = TestDtoFactory
                .makePopulatedDataSetDTO(1,
                        callingAnalysisDTO.getAnalysisId(),
                        analysisIds);

        RestUri projectsCollUri = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(ServiceRequestId.URL_DATASETS);
        GobiiEnvelopeRestResource<DataSetDTO> gobiiEnvelopeRestResourceForDataSetPost = new GobiiEnvelopeRestResource<>(projectsCollUri);
        PayloadEnvelope<DataSetDTO> resultEnvelope = gobiiEnvelopeRestResourceForDataSetPost
                .post(DataSetDTO.class, new PayloadEnvelope<>(dataSetDTORequest, GobiiProcessType.CREATE));

        //DataSetDTO dataSetDTOResponse = dtoRequestDataSet.process(dataSetDTORequest);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        DataSetDTO dataSetDTOResponse = resultEnvelope.getPayload().getData().get(0);

        Assert.assertNotEquals(null, dataSetDTOResponse);
        Assert.assertTrue(dataSetDTOResponse.getDataSetId() > 0);
        Assert.assertTrue(dataSetDTOResponse.getCallingAnalysisId() > 0);
        Assert.assertNotNull(dataSetDTOResponse.getAnalysesIds());
        Assert.assertTrue(dataSetDTOResponse.getAnalysesIds().size() > 0);
        Assert.assertTrue(dataSetDTOResponse.getTypeId() > 0);

        GlobalPkValues.getInstance().addPkVal(GobiiEntityNameType.DATASETS,dataSetDTOResponse.getDataSetId());


//        DataSetDTO dataSetDTOReRequest = new DataSetDTO();
//        dataSetDTOReRequest.setDataSetId(dataSetDTOResponse.getDataSetId());
//        //DataSetDTO dataSetDTOReResponse = dtoRequestDataSet.process(dataSetDTOReRequest);


        RestUri projectsByIdUri = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(ServiceRequestId.URL_DATASETS);
        GobiiEnvelopeRestResource<DataSetDTO> gobiiEnvelopeRestResourceForDataSetGet = new GobiiEnvelopeRestResource<>(projectsByIdUri);
        gobiiEnvelopeRestResourceForDataSetGet.setParamValue("id", dataSetDTOResponse.getDataSetId().toString());
        resultEnvelope = gobiiEnvelopeRestResourceForDataSetGet
                .get(DataSetDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));

        DataSetDTO dataSetDTOReResponse = resultEnvelope.getPayload().getData().get(0);

        Assert.assertNotEquals(null, dataSetDTOReResponse);
        Assert.assertTrue(dataSetDTOReResponse.getDataSetId() > 0);
        Assert.assertTrue(dataSetDTOReResponse.getCallingAnalysisId() > 0);
        Assert.assertNotNull(dataSetDTOReResponse.getAnalysesIds());
        Assert.assertTrue(dataSetDTOReResponse.getAnalysesIds().size() > 0);
        Assert.assertTrue(0 == dataSetDTOReResponse
                .getAnalysesIds()
                .stream()
                .filter(a -> a.equals(null))
                .count());

    }

    @Test
    @Override
    public void update() throws Exception {

        // ******** make analyses we'll need for the new data set
        EntityParamValues entityParamValues = TestDtoFactory.makeArbitraryEntityParams();
        DtoRequestAnalysis dtoRequestAnalysis = new DtoRequestAnalysis();
        AnalysisDTO analysisDTORequest = TestDtoFactory
                .makePopulatedAnalysisDTO(GobiiProcessType.CREATE, 1, entityParamValues);

        AnalysisDTO newCallingAnalysisDTO = dtoRequestAnalysis.process(analysisDTORequest);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(newCallingAnalysisDTO));

        List<AnalysisDTO> analysesToCreate = new ArrayList<>();
        List<AnalysisDTO> analysesNew = new ArrayList<>();
        analysesToCreate.add(TestDtoFactory
                .makePopulatedAnalysisDTO(GobiiProcessType.CREATE,
                        2,
                        entityParamValues));
        analysesToCreate.add(TestDtoFactory
                .makePopulatedAnalysisDTO(GobiiProcessType.CREATE,
                        3,
                        entityParamValues));
        analysesToCreate.add(TestDtoFactory
                .makePopulatedAnalysisDTO(GobiiProcessType.CREATE,
                        4,
                        entityParamValues));

        List<Integer> analysisIds = new ArrayList<>();
        for (AnalysisDTO currentAnalysis : analysesToCreate) {
            AnalysisDTO newAnalysis = dtoRequestAnalysis.process(currentAnalysis);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(newAnalysis));
            analysesNew.add(newAnalysis);
            analysisIds.add(newAnalysis.getAnalysisId());
        }


        //DtoRequestDataSet dtoRequestDataSet = new DtoRequestDataSet();

        // create a new aataSet for our test
        DataSetDTO newDataSetDto = TestDtoFactory
                .makePopulatedDataSetDTO(1,
                        newCallingAnalysisDTO.getAnalysisId(),
                        analysisIds);

        //DataSetDTO newDataSetDTOResponse = dtoRequestDataSet.process(newDataSetDto);


        RestUri projectsCollUri = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(ServiceRequestId.URL_DATASETS);
        GobiiEnvelopeRestResource<DataSetDTO> gobiiEnvelopeRestResourceForDataSetPost = new GobiiEnvelopeRestResource<>(projectsCollUri);
        PayloadEnvelope<DataSetDTO> resultEnvelope = gobiiEnvelopeRestResourceForDataSetPost
                .post(DataSetDTO.class, new PayloadEnvelope<>(newDataSetDto, GobiiProcessType.CREATE));


        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));


        DataSetDTO newDataSetDTOResponse = resultEnvelope.getPayload().getData().get(0);

        // re-retrieve the aataSet we just created so we start with a fresh READ mode dto
//        DataSetDTO dataSetDTORequest = new DataSetDTO();
//        dataSetDTORequest.setDataSetId(newDataSetDTOResponse.getDataSetId());
//        DataSetDTO dataSetDTOReceived = dtoRequestDataSet.process(dataSetDTORequest);

        RestUri projectsByIdUri = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(ServiceRequestId.URL_DATASETS);
        GobiiEnvelopeRestResource<DataSetDTO> gobiiEnvelopeRestResourceForDataSetById = new GobiiEnvelopeRestResource<>(projectsByIdUri);
        gobiiEnvelopeRestResourceForDataSetById.setParamValue("id", newDataSetDTOResponse.getDataSetId().toString());
        resultEnvelope = gobiiEnvelopeRestResourceForDataSetById
                .get(DataSetDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));

        DataSetDTO dataSetDTOReceived = resultEnvelope.getPayload().getData().get(0);

        // so this would be the typical workflow for the client app
        String newDataFile = UUID.randomUUID().toString();
        dataSetDTOReceived.setDataFile(newDataFile);
        Integer anlysisIdRemovedFromList = dataSetDTOReceived.getAnalysesIds().remove(0);
        Integer newCallingAnalysisId = anlysisIdRemovedFromList;
        dataSetDTOReceived.setCallingAnalysisId(newCallingAnalysisId);


        //DataSetDTO dataSetDTOResponse = dtoRequestDataSet.process(dataSetDTOReceived);

        resultEnvelope = gobiiEnvelopeRestResourceForDataSetById
                .put(DataSetDTO.class, new PayloadEnvelope<>(dataSetDTOReceived, GobiiProcessType.UPDATE));

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));

        DataSetDTO dataSetDTOResponse = resultEnvelope.getPayload().getData().get(0);

        gobiiEnvelopeRestResourceForDataSetById.setParamValue("id", dataSetDTOResponse.getDataSetId().toString());
        resultEnvelope = gobiiEnvelopeRestResourceForDataSetById
                .get(DataSetDTO.class);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        DataSetDTO dtoRequestDataSetReRetrieved = resultEnvelope.getPayload().getData().get(0);

//        dataSetDTORequest.setGobiiProcessType(GobiiProcessType.READ);
//        dataSetDTORequest.setDataSetId(dataSetDTOResponse.getDataSetId());
//        DataSetDTO dtoRequestDataSetReRetrieved =
//                dtoRequestDataSet.process(dataSetDTORequest);

        Assert.assertTrue(dtoRequestDataSetReRetrieved.getDataSetId().equals(dataSetDTOReceived.getDataSetId()));
        Assert.assertTrue(dtoRequestDataSetReRetrieved.getDataFile().equals(newDataFile));
        Assert.assertTrue(dtoRequestDataSetReRetrieved.getCallingAnalysisId().equals(newCallingAnalysisId));
        Assert.assertTrue(dtoRequestDataSetReRetrieved
                .getAnalysesIds()
                .stream()
                .filter(a -> a.equals(anlysisIdRemovedFromList))
                .toArray().length == 0);
    }


    @Test
    @Override
    public void getList() throws Exception {

        RestUri restUriDataSet = ClientContext.getInstance(null, false)
                .getUriFactory().resourceColl(ServiceRequestId.URL_DATASETS);
        GobiiEnvelopeRestResource<DataSetDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriDataSet);
        PayloadEnvelope<DataSetDTO> resultEnvelope = gobiiEnvelopeRestResource
                .get(DataSetDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<DataSetDTO> dataSetDTOList = resultEnvelope.getPayload().getData();
        Assert.assertNotNull(dataSetDTOList);
        Assert.assertTrue(dataSetDTOList.size() > 0);
        Assert.assertNotNull(dataSetDTOList.get(0).getName());


        LinkCollection linkCollection = resultEnvelope.getPayload().getLinkCollection();
        Assert.assertTrue(linkCollection.getLinksPerDataItem().size() == dataSetDTOList.size());

        List<Integer> itemsToTest = new ArrayList<>();
        if (dataSetDTOList.size() > 50) {
            itemsToTest = TestUtils.makeListOfIntegersInRange(10, dataSetDTOList.size());

        } else {
            for (int idx = 0; idx < dataSetDTOList.size(); idx++) {
                itemsToTest.add(idx);
            }
        }

        for (Integer currentIdx : itemsToTest) {
            DataSetDTO currentDataSetDto = dataSetDTOList.get(currentIdx);

            Link currentLink = linkCollection.getLinksPerDataItem().get(currentIdx);

            RestUri restUriDataSetForGetById = ClientContext.getInstance(null, false)
                    .getUriFactory()
                    .RestUriFromUri(currentLink.getHref());
            GobiiEnvelopeRestResource<DataSetDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriDataSetForGetById);
            PayloadEnvelope<DataSetDTO> resultEnvelopeForGetByID = gobiiEnvelopeRestResourceForGetById
                    .get(DataSetDTO.class);
            Assert.assertNotNull(resultEnvelopeForGetByID);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));
            DataSetDTO dataSetDTOFromLink = resultEnvelopeForGetByID.getPayload().getData().get(0);
            Assert.assertTrue(currentDataSetDto.getName().equals(dataSetDTOFromLink.getName()));
            Assert.assertTrue(currentDataSetDto.getDataSetId().equals(dataSetDTOFromLink.getDataSetId()));

            Assert.assertNotNull(dataSetDTOFromLink.getAnalysesIds());
            for (Integer currentAnalysisId : dataSetDTOFromLink.getAnalysesIds()) {
                DtoRequestAnalysis dtoRequestAnalysis = new DtoRequestAnalysis();
                AnalysisDTO analysisDTORequest = new AnalysisDTO();
                analysisDTORequest.setAnalysisId(currentAnalysisId);
                AnalysisDTO analysisDTOResponse = dtoRequestAnalysis.process(analysisDTORequest);

                Assert.assertNotEquals(null, analysisDTOResponse);
                Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(analysisDTOResponse));
                Assert.assertNotEquals(null, analysisDTOResponse.getProgram());
            }

        }

    }
}
