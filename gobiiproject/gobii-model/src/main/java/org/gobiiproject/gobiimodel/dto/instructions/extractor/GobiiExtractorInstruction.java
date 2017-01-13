package org.gobiiproject.gobiimodel.dto.instructions.extractor;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a complete extraction request.
 * @see GobiiDataSetExtract for data related to specific data sets
 * Created by Phil on 6/6/2016.
 */
public class GobiiExtractorInstruction {

    List<GobiiDataSetExtract> dataSetExtracts = new ArrayList<>();

    //Contact for this instruction
    Integer contactId;
    //Email address of the primary contact for this instruction
    String contactEmail;
    //Crop that this applies to
    private String gobiiCropType = null;


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

    public void setDataSetExtracts(List<GobiiDataSetExtract> dataSetExtracts) {
        this.dataSetExtracts = dataSetExtracts;
    }

    public String getGobiiCropType() {
        return gobiiCropType;
    }

    public void setGobiiCropType(String gobiiCropType) {
        this.gobiiCropType = gobiiCropType;
    }
}
