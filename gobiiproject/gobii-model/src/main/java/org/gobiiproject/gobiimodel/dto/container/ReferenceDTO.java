package org.gobiiproject.gobiimodel.dto.container;

import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Angel on 5/6/2016.
 */
public class ReferenceDTO extends DtoMetaData {

    public ReferenceDTO() {}

    public ReferenceDTO(ProcessType processType) {
        super(processType);
    }

    // we are waiting until we a have a view to retirn
    // properties for that property: we don't know how to represent them yet
    private Integer referenceId;

    private String name;
    private String version;
    private String link;
    private String filePath;

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

}
