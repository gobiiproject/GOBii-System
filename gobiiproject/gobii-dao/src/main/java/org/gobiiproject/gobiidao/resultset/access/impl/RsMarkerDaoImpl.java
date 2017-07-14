package org.gobiiproject.gobiidao.resultset.access.impl;

import org.apache.commons.lang.NotImplementedException;
import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsMarkerDao;
import org.gobiiproject.gobiidao.resultset.core.SpRunnerCallable;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpInsMarker;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetMarkersByMarkerId;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetMarkersByMarkerName;
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
public class RsMarkerDaoImpl implements RsMarkerDao {

    Logger LOGGER = LoggerFactory.getLogger(RsMarkerDaoImpl.class);


    @Autowired
    private StoredProcExec storedProcExec = null;

    @Autowired
    private SpRunnerCallable spRunnerCallable;


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getMarkerDetailsByMarkerId(Integer markerId) throws GobiiDaoException {


        ResultSet returnVal;

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("markerId", markerId);
            SpGetMarkersByMarkerId spGetMarkerDetailsByExperimentId = new SpGetMarkersByMarkerId(parameters);
            storedProcExec.doWithConnection(spGetMarkerDetailsByExperimentId);
            returnVal = spGetMarkerDetailsByExperimentId.getResultSet();

        } catch (SQLGrammarException e) {
            LOGGER.error("Error retrieving marker detail with SQL " + e.getSQL(), e.getSQLException());
            throw new GobiiDaoException(e.getSQLException());
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
            SpGetMarkersByMarkerName spGetMarkerDetailsByExperimentId = new SpGetMarkersByMarkerName(parameters);

            storedProcExec.doWithConnection(spGetMarkerDetailsByExperimentId);

            returnVal = spGetMarkerDetailsByExperimentId.getResultSet();

        } catch (SQLGrammarException e) {
            LOGGER.error("Error retrieving markers by marker name  SQL " + e.getSQL(), e.getSQLException());
            throw new GobiiDaoException(e.getSQLException());
        }

        return returnVal;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer createMarker(Map<String, Object> parameters) throws GobiiDaoException {

        Integer returnVal = null;

        try {

            spRunnerCallable.run(new SpInsMarker(), parameters);
            returnVal = spRunnerCallable.getResult();

        } catch (SQLGrammarException e) {
            LOGGER.error("Error creating marker with SQL " + e.getSQL(), e.getSQLException());
            throw new GobiiDaoException(e.getSQLException());
        }

        return returnVal;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateMarker(Map<String, Object> parameters) throws GobiiDaoException {

        throw new NotImplementedException();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getMarkers() throws GobiiDaoException {
        // TODO Auto-generated method stub

        throw new NotImplementedException();
    }


} //
