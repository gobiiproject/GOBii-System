package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.resultset.access.RsAnalysisDao;
import org.gobiiproject.gobiidao.resultset.access.RsDataSetDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidao.resultset.core.listquery.DtoListQueryColl;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidtomapping.DtoMapAnalysis;
import org.gobiiproject.gobiidtomapping.DtoMapDataSet;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.headerlesscontainer.DataSetDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.ProjectDTO;
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
 * Created by Phil on 4/21/2016.
 */
public class DtoMapDataSetImpl implements DtoMapDataSet {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapDataSetImpl.class);


    @Autowired
    private RsDataSetDao rsDataSetDao;

    @Autowired
    private DtoListQueryColl dtoListQueryColl;

    @SuppressWarnings("unchecked")
    @Override
    public List<DataSetDTO> getDataSets() throws GobiiDtoMappingException {

        List<DataSetDTO> returnVal = new ArrayList<>();

        try {

            returnVal = (List<DataSetDTO>) dtoListQueryColl.getList(ListSqlId.QUERY_ID_DATASET_ALL,null);

        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }    

    @Transactional
    @Override
    public DataSetDTO getDataSetDetails(Integer dataSetId) throws GobiiDtoMappingException {

        DataSetDTO returnVal = new DataSetDTO();


        try {
            ResultSet resultSet = rsDataSetDao.getDataSetDetailsByDataSetId(dataSetId);

            if (resultSet.next()) {

                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);
            }
        } catch(SQLException e) {
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;

    }

    @Override
    public DataSetDTO createDataSet(DataSetDTO dataSetDTO) throws GobiiDtoMappingException {

        DataSetDTO returnVal = dataSetDTO;


        Map<String, Object> parameters = ParamExtractor.makeParamVals(dataSetDTO);
        Integer datasetId = rsDataSetDao.createDataset(parameters);
        returnVal.setDataSetId(datasetId);

        return returnVal;
    }

    @Override
    public DataSetDTO replaceDataSet(Integer dataSetId, DataSetDTO dataSetDTO) throws GobiiDtoMappingException {

        DataSetDTO returnVal = dataSetDTO;


        Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
        parameters.put("projectId", dataSetId);
        rsDataSetDao.updateDataSet(parameters);


        return returnVal;
    }


}
