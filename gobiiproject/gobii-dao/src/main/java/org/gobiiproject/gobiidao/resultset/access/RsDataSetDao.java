package org.gobiiproject.gobiidao.resultset.access;

import org.gobiiproject.gobiidao.GobiiDaoException;

import java.sql.ResultSet;
import java.util.Map;

/**
 * Created by Phil on 4/21/2016.
 */
public interface RsDataSetDao {

    ResultSet getDatasetNamesByExperimentId(Integer experimentId) throws GobiiDaoException;
    ResultSet getDataSetDetailsByDataSetId(Integer projectId) throws GobiiDaoException;
    Integer createDataset(Map<String,Object> parameters) throws GobiiDaoException;
    void updateDataSet(Map<String,Object> parameters) throws GobiiDaoException;
    Integer createUpdateParameter(Map<String, Object> parameters) throws GobiiDaoException;
	ResultSet getDatasetNames() throws GobiiDaoException;
}
