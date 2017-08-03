// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.gobii.dbops.crud;


import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiiapimodel.hateos.Link;
import org.gobiiproject.gobiiapimodel.hateos.LinkCollection;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.gobii.Helpers.DtoRestRequestUtils;
import org.gobiiproject.gobiiclient.gobii.Helpers.EntityParamValues;
import org.gobiiproject.gobiiclient.gobii.Helpers.GlobalPkColl;
import org.gobiiproject.gobiiclient.gobii.Helpers.GlobalPkValues;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.headerlesscontainer.AnalysisDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.DataSetDTO;


import org.gobiiproject.gobiimodel.headerlesscontainer.NameIdDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiFilterType;
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
        Assert.assertTrue(GobiiClientContextAuth.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.deAuthenticate());
    }


    @Test
    @Override
    public void get() throws Exception {


//        DtoRequestDataSet dtoRequestDataSet = new DtoRequestDataSet();
//        DataSetDTO dataSetDTORequest = new DataSetDTO();
//        dataSetDTORequest.setDataSetId(2);
//        DataSetDTO dataSetDTOResponse = dtoRequestDataSet.process(dataSetDTORequest);

        Integer dataSetid = (new GlobalPkColl<DtoCrudRequestDataSetTest>().getAPkVal(DtoCrudRequestDataSetTest.class, GobiiEntityNameType.DATASETS));

        RestUri projectsUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_DATASETS);
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
                new DtoRestRequestUtils<>(DataSetDTO.class, GobiiServiceRequestId.URL_DATASETS);
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

        RestUri namesUri = GobiiClientContext.getInstance(null, false).getUriFactory().nameIdListByQueryParams();
        namesUri.setParamValue("entity", GobiiEntityNameType.CVTERMS.toString().toLowerCase());
        namesUri.setParamValue("filterType", StringUtils.capitalize(GobiiFilterType.BYTYPENAME.toString()));
        namesUri.setParamValue("filterValue", "analysis_type");

        GobiiEnvelopeRestResource<NameIdDTO> gobiiEnvelopeRestResourceForAnalysisTerms = new GobiiEnvelopeRestResource<>(namesUri);
        PayloadEnvelope<NameIdDTO> resultEnvelopeAnalysis = gobiiEnvelopeRestResourceForAnalysisTerms
                .get(NameIdDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeAnalysis.getHeader()));
        List<NameIdDTO> analysisTypes = resultEnvelopeAnalysis.getPayload().getData();

        List<NameIdDTO> analysisProperTerms = new ArrayList<>(analysisTypes);
        EntityParamValues entityParamValues = TestDtoFactory
                .makeConstrainedEntityParams(analysisProperTerms, 1);

        // ******** make analyses we'll need for the new data set
        AnalysisDTO analysisDTORequest = TestDtoFactory
                .makePopulatedAnalysisDTO(GobiiProcessType.CREATE, 1, entityParamValues);

        PayloadEnvelope<AnalysisDTO> payloadEnvelopeAnalysis = new PayloadEnvelope<>(analysisDTORequest, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<AnalysisDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_ANALYSIS));
        PayloadEnvelope<AnalysisDTO> analysisDTOResponseEnvelope = gobiiEnvelopeRestResource.post(AnalysisDTO.class,
                payloadEnvelopeAnalysis);
        AnalysisDTO callingAnalysisDTO = analysisDTOResponseEnvelope.getPayload().getData().get(0);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(analysisDTOResponseEnvelope.getHeader()));

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

            payloadEnvelopeAnalysis = new PayloadEnvelope<>(currentAnalysis, GobiiProcessType.CREATE);
            gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                    .getUriFactory()
                    .resourceColl(GobiiServiceRequestId.URL_ANALYSIS));
            analysisDTOResponseEnvelope = gobiiEnvelopeRestResource.post(AnalysisDTO.class,
                    payloadEnvelopeAnalysis);
            AnalysisDTO createdAnalysis = analysisDTOResponseEnvelope.getPayload().getData().get(0);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(analysisDTOResponseEnvelope.getHeader()));

            analysisIds.add(createdAnalysis.getAnalysisId());
        }


        // ********** make raw data set dto and add anlyses
        //DtoRequestDataSet dtoRequestDataSet = new DtoRequestDataSet();
        DataSetDTO dataSetDTORequest = TestDtoFactory
                .makePopulatedDataSetDTO(1,
                        callingAnalysisDTO.getAnalysisId(),
                        analysisIds);

        RestUri projectsCollUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_DATASETS);
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


        RestUri projectsByIdUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_DATASETS);
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
        RestUri namesUri = GobiiClientContext.getInstance(null, false).getUriFactory().nameIdListByQueryParams();
        namesUri.setParamValue("entity", GobiiEntityNameType.CVTERMS.toString().toLowerCase());
        namesUri.setParamValue("filterType", StringUtils.capitalize(GobiiFilterType.BYTYPENAME.toString()));
        namesUri.setParamValue("filterValue", "analysis_type");

        GobiiEnvelopeRestResource<NameIdDTO> gobiiEnvelopeRestResourceForAnalysisTerms = new GobiiEnvelopeRestResource<>(namesUri);
        PayloadEnvelope<NameIdDTO> resultEnvelopeAnalysis = gobiiEnvelopeRestResourceForAnalysisTerms
                .get(NameIdDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeAnalysis.getHeader()));
        List<NameIdDTO> analysisTypes = resultEnvelopeAnalysis.getPayload().getData();

        List<NameIdDTO> analysisProperTerms = new ArrayList<>(analysisTypes);
        EntityParamValues entityParamValues = TestDtoFactory
                .makeConstrainedEntityParams(analysisProperTerms, 1);

        AnalysisDTO analysisDTORequest = TestDtoFactory
                .makePopulatedAnalysisDTO(GobiiProcessType.CREATE, 1, entityParamValues);

        PayloadEnvelope<AnalysisDTO> payloadEnvelopeAnalysis = new PayloadEnvelope<>(analysisDTORequest, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<AnalysisDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_ANALYSIS));
        PayloadEnvelope<AnalysisDTO> analysisDTOResponseEnvelope = gobiiEnvelopeRestResource.post(AnalysisDTO.class,
                payloadEnvelopeAnalysis);
        AnalysisDTO newCallingAnalysisDTO = analysisDTOResponseEnvelope.getPayload().getData().get(0);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(analysisDTOResponseEnvelope.getHeader()));

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

            payloadEnvelopeAnalysis = new PayloadEnvelope<>(currentAnalysis, GobiiProcessType.CREATE);
            gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                    .getUriFactory()
                    .resourceColl(GobiiServiceRequestId.URL_ANALYSIS));
            analysisDTOResponseEnvelope = gobiiEnvelopeRestResource.post(AnalysisDTO.class,
                    payloadEnvelopeAnalysis);
            AnalysisDTO newAnalysis = analysisDTOResponseEnvelope.getPayload().getData().get(0);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(analysisDTOResponseEnvelope.getHeader()));

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


        RestUri projectsCollUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_DATASETS);
        GobiiEnvelopeRestResource<DataSetDTO> gobiiEnvelopeRestResourceForDataSetPost = new GobiiEnvelopeRestResource<>(projectsCollUri);
        PayloadEnvelope<DataSetDTO> resultEnvelope = gobiiEnvelopeRestResourceForDataSetPost
                .post(DataSetDTO.class, new PayloadEnvelope<>(newDataSetDto, GobiiProcessType.CREATE));


        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));


        DataSetDTO newDataSetDTOResponse = resultEnvelope.getPayload().getData().get(0);

        // re-retrieve the aataSet we just created so we start with a fresh READ mode dto
//        DataSetDTO dataSetDTORequest = new DataSetDTO();
//        dataSetDTORequest.setDataSetId(newDataSetDTOResponse.getDataSetId());
//        DataSetDTO dataSetDTOReceived = dtoRequestDataSet.process(dataSetDTORequest);

        RestUri projectsByIdUri = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_DATASETS);
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

        RestUri restUriDataSet = GobiiClientContext.getInstance(null, false)
                .getUriFactory().resourceColl(GobiiServiceRequestId.URL_DATASETS);
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

            RestUri restUriDataSetForGetById = GobiiClientContext.getInstance(null, false)
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

                RestUri restUriAnalysisForGetById = GobiiClientContext.getInstance(null, false)
                        .getUriFactory()
                        .resourceByUriIdParam(GobiiServiceRequestId.URL_ANALYSIS);
                restUriAnalysisForGetById.setParamValue("id", currentAnalysisId.toString());
                GobiiEnvelopeRestResource<AnalysisDTO> gobiiEnvelopeRestResourceAnalysisForGetById = new GobiiEnvelopeRestResource<>(restUriAnalysisForGetById);
                PayloadEnvelope<AnalysisDTO> resultEnvelopeForGetById = gobiiEnvelopeRestResourceAnalysisForGetById
                        .get(AnalysisDTO.class);

                Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
                AnalysisDTO analysisDTO = resultEnvelopeForGetById.getPayload().getData().get(0);
                Assert.assertTrue(analysisDTO.getAnalysisId() > 0);
                Assert.assertNotNull(analysisDTO.getAnalysisName());

            }

        }

    }

    @Test
    public void getDataSetsByTypeId() throws Exception {

        Integer dataSetid = (new GlobalPkColl<DtoCrudRequestDataSetTest>().getAPkVal(DtoCrudRequestDataSetTest.class, GobiiEntityNameType.DATASETS));

        RestUri restUriForDataSets = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_DATASETS);
        restUriForDataSets.setParamValue("id", dataSetid.toString());
        GobiiEnvelopeRestResource<DataSetDTO> gobiiEnvelopeRestResourceForDataSet = new GobiiEnvelopeRestResource<>(restUriForDataSets);
        PayloadEnvelope<DataSetDTO> resultEnvelopeDataSet = gobiiEnvelopeRestResourceForDataSet
                .get(DataSetDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeDataSet.getHeader()));
        DataSetDTO dataSetDTOResponse = resultEnvelopeDataSet.getPayload().getData().get(0);

        Integer typeId = dataSetDTOResponse.getTypeId();

        RestUri restUriForDataTypes = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_DATASETTYPES)
                .addUriParam("id")
                .setParamValue("id", typeId.toString());

        GobiiEnvelopeRestResource<DataSetDTO> gobiiEnvelopeRestResourceForDataTypes = new GobiiEnvelopeRestResource<>(restUriForDataTypes);
        PayloadEnvelope<DataSetDTO> resultEnvelopeDataTypes = gobiiEnvelopeRestResourceForDataTypes
                .get(DataSetDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeDataTypes.getHeader()));

        List<DataSetDTO> dataSetDTOList = resultEnvelopeDataTypes.getPayload().getData();

        Assert.assertNotNull(dataSetDTOList);
        Assert.assertTrue(dataSetDTOList.size() >= 0);

        if(dataSetDTOList.size() > 0) {
            Assert.assertNotNull(dataSetDTOList.get(0).getName());
        }

        LinkCollection linkCollection = resultEnvelopeDataTypes.getPayload().getLinkCollection();
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
            DataSetDTO currentDatasetDto = dataSetDTOList.get(currentIdx);

            Link currentLink = linkCollection.getLinksPerDataItem().get(currentIdx);

            RestUri restUriDForGetById = GobiiClientContext.getInstance(null, false)
                    .getUriFactory()
                    .RestUriFromUri(currentLink.getHref());
            GobiiEnvelopeRestResource<DataSetDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriDForGetById);
            PayloadEnvelope<DataSetDTO> resultEnvelopeForGetByID = gobiiEnvelopeRestResourceForGetById
                    .get(DataSetDTO.class);
            Assert.assertNotNull(resultEnvelopeForGetByID);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));
            DataSetDTO dataDTOFromLink = resultEnvelopeForGetByID.getPayload().getData().get(0);
            Assert.assertTrue(currentDatasetDto.getName().equals(dataDTOFromLink.getName()));
            Assert.assertTrue(currentDatasetDto.getDataSetId().equals(dataDTOFromLink.getDataSetId()));
        }

    }
}
