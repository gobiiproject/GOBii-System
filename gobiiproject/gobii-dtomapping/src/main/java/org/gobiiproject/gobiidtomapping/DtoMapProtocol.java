package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.headerlesscontainer.OrganizationDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.ProtocolDTO;

import java.util.List;

/**
 * Created by VCalaminos on 2016-12-14.
 */
public interface DtoMapProtocol {

    ProtocolDTO getProtocolDetails(Integer protocolId) throws Exception;
    ProtocolDTO createProtocol(ProtocolDTO protocolDTO) throws GobiiDtoMappingException;
    ProtocolDTO replaceProtocol(Integer protocolId, ProtocolDTO protocolDTO) throws GobiiDtoMappingException;
    List<ProtocolDTO> getProtocols() throws GobiiDtoMappingException;
    OrganizationDTO addVendotrToProtocol(Integer protocolId, OrganizationDTO organizationDTO) throws GobiiDtoMappingException;
    OrganizationDTO getVendorForProtocolByName(String vendorProtocolName) throws GobiiDtoMappingException;    List<OrganizationDTO> getVendorsForProtocolByProtocolId(Integer protocolId) throws GobiiDtoMappingException;
    void addVendorProtocolsToOrganization(OrganizationDTO organizationDTO)  throws GobiiException;
    void addVendorProtocolsToProtocol(ProtocolDTO protocolDTO)  throws GobiiException;
    OrganizationDTO updateOrReplaceVendotrByProtocolId(Integer protocolId, OrganizationDTO organizationDTO) throws GobiiDtoMappingException;
}
