package org.gobiiproject.gobiiapimodel.restresources;


import org.gobiiproject.gobiiapimodel.types.ControllerType;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Phil on 9/7/2016.
 */
public class UriFactory {

    private ControllerType controllerType;
    private String cropContextRoot;

    public UriFactory(String cropContextRoot, ControllerType controllerType) {

        this.controllerType = controllerType;
        this.cropContextRoot = cropContextRoot;
    }

    public UriFactory(String cropContextRoot) {

        this.controllerType = ControllerType.GOBII;
        this.cropContextRoot = cropContextRoot;
    }

    public RestUri RestUriFromUri(String uri) {
        return new RestUri(uri);
    }

    public RestUri resourceColl(ServiceRequestId serviceRequestId) throws Exception {

        RestUri returnVal;

        returnVal = new RestUri(this.cropContextRoot,
                this.controllerType,
                serviceRequestId);

        return returnVal;

    } //

    public static RestUri resourceColl(String contextRoot, ServiceRequestId serviceRequestId) throws Exception {

        RestUri returnVal;

        returnVal = new RestUri(contextRoot,
                ControllerType.GOBII,
                serviceRequestId);

        return returnVal;

    } //

    public RestUri resourceByUriIdParam(ServiceRequestId serviceRequestId) throws Exception {

        String paramName = "id";
        return new RestUri(this.cropContextRoot,
                this.controllerType,
                serviceRequestId)
                .addUriParam(paramName);
    } //

    public RestUri resourceByUriIdParamName(String paramName, ServiceRequestId serviceRequestId) throws Exception {

        return new RestUri(this.cropContextRoot,
                this.controllerType,
                serviceRequestId)
                .addUriParam(paramName);
    } //

    public static RestUri resourceByUriIdParam(String contextRoot, ServiceRequestId serviceRequestId) throws Exception {

        String paramName = "id";
        return new RestUri(contextRoot,
                ControllerType.GOBII,
                serviceRequestId)
                .addUriParam(paramName);
    } //


    public RestUri childResourceByUriIdParam(ServiceRequestId parentServiceRequestId, ServiceRequestId childServiceRequestId) throws Exception {

        String paramName = "id";
        RestUri returnVal = new RestUri(this.cropContextRoot,
                this.controllerType,
                parentServiceRequestId)
                .addUriParam(paramName)
                .appendSegment(childServiceRequestId);

        return returnVal;

    } //

    public RestUri contactsByQueryParams() throws Exception {

        RestUri returnVal = new RestUri(this.cropContextRoot,
                this.controllerType,
                ServiceRequestId.URL_CONTACT_SEARCH)
                .addQueryParam("email")
                .addQueryParam("lastName")
                .addQueryParam("firstName")
                .addQueryParam("userName");

        return returnVal;

    } //

    public RestUri markerssByQueryParams() throws Exception {

        RestUri returnVal = new RestUri(this.cropContextRoot,
                this.controllerType,
                ServiceRequestId.URL_MARKER_SEARCH)
                .addQueryParam("name");

        return returnVal;

    }

    public RestUri nameIdListByQueryParams() throws Exception {

        RestUri returnVal = new RestUri(this.cropContextRoot,
                this.controllerType,
                ServiceRequestId.URL_NAMES)
                .addUriParam("entity")
                .addQueryParam("filterType")
                .addQueryParam("filterValue");

        return returnVal;

    } //

    public RestUri fileLoaderPreview() throws Exception {

        RestUri returnVal = new RestUri(this.cropContextRoot,
                this.controllerType,
                ServiceRequestId.URL_FILE_LOAD)
                .addUriParam("directoryName")
                .addQueryParam("fileFormat");

        return returnVal;

    } //
}


