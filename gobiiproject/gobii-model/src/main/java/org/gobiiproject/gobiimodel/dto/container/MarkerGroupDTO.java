// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobiimodel.dto.container;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityParam;

import java.util.*;

public class MarkerGroupDTO extends DtoMetaData {


    public MarkerGroupDTO() {
    }

    public MarkerGroupDTO(ProcessType processType) {
        super(processType);
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
    private Integer status;


    @GobiiEntityParam(paramName = "markerGroupId")
    public Integer getMarkerGroupId() {
        return markerGroupId;
    }

    @GobiiEntityColumn(columnName = "marker_group_id")
    public void setMarkerGroupId(Integer markerGroupId) {
        this.markerGroupId = markerGroupId;
    }


    @GobiiEntityParam(paramName = "status")
    public Integer getStatus() {
        return status;
    }

    @GobiiEntityColumn(columnName = "status")
    public void setStatus(Integer status) {
        this.status = status;
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
