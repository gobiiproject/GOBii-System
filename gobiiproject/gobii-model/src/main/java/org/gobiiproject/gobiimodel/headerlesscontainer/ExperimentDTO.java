package org.gobiiproject.gobiimodel.headerlesscontainer;

import org.gobiiproject.gobiimodel.tobemovedtoapimodel.Header;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityParam;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;

import java.util.Date;

/**
 * Created by Angel on 4/13/2016.
 */
public class ExperimentDTO extends DTOBase {

    public ExperimentDTO() {
    }


    private Integer experimentId = 0;
    private String experimentName = null;
    private String experimentCode = null;
    private String experimentDataFile = null;
    private Integer projectId;
    private Integer vendorProtocolId = null;
    private Integer manifestId;
    private Integer createdBy;
    private Date createdDate;
    private Integer modifiedBy;
    private Date modifiedDate;
    private Integer statusId;


    @Override
    public Integer getId() {
        return this.experimentId;
    }

    @Override
    public void setId(Integer id) {
        this.experimentId = id;
    }

    @GobiiEntityParam(paramName = "experimentId")
    public Integer getExperimentId() {
        return experimentId;
    }

    @GobiiEntityColumn(columnName = "experiment_id")
    public void setExperimentId(Integer experimentId) {
        this.experimentId = experimentId;
    }

    @GobiiEntityParam(paramName = "experimentName")
    public String getExperimentName() {
        return experimentName;
    }

    @GobiiEntityColumn(columnName = "name")
    public void setExperimentName(String experimentName) {
        this.experimentName = experimentName;
    }

    @GobiiEntityParam(paramName = "experimentCode")
    public String getExperimentCode() {
        return experimentCode;
    }

    @GobiiEntityColumn(columnName = "code")
    public void setExperimentCode(String experimentCode) {
        this.experimentCode = experimentCode;
    }

    @GobiiEntityParam(paramName = "experimentDataFile")
    public String getExperimentDataFile() {
        return experimentDataFile;
    }

    @GobiiEntityColumn(columnName = "data_file")
    public void setExperimentDataFile(String dataFile) {
        this.experimentDataFile = dataFile;
    }

    @GobiiEntityParam(paramName = "projectId")
    public Integer getProjectId() {
        return projectId;
    }

    @GobiiEntityColumn(columnName = "project_id")
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }


    @GobiiEntityParam(paramName = "vendorProtocolId")
    public Integer getVendorProtocolId() {
        return vendorProtocolId;
    }

    @GobiiEntityColumn(columnName = "vendor_protocol_id")
    public void setVendorProtocolId(Integer vendorProtocolId) {
        this.vendorProtocolId = vendorProtocolId;
    }

    @GobiiEntityParam(paramName = "manifestId")
    public Integer getManifestId() {
        return manifestId;
    }

    @GobiiEntityColumn(columnName = "manifest_id")
    public void setManifestId(Integer manifestId) {
        this.manifestId = manifestId;
    }

    @GobiiEntityParam(paramName = "createdBy")
    public Integer getCreatedBy() {
        return createdBy;
    }

    @GobiiEntityColumn(columnName = "created_by")
    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    @GobiiEntityParam(paramName = "createDate")
    public Date getCreatedDate() {
        return createdDate;
    }

    @GobiiEntityColumn(columnName = "created_date")
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @GobiiEntityParam(paramName = "modifiedBy")
    public Integer getModifiedBy() {
        return modifiedBy;
    }

    @GobiiEntityColumn(columnName = "modified_by")
    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @GobiiEntityParam(paramName = "modifiedDate")
    public Date getModifiedDate() {
        return modifiedDate;
    }

    @GobiiEntityColumn(columnName = "modified_date")
    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @GobiiEntityParam(paramName = "status")
    public Integer getStatusId() {
        return statusId;
    }

    @GobiiEntityColumn(columnName = "status")
    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }


}
