// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.core.DtoRequestProcessor;
import org.gobiiproject.gobiimodel.dto.container.ManifestDTO;
import org.gobiiproject.gobiimodel.dto.types.ControllerType;
import org.gobiiproject.gobiimodel.dto.types.ServiceRequestId;

public class DtoRequestManifest {

    public ManifestDTO process(ManifestDTO manifestDTO) throws Exception {

        return new DtoRequestProcessor<ManifestDTO>().process(manifestDTO,
                ManifestDTO.class,
                ControllerType.LOADER,
                ServiceRequestId.URL_MANIFEST);

    } // getPing()

}
