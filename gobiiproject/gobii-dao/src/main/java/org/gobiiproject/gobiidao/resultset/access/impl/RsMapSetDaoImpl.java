package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsMapSetDao;
import org.gobiiproject.gobiidao.resultset.core.EntityPropertyParamNames;
import org.gobiiproject.gobiidao.resultset.core.SpRunnerCallable;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpInsMapsetProperties;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpInsMapset;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpUpdMapset;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.*;
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
public class RsMapSetDaoImpl implements RsMapSetDao {

    Logger LOGGER = LoggerFactory.getLogger(RsMapSetDaoImpl.class);

    @Autowired
    private SpRunnerCallable spRunnerCallable;


    @Autowired
    private StoredProcExec storedProcExec = null;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultSet getMapNames() throws GobiiDaoException {
        ResultSet returnVal = null;

        try {
            SpGetMapSetNames spGetMapSetNames = new SpGetMapSetNames();
            storedProcExec.doWithConnection(spGetMapSetNames);
            returnVal = spGetMapSetNames.getResultSet();
        } catch (Exception e) {

            LOGGER.error("Error retrieving mapset names", e);
            throw (new GobiiDaoException(e));

        }


        return returnVal;

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultSet getMapNamesByTypeId(int typeId) throws GobiiDaoException {
        // TODO Auto-generated method stub

        ResultSet returnVal = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("typeId", typeId);
            SpGetMapNamesByTypeId spGetMapNamesByTypeId = new SpGetMapNamesByTypeId(parameters);

            storedProcExec.doWithConnection(spGetMapNamesByTypeId);

            returnVal = spGetMapNamesByTypeId.getResultSet();

        } catch (Exception e) {

            LOGGER.error("Error retrieving map names", e);
            throw (new GobiiDaoException(e));

        }

        return returnVal;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getMapsetDetailsByMapsetId(int mapSetId) throws GobiiDaoException {
        ResultSet returnVal = null;

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("mapSetId", mapSetId);
            SpGetMapsetDetailsByMapsetId spGetMapsetDetailsByMapsetId = new SpGetMapsetDetailsByMapsetId(parameters);

            storedProcExec.doWithConnection(spGetMapsetDetailsByMapsetId);

            returnVal = spGetMapsetDetailsByMapsetId.getResultSet();

        } catch (Exception e) {

            LOGGER.error("Error retrieving mapset details", e);
            throw (new GobiiDaoException(e));

        }

        return returnVal;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer createMapset(Map<String, Object> parameters) throws GobiiDaoException {

        Integer returnVal = null;

        try {

            if (spRunnerCallable.run(new SpInsMapset(), parameters)) {

                returnVal = spRunnerCallable.getResult();

            } else {

                throw new GobiiDaoException(spRunnerCallable.getErrorString());

            }

        } catch (Exception e) {

            LOGGER.error("Error creating contact", e);
            throw (new GobiiDaoException(e));

        }

        return returnVal;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateMapset(Map<String, Object> parameters) throws GobiiDaoException {

        try {

            if (!spRunnerCallable.run(new SpUpdMapset(), parameters)) {
                throw new GobiiDaoException(spRunnerCallable.getErrorString());
            }

        } catch (Exception e) {

            LOGGER.error("Error creating mapset", e);
            throw (new GobiiDaoException(e));
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer createUpdateMapSetProperty(Map<String, Object> parameters) throws GobiiDaoException {

        Integer returnVal = null;

        try {

            spRunnerCallable.run(new SpInsMapsetProperties(), parameters);
            returnVal = spRunnerCallable.getResult();

        } catch (Exception e) {

            LOGGER.error("Error updating project property", e);
            throw (new GobiiDaoException(e));

        }

        return returnVal;
    } // createUpdateMapSetProperty

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getProperties(Integer mapSetId) throws GobiiDaoException {
        ResultSet returnVal = null;

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put(EntityPropertyParamNames.PROPPCOLARAMNAME_ENTITY_ID, mapSetId);
            SpGetPropertiesForMapset spGetPropertiesForMapset = new SpGetPropertiesForMapset(parameters);
            storedProcExec.doWithConnection(spGetPropertiesForMapset);
            returnVal = spGetPropertiesForMapset.getResultSet();

        } catch (Exception e) {

            LOGGER.error("Error retrieving project properties", e);
            throw (new GobiiDaoException(e));

        }

        return returnVal;
    }
}
