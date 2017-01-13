// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.core.DtoRequestProcessor;
import org.gobiiproject.gobiimodel.dto.container.OrganizationDTO;
import org.gobiiproject.gobiimodel.dto.types.ControllerType;
import org.gobiiproject.gobiimodel.dto.types.ServiceRequestId;

public class DtoRequestOrganization {

    public OrganizationDTO process(OrganizationDTO organizationDTO) throws Exception {

        return new DtoRequestProcessor<OrganizationDTO>().process(organizationDTO,
                OrganizationDTO.class,
                ControllerType.LOADER,
                ServiceRequestId.URL_ORGANIZATION);

    } // getPing()

}
