package org.gobiiproject.gobiimodel.dto.instructions.extractor;

import org.gobiiproject.gobiimodel.dto.instructions.GobiiFilePropNameId;
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
    private GobiiJobStatus gobiiJobStatus;
    //Internal ID of the data set. Used for internal lookups.
    String extractDestinationDirectory = null;


    private GobiiExtractFilterType gobiiExtractFilterType;
    private List<String> markerList = new ArrayList<>();
    private List<String> sampleList = new ArrayList<>();
    private String listFileName;
    private GobiiFilePropNameId gobiiDatasetType = new GobiiFilePropNameId();
    private GobiiFilePropNameId principleInvestigator = new GobiiFilePropNameId();
    private GobiiFilePropNameId project = new GobiiFilePropNameId();
    private GobiiFilePropNameId dataSet = new GobiiFilePropNameId();
    private List<Integer> platformIds = new ArrayList<>();
    private GobiiSampleListType gobiiSampleListType;


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

    public GobiiFilePropNameId getGobiiDatasetType() {
        return gobiiDatasetType;
    }

    public void setGobiiDatasetType(GobiiFilePropNameId gobiiDatasetType) {
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


    public GobiiFilePropNameId getPrincipleInvestigator() {
        return principleInvestigator;
    }

    public void setPrincipleInvestigator(GobiiFilePropNameId principleInvestigator) {
        this.principleInvestigator = principleInvestigator;
    }

    public GobiiFilePropNameId getProject() {
        return project;
    }

    public void setProject(GobiiFilePropNameId project) {
        this.project = project;
    }

    public GobiiFilePropNameId getDataSet() {
        return dataSet;
    }

    public void setDataSet(GobiiFilePropNameId dataSet) {
        this.dataSet = dataSet;
    }

}
