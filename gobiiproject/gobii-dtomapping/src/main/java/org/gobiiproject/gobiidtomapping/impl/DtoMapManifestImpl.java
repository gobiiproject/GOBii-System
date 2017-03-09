package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.resultset.access.RsManifestDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.DtoMapManifest;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.headerlesscontainer.ManifestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Angel on 5/4/2016.
 * Modified by Yanii on 1/27/2017
 */
public class DtoMapManifestImpl implements DtoMapManifest {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapManifestImpl.class);


    @Autowired
    private RsManifestDao rsManifestDao;

    @Transactional
    @Override
    public ManifestDTO getManifestDetails(Integer manifestId) throws GobiiDtoMappingException {

        ManifestDTO returnVal = new ManifestDTO();

        try {

            ResultSet resultSet = rsManifestDao.getManifestDetailsByManifestId(manifestId);

            if (resultSet.next()) {

                // apply manifest values
                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);

            } // iterate resultSet

        } catch (Exception e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;

    }

    @Override
    public List<ManifestDTO> getManifests() throws GobiiDtoMappingException {

        List<ManifestDTO> returnVal = new ArrayList<>();

        try {
            ResultSet resultSet = rsManifestDao.getManifestNames();
            while (resultSet.next()) {
                ManifestDTO currentManifestDTO = new ManifestDTO();
                currentManifestDTO.setName(resultSet.getString("name"));
                currentManifestDTO.setManifestId(resultSet.getInt("manifest_id"));
                returnVal.add(currentManifestDTO);
            }

        } catch (SQLException e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

    @Override
    public ManifestDTO createManifest(ManifestDTO manifestDTO) throws GobiiDtoMappingException {
        ManifestDTO returnVal = manifestDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(manifestDTO);
            Integer manifestId = rsManifestDao.createManifest(parameters);
            returnVal.setManifestId(manifestId);

        } catch (Exception e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }


    @Override
    public ManifestDTO replaceManifest(Integer manifestId, ManifestDTO manifestDTO) throws GobiiDtoMappingException {

        ManifestDTO returnVal = manifestDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
            parameters.put("manifestId", manifestId);
            rsManifestDao.updateManifest(parameters);

        } catch (Exception e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }
}
