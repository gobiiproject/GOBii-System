// ************************************************************************
// (c) 2016 GOBii DataSet
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;


import org.gobiiproject.gobiiclient.core.DtoRequestProcessor;
import org.gobiiproject.gobiiclient.core.Urls;
import org.gobiiproject.gobiimodel.dto.container.MapsetDTO;
import org.gobiiproject.gobiimodel.dto.types.ControllerType;
import org.gobiiproject.gobiimodel.dto.types.ServiceRequestId;

public class DtoRequestMapset {


    public MapsetDTO process(MapsetDTO mapsetDTO) throws Exception {

        return new DtoRequestProcessor<MapsetDTO>().process(mapsetDTO,
                MapsetDTO.class,
                ControllerType.LOADER,
                ServiceRequestId.URL_MAPSET);

    }
}
