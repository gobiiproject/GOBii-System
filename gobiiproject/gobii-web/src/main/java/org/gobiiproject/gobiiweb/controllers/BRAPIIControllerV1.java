// ************************************************************************
// (c) 2016 GOBii Projects
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobiiweb.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.ObjectUtils;
import org.gobiiproject.gobidomain.services.AnalysisService;
import org.gobiiproject.gobidomain.services.ConfigSettingsService;
import org.gobiiproject.gobidomain.services.ContactService;
import org.gobiiproject.gobidomain.services.CvService;
import org.gobiiproject.gobidomain.services.DataSetService;
import org.gobiiproject.gobidomain.services.DisplayService;
import org.gobiiproject.gobidomain.services.ExperimentService;
import org.gobiiproject.gobidomain.services.ExtractorInstructionFilesService;
import org.gobiiproject.gobidomain.services.LoaderFilesService;
import org.gobiiproject.gobidomain.services.LoaderInstructionFilesService;
import org.gobiiproject.gobidomain.services.ManifestService;
import org.gobiiproject.gobidomain.services.MapsetService;
import org.gobiiproject.gobidomain.services.MarkerGroupService;
import org.gobiiproject.gobidomain.services.MarkerService;
import org.gobiiproject.gobidomain.services.NameIdListService;
import org.gobiiproject.gobidomain.services.OrganizationService;
import org.gobiiproject.gobidomain.services.PingService;
import org.gobiiproject.gobidomain.services.PlatformService;
import org.gobiiproject.gobidomain.services.ProjectService;
import org.gobiiproject.gobidomain.services.ReferenceService;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiibrapi.calls.calls.BrapiResponseCallsItem;
import org.gobiiproject.gobiibrapi.calls.calls.BrapiResponseMapCalls;
import org.gobiiproject.gobiibrapi.calls.germplasm.BrapiResponseGermplasmByDbId;
import org.gobiiproject.gobiibrapi.calls.germplasm.BrapiResponseMapGermplasmByDbId;
import org.gobiiproject.gobiibrapi.calls.studies.observationvariables.BrapiResponseMapObservationVariables;
import org.gobiiproject.gobiibrapi.calls.studies.observationvariables.BrapiResponseObservationVariablesMaster;
import org.gobiiproject.gobiibrapi.calls.studies.search.BrapiRequestStudiesSearch;
import org.gobiiproject.gobiibrapi.calls.studies.search.BrapiResponseMapStudiesSearch;
import org.gobiiproject.gobiibrapi.calls.studies.search.BrapiResponseStudiesSearchItem;
import org.gobiiproject.gobiibrapi.core.common.BrapiRequestReader;
import org.gobiiproject.gobiibrapi.core.derived.BrapiListResult;
import org.gobiiproject.gobiibrapi.core.derived.BrapiResponseEnvelopeList;
import org.gobiiproject.gobiibrapi.core.derived.BrapiResponseEnvelopeMaster;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * This controller is only for BRAPI v1. compliant calls. It consumes the gobii-brapi module.
 *
 * BRAPI responses all containa a "metadata" and a "result" key. The "result" key's value can take
 * three forms:
 * 1) A "data" property that is an array of items of the same type (e.g., /studies-search)
 * 2) A set of arbitrary properties (i.e., a specific type in Java) (e.g., /germplasm/{id})
 * 3) A set of arbitrary properties and a "data" property (e.g., /studies/{studyDbId}/observationVariables)
 *
 * Type 3) is for a master-detail scenario: you have a master record that is related to one or more detail
 * records. Type 2) is just the master. Type 3) is just the detail records.
 *
 * The classes that support this API are as follows:
 *
 * BrapiResponseEnvelopeList: This class is used for type 1). It encapsulates "metadata" and is
 * type-parmaeterized for type type of the items contained in the list. The class for which
 * it is type-parameterized is an arbitrary pojo.
 *
 * BrapiResponseEnvelopeMaster: This class is used for type 2) and 3). It is type-parameterized
 * for an arbitrary pojo. The pojo may (as in the case of BrapiResponseObservationVariablesMaster)
 * extend BrapiResponseEnvelopeList. In that case, it is of type 3). If it does not,
 * it is of type 2.
 *
 * In the calls namespace of gobii-brapi is organized as the brapi API is organized.
 * Each call contains several sorts of classes:
 * ---- POJOs named BrapiResponse<CallName>: these are the arbitrary pojos that
 *      type-parameterize BrapiResponseEnvelopeList and BrapiResponseEnvelopeMaster
 * ---- POJOs anemed BrapiRequest<CallName>: these are POST/PUT bodies for which the
 *      relevant methods in this class have @RequestBody parameters (e.g., BrapiRequestStudiesSearch)
 * ---- POJOs named BrapiResponseMap<CallName>: Right now these clases crate dummy responses; the real
 *      implementations of these classes will consume classes from the gobii-domain project (i.e., the Service
 *      classes): they will get data from gobii in terms of gobii DTOs and convert the DTOs in to the
 *      BRAPI POJOs
 *
 * Note that this controller receives and sends plain String json data. This approach is different
 * from the gobii api. The BrapiResponseEnvelopeList and BrapiResponseEnvelopeMaster are serialzied to
 * json and sent over the wire in this way rather than letting the Jackson embedded through Spring do
 * the job automatically. This approach is more traditionally the web service way of doing things.
 *
 * The current state of this controller and the supporting classes in gobii-brapi is a proof-of-concept.
 * There is more work to be done. For example, the two types of BrapiReseponseEnvelope* should extend a
 * class that has "metadata" as a property. I'm also puzzled by the fact that in
 * BrapiEnvelopeRestResource.getMasterObjFromResult() we have to pull out the json segments separately for
 * de-serialization whilst in getTypedListObjFromResult() the derserialization just works autoamtically. However,
 * what we have here is the most systematic way I could find to deal with with BRAPI's document oriented
 * response structure in a way that is systematic for a strongly typed web framework in Java.
 *
 */
@Scope(value = "request")
@Controller
@RequestMapping(ServiceRequestId.SERVICE_PATH_BRAPI)
public class BRAPIIControllerV1 {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BRAPIIControllerV1.class);

    @Autowired
    private PingService pingService = null;

    @Autowired
    private ProjectService projectService = null;

    @Autowired
    private ContactService contactService = null;

    @Autowired
    private ReferenceService referenceService = null;

    @Autowired
    private AnalysisService analysisService = null;

    @Autowired
    private ManifestService manifestService = null;

    @Autowired
    private MarkerGroupService markerGroupService = null;

    @Autowired
    private OrganizationService organizationService = null;

    @Autowired
    private ExperimentService experimentService = null;

    @Autowired
    private NameIdListService nameIdListService = null;

    @Autowired
    private LoaderInstructionFilesService loaderInstructionFilesService = null;

    @Autowired
    private ExtractorInstructionFilesService extractorInstructionFilesService = null;

    @Autowired
    private LoaderFilesService loaderFilesService = null;

    @Autowired
    private DisplayService displayService = null;

    @Autowired
    private CvService cvService = null;

    @Autowired
    private MarkerService markerService = null;

    @Autowired
    private DataSetService dataSetService = null;

    @Autowired
    private PlatformService platformService = null;

    @Autowired
    private MapsetService mapsetService = null;

    @Autowired
    private ConfigSettingsService configSettingsService;


    // *********************************************
    // *************************** CALLS
    // *********************************************
    @RequestMapping(value = "/calls",
            method = RequestMethod.GET,
            produces = "application/json")
    @ResponseBody
    public String getCalls(
            HttpServletRequest request,
            HttpServletResponse response) throws Exception{

        String returnVal;

        BrapiResponseEnvelopeList<ObjectUtils.Null, BrapiResponseCallsItem> brapiResponseEnvelopeList =
                new BrapiResponseEnvelopeList<>(ObjectUtils.Null.class, BrapiResponseCallsItem.class);
        try {

            BrapiResponseMapCalls brapiResponseMapCalls = new BrapiResponseMapCalls(request);
            BrapiListResult<BrapiResponseCallsItem> brapiResponseCallsItems = brapiResponseMapCalls
                    .getBrapiResponseListCalls();


            brapiResponseEnvelopeList.setData(brapiResponseCallsItems);


        } catch (GobiiException e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            brapiResponseEnvelopeList.getBrapiMetaData().addStatusMessage("exception", message);

        } catch (Exception e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            brapiResponseEnvelopeList.getBrapiMetaData().addStatusMessage("exception", message);
        }

        returnVal = (new ObjectMapper()).writeValueAsString(brapiResponseEnvelopeList);


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

        BrapiResponseEnvelopeList<ObjectUtils.Null, BrapiResponseStudiesSearchItem> brapiResponseEnvelopeList =
                new BrapiResponseEnvelopeList<>(ObjectUtils.Null.class, BrapiResponseStudiesSearchItem.class);

        try {

            BrapiRequestReader<BrapiRequestStudiesSearch> brapiRequestReader = new BrapiRequestReader<>(BrapiRequestStudiesSearch.class);
            BrapiRequestStudiesSearch brapiRequestStudiesSearch = brapiRequestReader.makeRequestObj(studiesRequestBody);

            BrapiListResult<BrapiResponseStudiesSearchItem> brapiListResult = (new BrapiResponseMapStudiesSearch()).getBrapiResponseStudySearchItems(brapiRequestStudiesSearch);

            brapiResponseEnvelopeList.setData(brapiListResult);

        } catch (GobiiException e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            brapiResponseEnvelopeList.getBrapiMetaData().addStatusMessage("exception", message);

        }catch (Exception e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            brapiResponseEnvelopeList.getBrapiMetaData().addStatusMessage("exception", message);
        }

        returnVal = (new ObjectMapper()).writeValueAsString(brapiResponseEnvelopeList);
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
                = new BrapiResponseEnvelopeMaster<>(BrapiResponseGermplasmByDbId.class);

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

        returnVal = (new ObjectMapper()).writeValueAsString(responseEnvelope);
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

        BrapiResponseEnvelopeMaster<BrapiResponseObservationVariablesMaster> responseEnvelope
                = new BrapiResponseEnvelopeMaster<>(BrapiResponseObservationVariablesMaster.class);

        String returnVal;

        try {

            BrapiResponseMapObservationVariables brapiResponseMapObservationVariables = new BrapiResponseMapObservationVariables();

            BrapiResponseObservationVariablesMaster brapiResponseObservationVariablesMaster = brapiResponseMapObservationVariables.gerObservationVariablesByStudyId(studyDbId);

            responseEnvelope.setResult(brapiResponseObservationVariablesMaster);

        } catch (GobiiException e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            responseEnvelope.getBrapiMetaData().addStatusMessage("exception", message);

        }catch (Exception e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            responseEnvelope.getBrapiMetaData().addStatusMessage("exception", message);

        }

        returnVal = (new ObjectMapper()).writeValueAsString(responseEnvelope);

        return returnVal;
    }


}// BRAPIController
