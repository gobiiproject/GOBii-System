package org.gobiiproject.gobiidao.resultset.access;

import org.gobiiproject.gobiidao.GobiiDaoException;

import java.sql.ResultSet;
import java.util.Map;

/**
 * Created by Phil on 4/22/2016.
 */
public interface RsAnalysisDao {

    ResultSet getAnalysisDetailsByAnalysisId(Integer analysisId) throws GobiiDaoException;
    
	ResultSet getAnalysisNames() throws GobiiDaoException;

    Integer createAnalysis(Map<String, Object> parameters) throws GobiiDaoException;

    void updateAnalysis(Map<String, Object> parameters) throws GobiiDaoException;

	ResultSet getAnalysisNamesByTypeId(int i) throws GobiiDaoException;

    void createUpdateParameter(Map<String, Object> parameters) throws GobiiDaoException;

    ResultSet getParameters(Integer analysisId ) throws GobiiDaoException;

}
