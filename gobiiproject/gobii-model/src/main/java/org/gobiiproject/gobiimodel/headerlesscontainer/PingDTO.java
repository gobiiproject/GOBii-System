// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobiimodel.headerlesscontainer;


import java.util.*;

public class PingDTO extends DTOBase {

    public PingDTO() {
    }


    private Integer ping_id = 0;
    @Override
    public Integer getId() {
        return this.ping_id;
    }

    @Override
    public void setId(Integer id) {
        this.ping_id = id;
    }

    private List<String> pingRequests = new ArrayList<>();
    private List<String> pingResponses = new ArrayList<>();

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
