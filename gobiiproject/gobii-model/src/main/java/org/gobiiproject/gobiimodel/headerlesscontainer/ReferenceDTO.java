package org.gobiiproject.gobiimodel.headerlesscontainer;

import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityParam;

import java.util.Date;


/**
 * Created by Angel on 5/6/2016.
 * Modified by Yanii on 1/26/2017
 */
public class ReferenceDTO extends DTOBase {

    public ReferenceDTO() {}

    // we are waiting until we a have a view to retirn
    // properties for that property: we don't know how to represent them yet
    private Integer referenceId;

    private String name;
    private String version;
    private String link;
    private String filePath;
    private Integer createdBy;
    private Date createdDate;
    private Integer modifiedBy;
    private Date modifiedDate;

    @Override
    public Integer getId() {
        return this.referenceId;
    }

    @Override
    public void setId(Integer id) {
        this.referenceId = id;
    }

    @GobiiEntityParam(paramName = "referenceId")
    public Integer getReferenceId() {return referenceId;}
    @GobiiEntityColumn(columnName = "reference_id")
    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    @GobiiEntityParam(paramName = "name")
    public String getName() {
        return name;
    }
    @GobiiEntityColumn(columnName = "name")
    public void setName(String name) {
        this.name = name;
    }

    @GobiiEntityParam(paramName = "version")
    public String getVersion() {
        return version;
    }
    @GobiiEntityColumn(columnName = "version")
    public void setVersion(String version) {
        this.version = version;
    }

    @GobiiEntityParam(paramName = "link")
    public String getLink() {
        return link;
    }
    @GobiiEntityColumn(columnName = "link")
    public void setLink(String link) {
        this.link = link;
    }

    @GobiiEntityParam(paramName = "filePath")
    public String getFilePath() {return filePath;}
    @GobiiEntityColumn(columnName = "file_path")
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @GobiiEntityParam(paramName = "createdBy")
    public Integer getCreatedBy() {
        return createdBy;
    }

    @GobiiEntityColumn(columnName = "created_by")
    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    @GobiiEntityParam(paramName = "createdDate")
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


}
