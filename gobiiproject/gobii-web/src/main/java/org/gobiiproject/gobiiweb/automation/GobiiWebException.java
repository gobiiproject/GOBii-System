package org.gobiiproject.gobiiweb.automation;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;

/**
 * Created by Phil on 9/25/2016.
 */
public class GobiiWebException extends GobiiException {

    public GobiiWebException(Exception e) {
        super(e);
    }


    public GobiiWebException(String message) {
        super(message);
    }

    public GobiiWebException(GobiiStatusLevel gobiiStatusLevel,
                                GobiiValidationStatusType gobiiValidationStatusType,
                                String message) {

        super(gobiiStatusLevel, gobiiValidationStatusType,message);

    } //
    
}
