package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsPlatformDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidao.resultset.core.listquery.DtoListQueryColl;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidtomapping.DtoMapPlatform;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.core.EntityProperties;
import org.gobiiproject.gobiimodel.dto.container.EntityPropertyDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.OrganizationDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.PlatformDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 4/21/2016.
 */
public class DtoMapPlatformImpl implements DtoMapPlatform {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapPlatformImpl.class);


    @Autowired
    private DtoListQueryColl dtoListQueryColl;

    @Autowired
    private RsPlatformDao rsPlatformDao;

    @SuppressWarnings("unchecked")
    @Override
    public List<PlatformDTO> getPlatforms() throws GobiiDtoMappingException {

        List<PlatformDTO> returnVal = new ArrayList<PlatformDTO>();


        returnVal = (List<PlatformDTO>) dtoListQueryColl.getList(ListSqlId.QUERY_ID_PLATFORM_ALL, null);


        return returnVal;
    }

    @Override
    public PlatformDTO getPlatformDetails(Integer platformId) throws GobiiDtoMappingException {

        PlatformDTO returnVal = new PlatformDTO();

        ResultSet resultSet = rsPlatformDao.getPlatformDetailsByPlatformId(platformId);

        try {

            if (resultSet.next()) {

                // apply platform values
                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);

                ResultSet propertyResultSet = rsPlatformDao.getProperties(returnVal.getPlatformId());
                List<EntityPropertyDTO> entityPropertyDTOs =
                        EntityProperties.resultSetToProperties(returnVal.getPlatformId(), propertyResultSet);

                returnVal.setProperties(entityPropertyDTOs);

            } // if result set has a row
        } catch (SQLException e) {
            LOGGER.error("Error retreving platform details", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;

    } // getPlatformDetails()

    @Override
    public PlatformDTO createPlatform(PlatformDTO platformDTO) throws GobiiDtoMappingException {
        PlatformDTO returnVal = platformDTO;


        Map<String, Object> parameters = ParamExtractor.makeParamVals(platformDTO);
        Integer platformId = rsPlatformDao.createPlatform(parameters);
        returnVal.setPlatformId(platformId);

        List<EntityPropertyDTO> platformParameters = platformDTO.getProperties();
        upsertPlatformProperties(platformDTO.getPlatformId(), platformParameters);

        return returnVal;
    }


    private void upsertPlatformProperties(Integer platformId, List<EntityPropertyDTO> platformProperties) throws GobiiDaoException {

        for (EntityPropertyDTO currentProperty : platformProperties) {

            Map<String, Object> spParamsParameters =
                    EntityProperties.propertiesToParams(platformId, currentProperty);

            Integer propertyId = rsPlatformDao.createUpdatePlatformProperty(spParamsParameters);

            if (null == propertyId || propertyId <= 0) {
                throw (new GobiiDaoException("Unable to update platform property with name: "
                        + currentProperty.getPropertyName()
                        + "; the property name must be a cv term"));
            }

            currentProperty.setEntityIdId(platformId);
            currentProperty.setPropertyId(propertyId);
        }

    }

    @Override
    public PlatformDTO replacePlatform(Integer platformId, PlatformDTO platformDTO) throws GobiiDtoMappingException {

        PlatformDTO returnVal = platformDTO;


        Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
        parameters.put("platformId", platformId);
        rsPlatformDao.updatePlatform(parameters);

        if (null != platformDTO.getProperties()) {
            upsertPlatformProperties(platformId,
                    platformDTO.getProperties());
        }

        return returnVal;
    }
}
