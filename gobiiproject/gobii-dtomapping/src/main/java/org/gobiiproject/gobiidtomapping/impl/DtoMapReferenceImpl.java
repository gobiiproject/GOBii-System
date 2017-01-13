package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsReferenceDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.DtoMapReference;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.container.ReferenceDTO;
import org.gobiiproject.gobiimodel.dto.container.ReferenceDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Angel on 5/4/2016.
 */
public class DtoMapReferenceImpl implements DtoMapReference {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapReferenceImpl.class);


    @Autowired
    private RsReferenceDao rsReferenceDao;

    @Transactional
    @Override
    public ReferenceDTO getReferenceDetails(ReferenceDTO referenceDTO) throws GobiiDtoMappingException {

        ReferenceDTO returnVal = referenceDTO;

        try {

            ResultSet resultSet = rsReferenceDao.getReferenceDetailsByReferenceId(referenceDTO.getReferenceId());

            if (resultSet.next()) {

                // apply reference values
                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);

            } // iterate resultSet

        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
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
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;
    }

    @Override
    public ReferenceDTO updateReference(ReferenceDTO referenceDTO) throws GobiiDtoMappingException {

        ReferenceDTO returnVal = referenceDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
            rsReferenceDao.updateReference(parameters);

        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;
    }
}
