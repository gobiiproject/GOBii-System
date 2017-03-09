package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsProtocolDao;

import org.gobiiproject.gobiidao.resultset.core.SpRunnerCallable;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpInsProtocol;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpInsVendorProtocol;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpUpdProtocol;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpUpdVendorProtocol;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.*;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by VCalaminos on 2016-12-14.
 */
public class RsProtocolDaoImpl implements RsProtocolDao {

    Logger LOGGER = LoggerFactory.getLogger(RsProtocolDao.class);

    @Autowired
    private StoredProcExec storedProcExec = null;

    @Autowired
    private SpRunnerCallable spRunnerCallable;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer createProtocol(Map<String, Object> paramaters) throws GobiiDaoException {
        Integer returnVal = null;

        try {
            spRunnerCallable.run(new SpInsProtocol(), paramaters);
            returnVal = spRunnerCallable.getResult();
        } catch (SQLGrammarException e) {
            LOGGER.error("Error creating dataset with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));
        }

        return returnVal;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateProtocol(Map<String, Object> parameters) throws GobiiDaoException {

        try {

            spRunnerCallable.run(new SpUpdProtocol(), parameters);

        } catch (SQLGrammarException e) {
            LOGGER.error("Error updating protocol with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));
        }

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateVendorProtocol(Map<String, Object> parameters) throws GobiiDaoException {
        try {

            spRunnerCallable.run(new SpUpdVendorProtocol(), parameters);

        } catch (SQLGrammarException e) {
            LOGGER.error("Error updating protocol with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));
        }
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getProtocolDetailsByProtocolId(Integer protocolId) throws GobiiDaoException {

        ResultSet returnVal = null;

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("protocolId", protocolId);
            SpGetProtocolDetailsByProtocolId spGetProtocolDetailsByProtocolId = new SpGetProtocolDetailsByProtocolId(parameters);

            storedProcExec.doWithConnection(spGetProtocolDetailsByProtocolId);

            returnVal = spGetProtocolDetailsByProtocolId.getResultSet();

        } catch (SQLGrammarException e) {

            LOGGER.error("Error retrieving protocol details", e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return returnVal;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getProtocolDetailsByExperimentId(Integer experimentId) throws GobiiDaoException {

        ResultSet returnVal = null;

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("experimentId", experimentId);

            SpGetProtocolDetailsByExperimentId spGetProtocolDetailsByExperimentId = new SpGetProtocolDetailsByExperimentId(parameters);

            storedProcExec.doWithConnection(spGetProtocolDetailsByExperimentId);

            returnVal = spGetProtocolDetailsByExperimentId.getResultSet();

        } catch (SQLGrammarException e) {

            LOGGER.error("Error retrieving protocol details by experiment Id", e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return returnVal;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getProtocolNames() throws GobiiDaoException {

        ResultSet returnVal;

        try {
            SpGetProtocolNames spGetProtocolNames = new SpGetProtocolNames();

            storedProcExec.doWithConnection(spGetProtocolNames);

            returnVal = spGetProtocolNames.getResultSet();

        } catch (SQLGrammarException e) {

            LOGGER.error("Error retrieving protocol names with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return returnVal;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getVendorProtocolNames() throws GobiiDaoException {

        ResultSet returnVal;

        try {
            SpGetVendorProtocolNames spGetVendorProtocolNames = new SpGetVendorProtocolNames();

            storedProcExec.doWithConnection(spGetVendorProtocolNames);

            returnVal = spGetVendorProtocolNames.getResultSet();

        } catch (SQLGrammarException e) {

            LOGGER.error("Error retrieving VendorProtocol names with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return returnVal;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer createVendorProtocol(Map<String, Object> parameters) throws GobiiDaoException {
        Integer returnVal;

        try {
            spRunnerCallable.run(new SpInsVendorProtocol(), parameters);
            returnVal = spRunnerCallable.getResult();
        } catch (SQLGrammarException e) {
            LOGGER.error("Error creating vendor protocol record with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));
        }

        return returnVal;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getVendorByProtocolVendorName(Map<String, Object> parameters) throws GobiiDaoException {

        ResultSet returnVal;

        try {
            SpGetProtocolVendorByName spGetProtocolVendorByName = new SpGetProtocolVendorByName(parameters);

            storedProcExec.doWithConnection(spGetProtocolVendorByName);

            returnVal = spGetProtocolVendorByName.getResultSet();

        } catch (SQLGrammarException e) {

            LOGGER.error("Error retrieving VendorProtocol by name " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return returnVal;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getVendorByProtocolByCompoundIds(Integer protocolId, Integer vendorId) throws GobiiDaoException {
        ResultSet returnVal;

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("protocolId", protocolId);
            parameters.put("vendorId", vendorId);

            SpGetProtocolVendorByCompoundIds spGetProtocolVendorByCompoundIds = new SpGetProtocolVendorByCompoundIds(parameters);

            storedProcExec.doWithConnection(spGetProtocolVendorByCompoundIds);

            returnVal = spGetProtocolVendorByCompoundIds.getResultSet();

        } catch (SQLGrammarException e) {

            LOGGER.error("Error retrieving VendorProtocol by name " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return returnVal;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getVendorsByProtocolId(Integer protocolId) throws GobiiDaoException {

        ResultSet returnVal;

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("protocolId", protocolId);
            SpGetProtocolVendorsByProtocolId spGetProtocolVendorsByProtocolId = new SpGetProtocolVendorsByProtocolId(parameters);

            storedProcExec.doWithConnection(spGetProtocolVendorsByProtocolId);

            returnVal = spGetProtocolVendorsByProtocolId.getResultSet();

        } catch (SQLGrammarException e) {

            LOGGER.error("Error retrieving VendorProtocol by name " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return returnVal;


    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getVendorsProtocolNamesByProtocolId(Integer protocolId) throws GobiiDaoException {

        ResultSet returnVal;

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("protocolId", protocolId);
            SpGetProtocolVendorByProtocolId spGetProtocolVendorByProtocolIds =
                    new SpGetProtocolVendorByProtocolId(parameters);

            storedProcExec.doWithConnection(spGetProtocolVendorByProtocolIds);

            returnVal = spGetProtocolVendorByProtocolIds.getResultSet();

        } catch (SQLGrammarException e) {

            LOGGER.error("Error retrieving VendorProtocol names with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return returnVal;


    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getProtocolNamesByPlatformId(Integer platformId) throws GobiiDaoException {

        ResultSet returnVal;

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("platformId", platformId);
            SpGetProtocolNamesByPlatformId spGetProtocolNamesByPlatformId =
                    new SpGetProtocolNamesByPlatformId(parameters);

            storedProcExec.doWithConnection(spGetProtocolNamesByPlatformId);

            returnVal = spGetProtocolNamesByPlatformId.getResultSet();

        } catch (SQLGrammarException e) {

            LOGGER.error("Error retrieving VendorProtocol names with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return returnVal;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getVendorProtocolsForVendor(Integer organizationId) throws GobiiDaoException {

        ResultSet returnVal;

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("organizationId", organizationId);
            SpGetVendorProtocolsForVendor spGetVendorProtocolsForVendor =
                    new SpGetVendorProtocolsForVendor(parameters);

            storedProcExec.doWithConnection(spGetVendorProtocolsForVendor);

            returnVal = spGetVendorProtocolsForVendor.getResultSet();

        } catch (SQLGrammarException e) {

            LOGGER.error("Error retrieving VendorProtocols names with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return returnVal;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getVendorProtocolsForProtocol(Integer protocolId) throws GobiiDaoException {

        ResultSet returnVal;

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("protocolId", protocolId);
            SpGetVendorProtocolsForProtocol spGetVendorProtocolsForProtocol =
                    new SpGetVendorProtocolsForProtocol(parameters);

            storedProcExec.doWithConnection(spGetVendorProtocolsForProtocol);

            returnVal = spGetVendorProtocolsForProtocol.getResultSet();

        } catch (SQLGrammarException e) {

            LOGGER.error("Error retrieving VendorProtocols names with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return returnVal;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getVendorProtocolForVendorProtocolId(Integer vendorProtocolId) throws GobiiDaoException {

        ResultSet returnVal;

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("vendorProtocolId", vendorProtocolId);
            SpVendorProtocolForVendorProtoclId spVendorProtocolForVendorProtoclId =
                    new SpVendorProtocolForVendorProtoclId(parameters);

            storedProcExec.doWithConnection(spVendorProtocolForVendorProtoclId);

            returnVal = spVendorProtocolForVendorProtoclId.getResultSet();

        } catch (SQLGrammarException e) {

            LOGGER.error("Error retrieving VendorProtocols names with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return returnVal;

    }

}