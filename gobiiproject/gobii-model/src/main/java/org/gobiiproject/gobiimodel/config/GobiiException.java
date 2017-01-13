package org.gobiiproject.gobiimodel.config;


import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;

/**
 * Created by Phil on 5/10/2016.
 */
public class GobiiException extends RuntimeException {


    private GobiiStatusLevel gobiiStatusLevel = GobiiStatusLevel.ERROR;
    private GobiiValidationStatusType gobiiValidationStatusType = GobiiValidationStatusType.NONE;

    public GobiiException(Exception e) {
        super(e);
    }


    public GobiiException(String message) {
        super(message);
    }

    public GobiiException(GobiiStatusLevel gobiiStatusLevel,
                          GobiiValidationStatusType gobiiValidationStatusType,
                          String message) {

        super(message);
        this.gobiiStatusLevel = gobiiStatusLevel;
        this.gobiiValidationStatusType = gobiiValidationStatusType;

    } //


    public GobiiStatusLevel getGobiiStatusLevel() {
        return gobiiStatusLevel;
    }

    public void setGobiiStatusLevel(GobiiStatusLevel gobiiStatusLevel) {
        this.gobiiStatusLevel = gobiiStatusLevel;
    }

    public GobiiValidationStatusType getGobiiValidationStatusType() {
        return gobiiValidationStatusType;
    }

    public void setGobiiValidationStatusType(GobiiValidationStatusType gobiiValidationStatusType) {
        this.gobiiValidationStatusType = gobiiValidationStatusType;
    }


}
