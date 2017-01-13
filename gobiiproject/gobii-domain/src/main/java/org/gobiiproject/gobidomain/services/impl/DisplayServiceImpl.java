package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.DisplayService;
import org.gobiiproject.gobidomain.services.NameIdListService;
import org.gobiiproject.gobiidtomapping.DtoMapDisplay;
import org.gobiiproject.gobiidtomapping.DtoMapNameIdList;
import org.gobiiproject.gobiidtomapping.DtoMapDisplay;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.container.DisplayDTO;
import org.gobiiproject.gobiimodel.dto.container.NameIdListDTO;
import org.gobiiproject.gobiimodel.dto.container.DisplayDTO;
import org.gobiiproject.gobiimodel.dto.header.DtoHeaderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by Phil on 4/6/2016.
 */
public class DisplayServiceImpl implements DisplayService {

    @Autowired
    DtoMapDisplay dtoMapDisplay = null;

    Logger LOGGER = LoggerFactory.getLogger(DisplayServiceImpl.class);

    @Override
    public DisplayDTO process(DisplayDTO displayDTO) {

        DisplayDTO returnVal = displayDTO;

        try {
            switch (displayDTO.getProcessType()) {
                case READ:
                    returnVal = dtoMapDisplay.getDisplayDetails(returnVal);
                    break;

                case CREATE:
                    returnVal.setCreatedDate(new Date());
                    returnVal.setModifiedDate(new Date());
                    returnVal = dtoMapDisplay.createDisplay(returnVal);
                    break;

                case UPDATE:
                    returnVal.setCreatedDate(new Date());
                    returnVal.setModifiedDate(new Date());
                    returnVal = dtoMapDisplay.updateDisplay(returnVal);
                    break;

                default:
                    returnVal.getDtoHeaderResponse().addStatusMessage(DtoHeaderResponse.StatusLevel.ERROR,
                            DtoHeaderResponse.ValidationStatusType.BAD_REQUEST,
                            "Unsupported proces type " + returnVal.getProcessType().toString());
                    break;

            }

        } catch (Exception e) {

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii service error", e);
        }

        return returnVal;
    }

}
