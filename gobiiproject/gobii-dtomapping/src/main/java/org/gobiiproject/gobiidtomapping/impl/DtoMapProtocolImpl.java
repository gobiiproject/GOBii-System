package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.resultset.access.RsProtocolDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidao.resultset.core.listquery.DtoListQueryColl;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidtomapping.DtoMapOrganization;
import org.gobiiproject.gobiidtomapping.DtoMapProtocol;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.headerlesscontainer.OrganizationDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.ProtocolDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.VendorProtocolDTO;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by VCalaminos on 2016-12-14.
 */
public class DtoMapProtocolImpl implements DtoMapProtocol {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapProtocolImpl.class);

    @Autowired
    private RsProtocolDao rsProtocolDao;

    @Autowired
    private DtoListQueryColl dtoListQueryColl;

    @Autowired
    private DtoMapOrganization dtoMapOrganization;

    @SuppressWarnings("unchecked")
    @Override
    public List<ProtocolDTO> getProtocols() throws GobiiDtoMappingException {

        List<ProtocolDTO> returnVal = new ArrayList<>();

        try {

            returnVal = (List<ProtocolDTO>) dtoListQueryColl.getList(ListSqlId.QUERY_ID_PROTOCOL_ALL, null);

            // yes, this is inefficient; however, at the moment we have
            // less development time than we do vendors per protocols --
            // in this case, the second level queries will not be a disaster
            for(ProtocolDTO currentProtocolDTO : returnVal) {
                this.addVendorProtocolsToProtocol(currentProtocolDTO);
            }

        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }


    @Override
    public ProtocolDTO createProtocol(ProtocolDTO protocolDTO) throws GobiiDtoMappingException {

        ProtocolDTO returnVal = protocolDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
            Integer protocolId = rsProtocolDao.createProtocol(parameters);
            returnVal.setProtocolId(protocolId);

        } catch (Exception e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);

        }

        return returnVal;
    }

    @Override
    public ProtocolDTO replaceProtocol(Integer protocolId, ProtocolDTO protocolDTO) throws GobiiDtoMappingException {

        ProtocolDTO returnVal = protocolDTO;

        Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
        parameters.put("protocolId", protocolId);
        rsProtocolDao.updateProtocol(parameters);

        return returnVal;
    }


    @Transactional
    @Override
    public ProtocolDTO getProtocolDetails(Integer protocolId) throws GobiiDtoMappingException {

        ProtocolDTO returnVal = new ProtocolDTO();

        try {

            ResultSet resultSet = rsProtocolDao.getProtocolDetailsByProtocolId(protocolId);

            if (resultSet.next()) {
                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);
                this.addVendorProtocolsToProtocol(returnVal);
            }

        } catch (Exception e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

    @Transactional
    @Override
    public List<OrganizationDTO> getVendorsForProtocolByProtocolId(Integer protocolId) throws GobiiDtoMappingException {

        List<OrganizationDTO> returnVal = new ArrayList<>();

        try {
            ResultSet resultSet = rsProtocolDao.getVendorsByProtocolId(protocolId);

            while (resultSet.next()) {
                OrganizationDTO currentOrganizationDTO = new OrganizationDTO();
                ResultColumnApplicator.applyColumnValues(resultSet, currentOrganizationDTO);
                returnVal.add(currentOrganizationDTO);
            }

        } catch (Exception e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

    @Transactional
    @Override
    public VendorProtocolDTO getVendorProtocolByVendorProtocolId(Integer vendorProtocolId) throws GobiiDtoMappingException {

        VendorProtocolDTO returnVal = new VendorProtocolDTO();

        try {

            ResultSet resultSet = rsProtocolDao.getVendorProtocolForVendorProtocolId(vendorProtocolId);

            if(resultSet.next()) {
                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);
            }

        } catch (Exception e) {

            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);

        }

        return returnVal;

    }

    @Transactional
    @Override
    public OrganizationDTO getVendorForProtocolByName(String vendorProtocolName) throws GobiiDtoMappingException {

        OrganizationDTO returnVal = new OrganizationDTO();

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("vendorProtocolName", vendorProtocolName);

            ResultSet resultSet = rsProtocolDao.getVendorByProtocolVendorName(parameters);

            if (resultSet.next()) {
                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);
            }

        } catch (Exception e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

    @Transactional
    @Override
    public void addVendorProtocolsToOrganization(OrganizationDTO organizationDTO) throws GobiiException {

        try {
            organizationDTO.getVendorProtocols().clear();
            ResultSet resultSet = this.rsProtocolDao.getVendorProtocolsForVendor(organizationDTO.getOrganizationId());
            while (resultSet.next()) {
                VendorProtocolDTO currentVendorProtocolDTO = new VendorProtocolDTO();
                ResultColumnApplicator.applyColumnValues(resultSet, currentVendorProtocolDTO);
                organizationDTO.getVendorProtocols().add(currentVendorProtocolDTO);
            }

        } catch (Exception e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

    } // addVendorProtocolsToOrganization

    @Transactional
    @Override
    public void addVendorProtocolsToProtocol(ProtocolDTO protocolDTO) throws GobiiException {

        try {
            protocolDTO.getVendorProtocols().clear();
            ResultSet resultSet = this.rsProtocolDao.getVendorProtocolsForProtocol(protocolDTO.getProtocolId());
            while (resultSet.next()) {
                VendorProtocolDTO currentVendorProtocolDTO = new VendorProtocolDTO();
                ResultColumnApplicator.applyColumnValues(resultSet, currentVendorProtocolDTO);
                protocolDTO.getVendorProtocols().add(currentVendorProtocolDTO);
            }

        } catch (Exception e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

    } // addVendorProtocolsToOrganization

    @Transactional
    @Override
    public ProtocolDTO getProtocolsByExperimentId(Integer experimentId) throws GobiiException {
        ProtocolDTO returnVal = new ProtocolDTO();
        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("experimentId", experimentId);


            ResultSet resultSet = this.rsProtocolDao.getProtocolDetailsByExperimentId(experimentId);
            if (resultSet.next()) {
                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);
            }

        } catch (Exception e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    } // getProtocolDetailsByExperimentId

    @Transactional
    @Override
    public OrganizationDTO addVendotrToProtocol(Integer protocolId, OrganizationDTO organizationDTO) throws GobiiDtoMappingException {

        OrganizationDTO returnVal = organizationDTO;

        if (protocolId == null || protocolId <= 0) {
            throw new GobiiDtoMappingException("Protocol ID must be greater than 0");
        }

        if (organizationDTO.getOrganizationId() == null || organizationDTO.getOrganizationId() <= 0) {
            throw new GobiiDtoMappingException("Organization ID must be greater than 0");
        }

        if (LineUtils.isNullOrEmpty(organizationDTO.getName())) {
            throw new GobiiDtoMappingException("Organization name must have a value");
        }

        ProtocolDTO protocolDTO = this.getProtocolDetails(protocolId);
        if (protocolDTO == null ||
                protocolDTO.getProtocolId() == null ||
                protocolDTO.getProtocolId() <= 0) {
            throw new GobiiDtoMappingException("There is no protocol corresponding to protocol id" + protocolId.toString());
        }

        VendorProtocolDTO vendorProtocolDTO = null;
        List<VendorProtocolDTO> vendorProtocolDTOList
                = organizationDTO
                .getVendorProtocols()
                .stream()
                .filter(vendorProtocolDTO1 ->
                        vendorProtocolDTO1
                                .getOrganizationId()
                                .equals(organizationDTO.getOrganizationId())
                                && vendorProtocolDTO1.getProtocolId().equals(protocolId))
                .collect(Collectors.toList());


        if (vendorProtocolDTOList.size() == 1) {
            vendorProtocolDTO = vendorProtocolDTOList.get(0);
        } else {
            vendorProtocolDTO = new VendorProtocolDTO();
        }

        String vendoProtocolName = vendorProtocolDTO.getName();

        if (LineUtils.isNullOrEmpty(vendoProtocolName)) {
            vendoProtocolName = organizationDTO.getName() + "_" + protocolDTO.getName();
        }

        OrganizationDTO organizationDTOForName = this.getVendorForProtocolByName(vendoProtocolName);
        if (organizationDTOForName.getOrganizationId() != null && organizationDTOForName.getOrganizationId() > 0) {
            throw (new GobiiDtoMappingException(GobiiStatusLevel.VALIDATION,
                    GobiiValidationStatusType.ENTITY_ALREADY_EXISTS,
                    "A vendor protocol association already exists for " + vendoProtocolName));
        }

        try {
            ResultSet existingVendorProtocolRs = this.rsProtocolDao.getVendorByProtocolByCompoundIds(protocolId, organizationDTO.getOrganizationId());
            if (existingVendorProtocolRs.next()) {
                VendorProtocolDTO vendorProtocolDTO1 = new VendorProtocolDTO();
                ResultColumnApplicator.applyColumnValues(existingVendorProtocolRs, vendorProtocolDTO1);
                throw (new GobiiDtoMappingException(GobiiStatusLevel.VALIDATION,
                        GobiiValidationStatusType.ENTITY_ALREADY_EXISTS,
                        "A vendor protocol association already exists for protocolId "
                                + protocolId.toString()
                                + " and vendorId "
                                + organizationDTO.getOrganizationId().toString()));
            }


            Map<String, Object> parameters = new HashMap<>();

            parameters.put("name", vendoProtocolName);
            parameters.put("vendorId", organizationDTO.getOrganizationId());
            parameters.put("protocolId", protocolId);
            parameters.put("status", 1);

            Integer vendorProtocolId = this.rsProtocolDao.createVendorProtocol(parameters);

            if (vendorProtocolId == null || vendorProtocolId <= 0) {
                throw new GobiiDtoMappingException("VendorProtocol record was not created");
            }

            this.addVendorProtocolsToOrganization(returnVal);

        } catch (Exception e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);

        }

        return returnVal;
    }

    @Transactional
    @Override
    public OrganizationDTO updateOrReplaceVendotrByProtocolId(Integer protocolId, OrganizationDTO organizationDTO) throws GobiiDtoMappingException {


        OrganizationDTO returnVal = organizationDTO;

        try {
            dtoMapOrganization.replaceOrganization(returnVal.getOrganizationId(), returnVal);

            List<VendorProtocolDTO> vendorProtocolsForProtocol = returnVal.getVendorProtocols()
                    .stream()
                    .filter(vendorProtocolDTO -> vendorProtocolDTO.getProtocolId().equals(protocolId))
                    .collect(Collectors.toList());

            if (returnVal.getVendorProtocols().size() > 1 && vendorProtocolsForProtocol.size() == 0) {
                throw new GobiiDtoMappingException("The vendor data contains vendorProtocol items, " +
                        "but none of them matches the protocolId parameter value: " +
                        protocolId.toString());
            }

            // there is some paranoid validation here because at the moment the schema
            // does not have a compound unique constraint for vendor_id and protocol_id
            for (VendorProtocolDTO currentVendorProtocolDTO : vendorProtocolsForProtocol) {

                if (!currentVendorProtocolDTO.getProtocolId().equals(protocolId)) {
                    throw new GobiiDtoMappingException("The VendorProtocolDTO protoclId ("
                            + currentVendorProtocolDTO.getProtocolId().toString()
                            + ") does not match the specified protocolId parameter value ("
                            + protocolId.toString()
                            + ")");
                }

                ResultSet resultSet =
                        this.rsProtocolDao.getVendorProtocolForVendorProtocolId(currentVendorProtocolDTO
                                .getVendorProtocolId());
                if (resultSet.next()) {
                    VendorProtocolDTO vendorProtocolDTOFromDb = new VendorProtocolDTO();
                    ResultColumnApplicator.applyColumnValues(resultSet, vendorProtocolDTOFromDb);

                    if (!vendorProtocolDTOFromDb.getProtocolId().equals(currentVendorProtocolDTO.getProtocolId())) {
                        throw new GobiiDtoMappingException("The VendorProtocolDTO protoclId ("
                                + currentVendorProtocolDTO.getProtocolId().toString()
                                + ") does not match the protocolId for the vendorProtocolId in the database("
                                + vendorProtocolDTOFromDb.getProtocolId().toString()
                                + "); "
                                + " the vendorProtocolId is "
                                + vendorProtocolDTOFromDb.getVendorProtocolId().toString());
                    }

                    if (!vendorProtocolDTOFromDb.getVendorProtocolId().equals(currentVendorProtocolDTO.getVendorProtocolId())) {
                        throw new GobiiDtoMappingException("The VendorProtocolDTO vendorId ("
                                + currentVendorProtocolDTO.getOrganizationId().toString()
                                + ") does not match the protocolId for the vendorProtocolId in the database("
                                + vendorProtocolDTOFromDb.getOrganizationId().toString()
                                + "); "
                                + " the vendorProtocolId is "
                                + vendorProtocolDTOFromDb.getVendorProtocolId().toString());
                    }

                    // in the end, all we should ever be setting on this record is the name
                    // the protocol id and vendor id are sacrosanct.
                    vendorProtocolDTOFromDb.setName(currentVendorProtocolDTO.getName());
                    Map<String, Object> parameters = ParamExtractor.makeParamVals(vendorProtocolDTOFromDb);
                    rsProtocolDao.updateVendorProtocol(parameters);


                } else {
                    throw (new GobiiDtoMappingException("There is no vendor_protocol record for vendor_protocol_id " +
                            currentVendorProtocolDTO.getVendorProtocolId()));
                }


            }


        } catch (Exception e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);

        }


        return returnVal;

    }


}
