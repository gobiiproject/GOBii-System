package org.gobiiproject.gobiimodel.tobemovedtoapimodel;


import org.codehaus.jackson.annotate.JsonIgnore;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.utils.ExceptionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by MrPhil on 6/18/2015.
 */
public class Status implements Serializable {

    private boolean succeeded = true;
    private ArrayList<HeaderStatusMessage> statusMessages = new ArrayList<>();

    private Map<String, String> statusMessagesByCode = new HashMap<>();

    @JsonIgnore
    public void addException(Exception e) {
        succeeded = false;
        String message = ExceptionUtils.makeMessageFromException(e);
        addStatusMessage(GobiiStatusLevel.ERROR,
                message);
    }


    @JsonIgnore
    public void addException(GobiiException gobiiException) {
        succeeded = false;

        String message = ExceptionUtils.makeMessageFromException(gobiiException);
        addStatusMessage(gobiiException.getGobiiStatusLevel(),
                gobiiException.getGobiiValidationStatusType(),
                message);
    }


    @JsonIgnore
    public void addStatusMessage(GobiiStatusLevel gobiiStatusLevel, String message) {
        this.addStatusMessage(gobiiStatusLevel, GobiiValidationStatusType.NONE, message);
    }

    @JsonIgnore
    public void addStatusMessage(GobiiStatusLevel gobiiStatusLevel, GobiiValidationStatusType gobiiValidationStatusType, String message) {

        succeeded = (GobiiStatusLevel.ERROR != gobiiStatusLevel)
                && (gobiiValidationStatusType.equals(GobiiValidationStatusType.NONE)
                || gobiiValidationStatusType.equals(GobiiValidationStatusType.UNKNOWN));

        statusMessages.add(new HeaderStatusMessage(gobiiStatusLevel, gobiiValidationStatusType, message));

        // BRAPI requires messages by "code"
        // Java maps don't have duplicate keys, so we need to make sure our key is unique
        Integer keyIndex = 0;
        boolean addedMessage = false;
        do {
            String codeKey = keyIndex + "-" + gobiiStatusLevel.toString() + "-" + gobiiValidationStatusType.toString();
            if (false == statusMessagesByCode.containsKey(codeKey)) {

                statusMessagesByCode.put(codeKey, message);
                addedMessage = true;
            } else {
                keyIndex++;
            }

        } while (false == addedMessage);

    }

    public ArrayList<HeaderStatusMessage> getStatusMessages() {
        return statusMessages;
    }

    public void setStatusMessages(ArrayList<HeaderStatusMessage> statusMessages) {
        this.statusMessages = statusMessages;
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public void setSucceeded(boolean succeeded) {
        this.succeeded = succeeded;
    }

    public Map<String, String> getStatusMessagesByCode() {
        return statusMessagesByCode;
    }
}//Status
