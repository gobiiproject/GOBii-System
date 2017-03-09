package org.gobiiproject.gobiimodel.dto.instructions.loader;


import org.gobiiproject.gobiimodel.dto.instructions.GobiiFilePropNameId;

import java.util.ArrayList;
import java.util.List;

/**
 * A loader instruction containing all the details nessisary to create a digest file.
 * See {@link GobiiFile} and {@link GobiiFileColumn}
 * Created by Phil on 4/12/2016.
 */
public class GobiiLoaderInstruction {

    //File location information (Each table can come from a separate file)
    private GobiiFile gobiiFile = new GobiiFile();
    //Name of this table. Used as filename for loading, and to determine what database table it goes to.
    private String table = null;
    //List of GobiiFileColumn columns, left to right ordering
    private List<GobiiFileColumn> gobiiFileColumns = new ArrayList<>();
    //Special filtering parameters for VCF (Mostly ignored)
    private VcfParameters vcfParameters = new VcfParameters();
    //ID of the dataset, used when loading matrix data
    private Integer dataSetId;
    //Name of the crop being loaded
    private String gobiiCropType;
    //ID of the primary contact for this action
    private Integer contactId;
    //Email of the primary contact for this action
    private String contactEmail;

    private boolean qcCheck;

    private GobiiFilePropNameId project = new GobiiFilePropNameId();
    private GobiiFilePropNameId platform = new GobiiFilePropNameId();
    private GobiiFilePropNameId dataSet = new GobiiFilePropNameId();
    private GobiiFilePropNameId datasetType = new GobiiFilePropNameId();
    private GobiiFilePropNameId Experiment = new GobiiFilePropNameId();
    private GobiiFilePropNameId mapset = new GobiiFilePropNameId();

    public GobiiFile getGobiiFile() {
        return gobiiFile;
    }

    public GobiiLoaderInstruction setGobiiFile(GobiiFile gobiiFile) {

        this.gobiiFile = gobiiFile;
        return this;
    }

    public String getTable() {
        return table;
    }

    public GobiiLoaderInstruction setTable(String table) {
        this.table = table;
        return this;
    }

    public List<GobiiFileColumn> getGobiiFileColumns() {
        return gobiiFileColumns;
    }

    public GobiiLoaderInstruction setGobiiFileColumns(List<GobiiFileColumn> gobiiFileColumns) {
        this.gobiiFileColumns = gobiiFileColumns;
        return this;
    }

    public VcfParameters getVcfParameters() {
        return vcfParameters;
    }

    public GobiiLoaderInstruction setVcfParameters(VcfParameters vcfParameters) {
        this.vcfParameters = vcfParameters;
        return this;
    }

    public Integer getDataSetId() {
        return dataSetId;
    }

    public GobiiLoaderInstruction setDataSetId(Integer dataSetId) {
        this.dataSetId = dataSetId;
        return this;
    }

    public String getGobiiCropType() {
        return gobiiCropType;
    }

    public GobiiLoaderInstruction setGobiiCropType(String gobiiCropType) {
        this.gobiiCropType = gobiiCropType;
        return this;
    }

    public Integer getContactId() {
        return contactId;
    }

    public GobiiLoaderInstruction setContactId(Integer contactId) {
        this.contactId = contactId;
        return this;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public GobiiLoaderInstruction setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
        return this;
    }

    public boolean isQcCheck() {
        return qcCheck;
    }

    public void setQcCheck(boolean qcCheck) {
        this.qcCheck = qcCheck;
    }

    public GobiiFilePropNameId getProject() {
        return project;
    }

    public void setProject(GobiiFilePropNameId project) {
        this.project = project;
    }

    public GobiiFilePropNameId getPlatform() {
        return platform;
    }

    public void setPlatform(GobiiFilePropNameId platform) {
        this.platform = platform;
    }

    public GobiiFilePropNameId getDataSet() {
        return dataSet;
    }

    public void setDataSet(GobiiFilePropNameId dataSet) {
        this.dataSet = dataSet;
    }

    public GobiiFilePropNameId getDatasetType() {
        return datasetType;
    }

    public void setDatasetType(GobiiFilePropNameId datasetType) {
        this.datasetType = datasetType;
    }

    public GobiiFilePropNameId getExperiment() {
        return Experiment;
    }

    public void setExperiment(GobiiFilePropNameId experiment) {
        Experiment = experiment;
    }

    public GobiiFilePropNameId getMapset() {
        return mapset;
    }

    public void setMapset(GobiiFilePropNameId mapset) {
        this.mapset = mapset;
    }
}
