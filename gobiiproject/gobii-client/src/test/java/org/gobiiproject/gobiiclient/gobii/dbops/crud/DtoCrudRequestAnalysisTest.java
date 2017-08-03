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
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.gobii.Helpers.*;
import org.gobiiproject.gobiimodel.headerlesscontainer.AnalysisDTO;
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


public class DtoCrudRequestAnalysisTest implements DtoCrudRequestTest {

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

        RestUri restUriAnalysis = GobiiClientContext.getInstance(null,false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_ANALYSIS);
        GobiiEnvelopeRestResource<AnalysisDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriAnalysis);
        PayloadEnvelope<AnalysisDTO> resultEnvelope = gobiiEnvelopeRestResource.get(AnalysisDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<AnalysisDTO> analysisDTOList = resultEnvelope.getPayload().getData();
        Assert.assertNotNull(analysisDTOList);
        Assert.assertTrue(analysisDTOList.size() > 0);
        Assert.assertNotNull(analysisDTOList.get(0).getAnalysisName());

        // use an arbitrary analysis id
        Integer analysisId = analysisDTOList.get(0).getAnalysisId();
        RestUri restUriAnalysisForGetById = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_ANALYSIS);
        restUriAnalysisForGetById.setParamValue("id", analysisId.toString());
        GobiiEnvelopeRestResource<AnalysisDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriAnalysisForGetById);
        PayloadEnvelope<AnalysisDTO> resultEnvelopeForGetById = gobiiEnvelopeRestResourceForGetById
                .get(AnalysisDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        AnalysisDTO analysisDTO = resultEnvelopeForGetById.getPayload().getData().get(0);
        Assert.assertTrue(analysisDTO.getAnalysisId() > 0);
        Assert.assertNotNull(analysisDTO.getAnalysisName());

    }

    @Override
    public void testEmptyResult() throws Exception {

        DtoRestRequestUtils<AnalysisDTO> dtoDtoRestRequestUtils = new DtoRestRequestUtils<>(AnalysisDTO.class,
                GobiiServiceRequestId.URL_ANALYSIS);
        Integer maxId = dtoDtoRestRequestUtils.getMaxPkVal();
        Integer nonExistentID = maxId + 1;

        PayloadEnvelope<AnalysisDTO> resultEnvelope = dtoDtoRestRequestUtils.getResponseEnvelopeForEntityId(nonExistentID.toString());

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        Assert.assertNotNull(resultEnvelope.getPayload());
        Assert.assertNotNull(resultEnvelope.getPayload().getData());
        Assert.assertTrue(resultEnvelope.getPayload().getData().size() == 0);

    }


    @Test
    @Override
    public void create() throws Exception {

        RestUri namesUri = GobiiClientContext.getInstance(null, false).getUriFactory().nameIdListByQueryParams();
        namesUri.setParamValue("entity", GobiiEntityNameType.CVTERMS.toString().toLowerCase());
        namesUri.setParamValue("filterType", StringUtils.capitalize(GobiiFilterType.BYTYPENAME.toString()));
        namesUri.setParamValue("filterValue", "analysis_type");

        GobiiEnvelopeRestResource<NameIdDTO> gobiiEnvelopeRestResourceForAnalysisTerms = new GobiiEnvelopeRestResource<>(namesUri);
        PayloadEnvelope<NameIdDTO> resultEnvelope = gobiiEnvelopeRestResourceForAnalysisTerms
                .get(NameIdDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<NameIdDTO> analysisTypes = resultEnvelope.getPayload().getData();

        List<NameIdDTO> analysisProperTerms = new ArrayList<>(analysisTypes);
        EntityParamValues entityParamValues = TestDtoFactory
                .makeConstrainedEntityParams(analysisProperTerms, 1);

        AnalysisDTO newAnalysisDto = TestDtoFactory
                .makePopulatedAnalysisDTO(GobiiProcessType.CREATE, 1, entityParamValues);

        PayloadEnvelope<AnalysisDTO> payloadEnvelope = new PayloadEnvelope<>(newAnalysisDto, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<AnalysisDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_ANALYSIS));

        PayloadEnvelope<AnalysisDTO> analysisDTOResponseEnvelope = gobiiEnvelopeRestResource.post(AnalysisDTO.class,
                payloadEnvelope);

        AnalysisDTO analysisDTOResponse = analysisDTOResponseEnvelope.getPayload().getData().get(0);

        Assert.assertNotEquals(null, analysisDTOResponse);
        Assert.assertTrue(analysisDTOResponse.getAnalysisId() > 0);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(analysisDTOResponseEnvelope.getHeader()));

        GlobalPkValues.getInstance().addPkVal(GobiiEntityNameType.ANALYSES, analysisDTOResponse.getAnalysisId());

        RestUri restUriAnalysisForGetById = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_ANALYSIS);
        restUriAnalysisForGetById.setParamValue("id", analysisDTOResponse.getAnalysisId().toString());
        GobiiEnvelopeRestResource<AnalysisDTO> gobiiEnvelopeRestResouceForGetById = new GobiiEnvelopeRestResource<>(restUriAnalysisForGetById);
        PayloadEnvelope<AnalysisDTO> resultEnvelopeForGetById = gobiiEnvelopeRestResouceForGetById
                .get(AnalysisDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetById.getHeader()));
        AnalysisDTO analysisDTOResponseForParams = resultEnvelopeForGetById.getPayload().getData().get(0);

        GlobalPkValues.getInstance().addPkVal(GobiiEntityNameType.ANALYSES, analysisDTOResponse.getAnalysisId());

    } // testAnalysisCreate

    @Test
    @Override
    public void update() throws Exception {

        RestUri namesUri = GobiiClientContext.getInstance(null, false).getUriFactory().nameIdListByQueryParams();
        namesUri.setParamValue("entity", GobiiEntityNameType.CVTERMS.toString().toLowerCase());
        namesUri.setParamValue("filterType", StringUtils.capitalize(GobiiFilterType.BYTYPENAME.toString()));
        namesUri.setParamValue("filterValue", "analysis_type");

        GobiiEnvelopeRestResource<NameIdDTO> gobiiEnvelopeRestResourceForAnalysisTerms = new GobiiEnvelopeRestResource<>(namesUri);
        PayloadEnvelope<NameIdDTO> resultEnvelope = gobiiEnvelopeRestResourceForAnalysisTerms
                .get(NameIdDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<NameIdDTO> analysisTypes = resultEnvelope.getPayload().getData();

        List<NameIdDTO> analysisProperTerms = new ArrayList<>(analysisTypes);
        EntityParamValues entityParamValues = TestDtoFactory
                .makeConstrainedEntityParams(analysisProperTerms, 1);

        // create a new analysis for our test
        AnalysisDTO newAnalysisDto = TestDtoFactory
                .makePopulatedAnalysisDTO(GobiiProcessType.CREATE, 1, entityParamValues);

        PayloadEnvelope<AnalysisDTO> payloadEnvelope = new PayloadEnvelope<>(newAnalysisDto, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<AnalysisDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_ANALYSIS));
        PayloadEnvelope<AnalysisDTO> analysisDTOResponseEnvelope = gobiiEnvelopeRestResource.post(AnalysisDTO.class,
                payloadEnvelope);
        AnalysisDTO newAnalysisDTOResponse = analysisDTOResponseEnvelope.getPayload().getData().get(0);

        // re-retrieve the analysis we just created so we start with a fresh READ mode dto

        RestUri restUriAnalysisForGetById = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(GobiiServiceRequestId.URL_ANALYSIS);
        restUriAnalysisForGetById.setParamValue("id", newAnalysisDTOResponse.getAnalysisId().toString());
        GobiiEnvelopeRestResource<AnalysisDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriAnalysisForGetById);
        PayloadEnvelope<AnalysisDTO> resultEnvelopeForGetByID = gobiiEnvelopeRestResourceForGetById
                .get(AnalysisDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));
        AnalysisDTO analysisDTOReceived = resultEnvelopeForGetByID.getPayload().getData().get(0);

        String newName = UUID.randomUUID().toString();
        analysisDTOReceived.setAnalysisName(newName);
        gobiiEnvelopeRestResourceForGetById.setParamValue("id", analysisDTOReceived.getAnalysisId().toString());
        PayloadEnvelope<AnalysisDTO> analysisDTOResponseEnvelopeUpdate = gobiiEnvelopeRestResourceForGetById.put(AnalysisDTO.class,
                new PayloadEnvelope<>(analysisDTOReceived, GobiiProcessType.UPDATE));

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(analysisDTOResponseEnvelopeUpdate.getHeader()));

        AnalysisDTO analysisDTORequest = analysisDTOResponseEnvelopeUpdate.getPayload().getData().get(0);


        restUriAnalysisForGetById.setParamValue("id", analysisDTORequest.getAnalysisId().toString());
        resultEnvelopeForGetByID = gobiiEnvelopeRestResourceForGetById
                .get(AnalysisDTO.class);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));


        AnalysisDTO dtoRequestAnalysisReRetrieved = resultEnvelopeForGetByID.getPayload().getData().get(0);


        Assert.assertTrue(dtoRequestAnalysisReRetrieved.getAnalysisName().equals(newName));

    }

    @Override
    public void getList() throws Exception {

        RestUri restUriAnalysis = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(GobiiServiceRequestId.URL_ANALYSIS);
        GobiiEnvelopeRestResource<AnalysisDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriAnalysis);
        PayloadEnvelope<AnalysisDTO> resultEnvelope = gobiiEnvelopeRestResource
                .get(AnalysisDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<AnalysisDTO> analysisDTOList = resultEnvelope.getPayload().getData();
        Assert.assertNotNull(analysisDTOList);
        Assert.assertTrue(analysisDTOList.size() > 0);
        Assert.assertNotNull(analysisDTOList.get(0).getAnalysisName());

        LinkCollection linkCollection = resultEnvelope.getPayload().getLinkCollection();
        Assert.assertTrue(linkCollection.getLinksPerDataItem().size() == analysisDTOList.size());

        List<Integer> itemsToTest = new ArrayList<>();
        if (analysisDTOList.size() > 50) {
            itemsToTest = TestUtils.makeListOfIntegersInRange(10, analysisDTOList.size());
        } else {
            for (int idx = 0; idx < analysisDTOList.size(); idx++) {
                itemsToTest.add(idx);
            }
        }

        for (Integer currentIdx : itemsToTest) {
            AnalysisDTO currentAnalysisDto = analysisDTOList.get(currentIdx);

            Link currentLink = linkCollection.getLinksPerDataItem().get(currentIdx);

            RestUri restUriAnalysisForGetById = GobiiClientContext.getInstance(null, false)
                    .getUriFactory()
                    .RestUriFromUri(currentLink.getHref());
            GobiiEnvelopeRestResource<AnalysisDTO> gobiiEnvelopeRestResourceForGetById = new GobiiEnvelopeRestResource<>(restUriAnalysisForGetById);
            PayloadEnvelope<AnalysisDTO> resultEnvelopeForGetById = gobiiEnvelopeRestResourceForGetById
                    .get(AnalysisDTO.class);
            Assert.assertNotNull(resultEnvelopeForGetById);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetById.getHeader()));
            AnalysisDTO analysisDTOFromLink = resultEnvelopeForGetById.getPayload().getData().get(0);
            Assert.assertTrue(currentAnalysisDto.getAnalysisName().equals(analysisDTOFromLink.getAnalysisName()));
            Assert.assertTrue(currentAnalysisDto.getAnalysisId().equals(analysisDTOFromLink.getAnalysisId()));
        }

    }

}
