package org.gobiiproject.gobiimodel.dto.instructions.extractor;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a complete extraction request.
 * @see GobiiDataSetExtract for data related to specific data sets
 * Created by Phil on 6/6/2016.
 */
public class GobiiExtractorInstruction {

    private List<GobiiDataSetExtract> dataSetExtracts = new ArrayList<>();
    private List<Integer> mapsetIds = new ArrayList<>();

    //Contact for this instruction
    private Integer contactId;
    //Email address of the primary contact for this instruction
    private String contactEmail;
    //Crop that this applies to
    private String gobiiCropType = null;
    //QC
    private boolean qcCheck = false;

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public List<GobiiDataSetExtract> getDataSetExtracts() {
        return dataSetExtracts;
    }

    public void setDataSetExtracts(List<GobiiDataSetExtract> dataSetExtracts) { this.dataSetExtracts = dataSetExtracts; }

    public String getGobiiCropType() {
        return gobiiCropType;
    }

    public void setGobiiCropType(String gobiiCropType) {
        this.gobiiCropType = gobiiCropType;
    }

    public List<Integer> getMapsetIds() {
        return mapsetIds;
    }

    public void setMapsetIds(List<Integer> mapsetIds) {
        this.mapsetIds = mapsetIds;
    }

    public boolean isQcCheck() {
        return qcCheck;
    }

    public void setQcCheck(boolean qcCheck) {
        this.qcCheck = qcCheck;
    }
}
