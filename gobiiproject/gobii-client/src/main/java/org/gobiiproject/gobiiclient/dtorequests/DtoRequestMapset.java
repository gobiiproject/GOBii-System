// ************************************************************************
// (c) 2016 GOBii DataSet
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;


import org.gobiiproject.gobiiclient.core.gobii.dtopost.DtoRequestProcessor;
import org.gobiiproject.gobiimodel.dto.container.MapsetDTO;
import org.gobiiproject.gobiiapimodel.types.ControllerType;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;

public class DtoRequestMapset {


    public MapsetDTO process(MapsetDTO mapsetDTO) throws Exception {

        return new DtoRequestProcessor<MapsetDTO>().process(mapsetDTO,
                MapsetDTO.class,
                ControllerType.LOADER,
                ServiceRequestId.URL_MAPSET);

    }
}
