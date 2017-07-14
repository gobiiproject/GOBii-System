package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsAnalysisDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.DtoMapAnalysis;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.core.EntityProperties;
import org.gobiiproject.gobiimodel.headerlesscontainer.AnalysisDTO;
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
 * Created by Phil on 4/22/2016.
 */
public class DtoMapAnalysisImpl implements DtoMapAnalysis {


    Logger LOGGER = LoggerFactory.getLogger(DtoMapDataSetImpl.class);


    @Autowired
    private RsAnalysisDao rsAnalysisDao;

    public AnalysisDTO getAnalysisDetails(Integer analysisId) throws GobiiDtoMappingException {

        AnalysisDTO returnVal = new AnalysisDTO();

        try {

            ResultSet resultSet = rsAnalysisDao.getAnalysisDetailsByAnalysisId(analysisId);

            if (resultSet.next()) {
                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);
            }

            ResultSet propertyResultSet = rsAnalysisDao.getParameters(returnVal.getAnalysisId());
            List<EntityPropertyDTO> entityPropertyDTOs =
                    EntityProperties.resultSetToProperties(returnVal.getAnalysisId(), propertyResultSet);

            returnVal.setParameters(entityPropertyDTOs);


        } catch (SQLException e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;

    }

    @Override
    public List<AnalysisDTO> getAnalyses() throws GobiiDtoMappingException {

        List<AnalysisDTO> returnVal = new ArrayList<>();

        try {
            ResultSet resultSet = rsAnalysisDao.getAnalysisNames();
            while (resultSet.next()) {
                AnalysisDTO currentAnalysisDTO = new AnalysisDTO();
                currentAnalysisDTO.setAnalysisName(resultSet.getString("name"));
                currentAnalysisDTO.setAnalysisId(resultSet.getInt("analysis_id"));
                returnVal.add(currentAnalysisDTO);
            }

        } catch (SQLException e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
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
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

    @Override
    public AnalysisDTO replaceAnalysis(Integer analysisId, AnalysisDTO analysisDTO) throws GobiiDtoMappingException {

        AnalysisDTO returnVal = analysisDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
            parameters.put("analysisId", analysisId);
            rsAnalysisDao.updateAnalysis(parameters);

            if (null != analysisDTO.getParameters()) {
                upsertAnalysisProperties(analysisDTO.getAnalysisId(),
                        analysisDTO.getParameters());
            }

        } catch (Exception e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }
}
