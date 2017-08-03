package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsExperimentDao;
import org.gobiiproject.gobiidao.resultset.core.SpRunnerCallable;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpInsExperiment;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpUpdExperiment;
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
import java.util.Optional;

/**
 * Created by Angel on 4/19/2016.
 */
public class RsExperimentDaoImpl implements RsExperimentDao {


    Logger LOGGER = LoggerFactory.getLogger(RsExperimentDao.class);

    @Autowired
    private StoredProcExec storedProcExec = null;

    @Autowired
    private SpRunnerCallable spRunnerCallable;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getExperimentNamesByProjectId(Integer projectId) throws GobiiDaoException {

        ResultSet returnVal = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("projectId", projectId);
            SpGetExperimentNamesByProjectId spGetExperimentNamesByProjectId = new SpGetExperimentNamesByProjectId(parameters);

            storedProcExec.doWithConnection(spGetExperimentNamesByProjectId);

            returnVal = spGetExperimentNamesByProjectId.getResultSet();

        } catch (SQLGrammarException e) {
            LOGGER.error("Error getting experiment names by project id  with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return returnVal;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getExperimentDetailsForExperimentId(int experimentId) throws GobiiDaoException {

        ResultSet returnVal = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("experimentId", experimentId);
            SpGetExperimentDetailsByExperimentId spGetExperimentDetailsByExperimentId = new SpGetExperimentDetailsByExperimentId(parameters);
            storedProcExec.doWithConnection(spGetExperimentDetailsByExperimentId);
            returnVal = spGetExperimentDetailsByExperimentId.getResultSet();

        } catch (SQLGrammarException e) {
            LOGGER.error("Error getting experiment details by experiment id with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return returnVal;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getExperimentNames() throws GobiiDaoException {
        // TODO Auto-generated method stub

        ResultSet returnVal = null;

        try {
            SpGetExperimentNames spGetExperimentNames = new SpGetExperimentNames();
            storedProcExec.doWithConnection(spGetExperimentNames);
            returnVal = spGetExperimentNames.getResultSet();

        } catch (SQLGrammarException e) {
            LOGGER.error("Error getting all experiment names with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return returnVal;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer createExperiment(Map<String, Object> parameters) throws GobiiDaoException {
        Integer returnVal = null;

        try {

            spRunnerCallable.run(new SpInsExperiment(), parameters);
            returnVal = spRunnerCallable.getResult();

        } catch (SQLGrammarException e) {
            LOGGER.error("Error creating dataset with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return returnVal;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateExperiment(Map<String, Object> parameters) throws GobiiDaoException {

        try {
            spRunnerCallable.run(new SpUpdExperiment(), parameters);

        } catch (SQLGrammarException e) {
            LOGGER.error("Error updating experiment with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getExperimentsByNameProjectid(String experimentName, Integer projectId) throws GobiiDaoException {
        ResultSet returnVal = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("experimentName", experimentName);
            parameters.put("projectId", projectId);
            SpGetExperimentByNameProjectId spGetExperimentByNameProjectId = new SpGetExperimentByNameProjectId(parameters);

            storedProcExec.doWithConnection(spGetExperimentByNameProjectId);

            returnVal = spGetExperimentByNameProjectId.getResultSet();

        } catch (SQLGrammarException e) {
            LOGGER.error("Error getting experiment by name and project id with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return returnVal;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getAlleleMatrices(Integer projectId) throws GobiiDaoException {

        ResultSet returnVal = null;

        try {

            if (projectId != null) {

                Map<String, Object> parameters = new HashMap<>();
                parameters.put("projectId", projectId);

                SpGetAlleleMatricesByStudyId spGetAlleleMatricesByStudyId = new SpGetAlleleMatricesByStudyId(parameters);

                storedProcExec.doWithConnection(spGetAlleleMatricesByStudyId);

                returnVal = spGetAlleleMatricesByStudyId.getResultSet();

            } else {

                SpGetAlleleMatrices spGetAlleleMatrices = new SpGetAlleleMatrices();

                storedProcExec.doWithConnection(spGetAlleleMatrices);

                returnVal = spGetAlleleMatrices.getResultSet();
            }


        } catch (SQLGrammarException e) {

            LOGGER.error("Error retrieving allele matrices with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));
        }


        return returnVal;

    }


} // RsProjectDaoImpl
