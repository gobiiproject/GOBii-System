package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.headerlesscontainer.ContactDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.PlatformDTO;

import java.util.List;

/**
 * Created by Anggel on 5/4/2016.
 */
public interface DtoMapContact {

    List<ContactDTO> getContacts() throws GobiiDtoMappingException;
    ContactDTO getContactDetails(Integer contactId) throws Exception;
    ContactDTO getContactByEmail(String email) throws Exception;
    ContactDTO createContact(ContactDTO contactDTO) throws Exception;
    ContactDTO replaceContact(Integer contactId, ContactDTO contactDTO) throws Exception;

}
