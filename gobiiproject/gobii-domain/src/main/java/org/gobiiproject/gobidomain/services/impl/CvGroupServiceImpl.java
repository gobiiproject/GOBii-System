package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobiidtomapping.DtoMapCvGroup;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.headerlesscontainer.CvDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.gobiiproject.gobidomain.services.CvGroupService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by VCalaminos on 2/1/2017.
 */
public class CvGroupServiceImpl implements CvGroupService {

    Logger LOGGER = LoggerFactory.getLogger(CvGroupServiceImpl.class);

    @Autowired
    DtoMapCvGroup dtoMapCvGroup = null;

    @Override
    public List<CvDTO> getCvsForGroup(Integer groupId) throws GobiiDtoMappingException {

        List<CvDTO> returnVal;

        returnVal = dtoMapCvGroup.getCvsForGroup(groupId);

        for (CvDTO currentCvDTO : returnVal) {

            currentCvDTO.getAllowedProcessTypes().add(GobiiProcessType.READ);
        }

        return returnVal;

    }


}
