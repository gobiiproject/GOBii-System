package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.services.NameIdListService;
import org.gobiiproject.gobiidtomapping.DtoMapNameIdList;
import org.gobiiproject.gobiimodel.dto.container.NameIdListDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Phil on 4/6/2016.
 */
public class NameIdListServiceImpl implements NameIdListService {


    private Logger LOGGER = LoggerFactory.getLogger(NameIdListServiceImpl.class);

    @Autowired
    DtoMapNameIdList dtoMapNameIdList;

    @Override
    public NameIdListDTO getNameIdList(NameIdListDTO nameIdListDTO) {

        NameIdListDTO returnVal = nameIdListDTO;
        try {
            return dtoMapNameIdList.getNameIdList(nameIdListDTO);
        } catch (Exception e) {

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii service error", e);
        }

        return returnVal;

    }
}
