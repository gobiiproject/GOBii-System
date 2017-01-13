package org.gobiiproject.gobiidao.resultset.access;

import org.gobiiproject.gobiidao.GobiiDaoException;

import java.sql.ResultSet;
import java.util.Map;

/**
 * Created by Phil on 4/21/2016.
 */
public interface RsMarkerDao {

    ResultSet getMarkerDetailsByMarkerId(Integer projectId) throws GobiiDaoException;
    ResultSet getMarkersByMarkerName(String markerName) throws GobiiDaoException;
    Integer createMarker(Map<String, Object> parameters) throws GobiiDaoException;
    void updateMarker(Map<String, Object> parameters) throws GobiiDaoException;
	ResultSet getMarkers() throws GobiiDaoException;
}
