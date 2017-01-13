package org.gobiiproject.gobiidao.resultset.access;

import org.gobiiproject.gobiidao.GobiiDaoException;

import java.sql.ResultSet;
import java.util.Map;

/**
 * Created by Phil on 4/7/2016.
 */
public interface RsOrganizationDao {


    ResultSet getOrganizationNames() throws GobiiDaoException;
    ResultSet getOrganizationDetailsByOrganizationId(Integer referenceId) throws GobiiDaoException;
    Integer createOrganization(Map<String, Object> parameters) throws GobiiDaoException;
    void updateOrganization(Map<String, Object> parameters) throws GobiiDaoException;
}
