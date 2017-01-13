// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.core.gobii.dtopost.DtoRequestProcessor;
import org.gobiiproject.gobiimodel.dto.container.ReferenceDTO;
import org.gobiiproject.gobiiapimodel.types.ControllerType;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;

public class DtoRequestReference {

    public ReferenceDTO process(ReferenceDTO referenceDTO) throws Exception {

        return new DtoRequestProcessor<ReferenceDTO>().process(referenceDTO,
                ReferenceDTO.class,
                ControllerType.LOADER,
                ServiceRequestId.URL_REFERENCE);

    } // getPing()

}
