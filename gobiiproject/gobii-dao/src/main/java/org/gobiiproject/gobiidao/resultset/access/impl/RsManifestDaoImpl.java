package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsManifestDao;
import org.gobiiproject.gobiidao.resultset.core.SpRunnerCallable;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpInsManifest;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpUpdManifest;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetManifestNames;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetManifestDetailsByManifestId;
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
public class RsManifestDaoImpl implements RsManifestDao {

    Logger LOGGER = LoggerFactory.getLogger(RsManifestDaoImpl.class);

    @Autowired
    private StoredProcExec storedProcExec = null;

    @Autowired
    private SpRunnerCallable spRunnerCallable;
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultSet getManifestNames() throws GobiiDaoException {

        ResultSet returnVal = null;

        try {

            SpGetManifestNames spGetManifestNames = new SpGetManifestNames();
            storedProcExec.doWithConnection(spGetManifestNames);
            returnVal = spGetManifestNames.getResultSet();

        } catch (Exception e) {

            LOGGER.error("Error retrieving manifest names", e);
            throw (new GobiiDaoException(e));

        }

        return returnVal;

    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getManifestDetailsByManifestId(Integer manifestId) throws GobiiDaoException {

        ResultSet returnVal = null;

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("manifestId", manifestId);
            SpGetManifestDetailsByManifestId spGetManifestDetailsByManifestId = new SpGetManifestDetailsByManifestId(parameters);

            storedProcExec.doWithConnection(spGetManifestDetailsByManifestId);

            returnVal = spGetManifestDetailsByManifestId.getResultSet();

        } catch (Exception e) {

            LOGGER.error("Error retrieving manifest details", e);
            throw (new GobiiDaoException(e));

        }

        return returnVal;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer createManifest(Map<String, Object> parameters) throws GobiiDaoException {

        Integer returnVal = null;

        try {

            if (spRunnerCallable.run(new SpInsManifest(), parameters)) {

                returnVal = spRunnerCallable.getResult();

            } else {

                throw new GobiiDaoException(spRunnerCallable.getErrorString());

            }

        } catch (Exception e) {

            LOGGER.error("Error creating manifest", e);
            throw (new GobiiDaoException(e));

        }

        return returnVal;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateManifest(Map<String, Object> parameters) throws GobiiDaoException {

        try {

            if (!spRunnerCallable.run(new SpUpdManifest(), parameters)) {
                throw new GobiiDaoException(spRunnerCallable.getErrorString());
            }

        } catch (Exception e) {

            LOGGER.error("Error creating manifest", e);
            throw (new GobiiDaoException(e));
        }
    }
}
