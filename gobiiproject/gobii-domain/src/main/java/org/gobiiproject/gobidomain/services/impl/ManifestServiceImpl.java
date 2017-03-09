package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.ManifestService;
import org.gobiiproject.gobiidtomapping.DtoMapManifest;
import org.gobiiproject.gobiimodel.headerlesscontainer.ManifestDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Angel on 5/4/2016.
 */
public class ManifestServiceImpl implements ManifestService {

    Logger LOGGER = LoggerFactory.getLogger(ManifestServiceImpl.class);

    @Autowired
    DtoMapManifest dtoMapManifest = null;

    @Override
    public ManifestDTO createManifest(ManifestDTO manifestDTO) throws GobiiDomainException {

        ManifestDTO returnVal;

        try {

            manifestDTO.setCreatedDate(new Date());
            manifestDTO.setModifiedDate(new Date());
            returnVal = dtoMapManifest.createManifest(manifestDTO);

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
    public ManifestDTO replaceManifest(Integer manifestId, ManifestDTO manifestDTO) throws GobiiDomainException {

        ManifestDTO returnVal;

        try {

            if(null == manifestDTO.getManifestId() ||
                    manifestDTO.getManifestId().equals(manifestId)) {

                ManifestDTO existingManifestDTO = dtoMapManifest.getManifestDetails(manifestId);
                if(null != existingManifestDTO.getManifestId() && existingManifestDTO.getManifestId().equals(manifestId)) {

                    returnVal = dtoMapManifest.replaceManifest(manifestId, manifestDTO);
                    returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
                    returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);

                } else {

                    throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                            GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                            "The specified manifestId ("
                                    + manifestId
                                    + ") does not match an existing manifest.");
                }
            } else {

                throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                        GobiiValidationStatusType.BAD_REQUEST,
                        "The manifestId specified in the dto ("
                                + manifestDTO.getManifestId()
                                + ") does not match the manifestId passed as a parameter "
                                + "("
                                + manifestId
                                + ")");
            }


        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }

        return returnVal;

    }

    @Override
    public List<ManifestDTO> getManifests() throws GobiiDomainException {

        List<ManifestDTO> returnVal;

        returnVal = dtoMapManifest.getManifests();
        for(ManifestDTO currentManifestDTO : returnVal) {
            currentManifestDTO.getAllowedProcessTypes().add(GobiiProcessType.READ);
            currentManifestDTO.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);
        }

        if(null == returnVal) {
            returnVal = new ArrayList<>();
        }

        return returnVal;
    }

    @Override
    public ManifestDTO getManifestById(Integer manifestId) throws GobiiDomainException {

        ManifestDTO returnVal;

        returnVal = dtoMapManifest.getManifestDetails(manifestId);
        returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
        returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);

        if (null == returnVal) {
            throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                    GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                    "The specified manifestId ("
                            + manifestId
                            + ") does not match an existing manifest ");
        }

        return returnVal;
    }

}