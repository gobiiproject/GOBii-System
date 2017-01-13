package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.container.ContactDTO;
import org.gobiiproject.gobiimodel.dto.container.DataSetDTO;

/**
 * Created by Anggel on 5/4/2016.
 */
public interface DtoMapContact {

    ContactDTO getContactDetails(ContactDTO contactDTO) throws GobiiDtoMappingException;
    ContactDTO  createContact(ContactDTO contactDTO) throws GobiiDtoMappingException;
    ContactDTO  updateContact(ContactDTO contactDTO) throws GobiiDtoMappingException;

}
