package org.gobiiproject.gobiiapimodel.payload;

import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;

import java.io.Serializable;

public class HeaderStatusMessage implements Serializable {


    private GobiiStatusLevel gobiiStatusLevel;


    private GobiiValidationStatusType gobiiValidationStatusType;
    private String message;

    public HeaderStatusMessage() {
    }

    public HeaderStatusMessage(GobiiStatusLevel gobiiStatusLevel,
                               GobiiValidationStatusType gobiiValidationStatusType,
                               String message) {
        this.gobiiStatusLevel = gobiiStatusLevel;
        this.gobiiValidationStatusType = gobiiValidationStatusType;
        this.message = message;
    }

    public GobiiStatusLevel getGobiiStatusLevel() {
        return gobiiStatusLevel;
    }

    public GobiiValidationStatusType getGobiiValidationStatusType() {
        return gobiiValidationStatusType;
    }

    public void setGobiiStatusLevel(GobiiStatusLevel gobiiStatusLevel) {
        this.gobiiStatusLevel = gobiiStatusLevel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String toString() {
        return "HeaderStatusMessage{" +
                "statusLevel=" + gobiiStatusLevel +
                ", Message='" + message + '\'' +
                "}\n";
    }//toString()
}
