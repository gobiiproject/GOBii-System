package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsDisplayDao;
import org.gobiiproject.gobiidao.resultset.core.SpRunnerCallable;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpInsDisplay;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpUpdDisplay;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetTableDisplayDetailByDisplayId;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetTableDisplayNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.util.Map;

/**
 * Created by Phil on 4/8/2016.
 */
public class RsDisplayDaoImpl implements RsDisplayDao {

    Logger LOGGER = LoggerFactory.getLogger(RsDisplayDaoImpl.class);

    @Autowired
    private StoredProcExec storedProcExec = null;

    @Autowired
    private SpRunnerCallable spRunnerCallable;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getTableDisplayNames() throws GobiiDaoException {

        ResultSet returnVal = null;

        try {

            SpGetTableDisplayNames spGetTableDisplayNames = new SpGetTableDisplayNames();
            storedProcExec.doWithConnection(spGetTableDisplayNames);
            returnVal = spGetTableDisplayNames.getResultSet();

        } catch (Exception e) {

            LOGGER.error("Error retrieving display names", e);
            throw (new GobiiDaoException(e));

        }

        return returnVal;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer createDisplay(Map<String, Object> parameters) throws GobiiDaoException {

        Integer returnVal = null;

        try {

            if (spRunnerCallable.run(new SpInsDisplay(), parameters)) {

                returnVal = spRunnerCallable.getResult();

            } else {

                throw new GobiiDaoException(spRunnerCallable.getErrorString());

            }

        } catch (Exception e) {

            LOGGER.error("Error creating display", e);
            throw (new GobiiDaoException(e));

        }

        return returnVal;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateDisplay(Map<String, Object> parameters) throws GobiiDaoException {

        try {

            if (!spRunnerCallable.run(new SpUpdDisplay(), parameters)) {
                throw new GobiiDaoException(spRunnerCallable.getErrorString());
            }

        } catch (Exception e) {

            LOGGER.error("Error creating display", e);
            throw (new GobiiDaoException(e));
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getTableDisplayDetailByDisplayId(Integer displayId) throws GobiiDaoException {
        ResultSet returnVal = null;

        try {

            SpGetTableDisplayDetailByDisplayId spGetTableDisplayDetailByDisplayId = new SpGetTableDisplayDetailByDisplayId();
            storedProcExec.doWithConnection(spGetTableDisplayDetailByDisplayId);
            returnVal = spGetTableDisplayDetailByDisplayId.getResultSet();

        } catch (Exception e) {

            LOGGER.error("Error retrieving display names", e);
            throw (new GobiiDaoException(e));

        }

        return returnVal;
    }
} // RsProjectDaoImpl
