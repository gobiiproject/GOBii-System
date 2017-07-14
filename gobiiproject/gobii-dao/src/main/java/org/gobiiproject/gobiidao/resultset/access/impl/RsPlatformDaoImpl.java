package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsPlatformDao;
import org.gobiiproject.gobiidao.resultset.core.EntityPropertyParamNames;
import org.gobiiproject.gobiidao.resultset.core.SpRunnerCallable;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.SpInsPlatformProperties;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpInsPlatform;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpUpdPlatform;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.*;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetPlatformDetailsByPlatformId;
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
 * Created by Phil on 4/7/2016.
 */
public class RsPlatformDaoImpl implements RsPlatformDao {

    Logger LOGGER = LoggerFactory.getLogger(RsPlatformDaoImpl.class);

    @Autowired
    private StoredProcExec storedProcExec = null;

    @Autowired
    private SpRunnerCallable spRunnerCallable;


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultSet getPlatformNames() throws GobiiDaoException {

        ResultSet returnVal = null;

        try {

            SpGetPlatformNames spGetPlatformNames = new SpGetPlatformNames();
            storedProcExec.doWithConnection(spGetPlatformNames);
            returnVal = spGetPlatformNames.getResultSet();

        } catch (SQLGrammarException e) {
            LOGGER.error("Error getting platform names with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }


        return returnVal;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getPlatformNamesByTypeId(Integer typeId) throws GobiiDaoException {

        ResultSet returnVal = null;

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("typeId", typeId);
            SpGetPlatformNamesByTypeId spGetPlatformNamesByTypeId = new SpGetPlatformNamesByTypeId(parameters);

            storedProcExec.doWithConnection(spGetPlatformNamesByTypeId);

            returnVal = spGetPlatformNamesByTypeId.getResultSet();

        } catch (SQLGrammarException e) {
            LOGGER.error("Error getting platform names by type id with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return returnVal;

    } // getPlatformDetailsByPlatformId()

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getPlatformDetailsByPlatformId(Integer platformId) throws GobiiDaoException {

        ResultSet returnVal = null;

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("platformId", platformId);
            SpGetPlatformDetailsByPlatformId spGetPlatformDetailsByPlatformId = new SpGetPlatformDetailsByPlatformId(parameters);

            storedProcExec.doWithConnection(spGetPlatformDetailsByPlatformId);

            returnVal = spGetPlatformDetailsByPlatformId.getResultSet();

        } catch (SQLGrammarException e) {
            LOGGER.error("Error getting platform details by platform id with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return returnVal;

    } // getPlatformDetailsByPlatformId()

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer createPlatform(Map<String, Object> parameters) throws GobiiDaoException {

        Integer returnVal = null;

        try {

            spRunnerCallable.run(new SpInsPlatform(), parameters);
            returnVal = spRunnerCallable.getResult();

        } catch (SQLGrammarException e) {
            LOGGER.error("Error creating platform with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return returnVal;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updatePlatform(Map<String, Object> parameters) throws GobiiDaoException {

        try {

            spRunnerCallable.run(new SpUpdPlatform(), parameters);

        } catch (SQLGrammarException e) {
            LOGGER.error("Error updating platform with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer createUpdatePlatformProperty(Map<String, Object> parameters) throws GobiiDaoException {

        Integer returnVal = null;

        try {

            spRunnerCallable.run(new SpInsPlatformProperties(), parameters);
            returnVal = spRunnerCallable.getResult();

        } catch (SQLGrammarException e) {
            LOGGER.error("Error updating platform property with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }
        return returnVal;

    } // createUpdatePlatformProperty

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getProperties(Integer platformId) throws GobiiDaoException {
        ResultSet returnVal = null;

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put(EntityPropertyParamNames.PROPPCOLARAMNAME_ENTITY_ID, platformId);
            SpGetPropertiesForPlatform spGetPropertiesForPlatform = new SpGetPropertiesForPlatform(parameters);
            storedProcExec.doWithConnection(spGetPropertiesForPlatform);
            returnVal = spGetPropertiesForPlatform.getResultSet();

        } catch (SQLGrammarException e) {
            LOGGER.error("Error retrieving platform properties with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return returnVal;
    }
}
