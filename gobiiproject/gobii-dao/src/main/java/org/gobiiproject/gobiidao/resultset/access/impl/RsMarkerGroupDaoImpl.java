package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsMarkerGroupDao;
import org.gobiiproject.gobiidao.resultset.access.RsPlatformDao;
import org.gobiiproject.gobiidao.resultset.core.EntityPropertyParamNames;
import org.gobiiproject.gobiidao.resultset.core.SpRunnerCallable;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.*;
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
 * Created by Angel on 4/27/2016.
 */
public class RsMarkerGroupDaoImpl implements RsMarkerGroupDao {

    Logger LOGGER = LoggerFactory.getLogger(RsMarkerGroupDaoImpl.class);

    @Autowired
    private StoredProcExec storedProcExec = null;

    @Autowired
    private SpRunnerCallable spRunnerCallable;


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultSet getMarkerGroupNames() throws GobiiDaoException {

        ResultSet returnVal = null;

        try {

            SpGetMarkerGroupNames spGetMarkerGroupNames = new SpGetMarkerGroupNames();
            storedProcExec.doWithConnection(spGetMarkerGroupNames);
            returnVal = spGetMarkerGroupNames.getResultSet();

        } catch (SQLGrammarException e) {

            LOGGER.error("Error retrieving marker group names with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }


        return returnVal;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getMarkerGroupDetailByMarkerGroupId(Integer markerGroupId) throws GobiiDaoException {

        ResultSet returnVal = null;

        try {


            Map<String, Object> parameters = new HashMap<>();
            parameters.put("markerGroupId", markerGroupId);
            SpGetMarkerGroupDetailsByMarkerGroupId spGetMarkerGroupDetailsByMarkerGroupId = new SpGetMarkerGroupDetailsByMarkerGroupId(parameters);

            storedProcExec.doWithConnection(spGetMarkerGroupDetailsByMarkerGroupId);

            returnVal = spGetMarkerGroupDetailsByMarkerGroupId.getResultSet();

        } catch (SQLGrammarException  e) {

            LOGGER.error("Error retrieving marker group details with SQL " + e.getSQL() , e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return returnVal;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer createMarkerGroup(Map<String, Object> parameters) throws GobiiDaoException {

        Integer returnVal = null;

        try {

            spRunnerCallable.run(new SpInsMarkerGroup(), parameters);
            returnVal = spRunnerCallable.getResult();

        } catch (SQLGrammarException e) {

            LOGGER.error("Error creating marker group with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return returnVal;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getMarkersByMarkerName(String markerName) throws GobiiDaoException {

        ResultSet returnVal = null;

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("markerName", markerName);
            SpGetMarkersByMarkerName spGetMarkersByMarkerName = new SpGetMarkersByMarkerName(parameters);

            storedProcExec.doWithConnection(spGetMarkersByMarkerName);

            returnVal = spGetMarkersByMarkerName.getResultSet();

        } catch (SQLGrammarException  e) {

            LOGGER.error("Error retrieving markers with SQL " + e.getSQL() , e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return returnVal;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getMarkersByMarkerAndPlatformName(String markerName, String platformName) throws GobiiDaoException {

        ResultSet returnVal = null;

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("markerName", markerName);
            parameters.put("platformName", platformName);

            SpGetMarkersByMarkerAndPlatformName spGetMarkersByMarkerAndPlatformName = new SpGetMarkersByMarkerAndPlatformName(parameters);

            storedProcExec.doWithConnection(spGetMarkersByMarkerAndPlatformName);

            returnVal = spGetMarkersByMarkerAndPlatformName.getResultSet();


        } catch (SQLGrammarException e) {

            LOGGER.error("Error retrieving markers with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));
        }


        return returnVal;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getMarkerByMarkerId(Integer markerId) throws GobiiDaoException {
        ResultSet returnVal = null;

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("markerId", markerId);
            SpGetMarkersByMarkerId spGetMarkersByMarkerId = new SpGetMarkersByMarkerId(parameters);

            storedProcExec.doWithConnection(spGetMarkersByMarkerId);

            returnVal = spGetMarkersByMarkerId.getResultSet();

        } catch (SQLGrammarException  e) {

            LOGGER.error("Error retrieving markers with SQL " + e.getSQL() , e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return returnVal;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void createUpdateMarkerGroupMarker(Map<String, Object> parameters) throws GobiiDaoException {

        try {

            spRunnerCallable.run(new SpInsMarkerGroupMarkers(), parameters);


        } catch (SQLGrammarException  e) {

            LOGGER.error("Error updating marker group marker with SQL " + e.getSQL() , e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));
        }

    } // createUpdateMapSetProperty

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteMarkerGroupMarker(Map<String, Object> parameters) throws GobiiDaoException {

        try {

            spRunnerCallable.run(new SpDelMarkerGroupMarkerById(), parameters);
        } catch (SQLGrammarException  e) {

            LOGGER.error("Error updating marker group marker with SQL " + e.getSQL() , e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));
        }

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateMarkerGroup(Map<String, Object> parameters) throws GobiiDaoException {

        try {

            spRunnerCallable.run(new SpUpdMarkerGroup(), parameters);
        } catch (SQLGrammarException  e) {

            LOGGER.error("Error updating marker group with SQL " + e.getSQL() , e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));
        }

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateMarkerGroupName(Map<String, Object> parameters) throws GobiiDaoException {

        try {

            spRunnerCallable.run(new SpUpdMarkerGroupName(), parameters);


        } catch (SQLGrammarException e) {

            LOGGER.error("Error update marker group name with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));
        }

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getMarkersForMarkerGroup(Integer markerGroupId) throws GobiiDaoException {
        ResultSet returnVal = null;

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("markerGroupId", markerGroupId);
            SpGetMarkersForMarkerGroup spGetMarkersForMarkerGroup = new SpGetMarkersForMarkerGroup(parameters);
            storedProcExec.doWithConnection(spGetMarkersForMarkerGroup);
            returnVal = spGetMarkersForMarkerGroup.getResultSet();

        } catch (SQLGrammarException  e) {

            LOGGER.error("Error retrieving marker group markers with SQL " + e.getSQL() , e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }


        return returnVal;
    }


}
