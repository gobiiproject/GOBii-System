package org.gobiiproject.gobiimodel.dto.instructions.extractor;

import org.gobiiproject.gobiimodel.types.GobiiExtractFilterType;
import org.gobiiproject.gobiimodel.types.GobiiFileType;
import org.gobiiproject.gobiimodel.types.GobiiJobStatus;
import org.gobiiproject.gobiimodel.types.GobiiSampleListType;

import java.util.ArrayList;
import java.util.List;

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


    private GobiiExtractFilterType gobiiExtractFilterType;

    private List<String> markerList = new ArrayList<>();

    private List<String> sampleList = new ArrayList<>();

    private String listFileName;

    private GobiiSampleListType gobiiSampleListType;

    private String gobiiDatasetType;

    private List<Integer> platformIds = new ArrayList<>();

    public GobiiExtractFilterType getGobiiExtractFilterType() {
        return gobiiExtractFilterType;
    }

    public void setGobiiExtractFilterType(GobiiExtractFilterType gobiiExtractFilterType) {
        this.gobiiExtractFilterType = gobiiExtractFilterType;
    }

    public List<String> getMarkerList() {
        return markerList;
    }

    public void setMarkerList(List<String> markerList) {
        this.markerList = markerList;
    }

    public List<String> getSampleList() {
        return sampleList;
    }

    public void setSampleList(List<String> sampleList) {
        this.sampleList = sampleList;
    }

    public String getListFileName() {
        return listFileName;
    }

    public void setListFileName(String listFileName) {
        this.listFileName = listFileName;
    }

    public GobiiSampleListType getGobiiSampleListType() {
        return gobiiSampleListType;
    }

    public void setGobiiSampleListType(GobiiSampleListType gobiiSampleListType) {
        this.gobiiSampleListType = gobiiSampleListType;
    }

    public String getGobiiDatasetType() {
        return gobiiDatasetType;
    }

    public void setGobiiDatasetType(String gobiiDatasetType) {
        this.gobiiDatasetType = gobiiDatasetType;
    }

    public List<Integer> getPlatformIds() {
        return platformIds;
    }

    public void setPlatformIds(List<Integer> platformIds) {
        this.platformIds = platformIds;
    }

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
