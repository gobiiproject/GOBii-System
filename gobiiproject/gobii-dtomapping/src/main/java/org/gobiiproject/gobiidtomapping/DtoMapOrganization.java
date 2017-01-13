package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.headerlesscontainer.OrganizationDTO;

import java.util.List;

/**
 * Created by Angel on 5/4/2016.
 */
public interface DtoMapOrganization {

    List<OrganizationDTO> getOrganizations() throws GobiiDtoMappingException;
    OrganizationDTO getOrganizationDetails(Integer organizationId) throws GobiiDtoMappingException;
    OrganizationDTO createOrganization(OrganizationDTO organizationDTO) throws GobiiDtoMappingException;
    OrganizationDTO replaceOrganization(Integer organizationId, OrganizationDTO organizationDTO) throws GobiiDtoMappingException;

}
