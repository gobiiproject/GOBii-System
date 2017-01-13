// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.core.DtoRequestProcessor;
import org.gobiiproject.gobiiclient.core.Urls;
import org.gobiiproject.gobiimodel.dto.container.DisplayDTO;
import org.gobiiproject.gobiimodel.dto.types.ControllerType;
import org.gobiiproject.gobiimodel.dto.types.ServiceRequestId;

public class DtoRequestDisplay {

    public DisplayDTO process(DisplayDTO displayDTO) throws Exception {

        return new DtoRequestProcessor<DisplayDTO>().process(displayDTO,
                DisplayDTO.class,
                ControllerType.LOADER,
                ServiceRequestId.URL_DISPLAY);


    }

}
