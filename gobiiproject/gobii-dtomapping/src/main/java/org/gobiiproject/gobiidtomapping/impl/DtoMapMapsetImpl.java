package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsMapSetDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.DtoMapMapset;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.core.EntityProperties;
import org.gobiiproject.gobiimodel.dto.container.MapsetDTO;
import org.gobiiproject.gobiimodel.dto.container.EntityPropertyDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 4/21/2016.
 */
public class DtoMapMapsetImpl implements DtoMapMapset {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapMapsetImpl.class);


    @Autowired
    private RsMapSetDao rsMapsetDao;

    @Override
    public MapsetDTO getMapsetDetails(MapsetDTO mapsetDTO) throws GobiiDtoMappingException {

        MapsetDTO returnVal = mapsetDTO;

        try {

            ResultSet resultSet = rsMapsetDao.getMapsetDetailsByMapsetId(mapsetDTO.getMapsetId());

            if (resultSet.next()) {

                // apply dataset values
                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);

                ResultSet propertyResultSet = rsMapsetDao.getProperties(mapsetDTO.getMapsetId());
                List<EntityPropertyDTO> entityPropertyDTOs =
                        EntityProperties.resultSetToProperties(mapsetDTO.getMapsetId(), propertyResultSet);

                mapsetDTO.setProperties(entityPropertyDTOs);

            } // if result set has a row

        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;
    }

    @Override
    public MapsetDTO createMapset(MapsetDTO mapsetDTO) throws GobiiDtoMappingException {
        MapsetDTO returnVal = mapsetDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(mapsetDTO);
            Integer mapsetId = rsMapsetDao.createMapset(parameters);
            returnVal.setMapsetId(mapsetId);

            List<EntityPropertyDTO> mapsetParameters = mapsetDTO.getProperties();
            upsertMapsetProperties(mapsetDTO.getMapsetId(), mapsetParameters);

        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;
    }

    private void upsertMapsetProperties(Integer mapsetId, List<EntityPropertyDTO> mapsetProperties) throws GobiiDaoException {

        for (EntityPropertyDTO currentProperty : mapsetProperties) {

            Map<String, Object> spParamsParameters =
                    EntityProperties.propertiesToParams(mapsetId, currentProperty);

            Integer propertyId = rsMapsetDao.createUpdateMapSetProperty(spParamsParameters);

            if (null == propertyId || propertyId <= 0) {
                throw (new GobiiDaoException("Unable to update mapset property with name: "
                        + currentProperty.getPropertyName()
                        + "; the property name must be a cv term"));
            }

            currentProperty.setEntityIdId(mapsetId);
            currentProperty.setPropertyId(propertyId);
        }

    }

    @Override
    public MapsetDTO updateMapset(MapsetDTO mapsetDTO) throws GobiiDtoMappingException {

        MapsetDTO returnVal = mapsetDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
            rsMapsetDao.updateMapset(parameters);

            if (null != mapsetDTO.getProperties()) {
                upsertMapsetProperties(mapsetDTO.getMapsetId(),
                        mapsetDTO.getProperties());
            }

        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;
    }
}
