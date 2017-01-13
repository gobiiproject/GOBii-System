package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsAnalysisDao;
import org.gobiiproject.gobiidao.resultset.access.RsDataSetDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.DtoMapAnalysis;
import org.gobiiproject.gobiidtomapping.DtoMapDataSet;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.container.DataSetDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Phil on 4/21/2016.
 */
public class DtoMapDataSetImpl implements DtoMapDataSet {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapDataSetImpl.class);


    @Autowired
    private RsDataSetDao rsDataSetDao;

    @Autowired
    private RsAnalysisDao rsAnalysisDao;

    @Autowired
    private DtoMapAnalysis dtoMapAnalysis;

    @Transactional
    @Override
    public DataSetDTO getDataSetDetails(DataSetDTO dataSetDTO) throws GobiiDtoMappingException {

        DataSetDTO returnVal = dataSetDTO;

        try {

            ResultSet resultSet = rsDataSetDao.getDataSetDetailsByDataSetId(dataSetDTO.getDataSetId());

            if (resultSet.next()) {

                // apply dataset values
                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);


//                // create calling analysis from annotations
//                if (null != returnVal.getCallingAnalysisId()) {
//
//                    ResultSet callingAnalysisResultSet =
//                            rsAnalysisDao.getAnalysisDetailsByAnalysisId(returnVal.getCallingAnalysisId());
//                    if (callingAnalysisResultSet.next()) {
//                        AnalysisDTO callingAnalysisDTO = new AnalysisDTO();
//                        ResultColumnApplicator.applyColumnValues(callingAnalysisResultSet, callingAnalysisDTO);
//                    }
//                }
//
//                // create analyses
//                for (Integer currentAnalysisId : returnVal.getAnalysesIds()) {
//                    ResultSet currentAnalysisResultSet = rsAnalysisDao.getAnalysisDetailsByAnalysisId(currentAnalysisId);
//                    if (currentAnalysisResultSet.next()) {
//                        AnalysisDTO currentAnalysisDTO = new AnalysisDTO();
//                        ResultColumnApplicator.applyColumnValues(currentAnalysisResultSet, currentAnalysisDTO);
//                        returnVal.getAnalyses().add(currentAnalysisDTO);
//                    }
//                }

            }

        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;

    }

    @Override
    public DataSetDTO createDataset(DataSetDTO dataSetDTO) throws GobiiDtoMappingException {
        DataSetDTO returnVal = dataSetDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(dataSetDTO);
            Integer datasetId = rsDataSetDao.createDataset(parameters);
            returnVal.setDataSetId(datasetId);

        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;
    }

    @Override
    public DataSetDTO updateDataset(DataSetDTO dataSetDTO) throws GobiiDtoMappingException {

        DataSetDTO returnVal = dataSetDTO;

        try {


//            for (AnalysisDTO currentAnalysisDTO : returnVal.getAnalyses()) {
//
//                if(DtoMetaData.ProcessType.CREATE == currentAnalysisDTO.getProcessType() ) {
//                    dtoMapAnalysis.createAnalysis(currentAnalysisDTO);
//                    returnVal.getAnalysesIds().add(currentAnalysisDTO.getAnalysisId());
//
//                }
//            }


            Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
            rsDataSetDao.updateDataSet(parameters);


        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;
    }


}
