package org.gobiiproject.gobiimodel.dto.header;

import java.io.Serializable;

public class HeaderStatusMessage implements Serializable {


    private DtoHeaderResponse.StatusLevel statusLevel;


    private DtoHeaderResponse.ValidationStatusType validationStatusType;
    private String message;

    public HeaderStatusMessage() {
    }

    public HeaderStatusMessage(DtoHeaderResponse.StatusLevel statusLevel,
                               DtoHeaderResponse.ValidationStatusType validationStatusType,
                               String message) {
        this.statusLevel = statusLevel;
        this.validationStatusType = validationStatusType;
        this.message = message;
    }

    public DtoHeaderResponse.StatusLevel getStatusLevel() {
        return statusLevel;
    }

    public DtoHeaderResponse.ValidationStatusType getValidationStatusType() {
        return validationStatusType;
    }

    public void setStatusLevel(DtoHeaderResponse.StatusLevel statusLevel) {
        this.statusLevel = statusLevel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String toString() {
        return "HeaderStatusMessage{" +
                "statusLevel=" + statusLevel +
                ", Message='" + message + '\'' +
                "}\n";
    }//toString()
}
