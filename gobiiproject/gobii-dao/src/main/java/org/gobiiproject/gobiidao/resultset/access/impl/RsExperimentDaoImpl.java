package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsExperimentDao;
import org.gobiiproject.gobiidao.resultset.core.SpRunnerCallable;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpInsDataSet;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpInsExperiment;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpUpdExperiment;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpUpdProject;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetExperimentByNameProjectIdPlatformId;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetExperimentDetailsByExperimentId;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetExperimentNames;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetExperimentNamesByProjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Angel on 4/19/2016.
 */
public class RsExperimentDaoImpl implements RsExperimentDao {


    Logger LOGGER = LoggerFactory.getLogger(RsExperimentDao.class);

    @Autowired
    private StoredProcExec storedProcExec = null;

    @Autowired
    private SpRunnerCallable spRunnerCallable;


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getExperimentNamesByProjectId(Integer projectId) throws GobiiDaoException {

        ResultSet returnVal = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("projectId", projectId);
            SpGetExperimentNamesByProjectId spGetExperimentNamesByProjectId = new SpGetExperimentNamesByProjectId(parameters);

            storedProcExec.doWithConnection(spGetExperimentNamesByProjectId);

            returnVal = spGetExperimentNamesByProjectId.getResultSet();
        } catch (Exception e) {

            LOGGER.error("Error retrieving experiment names", e);
            throw (new GobiiDaoException(e));

        }


        return returnVal;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getExperimentDetailsForExperimentId(int experimentId) throws GobiiDaoException {

        ResultSet returnVal = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("experimentId", experimentId);
            SpGetExperimentDetailsByExperimentId spGetExperimentDetailsByExperimentId = new SpGetExperimentDetailsByExperimentId(parameters);
            storedProcExec.doWithConnection(spGetExperimentDetailsByExperimentId);
            returnVal = spGetExperimentDetailsByExperimentId.getResultSet();
        } catch (Exception e) {

            LOGGER.error("Error retrieving experiment details", e);
            throw (new GobiiDaoException(e));

        }

        return returnVal;
    }


    @Transactional(propagation = Propagation.REQUIRED)
	@Override
	public ResultSet getExperimentNames() throws GobiiDaoException {
		// TODO Auto-generated method stub

        ResultSet returnVal = null;

        try {
            SpGetExperimentNames spGetExperimentNames = new SpGetExperimentNames();
            storedProcExec.doWithConnection(spGetExperimentNames);
            returnVal = spGetExperimentNames.getResultSet();
        } catch (Exception e) {

            LOGGER.error("Error retrieving all experiment names", e);
            throw (new GobiiDaoException(e));

        }

        return returnVal;
	}

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer createExperiment(Map<String, Object> parameters) throws GobiiDaoException {
        Integer returnVal = null;

        try {

            if (spRunnerCallable.run(new SpInsExperiment(), parameters)) {

                returnVal = spRunnerCallable.getResult();

            } else {

                throw new GobiiDaoException(spRunnerCallable.getErrorString());

            }

        } catch (Exception e) {

            LOGGER.error("Error creating dataset", e);
            throw (new GobiiDaoException(e));

        }

        return returnVal;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateExperiment(Map<String, Object> parameters) throws GobiiDaoException {

        try {

            if (!spRunnerCallable.run(new SpUpdExperiment(), parameters)) {
                throw new GobiiDaoException(spRunnerCallable.getErrorString());
            }

        } catch (Exception e) {

            LOGGER.error("Error creating experiment", e);
            throw (new GobiiDaoException(e));

        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getExperimentsByNameProjectidPlatformId(String experimentName, Integer projectId, Integer platformId) throws GobiiDaoException {
        ResultSet returnVal = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("experimentName", experimentName);
            parameters.put("projectId", projectId);
            parameters.put("platformId", platformId);
            SpGetExperimentByNameProjectIdPlatformId spGetExperimentByNameProjectIdPlatformId = new SpGetExperimentByNameProjectIdPlatformId(parameters);

            storedProcExec.doWithConnection(spGetExperimentByNameProjectIdPlatformId);

            returnVal = spGetExperimentByNameProjectIdPlatformId.getResultSet();

        } catch (Exception e) {

            LOGGER.error("Error retrieving experiment names", e);
            throw (new GobiiDaoException(e));

        }

        return returnVal;
    }


} // RsProjectDaoImpl
