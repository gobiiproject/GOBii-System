package org.gobiiproject.gobiidao.resultset.access;

import org.gobiiproject.gobiidao.GobiiDaoException;

import java.sql.ResultSet;
import java.util.Map;

/**
 * Created by Phil on 4/7/2016.
 * Modified by AVB on 9/29/2016.
 */
public interface RsMapSetDao {

    ResultSet getAllMapsetNames() throws GobiiDaoException;

	ResultSet getMapNamesByTypeId(int typeId) throws GobiiDaoException;

    ResultSet getMapsetDetailsByMapsetId(int mapSetId) throws GobiiDaoException;

    Integer createMapset(Map<String,Object> parameters) throws GobiiDaoException;

    Integer createUpdateMapSetProperty(Map<String, Object> parameters) throws GobiiDaoException;

    void updateMapset(Map<String, Object> parameters) throws GobiiDaoException;

    ResultSet getProperties(Integer mapsetId) throws GobiiDaoException;
}
