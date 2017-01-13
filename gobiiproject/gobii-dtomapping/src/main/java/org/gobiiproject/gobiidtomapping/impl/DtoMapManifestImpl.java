package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.resultset.access.RsManifestDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.DtoMapManifest;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.container.ManifestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.util.Map;

/**
 * Created by Angel on 5/4/2016.
 */
public class DtoMapManifestImpl implements DtoMapManifest {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapManifestImpl.class);


    @Autowired
    private RsManifestDao rsManifestDao;

    @Transactional
    @Override
    public ManifestDTO getManifestDetails(ManifestDTO manifestDTO) throws GobiiDtoMappingException {

        ManifestDTO returnVal = manifestDTO;

        try {

            ResultSet resultSet = rsManifestDao.getManifestDetailsByManifestId(manifestDTO.getManifestId());

            if (resultSet.next()) {

                // apply manifest values
                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);

            } // iterate resultSet

        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
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
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;
    }

    @Override
    public ManifestDTO updateManifest(ManifestDTO manifestDTO) throws GobiiDtoMappingException {

        ManifestDTO returnVal = manifestDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
            rsManifestDao.updateManifest(parameters);

        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;
    }
}
