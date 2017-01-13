/// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiapimodel.restresources;

import org.gobiiproject.gobiiapimodel.types.ControllerType;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;

public class ResourceBuilder {


    //private final static String APP_ROOT = "/gobii-web/";
    private final static String CTRLR_EXTRACT = "extract/";
    private final static String CTRLR_LOAD = "load/";
    private final static String CTRLR_GOBII = "gobii/v1/";
    private final static String CTRLR_BRAPI = "brapi/v1/";


    public static String getRequestUrl(ControllerType controllerType,
                                       String cropContextRoot,
                                       ServiceRequestId requestId) throws Exception {

        return ResourceBuilder.getRequestUrl(controllerType,
                requestId,
                cropContextRoot);
    }


    public static String getUrlSegment(ServiceRequestId serviceRequestId) throws Exception {

        String returnVal;

        switch (serviceRequestId) {

            case URL_AUTH:
                returnVal = "auth";
                break;

            case URL_PING:
                returnVal = "ping";
                break;

            case URL_PROJECTS:
                returnVal = "projects";
                break;

            case URL_NAME_ID_LIST_DEPRECATED:
                returnVal = "nameidlist";
                break;

            case URL_NAMES:
                returnVal = "names";
                break;

            case URL_FILE_LOAD:
                returnVal = "files/loader";
                break;

            case URL_FILE_LOAD_INSTRUCTIONS:
                returnVal = "instructions/loader";
                break;

            case URL_FILE_EXTRACTOR_INSTRUCTIONS:
                returnVal = "instructions/extractor";
                break;

            case URL_FILE_EXTRACTOR_STATUS:
                returnVal = "instructions/extractor/status";
                break;

            case URL_DISPLAY:
                returnVal = "display";
                break;

            case URL_CV:
                returnVal = "cv";
                break;

            case URL_CONTACTS:
                returnVal = "contacts";
                break;

            case URL_CONTACT_SEARCH:
                returnVal = "contact-search";
                break;

            case URL_ORGANIZATION_SEARCH:
                returnVal = "organization-search";
                break;

            case URL_REFERENCE:
                returnVal = "reference";
                break;

            case URL_EXPERIMENTS:
                returnVal = "experiments";
                break;

            case URL_DATASETS:
                returnVal = "datasets";
                break;

            case URL_ANALYSIS:
                returnVal = "analysis";
                break;

            case URL_MARKERGROUP:
                returnVal = "markergroup";
                break;

            case URL_MANIFEST:
                returnVal = "manifest";
                break;

            case URL_ORGANIZATION:
                returnVal = "organizations";
                break;

            case URL_PLATFORM:
                returnVal = "platforms";
                break;

            case URL_MAPSET:
                returnVal = "mapset";
                break;

            case URL_CONFIGSETTINGS:
                returnVal = "configsettings";
                break;

            case URL_ROLES:
                returnVal = "roles";
                break;


            case URL_MARKERS:
                returnVal = "markers";
                break;

            case URL_MARKER_SEARCH:
                returnVal = "marker-search";
                break;

            case URL_CALLS:
                returnVal = "calls";
                break;

            case URL_STUDIES_SEARCH:
                returnVal = "studies-search";
                break;

            case URL_STUDIES:
                returnVal = "studies";
                break;

            case URL_GERMPLASM:
                returnVal = "germplasm";
                break;

            case URL_OBSERVATION_VARIABLES:
                returnVal = "observationVariables";
                break;
            case URL_PROTOCOL:
                returnVal = "protocols";
                break;
            case URL_VENDORS:
                returnVal = "vendors";
                break;
            default:
                throw new Exception("Unknown request id : " + serviceRequestId.toString());
        }

        return returnVal;
    }

    public static String getRequestUrl(ControllerType controllerType,
                                       ServiceRequestId requestId,
                                       String contextRoot) throws Exception {

        String controller;
        if (controllerType == ControllerType.LOADER) {
            controller = CTRLR_LOAD;
        } else if (controllerType == ControllerType.EXTRACTOR) {
            controller = CTRLR_EXTRACT;
        } else if (controllerType == ControllerType.GOBII) {
            controller = CTRLR_GOBII;
        } else if (controllerType == ControllerType.BRAPI) {
            controller = CTRLR_BRAPI;
        } else {
            throw new Exception("Unknown controller type: " + controllerType.toString());
        }

        String returnVal = contextRoot + controller;

        returnVal += getUrlSegment(requestId);

        return returnVal;
    }

} // ResourceBuilder
