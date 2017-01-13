// ************************************************************************
// (c) 2016 GOBii DataSet
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;


import org.gobiiproject.gobiiclient.core.DtoRequestProcessor;
import org.gobiiproject.gobiiclient.core.Urls;
import org.gobiiproject.gobiimodel.dto.container.MarkerGroupDTO;
import org.gobiiproject.gobiimodel.dto.types.ControllerType;
import org.gobiiproject.gobiimodel.dto.types.ServiceRequestId;

public class DtoRequestMarkerGroup {


    public MarkerGroupDTO process(MarkerGroupDTO markerGroupDTO) throws Exception {

        return new DtoRequestProcessor<MarkerGroupDTO>().process(markerGroupDTO,
                MarkerGroupDTO.class,
                ControllerType.LOADER,
                ServiceRequestId.URL_MARKERGROUP);

    }
}
