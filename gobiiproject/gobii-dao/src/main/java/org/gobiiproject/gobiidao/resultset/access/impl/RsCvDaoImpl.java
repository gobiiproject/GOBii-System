package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsCvDao;
import org.gobiiproject.gobiidao.resultset.access.RsExperimentDao;
import org.gobiiproject.gobiidao.resultset.core.SpRunnerCallable;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpDelCv;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpInsCv;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpUpdCv;
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
 * Created by Angel on 4/19/2016.
 */
public class RsCvDaoImpl implements RsCvDao {


    Logger LOGGER = LoggerFactory.getLogger(RsExperimentDao.class);

    @Autowired
    private StoredProcExec storedProcExec = null;

    @Autowired
    private SpRunnerCallable spRunnerCallable;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getCvTermsByGroup(String groupName) throws GobiiDaoException {

        ResultSet returnVal = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("groupName", groupName);
            SpGetCvTermsByGroup spGetCvTermsByGroup = new SpGetCvTermsByGroup(parameters);

            storedProcExec.doWithConnection(spGetCvTermsByGroup);

            returnVal = spGetCvTermsByGroup.getResultSet();
        } catch (Exception e) {

            LOGGER.error("Error retrieving CV terms by group", e);
            throw (new GobiiDaoException(e));

        }


        return returnVal;
    }

	@Override
    @Transactional(propagation = Propagation.REQUIRED)
	public ResultSet getCvGroups() throws GobiiDaoException {
		// TODO Auto-generated method stub
		 ResultSet returnVal = null;

	        try {
	            Map<String, Object> parameters = new HashMap<>();
	            SpGetCvGroups spGetCvGroups = new SpGetCvGroups();

	            storedProcExec.doWithConnection(spGetCvGroups);

	            returnVal = spGetCvGroups.getResultSet();
	        } catch (Exception e) {

	            LOGGER.error("Error retrieving CV groups", e);
	            throw (new GobiiDaoException(e));

	        }


	        return returnVal;
	}

	@Override
    @Transactional(propagation = Propagation.REQUIRED)
	public ResultSet getCvNames() throws GobiiDaoException {
		// TODO Auto-generated method stub
		 ResultSet returnVal = null;

	        try {
	            Map<String, Object> parameters = new HashMap<>();
	            SpGetCvTerms spGetCvTerms = new SpGetCvTerms();

	            storedProcExec.doWithConnection(spGetCvTerms);

	            returnVal = spGetCvTerms.getResultSet();
	        } catch (Exception e) {

	            LOGGER.error("Error retrieving CV groups", e);
	            throw (new GobiiDaoException(e));

	        }


	        return returnVal;
	}


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultSet getAllCvItems() throws GobiiDaoException {
        // TODO Auto-generated method stub
        ResultSet returnVal = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            SpGetCvItems spGetCvItems = new SpGetCvItems();

            storedProcExec.doWithConnection(spGetCvItems);

            returnVal = spGetCvItems.getResultSet();
        } catch (Exception e) {

            LOGGER.error("Error retrieving CV groups", e);
            throw (new GobiiDaoException(e));

        }


        return returnVal;
    }

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultSet getDetailsForCvId(Integer cvId) throws GobiiDaoException {
		// TODO Auto-generated method stub

        ResultSet returnVal;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("cvId", cvId);
            SpGetCvDetailsByCvId spGetCvDetailsByCvId = new SpGetCvDetailsByCvId(parameters);
            storedProcExec.doWithConnection(spGetCvDetailsByCvId);
            returnVal = spGetCvDetailsByCvId.getResultSet();
        } catch (Exception e) {

            LOGGER.error("Error retrieving project details", e);
            throw (new GobiiDaoException(e));

        }


        return returnVal;
	}


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer createCv(Map<String, Object> parameters) throws GobiiDaoException {

        Integer returnVal = null;

        try {

            if (spRunnerCallable.run(new SpInsCv(), parameters)) {

                returnVal = spRunnerCallable.getResult();

            } else {

                throw new GobiiDaoException(spRunnerCallable.getErrorString());

            }

        } catch (Exception e) {

            LOGGER.error("Error creating cv", e);
            throw (new GobiiDaoException(e));

        }

        return returnVal;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateCv(Map<String, Object> parameters) throws GobiiDaoException {

        try {

            if (!spRunnerCallable.run(new SpUpdCv(), parameters)) {
                throw new GobiiDaoException(spRunnerCallable.getErrorString());
            }

        } catch (Exception e) {

            LOGGER.error("Error creating cv", e);
            throw (new GobiiDaoException(e));
        }
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteCv(Map<String, Object> parameters) throws GobiiDaoException {

        try {

            if (!spRunnerCallable.run(new SpDelCv(), parameters)) {
                throw new GobiiDaoException(spRunnerCallable.getErrorString());
            }

        } catch (Exception e) {

            LOGGER.error("Error creating cv", e);
            throw (new GobiiDaoException(e));
        }
    }
} // RsProjectDaoImpl
