package org.gobiiproject.gobiidao.resultset.access;

import org.gobiiproject.gobiidao.GobiiDaoException;

import java.sql.ResultSet;
import java.util.Map;

/**
 * Created by Angel on 4/27/2016.
 */
public interface RsMarkerGroupDao {


    ResultSet getMarkerGroupNames() throws GobiiDaoException;

    ResultSet getMarkerGroupDetailByMarkerGroupId(Integer markerGroupId) throws GobiiDaoException;

    Integer createMarkerGroup(Map<String, Object> parameters) throws GobiiDaoException;

    ResultSet getMarkersByMarkerName(String markerName) throws GobiiDaoException;

    ResultSet getMarkerByMarkerId(Integer markerId) throws GobiiDaoException;

    void createUpdateMarkerGroupMarker(Map<String, Object> parameters) throws GobiiDaoException;

    void deleteMarkerGroupMarker(Map<String, Object> parameters) throws GobiiDaoException;

    void updateMarkerGroup(Map<String, Object> parameters) throws GobiiDaoException;

    void updateMarkerGroupName(Map<String, Object> parameters) throws GobiiDaoException;

    ResultSet getMarkersForMarkerGroup(Integer markerGroupId) throws GobiiDaoException;

    ResultSet getMarkersByMarkerAndPlatformName(String markerName, String platformName) throws GobiiDaoException;

}
