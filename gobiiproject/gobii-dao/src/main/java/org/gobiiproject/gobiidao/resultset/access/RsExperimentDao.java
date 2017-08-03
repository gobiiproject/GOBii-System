package org.gobiiproject.gobiidao.resultset.access;

import org.gobiiproject.gobiidao.GobiiDaoException;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Angel on 4/19/2016.
 */
public interface RsExperimentDao {


    ResultSet getExperimentNamesByProjectId(Integer experimentId) throws GobiiDaoException;

    ResultSet getExperimentDetailsForExperimentId(int experimentId) throws GobiiDaoException;

    ResultSet getExperimentNames() throws GobiiDaoException;

    Integer createExperiment(Map<String, Object> parameters) throws GobiiDaoException;

    void updateExperiment(Map<String, Object> parameters) throws GobiiDaoException;

    ResultSet getExperimentsByNameProjectid(String experimentName,
                                            Integer projectId) throws GobiiDaoException;

    ResultSet getAlleleMatrices(Integer projectId) throws GobiiDaoException;


}
