// ************************************************************************
// (c) 2016 GOBii Projects
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobiiweb.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.gobiiproject.gobidomain.services.PingService;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiibrapi.calls.calls.BrapiResponseCalls;
import org.gobiiproject.gobiibrapi.calls.calls.BrapiResponseMapCalls;
import org.gobiiproject.gobiibrapi.calls.germplasm.BrapiResponseGermplasmByDbId;
import org.gobiiproject.gobiibrapi.calls.germplasm.BrapiResponseMapGermplasmByDbId;
import org.gobiiproject.gobiibrapi.calls.markerprofiles.allelematrices.BrapiResponseAlleleMatrices;
import org.gobiiproject.gobiibrapi.calls.markerprofiles.allelematrices.BrapiResponseMapAlleleMatrices;
import org.gobiiproject.gobiibrapi.calls.markerprofiles.allelematrixsearch.BrapiResponseMapAlleleMatrixSearch;
import org.gobiiproject.gobiibrapi.calls.studies.observationvariables.BrapiResponseMapObservationVariables;
import org.gobiiproject.gobiibrapi.calls.studies.observationvariables.BrapiResponseObservationVariablesMaster;
import org.gobiiproject.gobiibrapi.calls.studies.search.BrapiRequestStudiesSearch;
import org.gobiiproject.gobiibrapi.calls.studies.search.BrapiResponseMapStudiesSearch;
import org.gobiiproject.gobiibrapi.calls.studies.search.BrapiResponseStudiesSearch;

import org.gobiiproject.gobiibrapi.core.common.BrapiRequestReader;
import org.gobiiproject.gobiibrapi.core.responsemodel.BrapiResponseEnvelope;
import org.gobiiproject.gobiibrapi.core.responsemodel.BrapiResponseEnvelopeMaster;
import org.gobiiproject.gobiibrapi.core.responsemodel.BrapiResponseEnvelopeMasterDetail;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiiweb.CropRequestAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;


/**
 * This controller is only for BRAPI v1. compliant calls. It consumes the gobii-brapi module.
 * <p>
 * BRAPI responses all contain a "metadata" and a "result" key. The "result" key's value can take
 * three forms:
 * 1) A "data" property that is an array of items of the same type (e.g., /studies-search)
 * 2) A set of arbitrary properties (i.e., a specific type in Java) (e.g., /germplasm/{id})
 * 3) A set of arbitrary properties and a "data" property (e.g., /studies/{studyDbId}/observationVariables)
 * <p>
 * Type 3) is for a master-detail scenario: you have a master record that is related to one or more detail
 * records. Type 2) is just the master. Type 1) is just the detail records.
 * <p>
 * The classes that support this API are as follows:
 * <p>
 * BrapiResponseEnvelope: This is the base class for all response envelopes: it is responsible for the metadata
 * key of the response
 * <p>
 * BrapiResponseEnvelopeMasterDetail and BrapiResponseEnvelopeMaster derive from BrapiResponseEnvelope.
 * They are responsible for the result key of the response.
 * <p>
 * BrapiResponseEnvelopeMasterDetail: This class is used for types 1) and 3). It is
 * type-parmaeterized for the pojo that will be the value of the result key; the pojo must extend  BrapiResponseDataList;
 * this way, the pojo will always have a setData() method for specifying the list content of the data key.
 * <p>
 * In the case of (1):
 * The result pojo will be a class with no properties that derives from BrapiResponseDataList. The maping
 * class will use the pojo's setData() method to specify the list that will constitutes the data key
 * of the result key.
 * <p>
 * In the case of (3):
 * Thee result pojo will have its own properties; the pojo's own properties will constitute the values
 * of the result key; the pojos setData() method will be used to specify the list that constitutes the
 * data key;
 * <p>
 * BrapiResponseEnvelopeMaster: This class is used for type 2). It is type-parameterized
 * for an arbitrary pojo. Because type 2 responses do not have a data key, the pojo does
 * _not_ extend BrapiResponseDataList. Its properties will be the values of the response's result key.
 * <p>
 * The calls namespace of gobii-brapi is organized as the brapi API is organized. In the descriptions
 * below, <CallName> refers to the BRAPI call name.
 * Each call contains several sorts of classes:
 * ---- POJOs named BrapiResponse<CallName>: these are the arbitrary pojos that
 * type-parameterize BrapiResponseElvelopeMasterDetail and BrapiResponseElvelopeMaster
 * ---- POJOs named BrapiRequest<CallName>: these are POST/PUT bodies for which the
 * relevant methods in here the controller have @RequestBody parameters (e.g., BrapiRequestStudiesSearch)
 * ---- POJOs named BrapiResponseMap<CallName>: Right now these clases create dummy responses; the real
 * implementations of these classes will consume classes from the gobii-domain project (i.e., the Service
 * classes): they will get data from gobii in terms of gobii DTOs and convert the DTOs in to the
 * BRAPI POJOs
 * <p>
 * The BrapiController does the following:
 * 0.   If there is a post body, deserializes it to the appropriate post pojo;
 * i.   Instantiates a BrapiResponseEnvelopeMaster or BrapiResponseEnvelopeMasterDetail;
 * ii.  Uses the map classes for each call to get the pojo that the call will use for its payload;
 * iii. Assigns the pojo to the respective respone envelope;
 * iv.  Serialies the content of the response envelope;
 * v.   Sets the reponse of the method to the serialized content.
 * <p>
 * Note that this controller receives and sends plain String json data. This approach is different
 * from the gobii api. The BrapiResponseEnvelopeList and BrapiResponseEnvelopeMaster are serialzied to
 * json and sent over the wire in this way rather than letting the Jackson embedded through Spring do
 * the job automatically. This approach is more traditionally the web service way of doing things.
 */
@Scope(value = "request")
@Controller
@RequestMapping(GobiiControllerType.SERVICE_PATH_BRAPI)
public class BRAPIIControllerV1 {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BRAPIIControllerV1.class);

    @Autowired
    private PingService pingService = null;


    @Autowired
    private BrapiResponseMapStudiesSearch brapiResponseMapStudiesSearch = null;


    @Autowired
    private BrapiResponseMapAlleleMatrixSearch brapiResponseMapAlleleMatrixSearch = null;


    @Autowired
    private BrapiResponseMapAlleleMatrices brapiResponseMapAlleleMatrices = null;

    private ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);


    // *********************************************
    // *************************** CALLS
    // *********************************************
    @RequestMapping(value = "/calls",
            method = RequestMethod.GET,
            produces = "application/json")
    @ResponseBody
    public String getCalls(
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String returnVal;

        BrapiResponseEnvelopeMasterDetail<BrapiResponseCalls> brapiResponseEnvelopeMasterDetail =
                new BrapiResponseEnvelopeMasterDetail<>();
        try {

            BrapiResponseMapCalls brapiResponseMapCalls = new BrapiResponseMapCalls(request);

            BrapiResponseCalls brapiResponseCalls = brapiResponseMapCalls.getBrapiResponseCalls();
            brapiResponseEnvelopeMasterDetail.setResult(brapiResponseCalls);

        } catch (GobiiException e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            brapiResponseEnvelopeMasterDetail.getBrapiMetaData().addStatusMessage("exception", message);

        } catch (Exception e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            brapiResponseEnvelopeMasterDetail.getBrapiMetaData().addStatusMessage("exception", message);
        }

        returnVal = objectMapper.writeValueAsString(brapiResponseEnvelopeMasterDetail);

        return returnVal;
    }

    // *********************************************
    // *************************** STUDIES_SEARCH (DETAILS ONLY)
    // *************************** LIST ITEMS ONLY
    // *********************************************
    @RequestMapping(value = "/studies-search",
            method = RequestMethod.POST,
            produces = "application/json")
    @ResponseBody
    public String getStudies(@RequestBody String studiesRequestBody,
                             HttpServletRequest request,
                             HttpServletResponse response) throws Exception {

        String returnVal;

        BrapiResponseEnvelopeMasterDetail<BrapiResponseStudiesSearch> BrapiResponseEnvelopeMasterDetail =
                new BrapiResponseEnvelopeMasterDetail<>();

        try {

            BrapiRequestReader<BrapiRequestStudiesSearch> brapiRequestReader = new BrapiRequestReader<>(BrapiRequestStudiesSearch.class);
            BrapiRequestStudiesSearch brapiRequestStudiesSearch = brapiRequestReader.makeRequestObj(studiesRequestBody);

            BrapiResponseStudiesSearch brapiResponseStudySearch = brapiResponseMapStudiesSearch.getBrapiResponseStudySearch(brapiRequestStudiesSearch);

            BrapiResponseEnvelopeMasterDetail.setResult(brapiResponseStudySearch);

        } catch (GobiiException e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            BrapiResponseEnvelopeMasterDetail.getBrapiMetaData().addStatusMessage("exception", message);

        } catch (Exception e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            BrapiResponseEnvelopeMasterDetail.getBrapiMetaData().addStatusMessage("exception", message);
        }

        returnVal = objectMapper.writeValueAsString(BrapiResponseEnvelopeMasterDetail);

        return returnVal;
    }

    // *********************************************
    // *************************** Germplasm details [GET]
    // **************************** MASTER ONLY
    // *********************************************
    @RequestMapping(value = "/germplasm/{studyDbId}",
            method = RequestMethod.GET,
//            params = {"pageSize", "page"},
            produces = "application/json")
    @ResponseBody
    public String getGermplasmByDbId(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @PathVariable Integer studyDbId
//            ,
//                               @RequestParam(value = "pageSize",required = false) Integer pageSize,
//                               @RequestParam(value = "page", required = false) Integer page
    ) throws Exception {


        BrapiResponseEnvelopeMaster<BrapiResponseGermplasmByDbId> responseEnvelope
                = new BrapiResponseEnvelopeMaster<>();

        String returnVal;

        try {

            BrapiResponseMapGermplasmByDbId brapiResponseMapGermplasmByDbId = new BrapiResponseMapGermplasmByDbId();

            // extends BrapiMetaData, no list items
            BrapiResponseGermplasmByDbId brapiResponseGermplasmByDbId = brapiResponseMapGermplasmByDbId.getGermplasmByDbid(studyDbId);

            responseEnvelope.setResult(brapiResponseGermplasmByDbId);


        } catch (GobiiException e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            responseEnvelope.getBrapiMetaData().addStatusMessage("exception", message);

        } catch (Exception e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            responseEnvelope.getBrapiMetaData().addStatusMessage("exception", message);

        }

        returnVal = objectMapper.writeValueAsString(responseEnvelope);

        return returnVal;

    }

    // *********************************************
    // *************************** Study obsefvation variables (GET)
    // **************************** MASTER AND DETAIL
    // *********************************************
    @RequestMapping(value = "/studies/{studyDbId}/observationVariables",
            method = RequestMethod.GET,
//            params = {"pageSize", "page"},
            produces = "application/json")
    @ResponseBody
    public String getObservationVariables(HttpServletRequest request,
                                          HttpServletResponse response,
                                          @PathVariable Integer studyDbId) throws Exception {

        BrapiResponseEnvelopeMasterDetail<BrapiResponseObservationVariablesMaster> responseEnvelope
                = new BrapiResponseEnvelopeMasterDetail<>();

        String returnVal;

        try {

            BrapiResponseMapObservationVariables brapiResponseMapObservationVariables = new BrapiResponseMapObservationVariables();

            BrapiResponseObservationVariablesMaster brapiResponseObservationVariablesMaster = brapiResponseMapObservationVariables.gerObservationVariablesByStudyId(studyDbId);

            responseEnvelope.setResult(brapiResponseObservationVariablesMaster);

        } catch (GobiiException e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            responseEnvelope.getBrapiMetaData().addStatusMessage("exception", message);

        } catch (Exception e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            responseEnvelope.getBrapiMetaData().addStatusMessage("exception", message);

        }

        returnVal = objectMapper.writeValueAsString(responseEnvelope);

        return returnVal;
    }


    // *********************************************
    // *************************** ALLELE MATRICES
    // *********************************************
    @RequestMapping(value = "/allelematrices",
            method = RequestMethod.GET,
            produces = "application/json")
    @ResponseBody
    public String getAlleleMatrices(@RequestParam("studyDbId") Optional<String> studyDbIdd,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {

        String returnVal;

        BrapiResponseEnvelopeMasterDetail<BrapiResponseAlleleMatrices> BrapiResponseEnvelopeMasterDetail =
                new BrapiResponseEnvelopeMasterDetail<>();

        try {

            BrapiResponseAlleleMatrices brapiResponseAlleleMatrices;
            if (studyDbIdd.isPresent()) {
                Integer studyDbIdAsInteger = Integer.parseInt(studyDbIdd.get());
                brapiResponseAlleleMatrices = brapiResponseMapAlleleMatrices.getBrapiResponseAlleleMatricesItemsByStudyDbId(studyDbIdAsInteger);
            } else {
                brapiResponseAlleleMatrices = brapiResponseMapAlleleMatrices.getBrapiResponseAlleleMatrices();
            }

            BrapiResponseEnvelopeMasterDetail.setResult(brapiResponseAlleleMatrices);

        } catch (GobiiException e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            BrapiResponseEnvelopeMasterDetail.getBrapiMetaData().addStatusMessage("exception", message);

        } catch (Exception e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            BrapiResponseEnvelopeMasterDetail.getBrapiMetaData().addStatusMessage("exception", message);
        }

        returnVal = objectMapper.writeValueAsString(BrapiResponseEnvelopeMasterDetail);

        return returnVal;
    }

    @RequestMapping(value = "/allelematrix-search",
            method = {RequestMethod.GET,RequestMethod.POST},
            produces = "application/json")
    @ResponseBody
    public String getAlleleMatrix(@RequestParam("matrixDbId") String matrixDbId,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws Exception {

        String returnVal = null;

        BrapiResponseEnvelope brapiResponseEnvelope = new BrapiResponseEnvelope();
        try {

            String cropType = CropRequestAnalyzer.getGobiiCropType(request);
            brapiResponseEnvelope.setBrapiMetaData(brapiResponseMapAlleleMatrixSearch.search(cropType, matrixDbId));


        } catch (GobiiException e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            brapiResponseEnvelope.getBrapiMetaData().addStatusMessage("exception", message);

        } catch (Exception e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            brapiResponseEnvelope.getBrapiMetaData().addStatusMessage("exception", message);
        }

        returnVal = objectMapper.writeValueAsString(brapiResponseEnvelope);

        return returnVal;
    }

    @RequestMapping(value = "/allelematrix-search/status/{jobId}",
            method = RequestMethod.GET,
            produces = "application/json")
    @ResponseBody
    public String getAlleleMatrixStatus(@PathVariable("jobId") String jobId,
                                        HttpServletRequest request,
                                        HttpServletResponse response) throws Exception {

        String returnVal = null;

        BrapiResponseEnvelope brapiResponseEnvelope = new BrapiResponseEnvelope();
        try {

            String cropType = CropRequestAnalyzer.getGobiiCropType(request);
            brapiResponseEnvelope.setBrapiMetaData(brapiResponseMapAlleleMatrixSearch.getStatus(cropType,
                    jobId,
                    request));


        } catch (GobiiException e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            brapiResponseEnvelope.getBrapiMetaData().addStatusMessage("exception", message);

        } catch (Exception e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            brapiResponseEnvelope.getBrapiMetaData().addStatusMessage("exception", message);
        }

        returnVal = objectMapper.writeValueAsString(brapiResponseEnvelope);

        return returnVal;
    }


    /***
     * Returns a stream for the at at the path specified by the query parameter. This method is not
     * defined by the BRAPI spec, nor should it be: the spec only stipulates that entries in the files
     * section of the metadata object be accessible to the client.
     *
     * @param fqpn: fully qualified path of file to download
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/files",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public void getFile(@RequestParam("fqpn") String fqpn,
                                        HttpServletRequest request,
                                        HttpServletResponse response) throws Exception {

        try {

            response.setContentType("application/text");
            InputStream inputStream = new FileInputStream(fqpn);
            // copy it to response's OutputStream
            org.apache.commons.io.IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();

        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file " + fqpn + "to output stream: " + ex.getMessage());
        }
    }


}// BRAPIController
