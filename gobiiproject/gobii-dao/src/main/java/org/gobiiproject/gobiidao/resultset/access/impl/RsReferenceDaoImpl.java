package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsPlatformDao;
import org.gobiiproject.gobiidao.resultset.access.RsReferenceDao;
import org.gobiiproject.gobiidao.resultset.core.SpRunnerCallable;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpInsReference;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpUpdReference;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetReferenceDetailsByReferenceId;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetPlatformNames;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetReferenceNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Angel on 5/4/2016.
 */
public class RsReferenceDaoImpl implements RsReferenceDao {

    Logger LOGGER = LoggerFactory.getLogger(RsReferenceDao.class);

    @Autowired
    private StoredProcExec storedProcExec = null;

    @Autowired
    private SpRunnerCallable spRunnerCallable;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultSet getReferenceNames() throws GobiiDaoException {

        ResultSet returnVal = null;

        SpGetReferenceNames spGetReferenceNames = new SpGetReferenceNames();
        storedProcExec.doWithConnection(spGetReferenceNames);
        returnVal = spGetReferenceNames.getResultSet();

        return returnVal;

    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getReferenceDetailsByReferenceId(Integer referenceId) throws GobiiDaoException {

        ResultSet returnVal = null;

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("referenceId", referenceId);
            SpGetReferenceDetailsByReferenceId spGetReferenceDetailsByReferenceId = new SpGetReferenceDetailsByReferenceId(parameters);

            storedProcExec.doWithConnection(spGetReferenceDetailsByReferenceId);

            returnVal = spGetReferenceDetailsByReferenceId.getResultSet();

        } catch (Exception e) {

            LOGGER.error("Error retrieving reference details", e);
            throw (new GobiiDaoException(e));

        }

        return returnVal;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer createReference(Map<String, Object> parameters) throws GobiiDaoException {

        Integer returnVal = null;

        try {

            if (spRunnerCallable.run(new SpInsReference(), parameters)) {

                returnVal = spRunnerCallable.getResult();

            } else {

                throw new GobiiDaoException(spRunnerCallable.getErrorString());

            }

        } catch (Exception e) {

            LOGGER.error("Error creating reference", e);
            throw (new GobiiDaoException(e));

        }

        return returnVal;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateReference(Map<String, Object> parameters) throws GobiiDaoException {

        try {

            if (!spRunnerCallable.run(new SpUpdReference(), parameters)) {
                throw new GobiiDaoException(spRunnerCallable.getErrorString());
            }

        } catch (Exception e) {

            LOGGER.error("Error creating reference", e);
            throw (new GobiiDaoException(e));
        }
    }
}
