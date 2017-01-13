package org.gobiiproject.gobiimodel.config;

import org.gobiiproject.gobiimodel.dto.header.DtoHeaderResponse;

/**
 * Created by Phil on 5/10/2016.
 */
public class GobiiException extends RuntimeException {


    private DtoHeaderResponse.StatusLevel statusLevel;
    private DtoHeaderResponse.ValidationStatusType validationStatusType;

    public GobiiException(String message) {
        super(message);
    }

    public GobiiException(DtoHeaderResponse.StatusLevel statusLevel,
                          DtoHeaderResponse.ValidationStatusType validationStatusType,
                          String message) {

        super(message);
        this.statusLevel = statusLevel;
        this.validationStatusType = validationStatusType;

    } //


    public DtoHeaderResponse.StatusLevel getStatusLevel() {
        return statusLevel;
    }

    public void setStatusLevel(DtoHeaderResponse.StatusLevel statusLevel) {
        this.statusLevel = statusLevel;
    }

    public DtoHeaderResponse.ValidationStatusType getValidationStatusType() {
        return validationStatusType;
    }

    public void setValidationStatusType(DtoHeaderResponse.ValidationStatusType validationStatusType) {
        this.validationStatusType = validationStatusType;
    }


}
