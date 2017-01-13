package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.header.DtoHeaderResponse;

/**
 * Created by Phil on 4/7/2016.
 */
public class GobiiDtoMappingException extends GobiiException {


    public GobiiDtoMappingException(DtoHeaderResponse.StatusLevel statusLevel,
                                    DtoHeaderResponse.ValidationStatusType validationStatusType,
                                    String message) {
        super(statusLevel,validationStatusType,message);
    }
}
