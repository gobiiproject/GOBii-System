package org.gobiiproject.gobiimodel.dto.instructions.extractor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 6/6/2016.
 */
public class GobiiExtractorInstruction {

    List<GobiiDataSetExtract> dataSetExtracts = new ArrayList<>();

    Integer contactId;
    String contactEmail;

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
}
