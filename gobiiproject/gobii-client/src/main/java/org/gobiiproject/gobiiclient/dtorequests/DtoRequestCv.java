// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.core.DtoRequestProcessor;
import org.gobiiproject.gobiiclient.core.Urls;
import org.gobiiproject.gobiimodel.dto.container.CvDTO;
import org.gobiiproject.gobiimodel.dto.types.ControllerType;
import org.gobiiproject.gobiimodel.dto.types.ServiceRequestId;

public class DtoRequestCv {


    public CvDTO process(CvDTO cvDTO) throws Exception {

        return new DtoRequestProcessor<CvDTO>().process(cvDTO,
                CvDTO.class,
                ControllerType.LOADER,
                ServiceRequestId.URL_CV);
    }
}
