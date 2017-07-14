package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsAnalysisDao;
import org.gobiiproject.gobiidao.resultset.core.EntityPropertyParamNames;
import org.gobiiproject.gobiidao.resultset.core.SpRunnerCallable;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpInsAnalysis;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpInsAnalysisParameters;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpInsProject;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpInsProjectProperties;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpUpdAnalysis;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpUpdExperiment;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetAnalysisDetailsByAnalysisId;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetAnalysisNames;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetAnalysisNamesByTypeId;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetCvTermsByGroup;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetPlatformNames;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetPropertiesForAnalysis;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetPropertiesForProject;
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
 * Created by Phil on 4/22/2016.
 */
public class RsAnalysisDaoImpl implements RsAnalysisDao {


    Logger LOGGER = LoggerFactory.getLogger(RsAnalysisDaoImpl.class);

    @Autowired
    private StoredProcExec storedProcExec = null;

    @Autowired
    private SpRunnerCallable spRunnerCallable;

    @Transactional
    @Override
    public ResultSet getAnalysisDetailsByAnalysisId(Integer analysisId) throws GobiiDaoException {

        ResultSet returnVal = null;

        try {


            Map<String, Object> parameters = new HashMap<>();
            parameters.put("analysisId", analysisId);
            SpGetAnalysisDetailsByAnalysisId spGetDatasetDetailsByExperimentId = new SpGetAnalysisDetailsByAnalysisId(parameters);

            storedProcExec.doWithConnection(spGetDatasetDetailsByExperimentId);

            returnVal = spGetDatasetDetailsByExperimentId.getResultSet();

        } catch (SQLGrammarException e) {

            LOGGER.error("Error retrieving analysis details", e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return returnVal;

    } // getAnalysisDetailsByAnalysisId()

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultSet getAnalysisNames() {

        ResultSet returnVal = null;

        try {

            SpGetAnalysisNames spGetAnalysisNames = new SpGetAnalysisNames();
            storedProcExec.doWithConnection(spGetAnalysisNames);
            returnVal = spGetAnalysisNames.getResultSet();

        } catch (SQLGrammarException e) {

            LOGGER.error("Error retrieving analysis names", e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return returnVal;

    } // getAnalysisNames

    @Transactional
    @Override
    public Integer createAnalysis(Map<String, Object> parameters) throws GobiiDaoException {

        Integer returnVal = null;

        try {

            spRunnerCallable.run(new SpInsAnalysis(), parameters);

            returnVal = spRunnerCallable.getResult();

        } catch (SQLGrammarException e) {

            LOGGER.error("Error creating analysis with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));
        }

        return returnVal;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateAnalysis(Map<String, Object> parameters) throws GobiiDaoException {

        try {

            spRunnerCallable.run(new SpUpdAnalysis(), parameters);

        } catch (SQLGrammarException e) {

            LOGGER.error("Error updating analysis with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void createUpdateParameter(Map<String, Object> parameters) throws GobiiDaoException {

        try {
            spRunnerCallable.run(new SpInsAnalysisParameters(), parameters);

        } catch (SQLGrammarException e) {

            LOGGER.error("Error updating project property with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));
        }
    } //

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getParameters(Integer analysisId) throws GobiiDaoException {
        ResultSet returnVal = null;

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put(EntityPropertyParamNames.PROPPCOLARAMNAME_ENTITY_ID, analysisId);
            SpGetPropertiesForAnalysis spGetPropertiesForAnalysis = new SpGetPropertiesForAnalysis(parameters);
            storedProcExec.doWithConnection(spGetPropertiesForAnalysis);
            returnVal = spGetPropertiesForAnalysis.getResultSet();

        } catch (SQLGrammarException e) {

            LOGGER.error("Error retrieving project properties with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return returnVal;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getAnalysisNamesByTypeId(int typeId) throws GobiiDaoException {
        // TODO Auto-generated method stub

        ResultSet returnVal = null;

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("typeId", typeId);
            SpGetAnalysisNamesByTypeId spGetAnalysisNamesByTypeId = new SpGetAnalysisNamesByTypeId(parameters);

            storedProcExec.doWithConnection(spGetAnalysisNamesByTypeId);

            returnVal = spGetAnalysisNamesByTypeId.getResultSet();

        } catch (SQLGrammarException e) {

            LOGGER.error("Error retrieving Analysis Names by type with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }


        return returnVal;
    }
}
