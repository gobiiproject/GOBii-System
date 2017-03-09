package org.gobiiproject.gobiidao.resultset.access;

import org.gobiiproject.gobiidao.GobiiDaoException;

import java.sql.ResultSet;

/**
 * Created by VCalaminos on 2/1/2017.
 */
public interface RsCvGroupDao {

    ResultSet getCvsByGroupId(Integer groupId) throws GobiiDaoException;
    ResultSet getGroupTypeForGroupId(Integer groupId) throws GobiiDaoException;
}
