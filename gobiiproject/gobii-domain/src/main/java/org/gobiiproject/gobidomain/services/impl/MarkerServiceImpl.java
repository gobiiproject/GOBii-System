package org.gobiiproject.gobidomain.services.impl;

import org.apache.commons.lang.NotImplementedException;
import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.MarkerService;
import org.gobiiproject.gobidomain.services.MarkerService;
import org.gobiiproject.gobiidtomapping.DtoMapMarker;
import org.gobiiproject.gobiidtomapping.DtoMapMarker;
import org.gobiiproject.gobiimodel.headerlesscontainer.MarkerDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Phil on 4/21/2016.
 */
public class MarkerServiceImpl implements MarkerService {

    Logger LOGGER = LoggerFactory.getLogger(MarkerServiceImpl.class);


    @Autowired
    DtoMapMarker dtoMapMarker = null;

    @Override
    public List<MarkerDTO> getMarkers() throws GobiiDomainException {

        List<MarkerDTO> returnVal;

        try {
            returnVal = dtoMapMarker.getMarkers();

            for (MarkerDTO currentMarkerDTO : returnVal) {
                currentMarkerDTO.getAllowedProcessTypes().add(GobiiProcessType.READ);
                currentMarkerDTO.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);
            }


            if (null == returnVal) {
                returnVal = new ArrayList<>();
            }

        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);

        }

        return returnVal;
    }

    @Override
    public MarkerDTO getMarkerById(Integer markerId) {

        MarkerDTO returnVal;

        try {
            returnVal = dtoMapMarker.getMarkerDetails(markerId);

            returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
            returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);


            if (null == returnVal) {
                throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                        GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                        "The specified markerId ("
                                + markerId
                                + ") does not match an existing marker ");
            }

        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);

        }

        return returnVal;
    }

    @Override
    public MarkerDTO createMarker(MarkerDTO markerDTO) throws GobiiDomainException {

        MarkerDTO returnVal;

        returnVal = dtoMapMarker.createMarker(markerDTO);

        // When we have roles and permissions, this will be set programmatically
        returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
        returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);

        return returnVal;
    }

    @Override
    public MarkerDTO replaceMarker(Integer markerId, MarkerDTO markerDTO) throws GobiiDomainException {
        MarkerDTO returnVal;

        try {

            if (null == markerDTO.getMarkerId() ||
                    markerDTO.getMarkerId().equals(markerId)) {


                MarkerDTO existingMarkerDTO = dtoMapMarker.getMarkerDetails(markerId);

                if (null != existingMarkerDTO.getMarkerId() && existingMarkerDTO.getMarkerId().equals(markerId)) {


                    returnVal = dtoMapMarker.replaceMarker(markerId, markerDTO);
                    returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
                    returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);

                } else {

                    throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                            GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                            "The specified markerId ("
                                    + markerId
                                    + ") does not match an existing marker ");
                }

            } else {

                throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                        GobiiValidationStatusType.BAD_REQUEST,
                        "The markerId specified in the dto ("
                                + markerDTO.getMarkerId()
                                + ") does not match the markerId passed as a parameter "
                                + "("
                                + markerId
                                + ")");

            }


        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }


        return returnVal;
    }

    @Override
    public List<MarkerDTO> getMarkersByName(String markerName) throws GobiiDomainException {

        List<MarkerDTO> returnVal;

        try {
            returnVal = dtoMapMarker.getMarkersByName(markerName);

            for( MarkerDTO currentMarkerDTO : returnVal ) {
                currentMarkerDTO.getAllowedProcessTypes().add(GobiiProcessType.READ);
                currentMarkerDTO.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);

            }

            if (null == returnVal) {
                throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                        GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                        "The specified markerId ("
                                + markerName
                                + ") does not match an existing marker ");
            }

        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);

        }

        return returnVal;

    }

}
