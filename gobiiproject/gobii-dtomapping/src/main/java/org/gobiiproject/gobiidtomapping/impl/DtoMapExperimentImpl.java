package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsExperimentDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.DtoMapExperiment;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.container.EntityPropertyDTO;
import org.gobiiproject.gobiimodel.dto.container.ExperimentDTO;
import org.gobiiproject.gobiimodel.dto.container.ProjectDTO;
import org.gobiiproject.gobiimodel.dto.header.DtoHeaderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by Angel on 4/19/2016.
 */
public class DtoMapExperimentImpl implements DtoMapExperiment {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapNameIdListImpl.class);

    @Autowired
    private RsExperimentDao rsExperimentDao;

    public ExperimentDTO getExperiment(ExperimentDTO experimentDTO) throws GobiiDtoMappingException {


        ExperimentDTO returnVal = new ExperimentDTO();

        try {

            ResultSet resultSet = rsExperimentDao.getExperimentDetailsForExperimentId(experimentDTO.getExperimentId());

            boolean retrievedOneRecord = false;
            while (resultSet.next()) {

                if (true == retrievedOneRecord) {
                    throw (new GobiiDtoMappingException(DtoHeaderResponse.StatusLevel.ERROR,
                            DtoHeaderResponse.ValidationStatusType.VALIDATION_NOT_UNIQUE,
                            "There are more than one project records for project id: " + experimentDTO.getExperimentId()));
                }

                retrievedOneRecord = true;

                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);
            }

        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;
    }

    private boolean validateExperimentRequest(ExperimentDTO experimentDTO) throws Exception {

        boolean returnVal = true;

            String experimentName = experimentDTO.getExperimentName();
            Integer projectId = experimentDTO.getProjectId();
            Integer platformId = experimentDTO.getPlatformId();

            ResultSet resultSetExistingProject =
                    rsExperimentDao.getExperimentsByNameProjectidPlatformId(experimentName, projectId, platformId);

            if (resultSetExistingProject.next()) {

                returnVal = false;
                experimentDTO.getDtoHeaderResponse().addStatusMessage(DtoHeaderResponse.StatusLevel.OK,
                        DtoHeaderResponse.ValidationStatusType.VALIDATION_COMPOUND_UNIQUE,
                        "An experiment with name "
                                + experimentName
                                + " and project id "
                                + projectId
                                + "and platform id"
                                + platformId
                                + "already exists");
            }

        return returnVal;

    }

    @Override
    public ExperimentDTO createExperiment(ExperimentDTO experimentDTO) throws GobiiDtoMappingException {

        ExperimentDTO returnVal = experimentDTO;

        try {

            if (validateExperimentRequest(returnVal)) {
                Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
                Integer experimentId = rsExperimentDao.createExperiment(parameters);
                returnVal.setExperimentId(experimentId);
            }

        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;
    }

    @Override
    public ExperimentDTO updateExperiment(ExperimentDTO experimentDTO) throws GobiiDtoMappingException {

        ExperimentDTO returnVal = experimentDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
            rsExperimentDao.updateExperiment(parameters);

        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;

    }
}
