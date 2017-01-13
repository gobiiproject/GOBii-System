package org.gobiiproject.gobiimodel.dto.container;

import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityParam;

import java.util.Date;

/**
 * Created by Angel on 4/13/2016.
 */
public class ExperimentDTO extends DtoMetaData {

    public ExperimentDTO() {
    }

    public ExperimentDTO(ProcessType processType) {
        super(processType);
    }

    private Integer experimentId;
    private String experimentName = null;
    private String experimentCode = null;
    private String experimentDataFile = null;
    private Integer projectId;
    private Integer platformId;
    private String platformName;
    private Integer manifestId;
    private Integer createdBy;
    private Date createdDate;
    private Integer modifiedBy;
    private Date modifiedDate;
    private Integer status;


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

    @GobiiEntityParam(paramName = "platformId")
    public Integer getPlatformId() {
        return platformId;
    }

    @GobiiEntityColumn(columnName = "platform_id")
    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    @GobiiEntityParam(paramName = "platformName")
    public String getPlatformName() {
        return platformName;
    }

    @GobiiEntityColumn(columnName = "platform_name")
    public void setPlatformName(String platformName) {
        this.platformName = platformName;
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
    public Integer getStatus() {
        return status;
    }

    @GobiiEntityColumn(columnName = "status")
    public void setStatus(Integer status) {
        this.status = status;
    }


}
