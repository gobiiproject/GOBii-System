package org.gobiiproject.gobiiapimodel.types;

/**
 * Created by Phil on 5/13/2016.
 */
public enum GobiiControllerType {
    GOBII(GobiiControllerType.SERVICE_PATH_GOBII),
    BRAPI(GobiiControllerType.SERVICE_PATH_BRAPI);


    // we need these to be static final so that they can also be used for the root @RequestMapping
    // annotation in the Controllers themselves.
    public static final String SERVICE_PATH_GOBII = "/gobii/v1/";
    public static final String SERVICE_PATH_BRAPI = "/brapi/v1/";

    private String controllerPath;

    GobiiControllerType(String controllerPath) {
        this.controllerPath = controllerPath;
    }

    public String getControllerPath() {
        return this.controllerPath;
    }

}


