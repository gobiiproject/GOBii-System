package org.gobiiproject.gobiimodel.dto.container;

import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityParam;

import java.util.Date;


/**
 * Created by Angel on 5/6/2016.
 */
public class OrganizationDTO extends DtoMetaData {

    public OrganizationDTO() {}

    public OrganizationDTO(ProcessType processType) {
        super(processType);
    }

    // we are waiting until we a have a view to retirn
    // properties for that property: we don't know how to represent them yet
    private Integer organizationId;
    private String name;
    private String address;
    private String website;
    private Integer createdBy;
    private Date createdDate;
    private Integer modifiedBy;
    private Date modifiedDate;

    @GobiiEntityParam(paramName = "organizationId")
    public Integer getOrganizationId() {return organizationId;}
    @GobiiEntityColumn(columnName = "organization_id")
    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    @GobiiEntityParam(paramName = "name")
    public String getName() {
        return name;
    }
    @GobiiEntityColumn(columnName = "name")
    public void setName(String name) {
        this.name = name;
    }

    @GobiiEntityParam(paramName = "address")
    public String getAddress() {
        return address;
    }
    @GobiiEntityColumn(columnName = "address")
    public void setAddress(String address) {
        this.address = address;
    }

    @GobiiEntityParam(paramName = "website")
    public String getWebsite() {return website;}
    @GobiiEntityColumn(columnName = "website")
    public void setWebsite(String website) {
        this.website = website;
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

}
