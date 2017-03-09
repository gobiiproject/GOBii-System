package org.gobiiproject.gobiiapimodel.types;

/**
 * Created by Phil on 5/13/2016.
 */
public enum ControllerType {
    GOBII("gobii/v1/"),
    BRAPI("brapi/v1/");

    private String controllerPath;
    ControllerType(String controllerPath) {
        this.controllerPath=controllerPath;
    }

    public String getControllerPath() {
        return this.controllerPath;
    }
}


