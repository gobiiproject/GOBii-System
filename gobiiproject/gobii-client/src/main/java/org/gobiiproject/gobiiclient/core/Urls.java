// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.core;

import org.gobiiproject.gobiimodel.dto.types.ControllerType;
import org.gobiiproject.gobiimodel.dto.types.ServiceRequestId;

public class Urls {


    //private final static String APP_ROOT = "/gobii-web/";
    private final static String CTRLR_EXTRACT = "extract/";
    private final static String CTRLR_LOAD = "load/";


//        String contextRoot = ClientContext.getInstance(null, false).getCurrentCropContextRoot();


    public static String getRequestUrl(ControllerType controllerType,
                                       ServiceRequestId requestId) throws Exception {

        return Urls.getRequestUrl(controllerType,
                requestId,
                ClientContext.getInstance(null, false).getCurrentCropContextRoot());
    }

    public static String getRequestUrl(ControllerType controllerType,
                                       ServiceRequestId requestId,
                                       String contextRoot) throws Exception {

        String controller = null;
        if (controllerType == ControllerType.LOADER) {
            controller = CTRLR_LOAD;
        } else if (controllerType == ControllerType.EXTRACTOR) {
            controller = CTRLR_EXTRACT;
        }

        String returnVal = contextRoot + controller;

        switch (requestId) {

            case URL_AUTH:
                returnVal += "auth";
                break;

            case URL_PING:
                returnVal += "ping";
                break;

            case URL_PROJECT:
                returnVal += "project";
                break;

            case URL_NAME_ID_LIST:
                returnVal += "nameidlist";
                break;

            case URL_FILE_LOAD_INSTRUCTIONS:
                returnVal += "instructions";
                break;

            case URL_FILE_EXTRACTOR_INSTRUCTIONS:
                returnVal += "extractorInstructions";
                break;

            case URL_DISPLAY:
                returnVal += "display";
                break;

            case URL_CV:
                returnVal += "cv";
                break;

            case URL_CONTACT:
                returnVal += "contact";
                break;

            case URL_REFERENCE:
                returnVal += "reference";
                break;

            case URL_EXPERIMENT:
                returnVal += "experiment";
                break;

            case URL_DATASET:
                returnVal += "dataset";
                break;

            case URL_ANALYSIS:
                returnVal += "analysis";
                break;

            case URL_MARKERGROUP:
                returnVal += "markergroup";
                break;

            case URL_MANIFEST:
                returnVal += "manifest";
                break;

            case URL_ORGANIZATION:
                returnVal += "organization";
                break;

            case URL_PLATFORM:
                returnVal += "platform";
                break;

            case URL_MAPSET:
                returnVal += "mapset";
                break;

            case URL_CONFIGSETTINGS:
                returnVal += "configsettings";
                break;

            default:
                throw new Exception("Unknown request id : " + requestId.toString());
        }

        return returnVal;
    }

} // Urls
