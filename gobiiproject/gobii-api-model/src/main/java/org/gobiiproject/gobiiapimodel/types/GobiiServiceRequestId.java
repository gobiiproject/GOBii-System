package org.gobiiproject.gobiiapimodel.types;

import org.gobiiproject.gobiimodel.utils.LineUtils;

/**
 * Created by Phil on 5/13/2016.
 */


public enum GobiiServiceRequestId {

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
    URL_ALLELE_MATRIX_SEARCH("allelematrix-search"),
    URL_ALLELE_MATRIX_SEARCH_STATUS("allelematrix-search/status"),
    URL_ALLELE_MATRICES("allelematrices"),
    URL_FILES("files");


    private String requestPath;

    GobiiServiceRequestId(String requestPath) {
        this.requestPath = requestPath;
    }

    public String getResourcePath() {
        return this.requestPath;
    }

    public String getRequestUrl(String contextRoot, String secondaryPath) throws Exception {

        String returnVal = "";


        if (!LineUtils.isNullOrEmpty(contextRoot)) {
            returnVal = contextRoot;
        }

        if (!LineUtils.isNullOrEmpty(secondaryPath)) {
            returnVal += secondaryPath;
        }

        returnVal += this.getResourcePath();

        return returnVal;
    }

}
