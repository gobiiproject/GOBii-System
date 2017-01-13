package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsMarkerGroupDao;
import org.gobiiproject.gobiidao.resultset.access.RsPlatformDao;
import org.gobiiproject.gobiidao.resultset.core.EntityPropertyParamNames;
import org.gobiiproject.gobiidao.resultset.core.SpRunnerCallable;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpDelMarkerGroupMarkerById;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpInsAnalysis;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpInsAnalysisParameters;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpInsMarkerGroup;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpInsMarkerGroupMarkers;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpUpdAnalysis;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpUpdMarkerGroup;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetAnalysisDetailsByAnalysisId;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetMarkerGroupDetailsByMarkerGroupId;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetMarkerGroupNames;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetMarkersByMarkerId;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetMarkersByMarkerName;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetMarkersForMarkerGroup;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetPlatformNames;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetPropertiesForAnalysis;
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

        } catch (Exception e) {

            LOGGER.error("Error retrieving marker group names", e);
            throw (new GobiiDaoException(e));

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

        } catch (Exception e) {

            LOGGER.error("Error retrieving marker group details", e);
            throw (new GobiiDaoException(e));

        }

        return returnVal;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer createMarkerGroup(Map<String, Object> parameters) throws GobiiDaoException {

        Integer returnVal = null;

        try {

            if (spRunnerCallable.run(new SpInsMarkerGroup(), parameters)) {

                returnVal = spRunnerCallable.getResult();

            } else {

                throw new GobiiDaoException(spRunnerCallable.getErrorString());

            }

        } catch (Exception e) {

            LOGGER.error("Error creating marker group", e);
            throw (new GobiiDaoException(e));

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

        } catch (Exception e) {

            LOGGER.error("Error retrieving markers", e);
            throw (new GobiiDaoException(e));

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

        } catch (Exception e) {

            LOGGER.error("Error retrieving markers", e);
            throw (new GobiiDaoException(e));

        }

        return returnVal;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void createUpdateMarkerGroupMarker(Map<String, Object> parameters) throws GobiiDaoException {

        try {

            if( ! spRunnerCallable.run(new SpInsMarkerGroupMarkers(), parameters) ) {
                throw new GobiiDaoException(spRunnerCallable.getErrorString());

            }

        } catch (Exception e) {

            LOGGER.error("Error updating marker group marker", e);
            throw (new GobiiDaoException(e));
        }

    } // createUpdateMapSetProperty

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteMarkerGroupMarker(Map<String, Object> parameters) throws GobiiDaoException {
        try {

            if( ! spRunnerCallable.run(new SpDelMarkerGroupMarkerById(), parameters) ) {
                throw new GobiiDaoException(spRunnerCallable.getErrorString());

            }

        } catch (Exception e) {

            LOGGER.error("Error updating marker group marker", e);
            throw (new GobiiDaoException(e));
        }

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateMarkerGroup(Map<String, Object> parameters) throws GobiiDaoException {
        try {

            if (!spRunnerCallable.run(new SpUpdMarkerGroup(), parameters)) {
                throw new GobiiDaoException(spRunnerCallable.getErrorString());
            }

        } catch (Exception e) {

            LOGGER.error("Error updating marker group", e);
            throw (new GobiiDaoException(e));
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

        } catch (Exception e) {

            LOGGER.error("Error retrieving marker group markers", e);
            throw (new GobiiDaoException(e));

        }


        return returnVal;    }


}
