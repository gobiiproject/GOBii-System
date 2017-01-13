package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsDataSetDao;
import org.gobiiproject.gobiidao.resultset.core.SpRunnerCallable;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpInsDataSet;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpUpdDataSet;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.*;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phil on 4/8/2016.
 */
public class RsDataSetDaoImpl implements RsDataSetDao {

    Logger LOGGER = LoggerFactory.getLogger(RsDataSetDaoImpl.class);


    @Autowired
    private StoredProcExec storedProcExec = null;

    @Autowired
    private SpRunnerCallable spRunnerCallable;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getDatasetNamesByExperimentId(Integer experimentId) throws GobiiDaoException {

        ResultSet returnVal = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("experimentId", experimentId);
            SpGetDatasetNamesByExperimentId spGetDatasetNamesByExperimentId = new SpGetDatasetNamesByExperimentId(parameters);

            storedProcExec.doWithConnection(spGetDatasetNamesByExperimentId);

            returnVal = spGetDatasetNamesByExperimentId.getResultSet();

        } catch (Exception e) {

            LOGGER.error("Error retrieving dataset file names", e);
            throw (new GobiiDaoException(e));

        }

        return returnVal;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getDataSetDetailsByDataSetId(Integer dataSetId) throws GobiiDaoException {

        ResultSet returnVal = null;

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("dataSetId", dataSetId);
            SpGetDatasetDetailsByDataSetId spGetDatasetDetailsByExperimentId = new SpGetDatasetDetailsByDataSetId(parameters);

            storedProcExec.doWithConnection(spGetDatasetDetailsByExperimentId);

            returnVal = spGetDatasetDetailsByExperimentId.getResultSet();

        } catch (Exception e) {

            LOGGER.error("Error retrieving dataset details", e);
            throw (new GobiiDaoException(e));

        }

        return returnVal;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer createDataset(Map<String, Object> parameters) throws GobiiDaoException {

        Integer returnVal = null;

        try {

            spRunnerCallable.run(new SpInsDataSet(), parameters);

            returnVal = spRunnerCallable.getResult();

        } catch (SQLGrammarException e) {

            LOGGER.error("Error creating dataset with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return returnVal;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateDataSet(Map<String, Object> parameters) throws GobiiDaoException {
        try {

            spRunnerCallable.run(new SpUpdDataSet(), parameters);
        } catch (SQLGrammarException e) {

            LOGGER.error("Error creating dataSet with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));
        }

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getDatasetNames() throws GobiiDaoException {
        // TODO Auto-generated method stub

        ResultSet returnVal = null;

        try {
            SpGetDatasetNames spGetDatasetNames = new SpGetDatasetNames();

            storedProcExec.doWithConnection(spGetDatasetNames);

            returnVal = spGetDatasetNames.getResultSet();

        } catch (SQLGrammarException e) {

            LOGGER.error("Error retrieving dataset file names with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return returnVal;
    }


} // RsProjectDaoImpl
