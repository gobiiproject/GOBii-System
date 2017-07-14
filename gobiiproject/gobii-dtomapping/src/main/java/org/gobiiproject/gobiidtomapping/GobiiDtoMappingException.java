package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;


/**
 * Created by Phil on 4/7/2016.
 */
public class GobiiDtoMappingException extends GobiiException {

    public GobiiDtoMappingException(Exception e) {
        super(e);
    }


    public GobiiDtoMappingException(String message) {
        super(message);
    }

    public GobiiDtoMappingException(GobiiStatusLevel gobiiStatusLevel,
                                    GobiiValidationStatusType gobiiValidationStatusType,
                                    String message) {
        super(gobiiStatusLevel, gobiiValidationStatusType,message);
    }
}
