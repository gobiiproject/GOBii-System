// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobiimodel.headerlesscontainer;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityParam;

import java.util.*;

public class MarkerGroupDTO extends DTOBase {


    public MarkerGroupDTO() {
    }

    private Integer markerGroupId;
    private String name;
    private String code;
    private List<MarkerGroupMarkerDTO> markers = new ArrayList<>();
    private String germplasmGroup;
    private Integer createdBy;
    private Date createdDate;
    private Integer modifiedBy;
    private Date modifiedDate;
    private Integer statusId;


    @Override
    public Integer getId() {
        return this.markerGroupId;
    }

    @Override
    public void setId(Integer id) {
        this.markerGroupId = id;
    }

    @GobiiEntityParam(paramName = "markerGroupId")
    public Integer getMarkerGroupId() {
        return markerGroupId;
    }

    @GobiiEntityColumn(columnName = "marker_group_id")
    public void setMarkerGroupId(Integer markerGroupId) {
        this.markerGroupId = markerGroupId;
    }


    @GobiiEntityParam(paramName = "status")
    public Integer getStatusId() {
        return statusId;
    }

    @GobiiEntityColumn(columnName = "status")
    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    @GobiiEntityParam(paramName = "modifiedDate")
    public Date getModifiedDate() {
        return modifiedDate;
    }

    @GobiiEntityColumn(columnName = "modified_date")
    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @GobiiEntityParam(paramName = "modifiedBy")
    public Integer getModifiedBy() {
        return modifiedBy;
    }

    @GobiiEntityColumn(columnName = "modified_by")
    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @GobiiEntityParam(paramName = "createdDate")
    public Date getCreatedDate() {
        return createdDate;
    }

    @GobiiEntityColumn(columnName = "created_date")
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @GobiiEntityParam(paramName = "createdBy")
    public Integer getCreatedBy() {
        return createdBy;
    }

    @GobiiEntityColumn(columnName = "created_by")
    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    @GobiiEntityParam(paramName = "germplasmGroup")
    public String getGermplasmGroup() {
        return germplasmGroup;
    }

    @GobiiEntityColumn(columnName = "germplasm_group")
    public void setGermplasmGroup(String germplasmGroup) {
        this.germplasmGroup = germplasmGroup;
    }

    public List<MarkerGroupMarkerDTO> getMarkers() {
        return markers;
    }

    public void setMarkers(List<MarkerGroupMarkerDTO> markers) {
        this.markers = markers;
    }

    @GobiiEntityParam(paramName = "code")
    public String getCode() {
        return code;
    }

    @GobiiEntityColumn(columnName = "code")
    public void setCode(String code) {
        this.code = code;
    }

    @GobiiEntityParam(paramName = "name")
    public String getName() {
        return name;
    }

    @GobiiEntityColumn(columnName = "name")
    public void setName(String name) {
        this.name = name;
    }


    // ******************** below this line is garbage and can probably be removed
    private Map<String, List<String>> markerMap = new HashMap<>();

    public Map<String, List<String>> getMarkerMap() {
        return markerMap;
    }

    public void setMarkerMap(Map<String, List<String>> markerMap) {
        this.markerMap = markerMap;
    }

    @JsonIgnore
    public Set<String> getMarkerGroups() {
        return markerMap.keySet();
    }

}//ResourceDTO
