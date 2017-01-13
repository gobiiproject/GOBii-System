// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.services.PingService;
import org.gobiiproject.gobiidtomapping.DtoMapPing;
import org.gobiiproject.gobiimodel.dto.container.PingDTO;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Phil on 3/24/2016.
 */
public class PingServiceImpl implements PingService {

    Logger LOGGER = LoggerFactory.getLogger(PingServiceImpl.class);


    @Autowired
    private DtoMapPing dtoMapPing;

    @Override
    public PingDTO getPings(PingDTO pingDTO) {

        PingDTO returnVal = pingDTO;

        try {
            returnVal = dtoMapPing.getPings(pingDTO);
            String newPingMessage = LineUtils.wrapLine("Service layer responded");
            returnVal.getPingResponses().add(newPingMessage);
        } catch (Exception e) {

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii service error", e);
        }


        return returnVal;
    } // getMarkers()

}
