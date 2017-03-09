package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.headerlesscontainer.OrganizationDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.ProtocolDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.VendorProtocolDTO;

import java.util.List;

/**
 * Created by VCalaminos on 2016-12-14.
 */
public interface ProtocolService {


    ProtocolDTO createProtocol(ProtocolDTO protocolDTO) throws GobiiDomainException;

    ProtocolDTO replaceProtocol(Integer protocolId, ProtocolDTO protocolDTO) throws GobiiDomainException;

    ProtocolDTO getProtocolById(Integer protocolId) throws GobiiDomainException;

    List<ProtocolDTO> getProtocols() throws GobiiDomainException;

    OrganizationDTO addVendotrToProtocol(Integer protocolId, OrganizationDTO organizationDTO) throws GobiiDomainException;

    OrganizationDTO updateOrReplaceVendotrToProtocol(Integer protocolId, OrganizationDTO organizationDTO) throws GobiiDomainException;

    List<OrganizationDTO> getVendorsForProtocolByProtocolId(Integer protocolId) throws GobiiDaoException;

    ProtocolDTO getProtocolsByExperimentId(Integer experimentId) throws GobiiDomainException;
}
