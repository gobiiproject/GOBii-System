// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.core.DtoRequestProcessor;
import org.gobiiproject.gobiimodel.dto.container.ContactDTO;
import org.gobiiproject.gobiimodel.dto.types.ControllerType;
import org.gobiiproject.gobiimodel.dto.types.ServiceRequestId;

public class DtoRequestContact {

    public ContactDTO process(ContactDTO contactDTO) throws Exception {

        return new DtoRequestProcessor<ContactDTO>().process(contactDTO,
                ContactDTO.class,
                ControllerType.LOADER,
                ServiceRequestId.URL_CONTACT);
    }
}
