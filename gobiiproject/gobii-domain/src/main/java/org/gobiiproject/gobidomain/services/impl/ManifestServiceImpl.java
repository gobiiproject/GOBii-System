package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.services.ManifestService;
import org.gobiiproject.gobiidtomapping.DtoMapManifest;
import org.gobiiproject.gobiimodel.dto.container.ManifestDTO;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by Angel on 5/4/2016.
 */
public class ManifestServiceImpl implements ManifestService {

    Logger LOGGER = LoggerFactory.getLogger(ManifestServiceImpl.class);

    @Autowired
    DtoMapManifest dtoMapManifest = null;

    @Override
    public ManifestDTO process(ManifestDTO manifestDTO) {

        ManifestDTO returnVal = new ManifestDTO();

        try {
            switch (manifestDTO.getGobiiProcessType()) {
                case READ:
                    returnVal = dtoMapManifest.getManifestDetails(manifestDTO);
                    break;

                case CREATE:
                    returnVal = dtoMapManifest.createManifest(manifestDTO);
                    returnVal.setCreatedDate(new Date());
                    returnVal.setModifiedDate(new Date());
                    break;

                case UPDATE:
                    returnVal = dtoMapManifest.updateManifest(manifestDTO);
                    returnVal.setCreatedDate(new Date());
                    returnVal.setModifiedDate(new Date());
                    break;

                default:
                    returnVal.getStatus().addStatusMessage(GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.BAD_REQUEST,
                            "Unsupported proces Manifest type " + manifestDTO.getGobiiProcessType().toString());

            }

        } catch (Exception e) {

            returnVal.getStatus().addException(e);
            LOGGER.error("Gobii service error", e);
        }

        return returnVal;
    }
}