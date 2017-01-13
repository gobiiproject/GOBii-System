package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.services.ReferenceService;
import org.gobiiproject.gobiidtomapping.DtoMapReference;
import org.gobiiproject.gobiimodel.dto.container.ReferenceDTO;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Angel on 5/4/2016.
 */
public class ReferenceServiceImpl implements ReferenceService {

    Logger LOGGER = LoggerFactory.getLogger(ReferenceServiceImpl.class);

    @Autowired
    DtoMapReference dtoMapReference = null;

    @Override
    public ReferenceDTO processReference(ReferenceDTO referenceDTO) {

        ReferenceDTO returnVal = new ReferenceDTO();

        try {
            switch (referenceDTO.getGobiiProcessType()) {
                case READ:
                    returnVal = dtoMapReference.getReferenceDetails(referenceDTO);
                    break;

                case CREATE:
                    returnVal = dtoMapReference.createReference(referenceDTO);
                    break;

                case UPDATE:
                    returnVal = dtoMapReference.updateReference(referenceDTO);
                    break;

                default:
                    returnVal.getStatus().addStatusMessage(GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.BAD_REQUEST,
                            "Unsupported proces Reference type " + referenceDTO.getGobiiProcessType().toString());

            }

        } catch (Exception e) {

            returnVal.getStatus().addException(e);
            LOGGER.error("Gobii service error", e);
        }

        return returnVal;
    }
}