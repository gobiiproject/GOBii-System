package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsAnalysisDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.DtoMapAnalysis;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.core.EntityProperties;
import org.gobiiproject.gobiimodel.dto.container.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.container.EntityPropertyDTO;
import org.gobiiproject.gobiimodel.dto.container.ExperimentDTO;
import org.gobiiproject.gobiimodel.dto.container.ProjectDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 4/22/2016.
 */
public class DtoMapAnalysisImpl implements DtoMapAnalysis {


    Logger LOGGER = LoggerFactory.getLogger(DtoMapDataSetImpl.class);


    @Autowired
    private RsAnalysisDao rsAnalysisDao;

    public AnalysisDTO getAnalysisDetails(AnalysisDTO analysisDTO) throws GobiiDtoMappingException {

        AnalysisDTO returnVal = analysisDTO;

        try {

            ResultSet resultSet = rsAnalysisDao.getAnalysisDetailsByAnalysisId(analysisDTO.getAnalysisId());

            if (resultSet.next()) {
                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);
            }

            ResultSet propertyResultSet = rsAnalysisDao.getParameters(analysisDTO.getAnalysisId());
            List<EntityPropertyDTO> entityPropertyDTOs =
                    EntityProperties.resultSetToProperties(analysisDTO.getAnalysisId(), propertyResultSet);

            analysisDTO.setParameters(entityPropertyDTOs);


        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;

    }

    private void upsertAnalysisProperties(Integer analysisId, List<EntityPropertyDTO> analysisProperties) throws GobiiDaoException {

        for (EntityPropertyDTO currentProperty : analysisProperties) {

            Map<String, Object> spParamsParameters =
                    EntityProperties.propertiesToParams(analysisId, currentProperty);

            rsAnalysisDao.createUpdateParameter(spParamsParameters);

            currentProperty.setEntityIdId(analysisId);
        }

    }

    @Override
    public AnalysisDTO createAnalysis(AnalysisDTO analysisDTO) throws GobiiDtoMappingException {

        AnalysisDTO returnVal = analysisDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(analysisDTO);
            Integer analysisId = rsAnalysisDao.createAnalysis(parameters);
            returnVal.setAnalysisId(analysisId);

            List<EntityPropertyDTO> analysisParameters = analysisDTO.getParameters();
            upsertAnalysisProperties(analysisDTO.getAnalysisId(), analysisParameters);

        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;
    }

    @Override
    public AnalysisDTO updateAnalysis(AnalysisDTO analysisDTO) throws GobiiDtoMappingException {

        AnalysisDTO returnVal = analysisDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
            rsAnalysisDao.updateAnalysis(parameters);

            if (null != analysisDTO.getParameters()) {
                upsertAnalysisProperties(analysisDTO.getAnalysisId(),
                        analysisDTO.getParameters());
            }

        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;
    }
}
