package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsContactDao;
import org.gobiiproject.gobiidao.resultset.core.SpRunnerCallable;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpInsContact;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpUpdContact;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetContactNames;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetContactNamesByRoleName;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetContactsByRoleName;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetContactDetailsByContactId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phil on 4/7/2016.
 */
public class RsContactDaoImpl implements RsContactDao {

    Logger LOGGER = LoggerFactory.getLogger(RsContactDaoImpl.class);

    @Autowired
    private StoredProcExec storedProcExec = null;

    @Autowired
    private SpRunnerCallable spRunnerCallable;


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getAllContactNames() throws GobiiDaoException {

        ResultSet returnVal = null;

        try {
            SpGetContactNames spGetContactNames = new SpGetContactNames();

            storedProcExec.doWithConnection(spGetContactNames);

            returnVal = spGetContactNames.getResultSet();
        } catch (Exception e) {

            LOGGER.error("Error retrieving contact names", e);
            throw (new GobiiDaoException(e));

        }

        return returnVal;

    } // getAllContactNames

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getContactNamesForRoleName(String roleName) throws GobiiDaoException {

        ResultSet returnVal = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("roleName", roleName);
            SpGetContactNamesByRoleName spGetContactNamesByRoleName = new SpGetContactNamesByRoleName(parameters);

            storedProcExec.doWithConnection(spGetContactNamesByRoleName);

            returnVal = spGetContactNamesByRoleName.getResultSet();
        } catch (Exception e) {

            LOGGER.error("Error retrieving contact names", e);
            throw (new GobiiDaoException(e));

        }

        return returnVal;

    } // getContactNamesForRoleName


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getContactDetailsByContactId(Integer contactId) throws GobiiDaoException {

        ResultSet returnVal = null;

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("contactId", contactId);
            SpGetContactDetailsByContactId spGetContactDetailsByContactId = new SpGetContactDetailsByContactId(parameters);

            storedProcExec.doWithConnection(spGetContactDetailsByContactId);

            returnVal = spGetContactDetailsByContactId.getResultSet();

        } catch (Exception e) {

            LOGGER.error("Error retrieving contact details", e);
            throw (new GobiiDaoException(e));

        }

        return returnVal;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer createContact(Map<String, Object> parameters) throws GobiiDaoException {

        Integer returnVal = null;

        try {

            if (spRunnerCallable.run(new SpInsContact(), parameters)) {

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
    public void updateContact(Map<String, Object> parameters) throws GobiiDaoException {

        try {

            if (!spRunnerCallable.run(new SpUpdContact(), parameters)) {
                throw new GobiiDaoException(spRunnerCallable.getErrorString());
            }

        } catch (Exception e) {

            LOGGER.error("Error creating contact", e);
            throw (new GobiiDaoException(e));
        }
    }
}
