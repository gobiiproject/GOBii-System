// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.core.gobii.dtopost.DtoRequestProcessor;
import org.gobiiproject.gobiimodel.dto.container.CvDTO;
import org.gobiiproject.gobiiapimodel.types.ControllerType;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;

public class DtoRequestCv {


    public CvDTO process(CvDTO cvDTO) throws Exception {

        return new DtoRequestProcessor<CvDTO>().process(cvDTO,
                CvDTO.class,
                ControllerType.LOADER,
                ServiceRequestId.URL_CV);
    }
}
