package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsMapSetDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.DtoMapMapset;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.core.EntityProperties;
import org.gobiiproject.gobiimodel.headerlesscontainer.MapsetDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.EntityPropertyDTO;
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
 * Modified by AVB on 9/29/2016.
 */
public class DtoMapMapsetImpl implements DtoMapMapset {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapMapsetImpl.class);

    @Autowired
    private RsMapSetDao rsMapsetDao;

    @Override
    public List<MapsetDTO> getAllMapsetNames() throws GobiiDtoMappingException {

        List<MapsetDTO> returnVal = new ArrayList<MapsetDTO>();

        try {
            ResultSet resultSet = rsMapsetDao.getAllMapsetNames();
            while (resultSet.next()) {
                MapsetDTO currentMapsetDTO = new MapsetDTO();
                currentMapsetDTO.setName(resultSet.getString("name"));
                currentMapsetDTO.setMapsetId(resultSet.getInt("mapset_id"));
                returnVal.add(currentMapsetDTO);
            }
        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }


    @Override
    public List<MapsetDTO> getMapsets() throws GobiiDtoMappingException {

        List<MapsetDTO> returnVal = new ArrayList<>();

        try {
            ResultSet resultSet = rsMapsetDao.getAllMapsetNames();
            while (resultSet.next()) {
                MapsetDTO currentMapsetDTO = new MapsetDTO();
                currentMapsetDTO.setName(resultSet.getString("name"));
                currentMapsetDTO.setMapsetId(resultSet.getInt("mapset_id"));
                returnVal.add(currentMapsetDTO);
            }

        } catch (SQLException e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;

    }

    @Override
    public MapsetDTO getMapsetDetails(Integer mapsetId) throws GobiiDtoMappingException {

        MapsetDTO returnVal = new MapsetDTO();

        ResultSet resultSet = rsMapsetDao.getMapsetDetailsByMapsetId(mapsetId);

        try {

            if(resultSet.next()) {

                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);

                ResultSet propertyResultSet = rsMapsetDao.getProperties(returnVal.getMapsetId());
                List<EntityPropertyDTO> entityPropertyDTOs =
                        EntityProperties.resultSetToProperties(returnVal.getMapsetId(), propertyResultSet);

                returnVal.setProperties(entityPropertyDTOs);
            }

        } catch (SQLException e) {
            LOGGER.error("Error retrieving mapset details", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

    @Override
    public MapsetDTO createMapset(MapsetDTO mapsetDTO) throws GobiiDtoMappingException {

        MapsetDTO returnVal = mapsetDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
            Integer mapsetId = rsMapsetDao.createMapset(parameters);
            returnVal.setMapsetId(mapsetId);

        } catch (Exception e) {

            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
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
    public MapsetDTO replaceMapset(Integer mapsetId, MapsetDTO mapsetDTO) throws GobiiDtoMappingException {

        MapsetDTO returnVal = mapsetDTO;

        Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
        parameters.put("mapsetId", mapsetId);
        rsMapsetDao.updateMapset(parameters);

        if(null != mapsetDTO.getProperties()) {
            upsertMapsetProperties(mapsetId,
                    mapsetDTO.getProperties());
        }

        return returnVal;
    }
}
