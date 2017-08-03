package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.entity.pojos.Experiment;
import org.gobiiproject.gobiidao.resultset.access.RsExperimentDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidao.resultset.core.listquery.DtoListQueryColl;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidtomapping.DtoMapExperiment;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.headerlesscontainer.DataSetDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.ExperimentDTO;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Angel on 4/19/2016.
 */
public class DtoMapExperimentImpl implements DtoMapExperiment {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapNameIdListImpl.class);

    @Autowired
    private RsExperimentDao rsExperimentDao;

    @Autowired
    private DtoListQueryColl dtoListQueryColl;


    @Override
    public List<ExperimentDTO> getExperiments() throws GobiiDtoMappingException {

        List<ExperimentDTO> returnVal = new ArrayList<>();


        returnVal = (List<ExperimentDTO>) dtoListQueryColl.getList(ListSqlId.QUERY_ID_EXPERIMENT, null);


        return returnVal;
    }


    @Override
    public ExperimentDTO getExperimentDetails(Integer experimentId) throws GobiiDtoMappingException {


        ExperimentDTO returnVal = new ExperimentDTO();


        ResultSet resultSet = rsExperimentDao.getExperimentDetailsForExperimentId(experimentId);

        boolean retrievedOneRecord = false;

        try {
            while (resultSet.next()) {

                if (true == retrievedOneRecord) {
                    throw (new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.VALIDATION_NOT_UNIQUE,
                            "There are more than one project records for project id: " + experimentId));
                }

                retrievedOneRecord = true;

                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);
            }
        } catch (SQLException e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);

        }


        return returnVal;
    }

    private void validateExperimentRequest(ExperimentDTO experimentDTO) throws GobiiDtoMappingException {

        String experimentName = experimentDTO.getExperimentName();
        Integer projectId = experimentDTO.getProjectId();

        ResultSet resultSetExistingProject =
                rsExperimentDao.getExperimentsByNameProjectid(experimentName, projectId);

        try {
            if (resultSetExistingProject.next()) {

                throw new GobiiDtoMappingException(GobiiStatusLevel.VALIDATION,
                        GobiiValidationStatusType.VALIDATION_COMPOUND_UNIQUE,
                        "An experiment with name "
                                + experimentName
                                + " and project id "
                                + projectId);
            }
        } catch (SQLException e) {
            throw new GobiiDtoMappingException(e);
        }

    }

    @Override
    public ExperimentDTO createExperiment(ExperimentDTO experimentDTO) throws GobiiDtoMappingException {

        ExperimentDTO returnVal = experimentDTO;

        validateExperimentRequest(returnVal);
        Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
        Integer experimentId = rsExperimentDao.createExperiment(parameters);
        returnVal.setExperimentId(experimentId);

        return returnVal;
    }

    @Override
    public ExperimentDTO replaceExperiment(Integer experimentId, ExperimentDTO experimentDTO) throws
            GobiiDtoMappingException {

        ExperimentDTO returnVal = experimentDTO;

        Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
        parameters.put("experimentId", experimentId);
        rsExperimentDao.updateExperiment(parameters);


        return returnVal;

    }

    @Override
    public List<ExperimentDTO> getAlleleMatrices(Integer projectId) throws GobiiDtoMappingException {

        List<ExperimentDTO> returnVal = new ArrayList<>();

        try {

            ResultSet resultSet = rsExperimentDao.getAlleleMatrices(projectId);

            Integer currentExperimentId = null;

            ExperimentDTO currentExperimentDTO = new ExperimentDTO();

            boolean isFirst = true;

            while(resultSet.next()) {

                Integer experimentId = resultSet.getInt("experiment_id");

                if(currentExperimentId != experimentId) {

                    if(isFirst) {
                        isFirst = false;
                    } else {

                        returnVal.add(currentExperimentDTO);

                    }

                    currentExperimentDTO = new ExperimentDTO();
                    currentExperimentDTO.setExperimentName(resultSet.getString("experiment_name"));
                    currentExperimentDTO.setExperimentCode(resultSet.getString("code"));
                    currentExperimentDTO.setExperimentId(resultSet.getInt("experiment_id"));
                    currentExperimentDTO.setProjectId(resultSet.getInt("project_id"));

                    currentExperimentId = experimentId;

                }

                DataSetDTO dataSetDTO = new DataSetDTO();
                dataSetDTO.setExperimentId(resultSet.getInt("experiment_id"));
                dataSetDTO.setDataSetId(resultSet.getInt("matrixdbid"));
                dataSetDTO.setName(resultSet.getString("dataset_name"));
                dataSetDTO.setCallingAnalysisId(resultSet.getInt("callinganalysis_id"));

                currentExperimentDTO.getDatasets().add(dataSetDTO);

            }

            // add the last DTO
            returnVal.add(currentExperimentDTO);

            return returnVal;

        } catch (Exception e) {

            LOGGER.error("Gobii Mapping error", e);
            throw new GobiiDtoMappingException(e);

        }



    }
}
