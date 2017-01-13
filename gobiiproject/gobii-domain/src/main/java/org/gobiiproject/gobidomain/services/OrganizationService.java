package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.headerlesscontainer.OrganizationDTO;

import java.util.List;

/**
 * Created by Angel on 5/4/2016.
 */
public interface OrganizationService {

    OrganizationDTO createOrganization(OrganizationDTO organizationDTO) throws GobiiDomainException;
    OrganizationDTO replaceOrganization(Integer organizationId, OrganizationDTO organizationDTO) throws GobiiDomainException;
    List<OrganizationDTO> getOrganizations() throws GobiiDomainException;
    OrganizationDTO getOrganizationById(Integer organizationId);

}
