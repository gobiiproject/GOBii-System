package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.resultset.access.RsReferenceDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.DtoMapReference;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.headerlesscontainer.ReferenceDTO;
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
 */
public class DtoMapReferenceImpl implements DtoMapReference {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapReferenceImpl.class);


    @Autowired
    private RsReferenceDao rsReferenceDao;

    @Override
    public List<ReferenceDTO> getReferences() throws GobiiDtoMappingException {

        List<ReferenceDTO> returnVal = new ArrayList<>();

        try {
            ResultSet resultSet = rsReferenceDao.getReferenceNames();
            while (resultSet.next()) {
                ReferenceDTO currentReferenceDTO = new ReferenceDTO();
                currentReferenceDTO.setName(resultSet.getString("name"));
                currentReferenceDTO.setReferenceId(resultSet.getInt("reference_id"));
                returnVal.add(currentReferenceDTO);
            }

        } catch (SQLException e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;

    }

    @Transactional
    @Override
    public ReferenceDTO getReferenceDetails(Integer referenceId) throws GobiiDtoMappingException {

        ReferenceDTO returnVal = new ReferenceDTO();

        try {

            ResultSet resultSet = rsReferenceDao.getReferenceDetailsByReferenceId(referenceId);

            if (resultSet.next()) {

                // apply reference values
                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);

            } // iterate resultSet

        } catch (SQLException e) {
            LOGGER.error("Error retrieving reference details", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;

    }

    @Override
    public ReferenceDTO createReference(ReferenceDTO referenceDTO) throws GobiiDtoMappingException {
        ReferenceDTO returnVal = referenceDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(referenceDTO);
            Integer referenceId = rsReferenceDao.createReference(parameters);
            returnVal.setReferenceId(referenceId);

        } catch (Exception e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

    @Override
    public ReferenceDTO replaceReference(Integer referenceId, ReferenceDTO referenceDTO) throws GobiiDtoMappingException {

        ReferenceDTO returnVal = referenceDTO;

        Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
        parameters.put("referenceId", referenceId);
        rsReferenceDao.updateReference(parameters);

        return returnVal;
    }

}
