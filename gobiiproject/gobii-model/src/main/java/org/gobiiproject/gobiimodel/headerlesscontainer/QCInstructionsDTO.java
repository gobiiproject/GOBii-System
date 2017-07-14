package org.gobiiproject.gobiimodel.headerlesscontainer;

import org.gobiiproject.gobiimodel.dto.instructions.GobiiQCComplete;
import org.gobiiproject.gobiimodel.types.GobiiJobStatus;

/**
 * Created by araquel on 1/6/2017.
 */
public class QCInstructionsDTO extends DTOBase {

    Integer datasetId;
    Integer contactId;
    String dataFileName;
    String dataFileDirectory;
    String qualityFileName;
    GobiiJobStatus gobiiJobStatus;

    public QCInstructionsDTO() {
    }

//    GobiiQCComplete gobiiQCComplete = new GobiiQCComplete();

    @Override
    public Integer getId() {
        return 1;
    }

    @Override
    public void setId(Integer id) {

    }

//    public GobiiQCComplete getGobiiQCComplete() {
//        return gobiiQCComplete;
//    }

//    public void setGobiiQCComplete(GobiiQCComplete gobiiQCComplete) {
//        this.gobiiQCComplete = gobiiQCComplete;
//    }

    public GobiiJobStatus getGobiiJobStatus() { return gobiiJobStatus; }

    public void setGobiiJobStatus(GobiiJobStatus gobiiJobStatus) { this.gobiiJobStatus = gobiiJobStatus; }

    public Integer getDatasetId() { return datasetId; }

    public void setDatasetId(Integer datasetId) { this.datasetId = datasetId; }

    public Integer getContactId() { return contactId; }

    public void setContactId(Integer contactId) { this.contactId = contactId; }

    public String getDataFileName() { return dataFileName; }

    public void setDataFileName(String dataFileName) { this.dataFileName = dataFileName; }

    public String getDataFileDirectory() { return dataFileDirectory; }

    public void setDataFileDirectory(String dataFileDirectory) { this.dataFileDirectory = dataFileDirectory; }

    public String getQualityFileName() { return qualityFileName; }

    public void setQualityFileName(String qualityFileName) { this.qualityFileName = qualityFileName; }
}
