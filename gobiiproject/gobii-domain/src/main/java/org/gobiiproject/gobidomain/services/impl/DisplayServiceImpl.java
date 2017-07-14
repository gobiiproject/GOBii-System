package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.DisplayService;
import org.gobiiproject.gobiidtomapping.DtoMapDisplay;
import org.gobiiproject.gobiimodel.headerlesscontainer.DisplayDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Phil on 4/6/2016.
 */
public class DisplayServiceImpl implements DisplayService {

    @Autowired
    DtoMapDisplay dtoMapDisplay = null;

    Logger LOGGER = LoggerFactory.getLogger(DisplayServiceImpl.class);

    @Override
    public DisplayDTO createDisplay(DisplayDTO displayDTO) throws GobiiDomainException {

        DisplayDTO returnVal;

        try {

            displayDTO.setCreatedDate(new Date());
            displayDTO.setModifiedDate(new Date());
            returnVal = dtoMapDisplay.createDisplay(displayDTO);

            // When we have roles and permissions, this will be set programmatically
            returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
            returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);
        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }

        return returnVal;
    }

    @Override
    public DisplayDTO replaceDisplay(Integer displayId, DisplayDTO displayDTO) throws GobiiDomainException {

        DisplayDTO returnVal;

        try {

            if(null == displayDTO.getDisplayId() ||
                    displayDTO.getDisplayId().equals(displayId)) {

                DisplayDTO existingDisplayDTO = dtoMapDisplay.getDisplayDetails(displayId);
                if(null != existingDisplayDTO.getDisplayId() && existingDisplayDTO.getDisplayId().equals(displayId)) {

                    returnVal = dtoMapDisplay.replaceDisplay(displayId, displayDTO);
                    returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
                    returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);

                } else {

                    throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                            GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                            "The specified displayId ("
                                    + displayId
                                    + ") does not match an existing display.");
                }
            } else {

                throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                        GobiiValidationStatusType.BAD_REQUEST,
                        "The displayId specified in the dto ("
                                + displayDTO.getDisplayId()
                                + ") does not match the displayId passed as a parameter "
                                + "("
                                + displayId
                                + ")");
            }


        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }

        return returnVal;

    }

    @Override
    public List<DisplayDTO> getDisplays() throws GobiiDomainException {

        List<DisplayDTO> returnVal;

        returnVal = dtoMapDisplay.getDisplays();
        for(DisplayDTO currentDisplayDTO : returnVal) {
            currentDisplayDTO.getAllowedProcessTypes().add(GobiiProcessType.READ);
            currentDisplayDTO.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);
        }

        if(null == returnVal) {
            returnVal = new ArrayList<>();
        }

        return returnVal;
    }

    @Override
    public DisplayDTO getDisplayById(Integer displayId) throws GobiiDomainException {

        DisplayDTO returnVal;

        returnVal = dtoMapDisplay.getDisplayDetails(displayId);
        returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
        returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);

        if (null == returnVal) {
            throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                    GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                    "The specified displayId ("
                            + displayId
                            + ") does not match an existing display ");
        }

        return returnVal;
    }

}
