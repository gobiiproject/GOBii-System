package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.services.MapsetService;
import org.gobiiproject.gobiidtomapping.DtoMapMapset;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.container.MapsetDTO;
import org.gobiiproject.gobiimodel.dto.header.DtoHeaderResponse;
import org.gobiiproject.gobiimodel.dto.header.HeaderStatusMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by Phil on 4/28/2016.
 */
public class MapsetServiceImpl implements MapsetService {

    Logger LOGGER = LoggerFactory.getLogger(MapsetServiceImpl.class);


    @Autowired
    DtoMapMapset dtoMapMapset;

    @Override
    public MapsetDTO processMapset(MapsetDTO mapsetDTO) {

        MapsetDTO returnVal = mapsetDTO;

        try {

            switch (returnVal.getProcessType()) {
                case READ:
                    returnVal = dtoMapMapset.getMapsetDetails(returnVal);
                    break;

                case CREATE:
                    returnVal = dtoMapMapset.createMapset(mapsetDTO);
                    returnVal.setCreatedDate(new Date());
                    returnVal.setModifiedDate(new Date());
                    break;

                case UPDATE:
                    returnVal = dtoMapMapset.updateMapset(mapsetDTO);
                    returnVal.setCreatedDate(new Date());
                    returnVal.setModifiedDate(new Date());
                    break;

                default:
                    returnVal.getDtoHeaderResponse().addStatusMessage(DtoHeaderResponse.StatusLevel.ERROR,
                            DtoHeaderResponse.ValidationStatusType.BAD_REQUEST,
                            "Unsupported proces mapset type " + returnVal.getProcessType().toString());

            } // switch()

        } catch (Exception e) {

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii service error", e);
        }

        return returnVal;
    }
}
