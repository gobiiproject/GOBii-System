package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.services.PlatformService;
import org.gobiiproject.gobiidtomapping.DtoMapPlatform;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.container.PlatformDTO;
import org.gobiiproject.gobiimodel.dto.header.DtoHeaderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by Phil on 4/27/2016.
 */
public class PlatformServiceImpl implements PlatformService {

    Logger LOGGER = LoggerFactory.getLogger(PlatformServiceImpl.class);

    @Autowired
    DtoMapPlatform dtoMapPlatform;

    @Override
    public PlatformDTO processPlatform(PlatformDTO platformDTO) {

        PlatformDTO returnVal = platformDTO;

        try {

            switch (returnVal.getProcessType()) {
                case READ:
                    returnVal = dtoMapPlatform.getPlatformDetails(returnVal);
                    break;

                case CREATE:
                    returnVal = dtoMapPlatform.createPlatform(returnVal);
                    returnVal.setCreatedDate(new Date());
                    returnVal.setModifiedDate(new Date());
                    break;

                case UPDATE:
                    returnVal = dtoMapPlatform.updatePlatform(returnVal);
                    returnVal.setCreatedDate(new Date());
                    returnVal.setModifiedDate(new Date());
                    break;

                default:
                    returnVal.getDtoHeaderResponse().addStatusMessage(DtoHeaderResponse.StatusLevel.ERROR,
                            DtoHeaderResponse.ValidationStatusType.BAD_REQUEST,
                            "Unsupported procesCv type " + platformDTO.getProcessType().toString());

            } // switch()

        } catch (Exception e) {

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii service error", e);
        }


        return returnVal;
    }
}
