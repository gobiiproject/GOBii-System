package org.gobiiproject.gobiidao.resultset.access;

import org.gobiiproject.gobiidao.GobiiDaoException;

import java.sql.ResultSet;
import java.util.Map;

/**
 * Created by Phil on 4/13/2016.
 */
public interface RsDisplayDao {

    ResultSet getTableDisplayNames() throws GobiiDaoException;

    Integer createDisplay(Map<String, Object> parameters) throws GobiiDaoException;

    void updateDisplay(Map<String, Object> parameters) throws GobiiDaoException;

    ResultSet getTableDisplayDetailByDisplayId(Integer displayId) throws GobiiDaoException;
}
