package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.resultset.access.RsExperimentDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidao.resultset.core.listquery.DtoListQueryColl;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidtomapping.DtoMapExperiment;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.headerlesscontainer.ExperimentDTO;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
}
