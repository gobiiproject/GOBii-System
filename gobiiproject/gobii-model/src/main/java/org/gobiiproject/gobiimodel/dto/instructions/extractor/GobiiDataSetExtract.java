package org.gobiiproject.gobiimodel.dto.instructions.extractor;

import org.gobiiproject.gobiimodel.types.GobiiFileType;

/**
 * Created by Phil on 6/6/2016.
 */
public class GobiiDataSetExtract {

    private GobiiFileType gobiiFileType = null;
    private boolean accolate = false;
    private String dataSetName = null;
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


}
