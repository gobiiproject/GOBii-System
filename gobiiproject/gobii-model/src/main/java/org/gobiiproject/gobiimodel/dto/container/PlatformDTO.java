package org.gobiiproject.gobiimodel.dto.container;

import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Phil on 4/21/2016.
 */
public class PlatformDTO extends DtoMetaData {

    public PlatformDTO() {
    }

    public PlatformDTO(ProcessType processType) {
        super(processType);
    }

    private Integer platformId;
    private String platformName;
    private String platformCode;
    private Integer platformVendor;
    private String platformDescription;
    private Integer createdBy;
    private Date createdDate;
    private Integer modifiedBy;
    private Date modifiedDate;
    private Integer status;
    private Integer typeId;
    private List<EntityPropertyDTO> properties = new ArrayList<>();


    @GobiiEntityParam(paramName = "platformId")
    public Integer getPlatformId() {
        return platformId;
    }

    @GobiiEntityColumn(columnName ="platform_id")
    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    @GobiiEntityParam(paramName = "platformName")
    public String getPlatformName() {
        return platformName;
    }

    @GobiiEntityColumn(columnName ="name")
    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    @GobiiEntityParam(paramName = "platformCode")
    public String getPlatformCode() {
        return platformCode;
    }

    @GobiiEntityColumn(columnName ="code")
    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    @GobiiEntityParam(paramName = "platformVendor")
    public Integer getPlatformVendor() {
        return platformVendor;
    }

    @GobiiEntityColumn(columnName ="vendor_id")
    public void setPlatformVendor(Integer platformVendor) {
        this.platformVendor = platformVendor;
    }

    @GobiiEntityParam(paramName = "platformDescription")
    public String getPlatformDescription() {
        return platformDescription;
    }

    @GobiiEntityColumn(columnName ="description")
    public void setPlatformDescription(String platformDescription) {
        this.platformDescription = platformDescription;
    }

    @GobiiEntityParam(paramName = "createdBy")
    public Integer getCreatedBy() {
        return createdBy;
    }

    @GobiiEntityColumn(columnName ="created_by")
    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    @GobiiEntityParam(paramName = "createdDate")
    public Date getCreatedDate() {
        return createdDate;
    }

    @GobiiEntityColumn(columnName ="created_date")
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @GobiiEntityParam(paramName = "modifiedBy")
    public Integer getModifiedBy() {
        return modifiedBy;
    }

    @GobiiEntityColumn(columnName ="modified_by")
    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @GobiiEntityParam(paramName = "modifiedDate")
    public Date getModifiedDate() {
        return modifiedDate;
    }

    @GobiiEntityColumn(columnName ="modified_date")
    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @GobiiEntityParam(paramName = "status")
    public Integer getStatus() {
        return status;
    }

    @GobiiEntityColumn(columnName ="status")
    public void setStatus(Integer status) {
        this.status = status;
    }

    @GobiiEntityParam(paramName = "typeId")
    public Integer getTypeId() {
        return typeId;
    }

    @GobiiEntityColumn(columnName ="type_id")
    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }


    public List<EntityPropertyDTO> getProperties() {
        return properties;
    }

    public void setProperties(List<EntityPropertyDTO> properties) {
        this.properties = properties;
    }
}

