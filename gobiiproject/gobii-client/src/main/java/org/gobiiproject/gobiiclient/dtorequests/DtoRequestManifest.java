// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.core.gobii.dtopost.DtoRequestProcessor;
import org.gobiiproject.gobiimodel.dto.container.ManifestDTO;
import org.gobiiproject.gobiiapimodel.types.ControllerType;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;

public class DtoRequestManifest {

    public ManifestDTO process(ManifestDTO manifestDTO) throws Exception {

        return new DtoRequestProcessor<ManifestDTO>().process(manifestDTO,
                ManifestDTO.class,
                ControllerType.LOADER,
                ServiceRequestId.URL_MANIFEST);

    } // getPing()

}
