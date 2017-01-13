// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.core.DtoRequestProcessor;
import org.gobiiproject.gobiimodel.dto.container.PingDTO;
import org.gobiiproject.gobiimodel.dto.types.ServiceRequestId;

public class DtoRequestPing {

    public PingDTO process(PingDTO pingDTO) throws Exception {

        return new DtoRequestProcessor<PingDTO>().process(pingDTO,
                PingDTO.class,
                pingDTO.getControllerType(),
                ServiceRequestId.URL_PING);

    } // getPingFromExtractController()

}
