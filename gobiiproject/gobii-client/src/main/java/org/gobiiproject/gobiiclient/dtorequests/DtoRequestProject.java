// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.core.DtoRequestProcessor;
import org.gobiiproject.gobiimodel.dto.container.ProjectDTO;
import org.gobiiproject.gobiimodel.dto.types.ControllerType;
import org.gobiiproject.gobiimodel.dto.types.ServiceRequestId;

public class DtoRequestProject {


    public ProjectDTO process(ProjectDTO projectDTO) throws Exception {

        return new DtoRequestProcessor<ProjectDTO>().process(projectDTO,
                ProjectDTO.class,
                ControllerType.LOADER,
                ServiceRequestId.URL_PROJECT);
    }
}
