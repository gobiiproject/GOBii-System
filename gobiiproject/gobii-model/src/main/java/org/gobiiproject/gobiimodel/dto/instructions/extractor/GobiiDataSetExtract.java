package org.gobiiproject.gobiimodel.dto.instructions.extractor;

import org.gobiiproject.gobiimodel.types.GobiiFileType;
import org.gobiiproject.gobiimodel.types.GobiiJobStatus;

/**
 * Data Set-Specific extract. Each represents a single data set extracting to a unique file name.
 * Created by Phil on 6/6/2016.
 */
public class GobiiDataSetExtract {

    //Type of file to export (or meta data without separate data entries
    private GobiiFileType gobiiFileType = null;
    //Combine data sets into a single output file (Unused/unsupported)
    private boolean accolate = false;
    //Descriptive name of the data set. Used in reporting
    private String dataSetName = null;
    private GobiiJobStatus gobiiJobStatus;
    //Internal ID of the data set. Used for internal lookups.
    private Integer dataSetId = null;
    String extractDestinationDirectory = null;

    public GobiiFileType getGobiiFileType() {
        return gobiiFileType;
    }

    public void setGobiiFileType(GobiiFileType gobiiFileType) {
        this.gobiiFileType = gobiiFileType;
    }

    public boolean isAccolate() {
        return accolate;
    }

    public void setAccolate(boolean accolate) {
        this.accolate = accolate;
    }

    public String getDataSetName() {
        return dataSetName;
    }

    public void setDataSetName(String dataSetName) {
        this.dataSetName = dataSetName;
    }

    public Integer getDataSetId() {
        return dataSetId;
    }

    public void setDataSetId(Integer dataSetId) {
        this.dataSetId = dataSetId;
    }

    public String getExtractDestinationDirectory() {
        return extractDestinationDirectory;
    }

    public void setExtractDestinationDirectory(String extractDestinationDirectory) {
        this.extractDestinationDirectory = extractDestinationDirectory;
    }

    public GobiiJobStatus getGobiiJobStatus() {
        return gobiiJobStatus;
    }

    public void setGobiiJobStatus(GobiiJobStatus gobiiJobStatus) {
        this.gobiiJobStatus = gobiiJobStatus;
    }

}
