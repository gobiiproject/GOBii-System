package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.resultset.access.RsOrganizationDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.DtoMapOrganization;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.container.OrganizationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.util.Map;

/**
 * Created by Angel on 5/4/2016.
 */
public class DtoMapOrganizationImpl implements DtoMapOrganization {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapOrganizationImpl.class);


    @Autowired
    private RsOrganizationDao rsOrganizationDao;

    @Transactional
    @Override
    public OrganizationDTO getOrganizationDetails(OrganizationDTO organizationDTO) throws GobiiDtoMappingException {

        OrganizationDTO returnVal = organizationDTO;

        try {

            ResultSet resultSet = rsOrganizationDao.getOrganizationDetailsByOrganizationId(organizationDTO.getOrganizationId());

            if (resultSet.next()) {

                // apply organization values
                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);

            } // iterate resultSet

        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;

    }

    @Override
    public OrganizationDTO createOrganization(OrganizationDTO organizationDTO) throws GobiiDtoMappingException {
        OrganizationDTO returnVal = organizationDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(organizationDTO);
            Integer organizationId = rsOrganizationDao.createOrganization(parameters);
            returnVal.setOrganizationId(organizationId);

        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;
    }

    @Override
    public OrganizationDTO updateOrganization(OrganizationDTO organizationDTO) throws GobiiDtoMappingException {

        OrganizationDTO returnVal = organizationDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
            rsOrganizationDao.updateOrganization(parameters);

        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;
    }
}
