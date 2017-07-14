package org.gobiiproject.gobiiapimodel.types;

/**
 * Created by Phil on 5/13/2016.
 */


public enum ServiceRequestId {

    URL_AUTH("auth"),
    URL_PING("ping"),
    URL_PROJECTS("projects"),
    URL_NAME_ID_LIST_DEPRECATED("nameidlist"),
    URL_NAMES("names"),
    URL_FILE_LOAD("files/loader"),
    URL_FILE_LOAD_INSTRUCTIONS("instructions/loader"),
    URL_FILE_EXTRACTOR_INSTRUCTIONS("instructions/extractor"),
    URL_FILE_EXTRACTOR_STATUS("instructions/extractor/status"),
    URL_DISPLAY("displays"),
    URL_CV("cvs"),
    URL_CVGROUP("cvgroups"),
    URL_CONTACTS("contacts"),
    URL_CONTACT_SEARCH("contact-search"),
    URL_ORGANIZATION_SEARCH("organization-search"),
    URL_REFERENCE("references"),
    URL_EXPERIMENTS("experiments"),
    URL_DATASETS("datasets"),
    URL_DATASETTYPES("datasets/types"),
    URL_ANALYSIS("analyses"),
    URL_MARKERGROUP("markergroups"),
    URL_MANIFEST("manifests"),
    URL_ORGANIZATION("organizations"),
    URL_PLATFORM("platforms"),
    URL_MAPSET("mapsets"),
    URL_CONFIGSETTINGS("configsettings"),
    URL_ROLES("roles"),
    URL_MARKERS("markers"),
    URL_MARKER_SEARCH("marker-search"),
    URL_CALLS("calls"),
    URL_STUDIES_SEARCH("studies-search"),
    URL_STUDIES("studies"),
    URL_GERMPLASM("germplasm"),
    URL_OBSERVATION_VARIABLES("observationVariables"),
    URL_PROTOCOL("protocols"),
    URL_VENDORS("vendors"),
    URL_FILE_QC_INSTRUCTIONS("/instructions/qualitycontrol");


    public static final String SERVICE_PATH_GOBII = "/gobii/v1";
    public static final String SERVICE_PATH_BRAPI = "/brapi/v1";


    private String requestPath;

    ServiceRequestId(String requestPath) {
        this.requestPath = requestPath;
    }

    public String getRequestPath() {
        return this.requestPath;
    }

    public String getRequestUrl(String contextRoot, ControllerType controllerType) throws Exception {

        String controllerPath = getControllerPath(controllerType);

        String returnVal = null;

        if( contextRoot != null ) {
            returnVal = contextRoot + controllerPath;
        } else {
            returnVal = controllerPath;
        }

        returnVal += this.getRequestPath();

        return returnVal;
    }

    public static String getControllerPath(ControllerType controllerType) throws Exception {

        String returnVal = null;

        if (controllerType == ControllerType.GOBII) {

            returnVal = ServiceRequestId.SERVICE_PATH_GOBII + "/";

        } else if (controllerType == ControllerType.BRAPI) {

            returnVal = ServiceRequestId.SERVICE_PATH_BRAPI + "/";
        } else {
            throw new Exception("Unknown controller type: " + controllerType.toString());
        }

        return returnVal;

    } // getControllerPath()
}
