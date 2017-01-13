// ************************************************************************
// (c) 2016 GOBii DataSet
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;


import org.gobiiproject.gobiiclient.core.gobii.dtopost.DtoRequestProcessor;
import org.gobiiproject.gobiimodel.dto.container.MarkerGroupDTO;
import org.gobiiproject.gobiiapimodel.types.ControllerType;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;

public class DtoRequestMarkerGroup {


    public MarkerGroupDTO process(MarkerGroupDTO markerGroupDTO) throws Exception {

        return new DtoRequestProcessor<MarkerGroupDTO>().process(markerGroupDTO,
                MarkerGroupDTO.class,
                ControllerType.LOADER,
                ServiceRequestId.URL_MARKERGROUP);

    }
}
