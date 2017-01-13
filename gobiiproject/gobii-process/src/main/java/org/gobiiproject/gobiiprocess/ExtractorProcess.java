// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobiiprocess;

import org.gobiiproject.gobidomain.services.PingService;
import org.gobiiproject.gobiimodel.dto.container.PingDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 3/24/2016.
 */
public class ExtractorProcess {

    public static void main(String[] args) {

        Logger LOGGER = LoggerFactory.getLogger(ExtractorProcess.class);
        SpringContextLoader springContextLoader = new SpringContextLoader();

        try {

            PingService pingService = springContextLoader.getApplicationContext().getBean(PingService.class);

            List<String> requestStrings = new ArrayList<>();
            requestStrings.add("Test String 1");
            requestStrings.add("Test String 2");
            PingDTO pingDtoRequest = new PingDTO();
            pingDtoRequest.setPingRequests(requestStrings);

            PingDTO pingDTOResponse = pingService.getPings(pingDtoRequest);
            LOGGER.info("Got response : " + pingDTOResponse.getPingResponses());

        } catch (Exception exception) {
            LOGGER.error(exception.getMessage());
        }

    }//main

}
