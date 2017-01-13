
package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.dto.container.OrganizationDTO;

/**
 * Created by Angel on 5/4/2016.
 */
public interface DtoMapOrganization {

    OrganizationDTO getOrganizationDetails(OrganizationDTO organizationDTO) throws GobiiDtoMappingException;
    OrganizationDTO  createOrganization(OrganizationDTO organizationDTO) throws GobiiDtoMappingException;
    OrganizationDTO updateOrganization(OrganizationDTO organizationDTO) throws GobiiDtoMappingException;

}
