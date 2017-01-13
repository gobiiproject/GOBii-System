// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobiimodel.dto.container;


import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.types.ControllerType;

import java.util.*;

public class PingDTO extends DtoMetaData {

    public PingDTO() {
    }

    private ControllerType controllerType = ControllerType.LOADER;
    private List<String> pingRequests = new ArrayList<>();
    private List<String> pingResponses = new ArrayList<>();

    public ControllerType getControllerType() {
        return controllerType;
    }

    public void setControllerType(ControllerType controllerType) {
        this.controllerType = controllerType;
    }


    public List<String> getDbMetaData() {
        return pingRequests;
    }

    public void setPingRequests(List<String> pingRequests) {
        this.pingRequests = pingRequests;
    }

    public List<String> getPingResponses() {
        return pingResponses;
    }

    public void setPingResponses(List<String> pingResponses) {
        this.pingResponses = pingResponses;
    }

}//ResourceDTO
