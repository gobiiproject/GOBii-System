package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.ProtocolService;
import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidtomapping.DtoMapProtocol;
import org.gobiiproject.gobiimodel.headerlesscontainer.OrganizationDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.ProtocolDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.VendorProtocolDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VCalaminos on 2016-12-14.
 */
public class ProtocolServiceImpl implements ProtocolService {

    Logger LOGGER = LoggerFactory.getLogger(ProtocolServiceImpl.class);

    @Autowired
    DtoMapProtocol dtoMapProtocol = null;

    @Override
    public ProtocolDTO createProtocol(ProtocolDTO protocolDTO) throws GobiiDomainException {

        ProtocolDTO returnVal;

        try {

            returnVal = dtoMapProtocol.createProtocol(protocolDTO);

            returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
            returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);

        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);

        }
        return returnVal;

    }

    @Override
    public ProtocolDTO replaceProtocol(Integer protocolId, ProtocolDTO protocolDTO) throws GobiiDomainException {

        ProtocolDTO returnVal;

        try {

            if (null == protocolDTO.getProtocolId() ||
                    protocolDTO.getProtocolId().equals(protocolId)) {


                ProtocolDTO existingProtocolDTO = dtoMapProtocol.getProtocolDetails(protocolId);
                if (null != existingProtocolDTO.getProtocolId() && existingProtocolDTO.getProtocolId().equals(protocolId)) {


                    returnVal = dtoMapProtocol.replaceProtocol(protocolId, protocolDTO);
                    returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
                    returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);

                } else {

                    throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                            GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                            "The specified contactId ("
                                    + protocolId
                                    + ") does not match an existing contact ");
                }

            } else {

                throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                        GobiiValidationStatusType.BAD_REQUEST,
                        "The contactId specified in the dto ("
                                + protocolDTO.getProtocolId()
                                + ") does not match the contactId passed as a parameter "
                                + "("
                                + protocolId
                                + ")");

            }


        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }

        return returnVal;
    }

    @Override
    public ProtocolDTO getProtocolById(Integer ProtocolId) {

        ProtocolDTO returnVal;

        try {
            returnVal = dtoMapProtocol.getProtocolDetails(ProtocolId);

            returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
            returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);


            if (null == returnVal) {
                throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                        GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                        "The specified ProtocolId ("
                                + ProtocolId
                                + ") does not match an existing Protocol ");
            }

        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);

        }

        return returnVal;
    }

    @Override
    public List<ProtocolDTO> getProtocols() throws GobiiDomainException {

        List<ProtocolDTO> returnVal;

        try {
            returnVal = dtoMapProtocol.getProtocols();

            for (ProtocolDTO currentProtocolDTO : returnVal) {
                currentProtocolDTO.getAllowedProcessTypes().add(GobiiProcessType.READ);
                currentProtocolDTO.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);
            }


            if (null == returnVal) {
                returnVal = new ArrayList<>();
            }

        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);

        }

        return returnVal;
    }

    @Override
    public OrganizationDTO addVendotrToProtocol(Integer protocolId, OrganizationDTO organizationDTO) throws GobiiDomainException {

        OrganizationDTO returnVal;

        returnVal = dtoMapProtocol.addVendotrToProtocol(protocolId, organizationDTO);

        return returnVal;

    }

    @Override
    public List<OrganizationDTO> getVendorsForProtocolByProtocolId(Integer protocolId) throws GobiiDaoException {

        List<OrganizationDTO> returnVal;

        returnVal = dtoMapProtocol.getVendorsForProtocolByProtocolId(protocolId);

        return returnVal;
    }

    @Override
    public ProtocolDTO getProtocolsByExperimentId(Integer experimentId) throws GobiiDaoException {

        ProtocolDTO returnVal;

        returnVal = dtoMapProtocol.getProtocolsByExperimentId(experimentId);

        return returnVal;
    }

    @Override
    public OrganizationDTO updateOrReplaceVendotrToProtocol(Integer protocolId, OrganizationDTO organizationDTO) throws GobiiDomainException {

        OrganizationDTO returnVal;

        returnVal = dtoMapProtocol.updateOrReplaceVendotrByProtocolId(protocolId, organizationDTO);

        return returnVal;
    }


}
