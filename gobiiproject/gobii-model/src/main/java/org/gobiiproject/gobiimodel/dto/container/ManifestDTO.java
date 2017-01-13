package org.gobiiproject.gobiimodel.dto.container;

import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityParam;

import java.util.Date;


/**
 * Created by Angel on 5/6/2016.
 */
public class ManifestDTO extends DtoMetaData {

    public ManifestDTO() {}

    public ManifestDTO(ProcessType processType) {
        super(processType);
    }

    // we are waiting until we a have a view to retirn
    // properties for that property: we don't know how to represent them yet
    private Integer manifestId;
    private String name;
    private String code;
    private String filePath;
    private Integer createdBy;
    private Date createdDate;
    private Integer modifiedBy;
    private Date modifiedDate;

    @GobiiEntityParam(paramName = "manifestId")
    public Integer getManifestId() {return manifestId;}
    @GobiiEntityColumn(columnName = "manifest_id")
    public void setManifestId(Integer manifestId) {
        this.manifestId = manifestId;
    }

    @GobiiEntityParam(paramName = "name")
    public String getName() {
        return name;
    }
    @GobiiEntityColumn(columnName = "name")
    public void setName(String name) {
        this.name = name;
    }

    @GobiiEntityParam(paramName = "code")
    public String getCode() {
        return code;
    }
    @GobiiEntityColumn(columnName = "code")
    public void setCode(String code) {
        this.code = code;
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
