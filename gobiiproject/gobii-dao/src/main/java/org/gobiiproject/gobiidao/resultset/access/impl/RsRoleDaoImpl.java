package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsPlatformDao;
import org.gobiiproject.gobiidao.resultset.access.RsRoleDao;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetPlatformNames;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetRoleNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;

/**
 * Created by Angel on 4/27/2016.
 */
public class RsRoleDaoImpl implements RsRoleDao {

    Logger LOGGER = LoggerFactory.getLogger(RsRoleDaoImpl.class);

    @Autowired
    private StoredProcExec storedProcExec = null;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultSet getContactRoleNames() throws GobiiDaoException {

        ResultSet returnVal = null;

        try {

            SpGetRoleNames spGetRoleNames = new SpGetRoleNames();
            storedProcExec.doWithConnection(spGetRoleNames);
            returnVal = spGetRoleNames.getResultSet();

        } catch (Exception e) {

            LOGGER.error("Error retrieving role names", e);
            throw (new GobiiDaoException(e));

        }


        return returnVal;

    }
}
